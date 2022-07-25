package com.example.medication.Screens.Detail

import androidx.lifecycle.*
import com.example.medication.DatabaseRepository
import com.example.medication.Medication
import com.example.medication.ScheduleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: DatabaseRepository
) : ViewModel() {
    val uid = savedStateHandle.get<Int>("uid")!!
    val isEditMode = savedStateHandle.get<Boolean>("edit")!!

    var medication = repository.getMedication(uid).asLiveData()
    var schedule = repository.getScheduleByMedicationId(uid).asLiveData()

    fun update(medication: Medication) = viewModelScope.launch() {
        repository.updateMedication(medication)
    }

    fun delete(medication: Medication) = viewModelScope.launch {
        repository.deleteMedication(medication)
    }

    fun addScheduleItem(scheduleItem: ScheduleItem) = viewModelScope.launch {
        repository.addSheduleItem(scheduleItem)
    }

    fun updateScheduleItem(scheduleItem: ScheduleItem) = viewModelScope.launch {
        repository.updateSheduleItem(scheduleItem)
    }

    fun deleteScheduleItem(scheduleItem: ScheduleItem) = viewModelScope.launch {
        repository.deleteSheduleItem(scheduleItem)
    }

    fun prettyName(name: String): String = if (name == "") "Untitled" else name

}