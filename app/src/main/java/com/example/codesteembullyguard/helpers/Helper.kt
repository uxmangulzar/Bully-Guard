package com.example.codesteembullyguard.helpers
import android.app.usage.UsageStatsManager
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object Helper {

    fun getTop5AppsOnDate(context: Context, targetDate: String): List<Pair<String, Long>> {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val date = dateFormat.parse(targetDate)

        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val startTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)

        val endTime = calendar.timeInMillis

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )

        val appUsageMap = mutableMapOf<String, Long>()
        val packageManager = context.packageManager

        for (usageStats in usageStatsList) {
            val packageName = usageStats.packageName

            // Check if the package name is not a system app

            if (packageManager.getLaunchIntentForPackage(packageName) != null) {
                val totalUsageTimeMillis = usageStats.totalTimeInForeground
                val totalUsageTimeMinutes = totalUsageTimeMillis / (1000 * 60)
                appUsageMap[packageName] = appUsageMap.getOrDefault(packageName, 0L) + totalUsageTimeMinutes
            }
        }

        val sortedApps = appUsageMap.entries.sortedByDescending { it.value }

        return sortedApps.take(60).map { Pair(it.key, it.value) }
    }

    fun getUsageTimeForPackageOnDate(context: Context, packageName: String, targetDate: String): Long {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val date = dateFormat.parse(targetDate)

        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val startTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)

        val endTime = calendar.timeInMillis

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )

        val packageManager = context.packageManager

        for (usageStats in usageStatsList) {
            val packageStatsPackageName = usageStats.packageName

            if (packageStatsPackageName == packageName) {
                // Check if the package name is not a system app
                if (packageManager.getLaunchIntentForPackage(packageName) != null) {
                    return usageStats.totalTimeInForeground
                }
            }
        }

        // Package not found or not used on the specified date
        return 0
    }
   private fun isSystemApp(packageName: String): Boolean {
        return packageName.startsWith("com.android") || packageName == "android"
    }
}
