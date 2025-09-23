# 🏍️ Rider Pulse App

## 📝 App Overview

Rider Pulse is a dedicated mobile application designed to enhance safety for bikers. The app’s core functionality allows users to activate a **"Drive Mode"** with a single tap, which automatically manages incoming calls and blocks distracting notifications. This ensures a focused and secure riding experience without compromising on communication. A custom automated SMS is sent to callers, informing them that the user is currently on the road and will respond shortly. The app prioritizes a sleek, minimalist user interface for easy use and a reliable background service to guarantee continuous operation.

-----

## ✨ Core Features

  - **Drive Mode Toggle:** A single circular button to activate and deactivate all safety features.
  - **Automated SMS:** Sends a customizable text message to callers when in "Drive Mode."
  - **Call Management:** Automatically rejects all incoming phone calls to prevent distractions.
  - **Notification Blocker:** Silences all notifications to ensure a distraction-free riding experience.
  - **Customizable Message:** Allows the user to set their own automated SMS message.
  - **About Screen:** Provides information and credits for the app.

-----

## 👨‍💻 Developer

  - **Name:** Pranav V

-----

## 🛠️ Technical Stack

  - **Platform:** Android (Native)
  - **Language:** Kotlin
  - **IDE:** Android Studio
  - **Version Control:** Git & GitHub

-----

## 🔒 Permissions

For Rider Pulse to function correctly, it requires the following permissions, which are requested from the user at runtime:

  - `READ_PHONE_STATE`: To detect incoming calls.
  - `ANSWER_PHONE_CALLS`: To programmatically reject incoming calls.
  - `SEND_SMS`: To send the automated text message.
  - `MANAGE_OWN_CALLS`: A modern permission required for the `phoneCall` foreground service.
  - `FOREGROUND_SERVICE`: To run in the background as a persistent service.
  - `FOREGROUND_SERVICE_PHONE_CALL`: The type of foreground service being run.
  - `BIND_NOTIFICATION_LISTENER_SERVICE`: To block incoming notifications. This must be granted manually by the user in the phone's settings.

-----

## 🚀 Setup & Installation

### 1\. Clone the Repository

To get a local copy of this project, open your terminal or command prompt and run:

```bash
git clone https://github.com/pranavv1210/Rider-Pulse-App.git
```

### 2\. Open in Android Studio

Open Android Studio and select **Open an existing Android Studio project**. Navigate to the folder you just cloned and open it.

### 3\. Install on a Device

Connect your Android phone or a running emulator to your computer. Click the green **Run** button (▶) in the Android Studio toolbar. The app will be built and installed on your device.

### 4\. Grant Permissions

When you first open the app, it will ask for several permissions. You must accept all of them for the app to function. You will also need to manually grant **Notification Access** by tapping the "Drive Mode ON" button and following the on-screen instructions.

### 5\. Testing Core Functionality

To test the call management feature, you can use the Android Debug Bridge (ADB) from your terminal:

```bash
# Navigate to your platform-tools folder
cd [path_to_android_sdk]\platform-tools

# Run the following command
adb shell am broadcast -a android.telephony.test.PHONE_STATE --es "state" "RINGING" --es "incoming_number" "555-1234"
```

The app's **CallReceiver** should then be triggered, and you will see the messages in your Logcat window.

-----

## 🤝 Contributions

This project is for personal use by Pranav V.
