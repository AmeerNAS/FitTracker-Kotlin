package com.example.fittracker.data.local.dao

import com.example.fittracker.data.local.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: ExerciseEntity): Long

    @Query("SELECT * FROM exercises ORDER BY id DESC")
    fun getAll(): Flow<List<ExerciseEntity>>

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun count(): Int

    @Query("SELECT * FROM exercises WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ExerciseEntity?

    @Query("DELETE FROM exercises")
    suspend fun deleteAll()
}