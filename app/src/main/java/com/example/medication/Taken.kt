package com.example.medication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class Taken : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("Taken", "onReceive")

        val notificationId = intent?.getIntExtra("notification_id", 0)

        Log.d("Taken", notificationId.toString())

        with(NotificationManagerCompat.from(context)) {
            if (notificationId != null) {
                cancel(notificationId)
            }
        }
    }
}