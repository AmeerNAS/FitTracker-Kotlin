package com.example.fittracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fittracker.data.local.entity.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: WorkoutLogEntity): Long

    @Query("SELECT * FROM workout_logs WHERE exerciseId = :exerciseId ORDER BY date DESC")
    fun getLogsByExercise(exerciseId: Long): Flow<List<WorkoutLogEntity>>

    //
    @Query("SELECT * FROM workout_logs WHERE date = :date")
    suspend fun getLogsByDate(date: Long): List<WorkoutLogEntity>

    @Query("SELECT * FROM workout_logs WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getLogsByDateRange(start: Long, end: Long): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<WorkoutLogEntity>>


    @Query("DELETE FROM workout_logs")
    suspend fun deleteAll()
}