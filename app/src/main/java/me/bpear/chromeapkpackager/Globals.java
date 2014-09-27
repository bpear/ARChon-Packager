package me.bpear.chromeapkpackager;


public class Globals {
    private static Globals instance;

    // Global variable
    private int Selection;
    private int SelectedAppId;
    private String SelectedAppName;
    private String APKPath;
    private String PackageName;
    private String device = "phone";
    private String rotate = "portrait";
    private int step;
    public int test;

    // Restrict the constructor from being instantiated
    private Globals() {
    }

    //Ability to set and receive global variable to use across fragments.
    public void setSelection(int d) {
        this.Selection = d;
    }

    public int getSelection() {
        return this.Selection;
    }

    public void setSelectedAppId(int d) {
        this.SelectedAppId = d;
    }

    public int getSelectedAppId() {
        return this.SelectedAppId;
    }

    public void setSelectedAppName(String d) {
        this.SelectedAppName = d;
    }

    public String getSelectedAppName() {
        return this.SelectedAppName;
    }

    public void setAPKPath(String d) {
        this.APKPath = d;
    }

    public String getAPKPath() {
        return this.APKPath;
    }

    public void setPackageName(String d) {
        this.PackageName = d;
    }

    public String getPackageName() {
        return this.PackageName;
    }

    public void setdevice(String d) {
        this.device = d;
    }

    public String getdevice() {
        return this.device;
    }

    public void setrotate(String d) {
        this.rotate = d;
    }

    public String getrotate() {
        return this.rotate;
    }

    public void setstep(int d) {
        this.step = d;
    }

    public int getstep() {
        return this.step;
    }

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }
}