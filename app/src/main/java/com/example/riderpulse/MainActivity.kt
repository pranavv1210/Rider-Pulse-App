package com.example.riderpulse

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var driveModeButton: Button
    private lateinit var customMessageEditText: EditText
    private lateinit var aboutButton: ImageButton
    private lateinit var sharedPreferences: SharedPreferences

    private val PERMISSION_REQUEST_CODE = 101

    // An array of the permissions we need
    private val permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ANSWER_PHONE_CALLS,
        Manifest.permission.SEND_SMS,
        Manifest.permission.MANAGE_OWN_CALLS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("RiderPulsePrefs", Context.MODE_PRIVATE)
        driveModeButton = findViewById(R.id.driveModeButton)
        customMessageEditText = findViewById(R.id.customMessageEditText)
        aboutButton = findViewById(R.id.aboutButton)

        if (!hasPermissions()) {
            requestPermissions()
        }

        // Load the saved message when the app starts
        val savedMessage = sharedPreferences.getString("custom_sms_message", "")
        customMessageEditText.setText(savedMessage)

        // Set the initial button state
        updateButtonUI()

        driveModeButton.setOnClickListener {
            if (hasPermissions()) {
                if (!isNotificationServiceEnabled()) {
                    Toast.makeText(this, "Please grant Notification Access to block notifications.", Toast.LENGTH_LONG).show()
                    val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                    startActivity(intent)
                    return@setOnClickListener
                }
                toggleDriveMode()
            } else {
                Toast.makeText(this, "Permissions are required to activate Drive Mode.", Toast.LENGTH_SHORT).show()
                requestPermissions()
            }
        }

        aboutButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val packageName = packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).toTypedArray()
            for (name in names) {
                if (!TextUtils.isEmpty(name) && name.contains(packageName)) {
                    return true
                }
            }
        }
        return false
    }

    private fun hasPermissions(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                updateButtonUI()
            } else {
                Toast.makeText(this, "Permissions were not granted.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleDriveMode() {
        val isDriveModeOn = sharedPreferences.getBoolean("driveModeState", false)
        val editor = sharedPreferences.edit()

        if (isDriveModeOn) {
            editor.putBoolean("driveModeState", false)
            stopService(Intent(this, RiderPulseService::class.java))
        } else {
            editor.putBoolean("driveModeState", true)
            startService(Intent(this, RiderPulseService::class.java))
        }

        editor.apply()
        updateButtonUI()
    }

    private fun updateButtonUI() {
        val isDriveModeOn = sharedPreferences.getBoolean("driveModeState", false)
        driveModeButton.setTextColor(ContextCompat.getColor(this, R.color.white))
        if (isDriveModeOn) {
            driveModeButton.text = "DRIVE MODE ON"
            driveModeButton.setBackgroundResource(R.drawable.circular_button_on)
        } else {
            driveModeButton.text = "DRIVE MODE OFF"
            driveModeButton.setBackgroundResource(R.drawable.circular_button_off)
        }
    }
}