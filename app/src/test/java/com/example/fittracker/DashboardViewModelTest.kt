package com.example.fittracker

import FakeExerciseDao
import FakeWorkoutLogDao
import com.example.fittracker.data.local.entity.ExerciseEntity
import com.example.fittracker.data.local.entity.WorkoutLogEntity
import com.example.fittracker.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var fakeExerciseDao: FakeExerciseDao
    private lateinit var fakeWorkoutLogDao: FakeWorkoutLogDao
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        fakeExerciseDao = FakeExerciseDao()
        fakeWorkoutLogDao = FakeWorkoutLogDao()
        viewModel = DashboardViewModel(fakeExerciseDao, fakeWorkoutLogDao)
    }

    @Test
    fun `muscleGroupVolumes reflects logged exercises`() = runTest {
        // Arrange: one exercise for chest
        val chestExercise =
            ExerciseEntity(
                id = 1L,
                name = "Bench Press",
                muscleGroup = "CHEST",
                desc = "",
                isCustom = false
            )
        fakeExerciseDao.setExercises(listOf(chestExercise))

        // Log: 3 sets × 10 reps × 50kg
        val chestLog = WorkoutLogEntity(
            id = 1,
            exerciseId = 1,
            sets = 3,
            reps = 10,
            weight = 50f,
            date = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
        )
        fakeWorkoutLogDao.setLogs(listOf(chestLog))

        // Act
        val result = viewModel.muscleGroupVolumes.first()

        // Assert: chest volume = 3 × 10 × 50 = 1500
        Assert.assertEquals(1500, result["chest"])
    }
}