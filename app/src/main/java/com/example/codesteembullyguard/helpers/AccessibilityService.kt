
package com.example.codesteembullyguard.helpers

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.ActivityManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.codesteembullyguard.activities.OpenFromBackgroundActivity
import com.example.codesteembullyguard.db.AppDatabase
import com.example.codesteembullyguard.helpers.Helper.getUsageTimeForPackageOnDate
import com.example.codesteembullyguard.models.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AccessibilityService : android.accessibilityservice.AccessibilityService() {

    companion object {
        private const val TAG = "AccessibilityService"
    }

    private var lines = 0
    private val latestTexts = mutableListOf<String>()
    private val nextTexts = mutableListOf<String>()
    private val allSingleWords = arrayListOf(
        "airhead",
        "asshole",
        "ass-kisser",
        "bastard",
        "bimbo",
        "bugger",
        "dag",
        "dickhead",
        "flake",
        "geezer",
        "jerk",
        "psycho",
        "suicide",
        "kill",
        "sex",
        "bunk",
        "harassment",
        "alone",
        "sad",
        "fuck",
        "gay"
    )

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d(TAG, "on AccessibilityEvent")
        allSingleWords.clear()

        allSingleWords.addAll(
            listOf(
                "airhead",
                "asshole",
                "ass-kisser",
                "bastard",
                "bimbo",
                "bugger",
                "dag",
                "dickhead",
                "flake",
                "geezer",
                "jerk",
                "psycho",
                "suicide",
                "kill",
                "sex",
                "bunk",
                "harassment",
                "alone",
                "sad",
                "fuck",
                "gay",
                "je vais te tuer",
                "t'es mort",
                "tu est con",
                "comment ce suicider",
                "je veux tuer quelqu'un que j'aime pas",
                "missing"
            )
        )

        nextTexts.clear()
        getNextTexts(rootInActiveWindow)
        try {
            if (isTextChanged(latestTexts, nextTexts)) {


                latestTexts.clear()
                lines = 0
                getNodeInfoes(rootInActiveWindow, 0)



            }
        } catch (exception: Exception) {
        }
    }
    fun isSettingsAppInForeground(context: Context): Boolean {
        val settingsPackageName = "com.android.settings" // Package name for the Settings app
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses

        for (processInfo in runningAppProcesses) {
            if (processInfo.processName == settingsPackageName && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }

        return false
    }

    override fun onInterrupt() {
        Log.e(TAG, "OnInterrupt")
    }


    private fun isCurrentTimeInRange(startTime: String, endTime: String): Boolean {
        try {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentTime = Calendar.getInstance()
            val currentTimeFormatted = sdf.format(currentTime.time)

            val start = sdf.parse(startTime)
            val end = sdf.parse(endTime)
            val current = sdf.parse(currentTimeFormatted)

            if (start != null && end != null && current != null) {
                return !current.before(start) && !current.after(end)
            }
        } catch (e: Exception) {
            // Handle any parsing or comparison errors
        }

        return false
    }

    private fun isPackageNameBlocked(packageName: String?): Boolean {
        val appsModelManager = AppsModelManager(applicationContext)
        val retrievedAppsModelList = appsModelManager.getAppsModelList()

        // Check if listOneFromServer is not null and not empty
        if (retrievedAppsModelList.isNullOrEmpty()) {
            return false
        }

        // Iterate through the list and check if the package name matches any entry
        for (appModel in retrievedAppsModelList) {
            if (appModel.package_name == packageName) {
                var isInRange = isCurrentTimeInRange(appModel.from_time, appModel.to_time)
                val top5AppsUsage1 = getUsageTimeForPackageOnDate(
                    applicationContext,
                    appModel.package_name,
                    Util.getCurrentDate()
                )

                Log.e("top5AppsUsage1", java.lang.Long.toString(top5AppsUsage1 / 60000))
                if (appModel.from_time=="0"){
                    isInRange=true
                }
                val usage=top5AppsUsage1 / 60000
                var isUsageValid=true
                if (appModel.set_time!="0"){
                    if (usage>appModel.set_time.toInt()){
                        isUsageValid=false
                    }
                }
                return appModel.blocked == "yes"||!isInRange||!isUsageValid
                // Return true if blocked is "1"
            }
        }

        return false // Package name not found in the list
    }
    private fun getForegroundPackageName(context: Context): String? {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 10000 // Set the time window as needed (e.g., 10 seconds ago)

        val usageStatsList =
            usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime)

        if (usageStatsList.isNotEmpty()) {
            val sortedList = usageStatsList.sortedByDescending { it.lastTimeUsed }
            return sortedList[0].packageName
        }

        return null
    }
    fun isMyAppInForeground(context: Context): Boolean {
        val packageName = context.packageName
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses

        for (processInfo in runningAppProcesses) {
            if (processInfo.processName == packageName && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }

        return false
    }

    override fun onServiceConnected() {
        Log.d(TAG, "on Service Connected")

        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_HAPTIC
        info.notificationTimeout = 1000

        serviceInfo = info
    }

    private fun getNextTexts(node: AccessibilityNodeInfo?) {
        if (node?.text != null && node.text.isNotEmpty()) {
            nextTexts.add(node.text.toString())
        }

        if (node != null) {
            for (i in 0 until (node.childCount ?: 0)) {
                val child = node?.getChild(i)
                getNextTexts(child)
            }
        }

    }

    private fun isTextChanged(latestTexts: List<String>, nextTexts: List<String>): Boolean {
        if (nextTexts.isEmpty() || latestTexts.isEmpty()) {
            return true
        }
        for (i in latestTexts.indices) {
            if (latestTexts[i] != nextTexts[i]) {
                return true
            }
        }
        return false
    }

    private fun getNodeInfoes(node: AccessibilityNodeInfo?, tab: Int) {
        if (isMyAppInForeground(applicationContext)) {
            // Your app is currently opened and in the foreground
        } else {
            // Your app is not in the foreground
//                    val packageNameToCheck = getForegroundPackageName(applicationContext) // Replace with the actual package name to check
            if (node!!.packageName!="com.example.codesteembullyguard"&&node!!.packageName!="com.android.settings"){


                if (isPackageNameBlocked(node!!.packageName.toString())) {
                    // The package name is blocked
                    // You can take appropriate action here

                    val intent =
                        Intent(applicationContext, OpenFromBackgroundActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    applicationContext.startActivity(intent)

                } else {
                    // The package name is not blocked
                    // Handle accordingly
                }
            }
        }
        if (node == null || lines > 100) {
            return
        }

        val sb = StringBuilder()

        for (i in 0..tab) {
            sb.append("+++")
        }

        val rect = Rect()
        node.getBoundsInScreen(rect)
        sb.append("${node.packageName}==${node.className} $rect")

        if (node.isScrollable || node.className.toString().contains("ScrollView")) {
            sb.append(" <Scrollable> ")
        }

        if (node.isClickable) {
            sb.append(" [Clickable] ")
        }

        if (node.text != null && node.text.isNotEmpty()) {

            sb.append(" [Text: ${node.text} ]")

            latestTexts.add(node.text.toString())
            var containsItem = false
            for (item in allSingleWords) {
                if (node.text.toString().contains(item)) {
                    containsItem = true
                    break
                }
            }

            val result = if (containsItem) "yes" else "no"
            if (result == "yes") {
                lateinit var notification: Notification
                if (node.packageName.toString()=="com.android.chrome"){
                    notification = Notification(
                        0,
                        node.text.toString().toLowerCase(),
                        "web",
                        "not",
                        dateTime = System.currentTimeMillis() // Set the current timestamp
                        ,"com.android.chrome"

                    )
                }else{

                   notification = Notification(
                        0,
                        node.text.toString(),
                        "message",
                        "not",
                        dateTime = System.currentTimeMillis() // Set the current timestamp
                       ,node.packageName.toString()
                    )
                }

                GlobalScope.launch(Dispatchers.IO) {
                    val database = AppDatabase.getInstance(applicationContext)

                    val existingNotification = database.notificationDao().getNotificationByText(notification.notificationText)

                    if (existingNotification == null) {
                        // No matching notification found, so insert the new one
                        database.notificationDao().insert(notification)
                    } else {
                        // A notification with the same text already exists, handle it as needed
                        // You can update the existing notification or skip the insertion
                    }


                }
            }
        }

        if (node.contentDescription != null && node.contentDescription.isNotEmpty()) {
            sb.append(" [Description: ${node.contentDescription} ]")
        }

        val icon = Rect()
        node.getBoundsInScreen(icon)
        if (icon.width() == icon.height()) {
            sb.append(" [Maybe icon: ${icon.width()}, ${icon.height()}]")
        }

        Log.d(TAG, sb.toString())
        lines++

        for (i in 0 until (node.childCount ?: 0)) {
            val child = node.getChild(i)
            if (lines > 100) {
                return
            }
            getNodeInfoes(child, tab + 1)
        }
    }
}
