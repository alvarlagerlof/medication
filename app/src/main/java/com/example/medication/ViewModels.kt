package com.example.medication.ui.theme

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalTime
import javax.inject.Inject

data class ScheduleItem(var amount: Float, var time: LocalTime)

data class Medication(var id: Int, var name: String, var schedule: MutableList<ScheduleItem>)

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val id: String? = savedStateHandle.get<String>("id")

    val isCreate = id == null

    private val mutableState = MutableStateFlow<Medication>(
        when (id) {
            null -> Medication(0, "", mutableListOf())
            "0" -> Medication(0, "Olanzapine", mutableListOf(ScheduleItem(5f, LocalTime.now())))
            "1" -> Medication(0, "Lithionit", mutableListOf(ScheduleItem(3f, LocalTime.now())))
            else -> Medication(0, "Melatonin", mutableListOf(ScheduleItem(2f, LocalTime.now())))
        }
    )
    val medication = mutableState.asStateFlow()

    fun setName(name: String) {
        mutableState.value = mutableState.value.copy(name = name)
    }

    fun addScheduleItem(scheduleItem: ScheduleItem) {
        val newSchedule = mutableState.value.schedule.toMutableList().apply { add(scheduleItem) }
        mutableState.value = mutableState.value.copy(schedule = newSchedule)

    }
}