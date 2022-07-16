package com.example.medication.Screens.Start

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medication.Medication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabMedications(navController: NavController, viewModel: StartViewModel = hiltViewModel()) {
//    val medications = listOf(
//        "Olanzapine",
//        "Lithionit",
//        "Melatonin",
//        "Imovane",
//        "Other",
//        "Other 2",
//        "Other 3",
//        "Other 4",
//    )
    val medications: List<Medication>? by viewModel.medications.observeAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        itemsIndexed(medications ?: emptyList()) { index, item ->
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    navController.navigate("detail?uid=${item.uid}")
                }

            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = item.name, style = MaterialTheme.typography.titleLarge)
                    Text("Next at 22:00")
                }
            }
        }
    }
}