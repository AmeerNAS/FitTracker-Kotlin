package com.example.fittracker.domain.usecase

import com.example.fittracker.data.local.repo.ExerciseRepo
import com.example.fittracker.domain.model.Exercise

class AddExerciseUseCase(private val repository: ExerciseRepo) {
    suspend operator fun invoke(exercise: Exercise) = repository.addExercise(exercise)
}