package com.example.codesteembullyguard.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.codesteembullyguard.models.Notification

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notification: Notification)

    @Query("SELECT * FROM notifications WHERE status = 'not' limit 5")
    suspend fun getAllNotifications(): List<Notification>

    @Query("SELECT * FROM notifications WHERE notification_text = :notificationText")
    suspend fun getNotificationByText(notificationText: String): Notification?

    @Query("UPDATE notifications SET status = :newStatus WHERE id = :notificationId")
    suspend fun updateStatusById(notificationId: Long, newStatus: String)

}
