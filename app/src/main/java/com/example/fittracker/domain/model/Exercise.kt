package com.example.fittracker.domain.model

data class Exercise(
    val id: Long = 0,
    val name: String,
    val muscleGroup: MuscleGroup = MuscleGroup.CHEST, // Chest, Legs
    val isCustom: Boolean = false,
    val desc: String? = null
)

