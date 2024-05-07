package com.example.codesteembullyguard.helpers;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AppUsageStatsHelper {
    public static List<String> getTop5UsedApps(Context context, long dayInMillis) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        PackageManager packageManager = context.getPackageManager();

        // Calculate the start and end times for the specified day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dayInMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endTime = calendar.getTimeInMillis();

        // Get usage stats for the specified day
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        if (usageStatsList == null || usageStatsList.isEmpty()) {
            return Collections.emptyList();
        }

        // Create a list to store package names and their total usage time
        List<AppUsageInfo> appUsageInfoList = new ArrayList<>();

        for (UsageStats usageStats : usageStatsList) {
            String packageName = usageStats.getPackageName();
            long totalUsageTime = usageStats.getTotalTimeInForeground();

            // Filter out system apps
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    appUsageInfoList.add(new AppUsageInfo(packageName, totalUsageTime));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Sort the list by usage time in descending order
        Collections.sort(appUsageInfoList, new Comparator<AppUsageInfo>() {
            @Override
            public int compare(AppUsageInfo app1, AppUsageInfo app2) {
                return Long.compare(app2.getTotalUsageTime(), app1.getTotalUsageTime());
            }
        });

        // Get the top 5 used app package names
        List<String> top5UsedApps = new ArrayList<>();
        int maxApps = Math.min(5, appUsageInfoList.size());

        for (int i = 0; i < maxApps; i++) {
            top5UsedApps.add(appUsageInfoList.get(i).getPackageName());
        }

        return top5UsedApps;
    }



        public static String getAppName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            return packageManager.getApplicationLabel(appInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return packageName;
        }
    }
}

class AppUsageInfo {
    private String packageName;
    private long totalUsageTime;

    public AppUsageInfo(String packageName, long totalUsageTime) {
        this.packageName = packageName;
        this.totalUsageTime = totalUsageTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public long getTotalUsageTime() {
        return totalUsageTime;
    }
}

