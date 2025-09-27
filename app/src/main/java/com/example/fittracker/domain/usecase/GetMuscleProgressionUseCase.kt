package com.example.fittracker.domain.usecase

import com.example.fittracker.data.local.repo.ExerciseRepo
import com.example.fittracker.domain.model.MuscleGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMuscleProgressionUseCase(private val repo: ExerciseRepo) {

    data class MuscleProgression(
        val muscleGroup: MuscleGroup,
        val progression: Int // could be reps/weight/session count
    )

    operator fun invoke(): Flow<List<MuscleProgression>> {
        return repo.getExercises().map { exercises ->
            MuscleGroup.values().map { group ->
                val total = exercises.filter { it.muscleGroup == group }.size
                MuscleProgression(group, total)
            }
        }
    }
}