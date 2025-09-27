package com.example.fittracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_summaries")
data class DailySummaryEntity(
    @PrimaryKey val date: Long,
    val totalVolume: Int,
    val totalExercises: Int,
    val musclesTrained: String, // comma-separated, or better: relation table
)
