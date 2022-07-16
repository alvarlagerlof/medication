package com.example.medication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabMedications(navController: NavController) {
    val medications = listOf(
        "Olanzapine",
        "Lithionit",
        "Melatonin",
        "Imovane",
        "Other",
        "Other 2",
        "Other 3",
        "Other 4",
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        itemsIndexed(medications) { index, item ->
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { navController.navigate("detail?id=$index") }

            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = item, style = MaterialTheme.typography.titleLarge)
                    Text("Next at 22:00")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabTimeline() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text("Timeline not done")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabSettings() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text("Settings not done")
        }
    }
}