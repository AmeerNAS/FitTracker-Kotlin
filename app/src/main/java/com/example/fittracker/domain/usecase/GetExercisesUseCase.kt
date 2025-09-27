package com.example.fittracker.domain.usecase

import com.example.fittracker.data.local.repo.ExerciseRepo
import com.example.fittracker.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

class GetExercisesUseCase(private val repository: ExerciseRepo) {
    suspend operator fun invoke(): Flow<List<Exercise>> {
        return repository.getExercises()
    }
}