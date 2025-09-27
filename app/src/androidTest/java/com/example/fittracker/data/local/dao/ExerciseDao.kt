package com.example.fittracker.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fittracker.data.local.db.AppDatabase
import com.example.fittracker.data.local.entity.ExerciseEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExerciseDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ExerciseDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.exerciseDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insert_and_getAll_returns_items_in_desc_order() = runBlocking {
        val e1 = ExerciseEntity(name = "Bench Press", muscleGroup = "CHEST", isCustom = false, desc = "BB")
        val e2 = ExerciseEntity(name = "Squat", muscleGroup = "LEGS", isCustom = false, desc = "HB")

        val id1 = dao.insert(e1)
        val id2 = dao.insert(e2)

        val list = dao.getAll().first()
        assertEquals(2, list.size)
        // ORDER BY id DESC
        assertEquals(id2.toInt(), list[0].id)
        assertEquals("Squat", list[0].name)
        assertEquals(id1.toInt(), list[1].id)
        assertEquals("Bench Press", list[1].name)
    }
}