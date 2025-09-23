package com.example.riderpulse

import android.content.Context
import android.content.SharedPreferences
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationBlockerService : NotificationListenerService() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("RiderPulsePrefs", Context.MODE_PRIVATE)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        val isDriveModeOn = sharedPreferences.getBoolean("driveModeState", false)

        if (isDriveModeOn) {
            // Cancel the notification if Drive Mode is on
            if (sbn != null) {
                cancelNotification(sbn.key)
                Log.d("NotificationBlocker", "Notification from ${sbn.packageName} was blocked.")
            }
        }
    }
}