package com.example.fittracker.ui.exercises.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.example.fittracker.R
import com.example.fittracker.databinding.ActivityExerciseRunningTimerBinding
import com.example.fittracker.logic.StepCounterHelper
import com.example.fittracker.ui.exercises.detail.ExerciseDetailAcTimer.Companion.EXTRA_ELAPSED_TIME
import com.example.fittracker.ui.exercises.detail.ExerciseDetailAcTimer.Companion.EXTRA_EXERCISE_ID


//leider, idk how to test the sensor in a real example, for now only placeholder shows

/**
 * RunningActivity
 *
 * This activity tracks a running exercise by:
 * - Starting a timer (chronometer)
 * - Counting steps using the step counter sensor
 * - Returning results (steps + time) to the calling activity
 */
class RunningActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityExerciseRunningTimerBinding

    private var exerciseId: Int = -1

    //Step sensor vals
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var startSteps: Int = -1

    private val stepCounterHelper = StepCounterHelper()

    private var totalSteps: Int = 0

    private var startTime: Long = 0L


    //TODO properly setup layour inflator and stepcounter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseRunningTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //sensor setup
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        //start it
        startTime = SystemClock.elapsedRealtime()
        binding.simpleChronometer.base = startTime
        binding.simpleChronometer.start()
        startTime = SystemClock.elapsedRealtime()


        binding.btnFinish.setOnClickListener {
            val elapsedMillis = SystemClock.elapsedRealtime() - startTime
            val resultIntent = Intent().apply {
                putExtra("steps", totalSteps)
                putExtra("elapsed_time", elapsedMillis)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // Start timer from AcTimer
        startTime = SystemClock.elapsedRealtime()
        binding.simpleChronometer.base = startTime
        binding.simpleChronometer.start()

        //! Data transfer Intent 2
        // Finish button
        binding.btnFinish.setOnClickListener {
            val elapsedMillis = SystemClock.elapsedRealtime() - startTime
            val resultIntent = Intent().apply {
                putExtra(EXTRA_EXERCISE_ID, exerciseId)
                putExtra(EXTRA_ELAPSED_TIME, elapsedMillis)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
    companion object {
        const val EXTRA_EXERCISE_ID = "exercise_id"
        const val EXTRA_STEP_COUNTER = "steps"
        const val EXTRA_ELAPSED_TIME = "elapsed_time"
    }


    override fun onResume() {
        super.onResume()
        stepSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = stepCounterHelper.onSensorChanged(event.values[0])
            binding.stepsCount.text = "Steps: $totalSteps"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

