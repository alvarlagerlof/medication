package com.example.medication

import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.WorkerThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime
import javax.inject.Inject

class Converters {
    @TypeConverter
    fun fromStringLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun toLocalTime(value: LocalTime?): String? {
        return value?.toString()
    }
}

@Entity(tableName = "medication")
data class Medication(
    @NonNull
    @PrimaryKey(autoGenerate = true) val uid: Int,
    var name: String,
)

@Entity(tableName = "schedule_item")
data class ScheduleItem(
    @NonNull
    @PrimaryKey(autoGenerate = true) val uid: Int,
    var medicationUid: Int,
    var time: LocalTime,
    var amount: Float,
    var onMondays: Boolean,
    var onTuesdays: Boolean,
    var onWednedays: Boolean,
    var onThursdays: Boolean,
    var onFridays: Boolean,
    var onSaturdays: Boolean,
    var onSundays: Boolean
)

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medication")
    fun getAll(): Flow<List<Medication>>

    @Query("SELECT * FROM medication WHERE uid LIKE :uid")
    fun findByUid(uid: Int): Flow<Medication>

    @Update
    suspend fun update(medication: Medication)

    @Insert
    suspend fun insert(medication: Medication): Long

    @Delete
    suspend fun delete(medication: Medication)
}

@Dao
interface ScheduleItemDao {
    @Query("SELECT * FROM schedule_item WHERE medicationUid LIKE :medicationUid ORDER BY time")
    fun findByMedicatitonUid(medicationUid: Int): Flow<List<ScheduleItem>>

    @Insert
    suspend fun insert(scheduleItem: ScheduleItem): Long

    @Update
    suspend fun update(scheduleItem: ScheduleItem)

    @Delete
    suspend fun delete(scheduleItem: ScheduleItem)
}

@Database(
    entities = [Medication::class, ScheduleItem::class],
    version = 1,
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
//    ],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun scheduleItemDao(): ScheduleItemDao
}

class DatabaseRepository @Inject constructor(
    private val medicationDao: MedicationDao,
    private val scheduleItemDao: ScheduleItemDao
) {
    val medications: Flow<List<Medication>> = medicationDao.getAll()

    fun getMedication(uid: Int): Flow<Medication> = medicationDao.findByUid(uid)

    suspend fun addMedication(medication: Medication): Long = medicationDao.insert(medication)

    suspend fun updateMedication(medication: Medication) = medicationDao.update(medication)

    suspend fun deleteMedication(medication: Medication) = medicationDao.delete(medication)

    fun getSchedule(medicationUid: Int): Flow<List<ScheduleItem>> =
        scheduleItemDao.findByMedicatitonUid(medicationUid)

    suspend fun addSheduleItem(scheduleItem: ScheduleItem) = scheduleItemDao.insert(scheduleItem)

    suspend fun updateSheduleItem(scheduleItem: ScheduleItem) = scheduleItemDao.update(scheduleItem)

    suspend fun deleteSheduleItem(scheduleItem: ScheduleItem) = scheduleItemDao.delete(scheduleItem)
}

