package com.example.codesteembullyguard.helpers

import android.content.Context
import android.content.SharedPreferences
import com.example.codesteembullyguard.models.AppsModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppsModelManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppsModelPreferences", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val appsModelListKey = "appsModelList"

    fun saveAppsModelList(appsModelList: List<AppsModel>) {
        val jsonString = gson.toJson(appsModelList)
        sharedPreferences.edit().putString(appsModelListKey, jsonString).apply()
    }

    fun getAppsModelList(): List<AppsModel>? {
        val jsonString = sharedPreferences.getString(appsModelListKey, null)
        return if (jsonString != null) {
            val type = object : TypeToken<List<AppsModel>>() {}.type
            gson.fromJson(jsonString, type)
        } else {
            null
        }
    }
}
