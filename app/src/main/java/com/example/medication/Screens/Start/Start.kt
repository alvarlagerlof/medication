package com.example.medication

import TabSettings
import TabTimeline
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medication.Screens.Start.TabMedications
import com.example.medication.ui.theme.MedicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Start(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val tabs = listOf(
        Pair("Medications", Icons.Filled.List),
        Pair("Timeline", Icons.Filled.CheckCircle),
        Pair("Settings", Icons.Filled.Settings)
    )

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())

    MedicationTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                SmallTopAppBar(
                    modifier = Modifier.height(
                        92.dp + WindowInsets.statusBars.asPaddingValues()
                            .calculateBottomPadding()
                    ),
                    title = {
                        Text(
                            modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues()),
                            text = tabs[selectedItem].first
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            content = { innerPadding ->
                Surface(Modifier.padding(innerPadding)) {
                    when (selectedItem) {
                        0 -> TabMedications(navController)
                        1 -> TabTimeline()
                        2 -> TabSettings()
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                if (selectedItem == 0) {
                    LargeFloatingActionButton(
                        shape = RoundedCornerShape(percent = 100),
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        onClick = { navController.navigate("detail") }
                    ) {
                        Icon(
                            Icons.Filled.Add,
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





