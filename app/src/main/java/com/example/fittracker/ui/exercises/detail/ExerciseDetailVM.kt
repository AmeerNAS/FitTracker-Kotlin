package com.example.fittracker.ui.exercises.detail

import androidx.lifecycle.*
import com.example.fittracker.data.local.db.AppDatabase
import com.example.fittracker.data.local.entity.WorkoutLogEntity
import com.example.fittracker.data.local.repo.ExerciseRepo
import com.example.fittracker.data.local.repo.WorkoutRepo
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

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