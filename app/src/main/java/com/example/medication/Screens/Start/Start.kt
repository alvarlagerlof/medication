package com.example.medication

import TabSettings
import TabTimeline
import android.app.AlarmManager
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medication.Screens.Start.StartViewModel
import com.example.medication.Screens.Start.TabMedications
import com.example.medication.ui.theme.MedicationTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Start(
    navController: NavController,
    viewModel: StartViewModel = hiltViewModel(),
    alarmManager: AlarmManager
) {
    var selectedItem by remember { mutableStateOf(0) }

    val tabs = listOf(
        Pair("Medications", Icons.Outlined.Medication),
        Pair("Timeline", Icons.Outlined.Timeline),
        Pair("Settings", Icons.Outlined.Settings)
    )

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())

    val coroutineScope = rememberCoroutineScope()

    MedicationTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                SmallTopAppBar(
//                    modifier = Modifier.height(
//                        92.dp + WindowInsets.statusBars.asPaddingValues()
//                            .calculateBottomPadding()
//                    ),
                    title = {
                        Text(
//                            modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues()),
                            text = tabs[selectedItem].first
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            content = { innerPadding ->
                Surface(Modifier.padding(innerPadding)) {
                    AnimatedContent(
                        targetState = selectedItem,
                        transitionSpec = {
                            slideInVertically { 64 } + fadeIn() with
                                    fadeOut()
                        }
                    )
                    { selectedItem ->
                        when (selectedItem) {
                            0 -> TabMedications(navController, alarmManager = alarmManager)
                            1 -> TabTimeline()
                            2 -> TabSettings()
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                if (selectedItem == 0) {
                    LargeFloatingActionButton(
                        shape = RoundedCornerShape(percent = 100),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        onClick = {
                            coroutineScope.launch {
                                val uid = viewModel.addMedication(Medication(0, ""))
                                navController.navigate("detail?uid=${uid}&edit=${false}")
                            }
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = "Localized description",
                            modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize),
                        )
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.height(
                        76.dp + WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
                    ), contentPadding = WindowInsets.navigationBars.asPaddingValues()
                ) {
                    tabs.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.second,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(
                                    modifier = Modifier.padding(top = 20.dp),
                                    text = item.first
                                )
                            },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
            }
        )
    }
}





