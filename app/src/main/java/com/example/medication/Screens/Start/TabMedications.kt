package com.example.medication.Screens.Start

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medication.Medication
import com.example.medication.SendNotification
import java.util.*

val TAG = "TabMedications"

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabMedications(
    navController: NavController,
    viewModel: StartViewModel = hiltViewModel(),
    alarmManager: AlarmManager
) {
    val medications: List<Medication>? by viewModel.medications.observeAsState()

    val context = LocalContext.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxHeight(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {

            FilledTonalButton(onClick = {
//                val i = Intent(context, MainActivity::class.java)
//                val pendingIntent =
//                    PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)

                Log.d(TAG, "tapped button")


                val calNow: Calendar = Calendar.getInstance()
//                val calSet: Calendar = calNow.clone() as Calendar
//                calSet.set(Calendar.HOUR_OF_DAY, 22)
//                calSet.set(Calendar.MINUTE, 33)
//                calSet.set(Calendar.SECOND, 0)
//                calSet.set(Calendar.MILLISECOND, 0)

                if (alarmManager.canScheduleExactAlarms()) {
                    val intent = Intent(context, SendNotification::class.java)
                    val pendingIntent =
                        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

                    alarmManager.set(RTC_WAKEUP, calNow.timeInMillis, pendingIntent)

                    Log.d(TAG, "scheduled alarm")

//                    alarmManager.setAlarmClock(
//                        AlarmManager.AlarmClockInfo(
//                            calSet.timeInMillis,
//                            pendingIntent
//                        ), pendingIntent
//                    )
                }

            }, content = { Text("Add alarm") })
        }
        itemsIndexed(medications ?: emptyList()) { index, item ->
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    navController.navigate("detail?uid=${item.uid}&edit=${true}")
                }

            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = viewModel.prettyName(item.name),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text("Next at 22:00")
                }
            }
        }
    }
}