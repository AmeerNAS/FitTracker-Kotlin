package com.example.fittracker.ui.exercises.detail

import androidx.lifecycle.*
import com.example.fittracker.data.local.db.AppDatabase
import com.example.fittracker.data.local.entity.WorkoutLogEntity
import com.example.fittracker.data.local.repo.ExerciseRepo
import com.example.fittracker.data.local.repo.WorkoutRepo
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

/**
 * ExerciseDetailVM
 *
 * ViewModel for the Exercise Detail screen.
 *
 * Responsibilities:
 * - Loads a specific exercise by ID from theExerciseRepo.
 * - Exposes the exercise as LiveData for the UI to observe.
 * - Handles marking an exercise as done by logging sets, reps, and weight into the WorkoutRepo.
 *
 * Features:
 * - Uses [LiveData] and [switchMap] to reactively load exercise data.
 * - Uses [viewModelScope] to perform data operations.
 *
 * Dependencies:
 * - [ExerciseRepo]
 * - [WorkoutRepo]
 *
 * Usage:
 * - Able to [loadExercise] with an exercise ID to begin observing data.
 * - able to [markExerciseDone] to log a completed set.
 */
class ExerciseDetailVM(
    private val exerciseRepo: ExerciseRepo,
    private val workoutRepo: WorkoutRepo
) : ViewModel() {

    private val _exerciseId = MutableLiveData<Long>()

    val exercise: LiveData<com.example.fittracker.domain.model.Exercise?> =
        _exerciseId.switchMap { id ->
            liveData {
                emit(exerciseRepo.getExerciseById(id))
            }
        }

    fun loadExercise(id: Long) {
        _exerciseId.value = id
    }



    // refactored hard code
    fun markExerciseDone(sets: Int, reps: Int, weight: Float?) {
        val ex = exercise.value ?: return
        viewModelScope.launch {
            val log = WorkoutLogEntity(
                exerciseId = ex.id,
                date = todayEpochMillis(),
                sets = sets,
                reps = reps,
                weight = weight
            )
            workoutRepo.logWorkout(log)
        }
    }


    private fun todayEpochMillis(): Long {
        return LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    }
}