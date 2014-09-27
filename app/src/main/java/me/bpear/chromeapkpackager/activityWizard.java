package me.bpear.chromeapkpackager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class activityWizard extends FragmentActivity {
    Globals g = Globals.getInstance();
    public static Activity start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);
        start = this;
    }

    @Override
    public void onBackPressed() {
        if (g.getstep() == 1) {
            if (g.getstep() == 1) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Back button pressed. Do you want to exit?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("no", dialogClickListener).show();
            }
        }
        if (g.getstep() == 2) {
            recreate();
        }
        super.onBackPressed();
    }
}
