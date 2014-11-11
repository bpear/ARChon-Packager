package me.bpear.archonpackager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class activityInstalled extends Activity {

    private static final int PICKFILE_RESULT_CODE = 1;
    Globals g = Globals.getInstance();
    String PackageName;
    String SelectedAppName;
    int SelectedAppId;
    int selection = g.getSelection();
    private ProgressDialog pd;
    ViewGroup appButtonLayout;
    Map<String, Button> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.installed_list);
        g.setstep(0);

        //Open either file explorer
        if (selection == 1) {
            // Open file selector
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        }

        if (selection == 0) {
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() { //Start progress dialog and run task in background

                @Override
                protected void onPreExecute() {
                    pd = new ProgressDialog(activityInstalled.this);
                    pd.setTitle("Processing...");
                    pd.setMessage("Please wait.");
                    pd.setCancelable(false);
                    pd.setIndeterminate(true);
                    pd.show();
                }

                @Override
                protected Void doInBackground(Void... arg0) {
                    //or list installed apps
                    map = new TreeMap<String, Button>(); //Create TreeMap to store list of apps.

                    appButtonLayout = (ViewGroup) findViewById(R.id.installed_radio_group);  // This is the id of the RadioGroup we defined

                    List<PackageInfo> PackList = getPackageManager().getInstalledPackages(0); //Generate list of apps installed
                    for (int i = 0; i < PackList.size(); i++) {
                        PackageInfo PackInfo = PackList.get(i);
                        if ((PackInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // Do not use system apps. Only user/data apps.
                            final String AppName = PackInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                            RadioButton buttonr = new RadioButton(activityInstalled.this);
                            buttonr.setText(AppName); //Set text of button to applications name
                            buttonr.setId(i);
                            map.put(AppName, buttonr); //Put buttons in TreeMap
                            buttonr.setOnClickListener(new View.OnClickListener() { //When app radio button is selected
                                @Override
                                public void onClick(View view) {
                                    ((RadioGroup) view.getParent()).check(view.getId());
                                    SelectedAppId = view.getId(); //store AppID
                                    SelectedAppName = AppName; //Store App name
                                    Toast.makeText(activityInstalled.this, "App selected.", Toast.LENGTH_LONG).show();
                                    //Save variables for next fragment
                                    g.setSelectedAppId(SelectedAppId);// Sets global variable
                                    g.setSelectedAppName(SelectedAppName);

                                    AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() { //Start progress dialog and run task in background

                                        @Override
                                        protected void onPreExecute() {
                                            pd = new ProgressDialog(activityInstalled.this);
                                            pd.setTitle("Processing...");
                                            pd.setMessage("Please wait.");
                                            pd.setCancelable(false);
                                            pd.setIndeterminate(true);
                                            pd.show();
                                        }

                                        @Override
                                        protected Void doInBackground(Void... arg0) {
                                            pullAPk();
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void result) {
                                            if (pd != null) {
                                                pd.dismiss();
                                            }
                                        }

                                    };
                                    task.execute((Void[]) null);


                                }
                            });
                        }
                    }
                    return null;
                }


                @Override
                protected void onPostExecute(Void result) {
                    if (pd != null) {
                        for (Button b : map.values()) { //Add buttons in TreeMap to view.
                            appButtonLayout.addView(b);
                        }
                        pd.dismiss();
                    }
                }

            };
            task.execute((Void[]) null);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) { //If pick file was successful
                    g.setAPKPath(data.getData().getPath()); //Get apk path from global variables.
                    Toast.makeText(this, "APK Selected.", Toast.LENGTH_LONG).show();
                    AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() { //Start progress dialog and run task in background

                        @Override
                        protected void onPreExecute() {
                            pd = new ProgressDialog(activityInstalled.this);
                            pd.setTitle("Processing...");
                            pd.setMessage("Please wait.");
                            pd.setCancelable(false);
                            pd.setIndeterminate(true);
                            pd.show();
                        }

                        @Override
                        protected Void doInBackground(Void... arg0) {
                            try {
                                SelectedAPK();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            if (pd != null) {
                                pd.dismiss();
                            }
                        }

                    };
                    task.execute((Void[]) null);
                }
                break;

        }
    }

    private void copyAssets() { //Copy template.zip from app assets to /ChromeAPKS
        String AppNameFolder = g.getSelectedAppName();
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        assert files != null;
        for (String filename : files) {
            InputStream in;
            OutputStream out;
            try {
                in = assetManager.open(filename);
                out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + File.separator + "ChromeAPKS/" + AppNameFolder + "/" + filename);
                copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }

    public void pullAPk() { //Pull APK from selected installed app. Uses PackageInfo and PackageManager.
        List<PackageInfo> PackList = getPackageManager().getInstalledPackages(0);
        PackageInfo PackInfo = PackList.get(SelectedAppId);
        if ((PackInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            File org = new File(PackInfo.applicationInfo.publicSourceDir);
            String PackageNamePull = PackInfo.applicationInfo.packageName;
            g.setPackageName(PackageNamePull);
            setupFolders(); //Setup directories.


            //Extract app icon to use with chrome.
            PackageManager pm = getPackageManager();
            Drawable icon = PackInfo.applicationInfo.loadIcon(pm);
            Bitmap bmpIcon = ((BitmapDrawable) icon).getBitmap();

            String extStorageDirectory = Environment.getExternalStorageDirectory().toString() + "/ChromeAPKS/" + SelectedAppName + File.separator;
            OutputStream outStream = null;
            File file = new File(extStorageDirectory, "icon.png");
            try {
                outStream = new FileOutputStream(file);
                bmpIcon.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                Toast.makeText(activityInstalled.this, "Error: APK not pulled.", Toast.LENGTH_LONG).show();
            }


            try {

                String file_name = PackInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.d("file_name--", " " + file_name);
                PackageName = PackInfo.applicationInfo.packageName;


                File apk = new File(Environment.getExternalStorageDirectory().toString() + "/ChromeAPKS/Pulled"); // make directory for pulled apks
                apk.mkdirs();
                apk = new File(apk.getPath() + "/" + file_name + ".apk"); // Set pulled apk to its proper name
                apk.createNewFile(); // Move APK

                InputStream in = new FileInputStream(org);

                OutputStream out = new FileOutputStream(apk);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                System.out.println("File copied.");
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage() + " in the specified directory.");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            g.setAPKPath(Environment.getExternalStorageDirectory() + "/ChromeAPKS/Pulled/" + SelectedAppName + ".apk");
            File apksource = new File(Environment.getExternalStorageDirectory() + "/ChromeAPKS/Pulled/" + SelectedAppName + ".apk");
            File apkdst = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "ChromeAPKS/" + SelectedAppName + "/vendor/chromium/crx/" + PackageName + ".apk");
            try {
                copy(apksource, apkdst);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finish(); //Finish activity dialog to return to main wizard.
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private boolean unpackZip(String path, String zipname) { //Used to unpack template.zip
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                // Need to create directories if they do not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void setupFolders() {
        // Make sure that is directory is available on device.
        String AppNameFolder = g.getSelectedAppName();
        File folder = new File(Environment.getExternalStorageDirectory() + "/ChromeAPKS");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File folder2 = new File(Environment.getExternalStorageDirectory() + "/ChromeAPKS/" + AppNameFolder);
        if (!folder2.exists()) {
            folder2.mkdir();
        }

        File folder3 = new File(Environment.getExternalStorageDirectory() + "/ChromeAPKS/" + AppNameFolder + "/vendor");
        if (!folder3.exists()) {
            folder3.mkdir();
        }
        File folder4 = new File(Environment.getExternalStorageDirectory() + "/ChromeAPKS/" + AppNameFolder + "/vendor/chromium");
        if (!folder4.exists()) {
            folder4.mkdir();
        }
        File folder5 = new File(Environment.getExternalStorageDirectory() + "/ChromeAPKS/" + AppNameFolder + "/vendor/chromium/crx");
        if (!folder5.exists()) {
            folder5.mkdir();
        }

        copyAssets(); //Copy template zip over
        unpackZip(Environment.getExternalStorageDirectory().toString() + File.separator + "ChromeAPKs" + File.separator + AppNameFolder + File.separator, "template.zip"); // Extract contents of template
        File f = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "ChromeAPKs" + File.separator + AppNameFolder, "/template.zip"); // Cleanup template.
        f.delete();
    }

    public void SelectedAPK() throws IOException { //If user selects apk manually. Run this.

        String APKFilePath = g.getAPKPath();
        PackageManager pm = getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(APKFilePath, 0);

        // tell package info where app is located.
        pi.applicationInfo.sourceDir = APKFilePath;
        pi.applicationInfo.publicSourceDir = APKFilePath;


        String PackageName = pi.packageName;
        String AppName = (String) pi.applicationInfo.loadLabel(pm);
        g.setSelectedAppName(AppName);
        g.setPackageName(PackageName);

        Drawable icon = pi.applicationInfo.loadIcon(pm);
        Bitmap bmpIcon = ((BitmapDrawable) icon).getBitmap();

        setupFolders(); //Make folders on phones storage

        // Extract icon
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString() + "/ChromeAPKS/" + AppName + File.separator;
        OutputStream outStream = null;
        File file = new File(extStorageDirectory, "icon.png");
        try {
            outStream = new FileOutputStream(file);
            bmpIcon.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            Toast.makeText(activityInstalled.this, "Error: App Icon was not extracted.", Toast.LENGTH_LONG).show();
        }

        //Copy APK from selected source to ChromeAPKS location
        File apksource = new File(APKFilePath);
        File apkdst = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "ChromeAPKS/" + AppName + "/vendor/chromium/crx/" + PackageName + ".apk");
        copy(apksource, apkdst);

        finish(); // End dialog activity to return to app wizard.

        //TODO: Cleanup leftover directories (Unzipped app directory, pulled app directory are not needed.)
    }


}


