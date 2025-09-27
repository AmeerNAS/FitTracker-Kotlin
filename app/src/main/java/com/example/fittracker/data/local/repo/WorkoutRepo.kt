package com.example.fittracker.data.local.repo

import com.example.fittracker.data.local.dao.DailySummaryDao
import com.example.fittracker.data.local.dao.ExerciseDao
import com.example.fittracker.data.local.dao.WorkoutLogDao
import com.example.fittracker.data.local.entity.DailySummaryEntity
import com.example.fittracker.data.local.entity.WorkoutLogEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutRepo(
    private val logDao: WorkoutLogDao,
    private val summaryDao: DailySummaryDao,
    private val exerciseDao: ExerciseDao
) {

    /**
     * Insert a workout log and automatically update its daily summary.
     */
    suspend fun logWorkout(log: WorkoutLogEntity) = withContext(Dispatchers.IO) {
        // Insert raw log
        logDao.insert(log)

        val muscleGroup = getMuscleGroupForExercise(log.exerciseId) ?: "Unknown"

        val logVolume = log.sets * log.reps * (log.weight ?: 1f).toInt()

        val existingSummary = summaryDao.getSummary(log.date)
        if (existingSummary != null) {
            val updated = existingSummary.copy(
                totalVolume = existingSummary.totalVolume + logVolume,
                totalExercises = existingSummary.totalExercises + 1,
                musclesTrained = mergeMuscles(existingSummary.musclesTrained, muscleGroup)
            )
            summaryDao.insert(updated)
        } else {
            val newSummary = DailySummaryEntity(
                date = log.date,
                totalVolume = logVolume,
                totalExercises = 1,
                musclesTrained = muscleGroup
            )
            summaryDao.insert(newSummary)
        }
    }

    /**
     * Look up the muscle group of an exercise.
     * Returns null if exercise not found.
     */
    private suspend fun getMuscleGroupForExercise(exerciseId: Long): String? {
        val exercise = exerciseDao.getById(exerciseId)
        return exercise?.muscleGroup
    }

    private fun mergeMuscles(existing: String, newMuscle: String): String {
        val set = existing.split(",").filter { it.isNotBlank() }.toMutableSet()
        set.add(newMuscle)
        return set.joinToString(",")
    }


}