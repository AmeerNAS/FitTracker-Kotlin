package com.example.fittracker.domain.model

data class WorkoutSet(
    val id: Int = 0,
    val exerciseId: Int,

    val reps: Int,
    val weight: Float
)


