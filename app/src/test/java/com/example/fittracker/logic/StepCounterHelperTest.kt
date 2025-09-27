package com.example.fittracker.logic

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StepCounterHelperTest {

    private lateinit var stepCounterHelper: StepCounterHelper

    @Before
    fun setup() {
        stepCounterHelper = StepCounterHelper()
    }

    @Test
    fun testInitialStepCountIsZero() {
        val result = stepCounterHelper.onSensorChanged(1000f)
        assertEquals(0, result)
    }

    @Test
    fun testStepCountIncreasesCorrectly() {
        stepCounterHelper.onSensorChanged(1000f) // Initialize
        val result = stepCounterHelper.onSensorChanged(1007f)
        assertEquals(7, result)
    }

    @Test
    fun testResetWorksCorrectly() {
        stepCounterHelper.onSensorChanged(1000f)
        stepCounterHelper.onSensorChanged(1005f)
        stepCounterHelper.reset()
        val result = stepCounterHelper.onSensorChanged(2000f)
        assertEquals(0, result)
    }
}