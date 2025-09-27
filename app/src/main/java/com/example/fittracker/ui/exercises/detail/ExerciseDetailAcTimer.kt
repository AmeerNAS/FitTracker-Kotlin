package com.example.fittracker.ui.exercises.detail;

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import com.example.fittracker.databinding.ActivityExerciseTimerBinding
import java.util.Locale


class ExerciseDetailAcTimer : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseTimerBinding

    private var startTime: Long = 0L
    private var exerciseId: Long = -1L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exerciseId = intent.getLongExtra(EXTRA_EXERCISE_ID, -1L)

        // Old Timer
        /*running = true
        startTimer()*/

        // Start timer
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

    // old timer
    /*private fun startTimer() {
        // thanks to Marian P. for this
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (running) {
                    seconds++
                    val minutes = seconds / 60
                    val secs = seconds % 60
                    binding.timerText.text = String.format(Locale.US, "%02d:%02d", minutes, secs)

                }
                handler.postDelayed(this, 1000) // handler.postDelayed(this, 500)
            }
        })
    }*/

    companion object {
        const val EXTRA_EXERCISE_ID = "exercise_id"
        const val EXTRA_ELAPSED_TIME = "elapsed_time"
    }

}