package me.drakeet.materialdialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by drakeet on 9/28/14.
 */
public class MaterialDialog {
    Context mContext;
    AlertDialog mAlertDialog;
    TextView mTitleView;
    TextView mMessageView;
    LinearLayout mButtonLayout;

    public MaterialDialog(Context context) {
        this.mContext = context;
        mAlertDialog = new android.app.AlertDialog.Builder(mContext).create();
        show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.layout_materialdialog);
        mTitleView = (TextView) window.findViewById(R.id.title);
        mMessageView = (TextView) window.findViewById(R.id.message);
        mButtonLayout = (LinearLayout) window.findViewById(R.id.buttonLayout);
    }

    public void show() {
        mAlertDialog.show();
    }

    public void setTitle(int resId) {
        mTitleView.setText(resId);
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    public void setMessage(int resId) {
        mMessageView.setText(resId);
    }

    public void setMessage(String message) {
        mMessageView.setText(message);
    }

    /**
     * set positive button
     * @param text the name of button
     * @param listener
     */
    public void setPositiveButton(String text, final View.OnClickListener listener) {
        Button button = new Button(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.material_card);
        button.setTextColor(Color.argb(255, 35, 159, 242));
        button.setText(text);
        button.setGravity(Gravity.CENTER);
        button.setTextSize(14);
        button.setPadding(dip2px(12), 0, dip2px(32), dip2px(16));
        button.setOnClickListener(listener);
        mButtonLayout.addView(button);
    }

    /**
     * set negative button
     * @param text the name of button
     * @param listener
     */
    public void setNegativeButton(String text, final View.OnClickListener listener) {
        Button button = new Button(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.material_card);
        button.setText(text);
        button.setTextColor(Color.argb(222, 0, 0, 0));
        button.setTextSize(14);
        button.setGravity(Gravity.CENTER);
        button.setPadding(0, 0, 0, dip2px(16));
        button.setOnClickListener(listener);
        if (mButtonLayout.getChildCount() > 0) {
            params.setMargins(20, 0, 10, 0);
            button.setLayoutParams(params);
            mButtonLayout.addView(button, 1);
        } else {
            button.setLayoutParams(params);
            mButtonLayout.addView(button);
        }
    }

    public void dismiss() {
        mAlertDialog.dismiss();
    }

    public int dip2px( float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}