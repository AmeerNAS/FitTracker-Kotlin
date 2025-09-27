package com.example.fittracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fittracker.domain.model.Exercise
import com.example.fittracker.domain.model.MuscleGroup

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val muscleGroup: String,    // store as String in DB
    val isCustom: Boolean,
    val desc: String?
) {
    fun toDomain(): Exercise = Exercise(
        id = id,
        name = name,
        muscleGroup = try {
            MuscleGroup.valueOf(muscleGroup)
        } catch (e: Exception) {
            MuscleGroup.CHEST
        },
        isCustom = isCustom,
        desc = desc
    )

    companion object {
        fun fromDomain(exercise: Exercise): ExerciseEntity = ExerciseEntity(
            id = exercise.id,
            name = exercise.name,
            muscleGroup = exercise.muscleGroup.name,
            isCustom = exercise.isCustom,
            desc = exercise.desc
        )
    }
}