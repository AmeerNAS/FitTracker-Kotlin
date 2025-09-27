package com.example.fittracker.logic

class StepCounterHelper {

    private var startSteps: Int = -1

    fun onSensorChanged(newSensorValue: Float): Int {
        val currentSteps = newSensorValue.toInt()
        if (startSteps == -1) {
            startSteps = currentSteps
            return 0
        }
        return currentSteps - startSteps
    }

    fun reset() {
        startSteps = -1
    }
}