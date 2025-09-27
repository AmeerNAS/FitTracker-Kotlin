package com.example.fittracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fittracker.data.local.entity.DailySummaryEntity
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List

@Dao
interface DailySummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(summary: DailySummaryEntity)

    @Query("SELECT * FROM daily_summaries WHERE date = :date")
    suspend fun getSummary(date: Long): DailySummaryEntity?

    @Query("SELECT * FROM daily_summaries WHERE date = :date")
    fun getByDate(date: Long): Flow<DailySummaryEntity?>

    @Query("SELECT * FROM daily_summaries ORDER BY date DESC")
    fun getAll(): Flow<List<DailySummaryEntity>>

    @Query("select count(*) from daily_summaries")
    fun getCount(): Flow<Int>

    @Query("DELETE FROM daily_summaries")
    suspend fun deleteAll()
}