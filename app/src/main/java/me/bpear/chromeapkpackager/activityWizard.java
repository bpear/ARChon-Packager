package me.bpear.chromeapkpackager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import me.drakeet.materialdialog.MaterialDialog;

public class activityWizard extends FragmentActivity {
    MaterialDialog mMaterialDialog;
    Globals g = Globals.getInstance();
    public static Activity start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);
        start = this;

        // Only set the tint if the device is running KitKat or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #DDDDDD
            int actionBarColor = Color.parseColor("#394249");
            tintManager.setStatusBarTintColor(actionBarColor);
        }
    }

    @Override
    public void onBackPressed() {
        switch (g.getstep()) {
            case 1: // If on first step prompt user to exit.
                mMaterialDialog = new MaterialDialog(this);
                mMaterialDialog.setTitle("Back button pressed");
                mMaterialDialog.setMessage("Do you want to exit?");
                mMaterialDialog.setPositiveButton("YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        finish();
                    }
                });

                mMaterialDialog.setNegativeButton("NO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();

                    }
                });
                break;
            case 2: //If on step 2 restart wizard
                g.setstep(1);
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();

                overridePendingTransition(0, 0);
                startActivity(intent);
                break;
            default: // Any other step going back is fine.
                super.onBackPressed();
                break;
        }

    }
}
