package com.example.medication

import android.content.Context
import android.util.Log
import androidx.work.*
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class DailyWork {
    companion object {
        fun schedule(context: Context, scheduleItemId: Int, time: LocalTime) {
            Log.d("schedule", scheduleItemId.toString() + " - " + time.toString())

            val now = LocalTime.now()
            val timeDiff = Duration.between(time, now)

            Log.d("timeDiff", timeDiff.toString())

            val data: Data.Builder = Data.Builder()
            data.putInt("scheduleItemId", scheduleItemId)
            data.putString("localTime", time.toString())

            val request =
                OneTimeWorkRequestBuilder<ReminderWorker>()
                   /* .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS)*/
                    .setInitialDelay(timeDiff)
                    .setInputData(data.build())
                    .build()

            WorkManager
                .getInstance(context)
                .enqueueUniqueWork(scheduleItemId.toString(), ExistingWorkPolicy.REPLACE, request)
        }
    }

}
