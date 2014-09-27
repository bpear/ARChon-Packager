package me.bpear.chromeapkpackager.steps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import org.codepond.wizardroid.WizardStep;

import me.bpear.chromeapkpackager.Globals;
import me.bpear.chromeapkpackager.R;
import me.bpear.chromeapkpackager.activityInstalled;

public class Step2 extends WizardStep {
    Globals g = Globals.getInstance();
    private ProgressDialog pd;

    //You must have an empty constructor for every step
    public Step2() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.step2, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button rb1 = (Button) getActivity().findViewById(R.id.rbInstalled); //Listen to Installed App radio button
        rb1.setOnClickListener(next_Listener);

        Button rb2 = (Button) getActivity().findViewById(R.id.rbAPK); //Listen to Select an apk radio button
        rb2.setOnClickListener(next_Listener);
    }

    private View.OnClickListener next_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            //find out which radio button has been checked
            RadioButton rb1 = (RadioButton) getActivity().findViewById(R.id.rbInstalled);  //you dont need to do this again if global ...
            RadioButton rb2 = (RadioButton) getActivity().findViewById(R.id.rbAPK);  //you dont need to do this again if global ...
            if (rb1.isChecked()) { // If button 1 is checked set type int to 1
                g.setSelection(0);// Sets global variable
                notifyCompleted(true); //Notify wizard that step is complete.
            }
            if (rb2.isChecked()) {
                Globals g = Globals.getInstance();
                g.setSelection(1);// Sets global variable
                notifyCompleted(true); //Notify wizard that step is complete.
            }
        }
    };

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */

    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                Intent intent = new Intent(getActivity(), activityInstalled.class);
                startActivity(intent);//Start file/app selection activity
                bindDataFields();
                break;
            case WizardStep.EXIT_PREVIOUS:
                //Do nothing...
                break;
        }
    }

    private void bindDataFields() {

    }
}
