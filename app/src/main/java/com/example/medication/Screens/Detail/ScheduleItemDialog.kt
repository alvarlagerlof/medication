package com.example.medication.Screens.Detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medication.ScheduleItem
import java.time.LocalTime

@Composable
fun ScheduleItemDialog(
    viewModel: DetailViewModel = hiltViewModel(),
    open: MutableState<Boolean>,
    scheduleItem: ScheduleItem?,
    isEditMode: Boolean,
) {
    var time by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }

    if (open.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                open.value = false
            },
            title = {
                Text(text = if (isEditMode) "Edit" else "Add")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Time") })
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount") })
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Log.d("size", viewModel.schedule.value!!.size.toString())
                        viewModel.addScheduleItem(
                            ScheduleItem(
                                uid = 0,
                                medicationUid = viewModel.medication.value!!.uid,
                                amount = amount.toFloat(),
                                time = LocalTime.now()
                            )
                        )
                        open.value = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        open.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
