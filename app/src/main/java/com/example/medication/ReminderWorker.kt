package com.example.medication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import java.time.LocalTime
import javax.inject.Inject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted parameters: WorkerParameters,
    private val repository: DatabaseRepository
) :
    CoroutineWorker(appContext, parameters) {

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override suspend fun doWork(): Result {
        SystemClock.sleep(1000 * 60 * 30)

        if (!inputData.hasKeyWithValueOfType<Int>("scheduleItemId")) {
            // TODO reschedule all alarms
            return Result.failure()
        }

        DailyWork.schedule(
            applicationContext,
            inputData.getInt("scheduleItemId", 0),
            LocalTime.parse(inputData.getString("localTime")!!)
        )

        repository.getScheduleItem(inputData.getInt("scheduleItemId", 0)).collect { scheduleItem ->
            repository.getMedication(scheduleItem.medicationUid).collect { medication ->
                setForeground(createForegroundInfo(medication.name, scheduleItem.amount))
            }
        }

        return Result.success()
    }

    private fun createForegroundInfo(name: String, amount: Float): ForegroundInfo {
        // This PendingIntent can be used to cancel the worker
        val cancelIntent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        /* val fullScreenIntent = Intent(applicationContext, AlarmActivity::class.java)
         fullScreenIntent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP
         val fullScreenPendingIntent = PendingIntent.getActivity(
             applicationContext, 0,
             fullScreenIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
         )*/

        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle("Time to take ${name}")
            .setContentText("Take ${amount} pills")
            .setSmallIcon(R.drawable.notification_icon)
            //.setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            // .addAction(android.R.drawable.ic_delete, "Cancel", intent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            //.setCategory(NotificationCompat.CATEGORY_CALL)
            //.setFullScreenIntent(fullScreenPendingIntent, true)
            .setColorized(true)
            .setColor(0xFFEB0F)
            .addAction(0, "Taken", cancelIntent)
            .addAction(0, "Wont take", cancelIntent)
//            .setOngoing(true)
//            .setAutoCancel(false)
            .build()

        return ForegroundInfo(System.currentTimeMillis().toInt(), notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            "default",
            "Default",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Reminder notificatios"
        }

        // Register the channel with the system
        notificationManager.createNotificationChannel(channel)
    }

}