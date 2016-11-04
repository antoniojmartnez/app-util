package com.ibs.tecnicos.seyte.app;

import android.content.pm.PackageManager;

public class PackageInfo {
    private App app;

    public PackageInfo (App app) {
        this.app = app;
    }

    public int getVersionCode () {
        try {
            return this.app.getContext().getPackageManager().getPackageInfo(this.app.getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public String getVersionName () {
        try {
            return this.app.getContext().getPackageManager().getPackageInfo(this.app.getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getPackageName () {
        return this.app.getContext().getPackageName();
    }
}
