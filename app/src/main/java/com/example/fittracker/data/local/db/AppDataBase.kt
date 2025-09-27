package com.example.fittracker.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fittracker.data.local.dao.DailySummaryDao
import com.example.fittracker.data.local.dao.ExerciseDao
import com.example.fittracker.data.local.dao.WorkoutLogDao
import com.example.fittracker.data.local.entity.DailySummaryEntity
import com.example.fittracker.data.local.entity.ExerciseEntity
import com.example.fittracker.data.local.entity.WorkoutLogEntity

@Database(
    entities = [
        ExerciseEntity::class,
        WorkoutLogEntity::class,
        DailySummaryEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutLogDao(): WorkoutLogDao
    abstract fun dailySummaryDao(): DailySummaryDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fittracker-db"
                )
                    // development-friendly: recreate DB if schema changes
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = inst
                inst
            }
        }
    }
}