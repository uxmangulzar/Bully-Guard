package com.example.codesteembullyguard.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "notification_text") val notificationText: String,
    val type: String,
    val status: String, // Sent, NotSent
    @ColumnInfo(name = "date_time") val dateTime: Long,
    @ColumnInfo(name = "package_name") val package_name: String = "",
)
