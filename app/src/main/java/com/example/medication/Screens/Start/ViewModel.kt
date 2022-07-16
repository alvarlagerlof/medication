package com.example.medication.Screens.Start

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.medication.DatabaseRepository
import com.example.medication.Medication
import com.example.medication.MedicationDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val repository: DatabaseRepository
) : ViewModel() {
    val medications: LiveData<List<Medication>> = repository.medications.asLiveData()
}