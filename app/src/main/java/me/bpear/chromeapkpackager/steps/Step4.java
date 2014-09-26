package me.bpear.chromeapkpackager.steps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.codepond.wizardroid.WizardStep;

import java.io.File;

import me.bpear.chromeapkpackager.Globals;
import me.bpear.chromeapkpackager.R;

public class Step4 extends WizardStep {

    Globals g = Globals.getInstance();
    TextView tv;
    TextView tv2;
    TextView tv3;
    String rotate = g.getrotate().substring(0, 1).toUpperCase() + g.getrotate().substring(1);// Capitalize first letter of string
    String device = g.getdevice().substring(0, 1).toUpperCase() + g.getdevice().substring(1);

    //You must have an empty constructor for every step
    public Step4() {
    }

    private void sendFile(File outFile) {
        Uri uriToZip = Uri.fromFile(outFile);
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("*/*");
        sendIntent.putExtra(android.content.Intent.EXTRA_STREAM, uriToZip);
        startActivity(Intent.createChooser(sendIntent, "Send Chrome App:"));
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step4, container, false);

        //Output final results
        tv = (TextView) v.findViewById(R.id.appname);
        tv.setText("App converted: " + g.getSelectedAppName());

        tv2 = (TextView) v.findViewById(R.id.orientation);
        tv2.setText("Orientation selected: " + rotate);

        tv3 = (TextView) v.findViewById(R.id.device);
        tv3.setText("Device mode selected: " + device);

        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Button button = (Button) getActivity().findViewById(R.id.share);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                File chrome = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "/ChromeAPKS/" + g.getSelectedAppName() + ".zip");
                sendFile(chrome);
            }
        });
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */

    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
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
