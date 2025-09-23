package com.example.riderpulse

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import android.Manifest

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val prefs = context.getSharedPreferences("RiderPulsePrefs", Context.MODE_PRIVATE) ?: return
        val isDriveModeOn = prefs.getBoolean("driveModeState", false)

        Log.d("CallReceiver", "Received phone state broadcast. Drive mode is: $isDriveModeOn")

        if (!isDriveModeOn || !hasPermissions(context)) {
            return
        }

        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            Log.d("CallReceiver", "Incoming call from: $incomingNumber")

            if (incomingNumber != null) {
                // The automated SMS is sent here
                val customMessage = prefs.getString("custom_sms_message", "The person you are trying to reach is currently driving and will get back to you shortly.")
                if (customMessage != null) {
                    sendSms(incomingNumber, customMessage)
                }
            } else {
                Log.d("CallReceiver", "Incoming call detected, but phone number is null. Cannot send SMS.")
            }
        }
    }

    private fun hasPermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    }

    private fun sendSms(phoneNumber: String, message: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d("CallReceiver", "SMS sent to $phoneNumber: $message")
        } catch (e: Exception) {
            Log.e("CallReceiver", "SMS sending failed", e)
        }
    }
}