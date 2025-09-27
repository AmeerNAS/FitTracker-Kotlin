package com.example.fittracker.domain.model

enum class MuscleGroup {
    CHEST,
    NECK,
    THIGHS,
    BICEPS,
    TRICEPS,
    LEGS,
    CORE,
    DELTOILDS;

    // user-friendly label
    fun label(): String = name.lowercase().replaceFirstChar { it.uppercaseChar() }
}


