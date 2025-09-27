package com.example.fittracker.domain.model

data class WorkoutSession(
    val id: Int = 0,
    val date: Long, // timestamp
    val notes: String = ""
)
