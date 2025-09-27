package com.example.fittracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exerciseId: Long,
    val date: Long,
    val sets: Int,
    val reps: Int,
    val weight: Float?, // nullable
)