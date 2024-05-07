package com.example.codesteembullyguard.helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppInfoRetriever {

    public static AppInfo getAppInfo(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        AppInfo appInfo = new AppInfo();

        try {
            ApplicationInfo appInfoObj = packageManager.getApplicationInfo(packageName, 0);
            appInfo.appName = packageManager.getApplicationLabel(appInfoObj).toString();
            appInfo.appIcon = packageManager.getApplicationIcon(appInfoObj);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appInfo;
    }

    public static class AppInfo {
        public String appName;
        public Drawable appIcon;
    }
}

