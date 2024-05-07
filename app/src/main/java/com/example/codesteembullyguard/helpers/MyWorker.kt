package com.example.codesteembullyguard.helpers

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.BatteryManager
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.codesteembullyguard.activities.OpenFromBackgroundActivity
import com.example.codesteembullyguard.activities.StartActivity
import com.example.codesteembullyguard.db.AppDatabase
import com.example.codesteembullyguard.helpers.Helper.getTop5AppsOnDate
import com.example.codesteembullyguard.models.AppsModel
import com.example.codesteembullyguard.models.MainResponseModel
import com.example.codesteembullyguard.network.ApiClient
import com.example.codesteembullyguard.network.ApiInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class MyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    private var blockedApps: String?="no"
    private var listOneFromServer: MutableList<AppsModel>?=ArrayList()
    private lateinit var notifications_types: String
    private var imageActualFile: java.util.ArrayList<File>? = null
    private var appNames: String? = null
    private var timeSpent: String? = null
    private var imageNames: String? = null
    private var packageNames: String? = null
    private var dataSent: String? = null
    private var dateTimes: String? = null
    private var appIcons: String? = null
    private var device_types: String? = null


    override fun doWork(): Result {
        // Get the Shared Preferences instance
        getCurrentLocation(object : LocationCallback {
            override fun onLocationResult(location: Location?) {
                if (location != null) {
                    // Do something with latitude and longitude
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Do something with latitude and longitude

                    addLocation(latitude.toString(),longitude.toString())
                } else {
                    // Handle the case where location is not available
                }
            }
        })

        try {
            updateBattery()
            startAppStatsUpload()
        }catch (ex:Exception){
            Log.e("error",ex.toString())
        }



        GlobalScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getInstance(applicationContext)
            val listOfData=database.notificationDao().getAllNotifications()
            dataSent = ""
            dateTimes = ""
            appIcons = ""
            notifications_types = ""
            device_types = ""
            for (i in listOfData){
                val notiTxt = i.notificationText?.replace(" ", "_")
                if (!dataSent.isNullOrEmpty()) {
                    // If appNames is not empty, add a comma before the next app name
                    dataSent += ","
                }
                dataSent += notiTxt

                if (!dateTimes.isNullOrEmpty()) {
                    // If appNames is not empty, add a comma before the next app name
                    dateTimes += ","
                }
                dateTimes += convertTimestampToDateTime( i.dateTime)

                if (!notifications_types.isNullOrEmpty()) {
                    // If appNames is not empty, add a comma before the next app name
                    notifications_types += ","
                }
                notifications_types += i.type

                if (!appIcons.isNullOrEmpty()) {
                    // If appNames is not empty, add a comma before the next app name
                    appIcons += ","
                }
                val appName = getAppNameFromPackageName(applicationContext, i.package_name)
                val replacedApp = appName?.replace(" ", "_")
                appIcons += "$replacedApp.png"

                if (!device_types.isNullOrEmpty()) {
                    // If appNames is not empty, add a comma before the next app name
                    device_types += ","
                }
                device_types += "phone"


                val newStatus = "Sent" // Replace with the new status you want to set

                database.notificationDao().updateStatusById(i.id, newStatus)
            }
            if (dataSent!=""){
                addNotifications()
            }
        }
        getAppStats()
        val nextWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInitialDelay(120, TimeUnit.SECONDS) // Delay in minutes
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueue(nextWorkRequest)

        return Result.success()
    }


    private fun convertTimestampToDateTime(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(callback: LocationCallback) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)

        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val location = task.result
                callback.onLocationResult(location)
            } else {
                callback.onLocationResult(null)
            }
        }
    }

    interface LocationCallback {
        fun onLocationResult(location: Location?)
    }

    private fun startAppStatsUpload(){
        val top5AppsUsage = getTop5AppsOnDate(applicationContext, Util.getCurrentDate())
        appNames = ""
        timeSpent = ""
        imageNames = ""
        packageNames = ""
        imageActualFile = ArrayList<File>()

        for (appUsage in top5AppsUsage) {
            val packageName = appUsage.first
            val usageTimeMinutes = appUsage.second

            // Now you can work with packageName and usageTimeMinutes
            // For example, print the package name and usage time:
            println("Package Name: $packageName")
            println("Usage Time (Minutes): $usageTimeMinutes")

            val appIcon = getAppIcon(applicationContext, packageName)
            val bitmap = appIcon?.let { drawableToBitmap(it) }
            val appName = getAppNameFromPackageName(applicationContext, packageName)
            val replacedApp = appName?.replace(" ", "_")

            // Save Bitmap as a File
            val imageFile = saveBitmapAsFile(applicationContext, bitmap!!, replacedApp!!)

            if (!appNames.isNullOrEmpty()) {
                // If appNames is not empty, add a comma before the next app name
                appNames += ","
            }
            // Append the current app name
            appNames += appName

            if (!packageNames.isNullOrEmpty()) {
                // If appNames is not empty, add a comma before the next app name
                packageNames += ","
            }
            // Append the current app name
            packageNames += packageName

            if (!timeSpent.isNullOrEmpty()) {
                // If timeSpent is not empty, add a comma before the next value
                timeSpent += ","
            }
            // Append the current usage time
            timeSpent += usageTimeMinutes.toString()

            if (!imageNames.isNullOrEmpty()) {
                // If imageNames is not empty, add a comma before the next value
                imageNames += ","
            }
            // Append the current image name
            imageNames += "$replacedApp.png"

            imageActualFile!!.add(imageFile!!)
        }
        uploadFile(0)
    }
    private fun addLocation(lat : String,lon:String) {

        val apiClient = ApiClient()
        val apiInterface = apiClient.client.create(ApiInterface::class.java)
        val responseModelCall = apiInterface.add_location(
            SessionManager.getStringPref(HelperKeys.CHILD_ID, applicationContext),
            lat,
            lon,
            Util.getCurrentDate()
        )
        responseModelCall.enqueue(object : Callback<MainResponseModel?> {
            override fun onResponse(
                call: Call<MainResponseModel?>,
                response: Response<MainResponseModel?>
            ) {
                val data11 = response.body()
                if (data11 != null && data11.status == 200) {

                    Log.e("status","success")
                } else {
                    Log.e("status","failed")
                }
            }

            override fun onFailure(call: Call<MainResponseModel?>, t: Throwable) {
            }
        })
    }
    private fun getBatteryPercentage(): Int {
        val batteryStatus = applicationContext.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        return if (level != -1 && scale != -1) {
            (level * 100f / scale).toInt()
        } else {
            -1 // Battery percentage not available
        }
    }
    private fun updateBattery() {

        val apiClient = ApiClient()
        val apiInterface = apiClient.client.create(ApiInterface::class.java)
        val responseModelCall = apiInterface.update_battery_percent(
            SessionManager.getStringPref(HelperKeys.CHILD_ID, applicationContext),
            getBatteryPercentage().toString()
        )
        responseModelCall.enqueue(object : Callback<MainResponseModel?> {
            override fun onResponse(
                call: Call<MainResponseModel?>,
                response: Response<MainResponseModel?>
            ) {
                val data11 = response.body()
                if (data11 != null && data11.status == 200) {

                    Log.e("status","success")
                } else {
                    Log.e("status","failed")
                }
            }

            override fun onFailure(call: Call<MainResponseModel?>, t: Throwable) {
            }
        })
    }

    private fun getAppNameFromPackageName(context: Context, packageName: String?): String? {
        return try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(
                packageName!!, 0
            )
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null // Handle the case where the package name is not found
        }
    }

    private fun saveBitmapAsFile(context: Context, bitmap: Bitmap, appName: String): File? {
        val directory = context.filesDir
        val fileName = "$appName.png"
        val file = File(directory, fileName)
        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    fun getAppIcon(context: Context, packageName: String?): Drawable? {
        try {
            val packageManager = context.packageManager
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName!!)
            if (launchIntent != null) {
                val applicationInfo = packageManager.getApplicationInfo(
                    packageName, 0
                )
                return applicationInfo.loadIcon(packageManager)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null // Return null if the app icon could not be retrieved.
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun uploadFile(index: Int) {



        // Create a RequestBody to represent the file
        val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), imageActualFile!![index])
        val fileToUpload = MultipartBody.Part.createFormData("file", imageActualFile!![index].name, requestBody)

        val apiClient = ApiClient()
        val apiInterface = apiClient.client.create(ApiInterface::class.java)
        val responseModelCall = apiInterface.uploadFile(fileToUpload)

        responseModelCall.enqueue(object : Callback<MainResponseModel> {
            override fun onResponse(call: Call<MainResponseModel>, response: Response<MainResponseModel>) {

                val serverResponse = response.body()
                if (serverResponse != null) {
                    val imageFile = imageActualFile!![index]
                    if (imageFile.exists() && imageFile.isFile) {
                        imageFile.delete()
                    }
                    if (index == imageActualFile!!.size - 1) {
                        addAppStats()
                    } else {
                        uploadFile(index + 1)
                    }
                } else {
                    assert(false)
                    Log.v("Response", serverResponse.toString())
                }
            }

            override fun onFailure(call: Call<MainResponseModel>, t: Throwable) {

            }
        })
    }

    private fun addAppStats() {


        val apiClient = ApiClient()
        val apiInterface = apiClient.client.create(ApiInterface::class.java)
        val responseModelCall = apiInterface.add_apps_stats(
            SessionManager.getStringPref(HelperKeys.CHILD_ID, applicationContext),
            appNames,
            timeSpent,
            imageNames,
            Util.getCurrentDate(),packageNames
        )
        responseModelCall.enqueue(object : Callback<MainResponseModel?> {
            override fun onResponse(
                call: Call<MainResponseModel?>,
                response: Response<MainResponseModel?>
            ) {

                val data11 = response.body()
                if (data11 != null && data11.status == 200) {

                } else {
                }
            }

            override fun onFailure(call: Call<MainResponseModel?>, t: Throwable) {

            }
        })
    }
    private fun addNotifications() {


        val apiClient = ApiClient()
        val apiInterface = apiClient.client.create(ApiInterface::class.java)
        val modifiedSentence: String = dataSent!!.replace("_", " ")

        val responseModelCall = apiInterface.add_notifications(
            SessionManager.getStringPref(HelperKeys.CHILD_ID, applicationContext),
            dateTimes,
            device_types,
            appIcons,
            notifications_types,
            modifiedSentence
        )
        responseModelCall.enqueue(object : Callback<MainResponseModel?> {
            override fun onResponse(
                call: Call<MainResponseModel?>,
                response: Response<MainResponseModel?>
            ) {

                val data11 = response.body()
                if (data11 != null && data11.status == 200) {

                } else {
                }
            }

            override fun onFailure(call: Call<MainResponseModel?>, t: Throwable) {

            }
        })
    }

    private fun getAppStats() {
        val apiClient = ApiClient()
        val apiInterface = apiClient.client.create(ApiInterface::class.java)
        val responseModelCall = apiInterface.get_all_child_apps(
            1, 100, SessionManager.getStringPref(HelperKeys.CHILD_ID, applicationContext)
        )
        responseModelCall.enqueue(object : Callback<MainResponseModel?> {
            override fun onResponse(
                call: Call<MainResponseModel?>,
                response: Response<MainResponseModel?>
            ) {
                val data11 = response.body()
                if (data11 != null && data11.status == 200) {
                    listOneFromServer = data11.allappstats
                    if (listOneFromServer!=null&& listOneFromServer!!.size!=0){

                        val appsModelManager = AppsModelManager(applicationContext)
                        appsModelManager.saveAppsModelList(listOneFromServer!!)


                    }


                } else {
                }
            }

            override fun onFailure(call: Call<MainResponseModel?>, t: Throwable) {}
        })
    }



}
