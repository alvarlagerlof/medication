package com.example.medication

import androidx.annotation.NonNull
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
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

    @TypeConverter
    fun fromStringLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun toLocalDateTime(value: LocalDateTime?): String? {
        return value?.toString()
    }

/*    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }*/
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

@Entity(tableName = "timeline")
data class TimelineItem(
    @NonNull
    @PrimaryKey(autoGenerate = true) val uid: Int,
    var dateTime: LocalDateTime,
    var type: String,
    var amount: Float
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
    @Query("SELECT * FROM schedule_item")
    fun getAll(): Flow<List<ScheduleItem>>

    @Query("SELECT * FROM schedule_item WHERE uid LIKE :uid")
    fun findByUid(uid: Int): Flow<ScheduleItem>

    @Query("SELECT * FROM schedule_item WHERE medicationUid LIKE :medicationUid ORDER BY time")
    fun findByMedicationUid(medicationUid: Int): Flow<List<ScheduleItem>>

    @Insert
    suspend fun insert(scheduleItem: ScheduleItem): Long

    @Update
    suspend fun update(scheduleItem: ScheduleItem)

    @Delete
    suspend fun delete(scheduleItem: ScheduleItem)
}

@Dao
interface TimelineDao {
    @Insert
    suspend fun insert(timelineItem: TimelineItem): Long

    @Update
    suspend fun update(timelineItem: TimelineItem)

    @Delete
    suspend fun delete(timelineItem: TimelineItem)
}

@Database(
    entities = [Medication::class, ScheduleItem::class, TimelineItem::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun scheduleItemDao(): ScheduleItemDao
    abstract fun timelineDao(): TimelineDao
}

class DatabaseRepository @Inject constructor(
    private val medicationDao: MedicationDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val timelineDao: TimelineDao
) {
    val medications: Flow<List<Medication>> = medicationDao.getAll()
    val schedule: Flow<List<ScheduleItem>> = scheduleItemDao.getAll()

    fun getMedication(uid: Int): Flow<Medication> = medicationDao.findByUid(uid)

    suspend fun addMedication(medication: Medication): Long = medicationDao.insert(medication)

    suspend fun updateMedication(medication: Medication) = medicationDao.update(medication)

    suspend fun deleteMedication(medication: Medication) = medicationDao.delete(medication)

    fun getScheduleItem(scheduleItemUid: Int): Flow<ScheduleItem> =
        scheduleItemDao.findByUid(scheduleItemUid)

    fun getScheduleByMedicationId(medicationUid: Int): Flow<List<ScheduleItem>> =
        scheduleItemDao.findByMedicationUid(medicationUid)

    suspend fun addSheduleItem(scheduleItem: ScheduleItem) = scheduleItemDao.insert(scheduleItem)

    suspend fun updateSheduleItem(scheduleItem: ScheduleItem) = scheduleItemDao.update(scheduleItem)

    suspend fun deleteSheduleItem(scheduleItem: ScheduleItem) = scheduleItemDao.delete(scheduleItem)

    suspend fun addTimelineItem(timelineItem: TimelineItem) = timelineDao.insert(timelineItem)

    suspend fun updateTimelineItem(timelineItem: TimelineItem) = timelineDao.update(timelineItem)

    suspend fun deleteTimelineItem(timelineItem: TimelineItem) = timelineDao.delete(timelineItem)
}

