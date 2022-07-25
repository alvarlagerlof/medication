package com.example.medication

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "app.db"
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun medicationDao(db: AppDatabase) =
        db.medicationDao() // The reason we can implement a Dao for the database

    @Singleton
    @Provides
    fun scheduleItemDao(db: AppDatabase) = db.scheduleItemDao()

    @Singleton
    @Provides
    fun timelineDao(db: AppDatabase) = db.timelineDao()
}