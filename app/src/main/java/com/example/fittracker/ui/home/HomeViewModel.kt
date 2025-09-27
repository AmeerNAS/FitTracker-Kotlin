package com.example.fittracker.ui.home

import androidx.lifecycle.ViewModel
import com.example.fittracker.data.local.dao.DailySummaryDao
import com.example.fittracker.data.local.entity.DailySummaryEntity
import android.util.Log

//suggested to use flow, kinda better than 2d list
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

import java.util.Calendar

sealed class ForUI {
    object Empty : ForUI()
    data class HasData(val stats: LifetimeStats) : ForUI()
}

class HomeViewModel(
    private val dailySummaryDao: DailySummaryDao
) : ViewModel() {

    val summaries: Flow<List<DailySummaryEntity>> = dailySummaryDao.getAll()
    fun getTodaySummary(): Flow<DailySummaryEntity?> {
        val today = todayEpochMillis()
        return dailySummaryDao.getByDate(today)
    }

//    fun getLifetimeStats(): Flow<LifetimeStats> {
//
//        return dailySummaryDao.getAll().map { list ->
//            LifetimeStats(
//                totalWorkouts = list.size,
//                totalVolume = list.sumOf { it.totalVolume },
//                totalExercises = list.sumOf { it.totalExercises }
//            )
//        }
//    }

    // sealed class approach
    val forUI: Flow<ForUI> =
        dailySummaryDao.getAll().map { list ->
            if (list.isEmpty()) {
                ForUI.Empty
            } else {
                ForUI.HasData(
                    LifetimeStats(
                        totalWorkouts = list.size,
                        totalVolume = list.sumOf { it.totalVolume },
                        totalExercises = list.sumOf { it.totalExercises }
                    )
                )
            }
        }


    private fun todayEpochMillis(): Long {
        val now = Calendar.getInstance()
        now.set(Calendar.HOUR_OF_DAY, 0)
        now.set(Calendar.MINUTE, 0)
        now.set(Calendar.SECOND, 0)
        now.set(Calendar.MILLISECOND, 0)
        return now.timeInMillis
    }
}

data class LifetimeStats(
    val totalWorkouts: Int,
    val totalVolume: Int,
    val totalExercises: Int
)