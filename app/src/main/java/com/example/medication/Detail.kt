package com.example.medication

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medication.ui.theme.DetailViewModel
import com.example.medication.ui.theme.MedicationTheme
import com.example.medication.ui.theme.ScheduleItem
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detail(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val medication = viewModel.medication.collectAsState().value

    Log.d("Tag", medication.schedule.size.toString())

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())


    MedicationTheme {
        Scaffold(modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    modifier = Modifier.height(
                        160.dp + WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
                    ),
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier.padding(
                                top = WindowInsets.statusBars.asPaddingValues()
                                    .calculateTopPadding()
                            ), onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    },
                    actions = {
                        if (viewModel.isCreate) {
                            IconButton(
                                modifier = Modifier.padding(
                                    top = WindowInsets.statusBars.asPaddingValues()
                                        .calculateTopPadding()
                                ), onClick = { /* TODO */ }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = ""
                                )
                            }
                        }
                    },
                    title = {
                        Text(
                            text = if (viewModel.isCreate) "Add new medication" else "Edit ${medication.name}"
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Form()
            }
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(viewModel: DetailViewModel = hiltViewModel()) {
    val openDialog = remember { mutableStateOf(false) }

    val currentScheduleItem = rememberSaveable() {
        mutableStateOf<ScheduleItem?>(null)
    }

    val medication = viewModel.medication.collectAsState().value

    ScheduleItemDialog(
        open = openDialog,
        scheduleItem = currentScheduleItem.value,
        addScheduleItem = viewModel::addScheduleItem,
        isCreate = viewModel.isCreate
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = medication.name,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.setName(it) },
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

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            itemsIndexed(medication.schedule) { _, item ->
                ScheduleListItem(item)
            }
        }
    }
}

@Composable
fun ScheduleItemDialog(
    open: MutableState<Boolean>,
    scheduleItem: ScheduleItem?,
    isCreate: Boolean,
    addScheduleItem: (scheduleItem: ScheduleItem) -> Unit
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
                Text(text = if (isCreate) "Add" else "Edit")
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
                        addScheduleItem(ScheduleItem(amount.toFloat(), LocalTime.now()))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleListItem(scheduleItem: ScheduleItem) {
    Row {
        Text(scheduleItem.time.toString(), style = MaterialTheme.typography.bodyMedium)
        Text(scheduleItem.amount.toString())
    }

}