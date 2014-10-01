package me.bpear.archonpackager.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.codepond.wizardroid.WizardStep;

import me.bpear.archonpackager.R;
import me.bpear.archonpackager.Globals;

public class Step1 extends WizardStep {
    Globals g = Globals.getInstance();

    //You must have an empty constructor for every step
    public Step1() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step1, container, false);
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */

    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                g.setstep(2);
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
