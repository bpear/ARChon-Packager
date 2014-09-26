package me.bpear.chromeapkpackager.wizards;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;

import me.bpear.chromeapkpackager.steps.Step1;
import me.bpear.chromeapkpackager.steps.Step2;
import me.bpear.chromeapkpackager.steps.Step3;
import me.bpear.chromeapkpackager.steps.Step4;


public class wizardStartup extends BasicWizardLayout {

    public wizardStartup() {
        super();
    }

    /*
        You must override this method and create a wizard flow by
        using WizardFlow.Builder as shown in this example
     */
    @Override
    public WizardFlow onSetup() {
        return new WizardFlow.Builder()
                /*
                Add your steps in the order you want them to appear and eventually call create()
                to create the wizard flow.
                 */
                .addStep(Step1.class)
                .addStep(Step2.class)
                .addStep(Step3.class)
                .addStep(Step4.class)

                .create();
    }

    /*
        You'd normally override onWizardComplete to access the wizard context and/or close the wizard
     */
    @Override
    public void onWizardComplete() {
        super.onWizardComplete();   //Make sure to first call the super method before anything else
        getActivity().finish();     //Terminate the wizard
    }
}
