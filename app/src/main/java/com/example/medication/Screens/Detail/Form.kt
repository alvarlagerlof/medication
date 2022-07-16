package com.example.medication.Screens.Detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medication.Medication
import com.example.medication.ScheduleItem
import com.example.medication.Screens.Start.StartViewModel
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(viewModel: DetailViewModel = hiltViewModel()) {
    val openDialog = remember { mutableStateOf(false) }

    val currentScheduleItem = rememberSaveable() {
        mutableStateOf<ScheduleItem?>(null)
    }

    val medication: Medication? by viewModel.medication.observeAsState()
    val schedule: List<ScheduleItem>? by viewModel.schedule.observeAsState()

    var name by rememberSaveable { mutableStateOf<String?>(null) }

    ScheduleItemDialog(
        open = openDialog,
        scheduleItem = currentScheduleItem.value,
        isEditMode = viewModel.isEditMode,
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = name ?: medication?.name ?: "",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                name = it
                viewModel.update(medication!!.copy(name = it))
            },
            label = { Text("Name") },
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Schedule", style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = { openDialog.value = true }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
            }

        }

        // Log.d("details", schedule!!.size.toString())

        if (schedule != null) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                itemsIndexed(schedule!!) { _, item ->
                    Row {
                        Text(item.time.toString(), style = MaterialTheme.typography.bodyMedium)
                        Text(item.amount.toString())
                    }
                }
            }
        }


    }
}