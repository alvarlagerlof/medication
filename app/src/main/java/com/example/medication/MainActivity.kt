package com.example.medication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.medication.ui.theme.MedicationTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
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
                                    .calculateBottomPadding()),
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
                                0 -> PageMedications()
                                1 -> PageTimeline()
                                2 -> PageSettings()
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        LargeFloatingActionButton(
                            onClick = { /* do something */ },
                            shape = RoundedCornerShape(percent = 100),
                            elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Localized description",
                                modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize),
                            )
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageMedications() {
    val medications = listOf(
        "Olanzapine",
        "Lithionit",
        "Melatonin",
        "Imovane",
        "Olanzapine",
        "Lithionit",
        "Melatonin",
        "Imovane",
        "Olanzapine",
        "Lithionit",
        "Melatonin",
        "Imovane"
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        itemsIndexed(medications) { _, item ->
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()

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
fun PageTimeline() {
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
fun PageSettings() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text("Settings not done")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MedicationTheme {
        Greeting("Android")
    }
}