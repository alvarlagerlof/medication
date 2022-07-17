package com.example.medication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*


private const val TAG = "AlarmsChangedReceiver"

class AlarmsChangedReceiver : BroadcastReceiver() {
    /*
    1. Confirm that your app still has the special app access. To do so, call canScheduleExactAlarms(). This check protects your app from the case where the user grants your app the permission, then revokes it almost immediately afterward.
    2. Reschedule any exact alarms that your app needs, based on its current state. This logic should be similar to what your app does when it receives the ACTION_BOOT_COMPLETED broadcast.
    */

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            42, intent, PendingIntent.FLAG_CANCEL_CURRENT
//        )

        Log.d(TAG, "onRecieve changed alarms")

        val i = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)

       val calNow: Calendar = Calendar.getInstance()
//        val calSet: Calendar = calNow.clone() as Calendar
//        calSet.set(Calendar.HOUR_OF_DAY, 22)
//        calSet.set(Calendar.MINUTE, 0)
//        calSet.set(Calendar.SECOND, 0)
//        calSet.set(Calendar.MILLISECOND, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(calNow.timeInMillis + 1000*10, pendingIntent), pendingIntent)
        }
    }
}