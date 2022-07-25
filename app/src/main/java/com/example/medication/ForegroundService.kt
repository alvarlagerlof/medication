package com.example.medication

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat


class ForegroundService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // val input = intent.getStringExtra("inputExtra")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val channel = NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "Reminders"
        }

        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Register the channel with the system
        notificationManager.createNotificationChannel(channel)

        val notification: Notification = NotificationCompat.Builder(this, "default")
            .setContentTitle("Example Service")
            .setContentText("description")
            .setSmallIcon(com.example.medication.R.drawable.notification_icon)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        //do heavy work on a background thread
        //stopSelf();
        SystemClock.sleep(1000*30);


        return START_NOT_STICKY
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}