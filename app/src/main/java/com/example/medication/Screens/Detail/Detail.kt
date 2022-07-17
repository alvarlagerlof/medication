package com.example.medication

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medication.Screens.Detail.DetailViewModel
import com.example.medication.Screens.Detail.ScheduleListItem
import com.example.medication.ui.theme.MedicationTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun Detail(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarScrollState()
    )

    val medication: Medication? by viewModel.medication.observeAsState()
    val schedule: List<ScheduleItem>? by viewModel.schedule.observeAsState()

    val dialogOpen = remember { mutableStateOf(false) }

    MedicationTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                medication?.let { medication ->
                                    schedule?.let { schedule ->
                                        if (medication.name == "" && schedule.size == 0 && !viewModel.isEditMode) {
                                            viewModel.delete(medication)
                                        }
                                    }
                                }
                                navController.navigateUp()
                            }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    },
                    actions = {
                        if (viewModel.isEditMode) {
                            IconButton(
                                onClick = {
                                    dialogOpen.value = true
                                }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = ""
                                )
                            }
                        }
                    },
                    title = {
                        Text(
                            text = if (viewModel.isEditMode) "Edit ${
                                viewModel.prettyName(
                                    medication?.name ?: ""
                                )
                            }" else "Add new medication"
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { contentPadding ->
            if (dialogOpen.value) {
                AlertDialog(
                    onDismissRequest = {
                        dialogOpen.value = false
                    },
                    title = {
                        Text(text = "Delete medication?")
                    },
                    text = {
                        Text(text = "You can't undo this later.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                dialogOpen.value = false
                                navController.navigateUp()
                                viewModel.delete(medication!!)
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                dialogOpen.value = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp)
            ) {

                if (schedule != null) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 24.dp),
                    ) {
                        item {
                            Details(medication = medication, update = viewModel::update)
                        }
                        item {
                            Divider(color = MaterialTheme.colorScheme.outline)
                        }
                        item {
                            Header(
                                medication = medication,
                                addScheduleItem = viewModel::addScheduleItem
                            )
                        }
                        items(schedule!!, key = { it.uid }) { item ->
                            Box(Modifier.animateItemPlacement() ) {
                                ScheduleListItem(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Details(medication: Medication?, update: (medication: Medication) -> Unit) {
    var name by rememberSaveable { mutableStateOf<String?>(null) }

    OutlinedTextField(
        value = name ?: medication?.name ?: "",
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        onValueChange = {
            name = it
            if (medication != null) {
                update(medication.copy(name = it))
            }
        },
        label = { Text("Name") },
        singleLine = true
    )
}


@Composable
fun Header(
    medication: Medication?,
    addScheduleItem: (scheduleItem: ScheduleItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Schedule", style = MaterialTheme.typography.titleMedium)
        Button(
            onClick = {
                addScheduleItem(
                    ScheduleItem(
                        uid = 0,
                        medicationUid = medication?.uid ?: 0,
                        amount = 0f,
                        time = LocalTime.now(),
                        onMondays = true,
                        onTuesdays = true,
                        onWednedays = true,
                        onThursdays = true,
                        onFridays = true,
                        onSaturdays = true,
                        onSundays = true
                    )
                )
            }) {
            Text("Add")
        }
    }
}


