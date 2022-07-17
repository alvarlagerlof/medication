package com.example.medication.Screens.Detail

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medication.ScheduleItem
import java.time.format.DateTimeFormatter
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ScheduleListItem(item: ScheduleItem, viewModel: DetailViewModel = hiltViewModel()) {
    var open by remember { mutableStateOf(false) }

    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hour: Int, minute: Int ->
            val toLocalTime = LocalTime.of(hour, minute)
            viewModel.updateScheduleItem(item.copy(time = toLocalTime))
        }, item.time.hour, item.time.minute, true
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                open = !open
            }

    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.clickable {
                    timePickerDialog.show()
                }) {
                    Text(
                        item.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                        style = MaterialTheme.typography.displayLarge,
                    )
                }

                FilledIconButton(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(24.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    onClick = { open = !open }) {
                    if (open) {
                        Icon(
                            Icons.Outlined.KeyboardArrowUp,
                            contentDescription = "Localized description"
                        )
                    } else {
                        Icon(
                            Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "Localized description"
                        )
                    }
                }
            }

            AnimatedContent(targetState = open,
                transitionSpec = {
                    if (targetState == false) {
                        slideInVertically { height -> height / 8 } + fadeIn() with
                                slideOutVertically { height -> -height / 8 } + fadeOut()
                    } else {
                        slideInVertically { height -> -height / 8 } + fadeIn() with
                                slideOutVertically { height -> height / 8 } + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                }) { open ->
                if (open) {
                    Expanded(item = item)
                } else {
                    Collapsed(item)
                }
            }
        }
    }
}


@Composable
fun Collapsed(item: ScheduleItem) {
    val days = mutableListOf<String>()
    if (item.onMondays) days += "Mon"
    if (item.onTuesdays) days += "Tue"
    if (item.onWednedays) days += "Wed"
    if (item.onThursdays) days += "Thu"
    if (item.onFridays) days += "Fri"
    if (item.onSaturdays) days += "Sat"
    if (item.onSundays) days += "Sun"

    var content = ""
    if (item.onMondays && item.onTuesdays && item.onWednedays && item.onThursdays && item.onFridays && item.onFridays && item.onSaturdays && item.onSundays) {
        content = "Every day"
    } else if (!item.onMondays && !item.onTuesdays && !item.onWednedays && !item.onThursdays && !item.onFridays && !item.onFridays && !item.onSaturdays && !item.onSundays) {
        content = "No days"
    } else {
        content = days.joinToString(separator = ", ")
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(content)
        Text(item.amount.toString() + " pills")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Expanded(viewModel: DetailViewModel = hiltViewModel(), item: ScheduleItem) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RowWeekDays(item = item, updateScheduleItem = viewModel::updateScheduleItem)
        RowAmount(item = item, updateScheduleItem = viewModel::updateScheduleItem)
        RowDelete(item = item)
    }
}

@Composable
fun RowWeekDays(item: ScheduleItem, updateScheduleItem: (item: ScheduleItem) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        WeekButton(
            label = "M",
            active = item.onMondays,
            toggle = { active -> updateScheduleItem(item.copy(onMondays = active)) }
        )
        WeekButton(
            label = "T",
            active = item.onTuesdays,
            toggle = { active -> updateScheduleItem(item.copy(onTuesdays = active)) }
        )
        WeekButton(
            label = "W",
            active = item.onWednedays,
            toggle = { active -> updateScheduleItem(item.copy(onWednedays = active)) }
        )
        WeekButton(
            label = "T",
            active = item.onThursdays,
            toggle = { active -> updateScheduleItem(item.copy(onThursdays = active)) }
        )
        WeekButton(
            label = "F",
            active = item.onFridays,
            toggle = { active -> updateScheduleItem(item.copy(onFridays = active)) }
        )
        WeekButton(
            label = "S",
            active = item.onSaturdays,
            toggle = { active -> updateScheduleItem(item.copy(onSaturdays = active)) }
        )
        WeekButton(
            label = "S",
            active = item.onSundays,
            toggle = { active -> updateScheduleItem(item.copy(onSundays = active)) }
        )
    }
}

@Composable
fun WeekButton(label: String, active: Boolean, toggle: (active: Boolean) -> Unit) {
    FilledTonalButton(
        contentPadding = PaddingValues(all = 0.dp),
        modifier = Modifier.width(41.dp),
        colors = if (active) ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) else ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = if (active) BorderStroke(0.dp, Color.Transparent) else BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        onClick = {
            toggle(!active)
        }) {
        Text(
            text = label,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowAmount(item: ScheduleItem, updateScheduleItem: (item: ScheduleItem) -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Outlined.Medication, contentDescription = null)
            Text("${item.amount.toString()} pills")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledIconButton(
                enabled = item.amount > 0f,
//                colors = IconButtonDefaults.filledTonalIconButtonColors(
//                    containerColor = MaterialTheme.colorScheme.secondary,
//                    contentColor = MaterialTheme.colorScheme.onSecondary,
//                    disabledContainerColor = MaterialTheme.colorScheme.outline,
//                    disabledContentColor = MaterialTheme.colorScheme.surface
//                ),
                onClick = {
                    updateScheduleItem(item.copy(amount = item.amount - 0.5f))
                }) {
                Icon(Icons.Outlined.Remove, contentDescription = null)
            }
            FilledIconButton(
                enabled = item.amount < 8f,
//                colors = IconButtonDefaults.filledTonalIconButtonColors(
//                    containerColor = MaterialTheme.colorScheme.secondary,
//                    contentColor = MaterialTheme.colorScheme.onSecondary,
//                    disabledContainerColor = MaterialTheme.colorScheme.outline,
//                    disabledContentColor = MaterialTheme.colorScheme.surface
//                ),
                onClick = {
                    updateScheduleItem(item.copy(amount = item.amount + 0.5f))
                }) {
                Icon(Icons.Outlined.Add, contentDescription = null)
            }
        }
    }
}

@Composable
fun RowDelete(viewModel: DetailViewModel = hiltViewModel(), item: ScheduleItem) {
    val dialogOpen = remember { mutableStateOf(false) }

    if (dialogOpen.value) {
        AlertDialog(
            onDismissRequest = {
                dialogOpen.value = false
            },
            title = {
                Text(text = "Delete scheduled time?")
            },
            text = {
                Text(text = "You can't undo this later.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogOpen.value = false
                        viewModel.deleteScheduleItem(item)
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

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            dialogOpen.value = true
        }) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = null)
            Text("Delete")
        }
    }
}