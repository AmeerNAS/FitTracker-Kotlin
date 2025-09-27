package com.example.fittracker.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fittracker.data.local.dao.ExerciseDao
import com.example.fittracker.data.local.dao.WorkoutLogDao
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

/**
 * DashboardViewModel
 *
 * ViewModel for the Dashboard fragment, responsible for aggregating workout volume data
 * per muscle.
 *
 * Retrieves all exercises and workout logs from local DAOs.
 * Maps exercise IDs to their corresponding muscle groups.
 * Calculates total training volume per muscle group by combining:
 *     `volume = sets × reps × weight`
 *
 *
 * Uses [combine] to reactively merge exercise and workout log flows.
 *
 * Dependencies:
 * - [ExerciseDao]
 * - [WorkoutLogDao]
 */
class DashboardViewModel(
    private val exerciseDao: ExerciseDao,
    private val workoutLogDao: WorkoutLogDao
) : ViewModel() {

    // template code
    /*private val _text = MutableLiveData<String>().apply {
        postValue("This is dashboard Fragment")
    }
    val text: LiveData<String> = _text*/



    val muscleGroupVolumes = combine(
        exerciseDao.getAll(),   // Flow<List<Exercise>>
        workoutLogDao.getAllLogs() // Flow<List<WorkoutLog>>
    ) { exercises, logs ->

        // I have no Idea why this works
        val idToMuscleGroup = exercises.associate { it.id to it.muscleGroup }

        logs.groupBy { idToMuscleGroup[it.exerciseId] ?: "Unknown" }
            .mapValues { entry ->
                entry.value.sumOf { ((it.weight ?: 0f) * it.reps * it.sets).toInt() }
            }
    }

}
