package com.example.medication

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medication.Screens.Detail.DetailViewModel
import com.example.medication.Screens.Detail.Form
import com.example.medication.ui.theme.MedicationTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detail(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())

    val medication: Medication? by viewModel.medication.observeAsState()

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
                        if (viewModel.isEditMode) {
                            IconButton(
                                modifier = Modifier.padding(
                                    top = WindowInsets.statusBars.asPaddingValues()
                                        .calculateTopPadding()
                                ), onClick = {
                                    navController.navigateUp()
                                    viewModel.delete(medication!!)
                                }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = ""
                                )
                            }
                        }
                    },
                    title = {
                        Text(
                            text = if (viewModel.isEditMode) "Edit ${medication?.name}" else "Add new medication"
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


