package com.example.fittracker.data.local.repo

import android.util.Log
import com.example.fittracker.data.local.dao.ExerciseDao
import com.example.fittracker.data.local.entity.ExerciseEntity
import com.example.fittracker.domain.model.Exercise
import com.example.fittracker.domain.model.MuscleGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class ExerciseRepo(private val dao: ExerciseDao) {
    suspend fun addExercise(exercise: Exercise) {
        dao.insert(
            ExerciseEntity(
                name = exercise.name,
                muscleGroup = exercise.muscleGroup.name,
                isCustom = exercise.isCustom,
                desc = exercise.desc
            )
        )
    }

    fun getExercises(): Flow<List<Exercise>> =
        dao.getAll().map { list ->
            list.map { entity ->
                Exercise(
                    id = entity.id,
                    name = entity.name,
                    muscleGroup = MuscleGroup.valueOf(entity.muscleGroup),
                    isCustom = entity.isCustom,
                    desc = entity.desc
                )
            }
        }


    suspend fun getExerciseById(id: Long): Exercise? {
        val entity = dao.getById(id)
        return entity?.let {
            Exercise(
                id = it.id,
                name = it.name,
                muscleGroup = MuscleGroup.valueOf(it.muscleGroup),
                isCustom = it.isCustom,
                desc = it.desc
            )
        }
    }


    // Populating seed
    suspend fun seedExercises() {
        val existing = dao.count()
        if (existing > 0) return

        //! Tracking
        Log.i("Repo","Populating database")

        val defaults = listOf(
            Exercise(
                name = "Bench Press",
                muscleGroup = MuscleGroup.CHEST,
                isCustom = false,
                desc = "Barbell bench press"
            ),
            Exercise(
                name = "Squat",
                muscleGroup = MuscleGroup.LEGS,
                isCustom = false,
                desc = "Back squat"
            ),
            Exercise(
                name = "Pull-Up",
                muscleGroup = MuscleGroup.DELTOILDS,
                isCustom = false,
                desc = "Bodyweight pull-up"
            ),
            Exercise(
                name = "Shoulder Press",
                muscleGroup = MuscleGroup.DELTOILDS,
                isCustom = false,
                desc = "Overhead press"
            ),
            Exercise(
                name = "Bicep Curl",
                muscleGroup = MuscleGroup.BICEPS,
                isCustom = false,
                desc = "Dumbbell curl"
            ),
            Exercise(
                name = "Tricep Dip",
                muscleGroup = MuscleGroup.TRICEPS,
                isCustom = false,
                desc = "Bodyweight dip"
            ),
            Exercise(
                name = "Plank",
                muscleGroup = MuscleGroup.CORE,
                isCustom = false,
                desc = "Core hold"
            ),
            Exercise(
                name = "Running",
                desc = "Track your running distance and steps",
                muscleGroup = MuscleGroup.LEGS,
                isCustom = false,
            ),
            Exercise(
                name = "Neck Harness",
                desc = "Track your running distance and steps",
                muscleGroup = MuscleGroup.NECK,
                isCustom = false,
        )
        )

        defaults.forEach { addExercise(it) }
    }
}