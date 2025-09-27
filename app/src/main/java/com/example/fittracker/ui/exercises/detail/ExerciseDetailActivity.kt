package com.example.fittracker.ui.exercises.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fittracker.R
import com.example.fittracker.data.local.db.AppDatabase
import com.example.fittracker.data.local.repo.ExerciseRepo
import com.example.fittracker.data.local.repo.WorkoutRepo
import com.example.fittracker.databinding.ActivityExerciseDetailBinding
import com.example.fittracker.ui.util.applySystemBarPadding
import com.example.fittracker.ui.exercises.detail.map_of_embeds

/**
 * ExerciseDetailActivity
 *
 * This activity displays detailed information about a selected exercise,
 * including its name, description, muscle group, and a related video (if available).
 * It also allows the user to:
 * - Start the exercise (timer or running-specific)
 * - Mark the exercise as done (log sets, reps, weight)
 * - Handles the Dialog to mark the exercise as done.
 *
 * Usage:
 * This activity is launched when the user clicks on an exercise.
 * It provides complete overview of the exercise entity with 2 buttons, "Mark as done" and a "Start Exercise" button.
 *
 * Data Passed:
 * - Input: [EXTRA_EXERCISE_ID] (Int) — ID of the current exercise.
 *
 */
class ExerciseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailBinding
    private var openMarkDone: Boolean = false

    private val viewModel: ExerciseDetailVM by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        val exerciseRepo = ExerciseRepo(db.exerciseDao())
        val workoutRepo = WorkoutRepo(db.workoutLogDao(), db.dailySummaryDao(), db.exerciseDao())

        // Gotten from OS
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ExerciseDetailVM(exerciseRepo, workoutRepo) as T
            }
        }
    }

    //show timer approach
    private val timerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("IntentDebug","init TimeLauncher started")
                val elapsedTime = result.data?.getLongExtra(
                    ExerciseDetailAcTimer.EXTRA_ELAPSED_TIME, 0L
                ) ?: 0L
                Log.d("IntentDebug","Trying to open showMarkDone with ET = " + elapsedTime.toString())
                showMarkDoneDialog(elapsedTime)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.applySystemBarPadding()

        // Back btn Handling
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Getting exerciseId from intent
        val exerciseId = intent.getLongExtra(EXTRA_EXERCISE_ID, -1L)

        // no id
        if (exerciseId == -1L) finish()

        viewModel.loadExercise(exerciseId)
        val webView = binding.root.findViewById<WebView>(R.id.videoContainer)

        // For the selected exercise
        viewModel.exercise.observe(this) { exercise ->
            if (exercise != null) {
                binding.exerciseName.text = exercise.name
                binding.exerciseDescription.text = exercise.desc
                binding.muscleGroup.text = exercise.muscleGroup.name

                val videoUrl = ExerciseVideoHelper.getVideoUrlForExercise(this, exercise.name)

                // Video only if built-in & has link
                if (!exercise.isCustom && videoUrl != null) {
                    binding.videoContainer.visibility = View.VISIBLE
                    webView.loadData(videoUrl, "text/html", "utf-8")

                    //! maybe change to a function that protects from vulnerabilities
                    webView.getSettings().javaScriptEnabled = true;
                } else {
                    binding.videoContainer.visibility = View.GONE
                }

            }
        }

        //! hard code intent for running in btnStartExercise
        // TODO find a better solution

        // Start btn
        binding.btnStartExercise.setOnClickListener {
            val exercise = viewModel.exercise.value ?: return@setOnClickListener

            if (exercise.name.equals("Running", ignoreCase = true)) {
                // 🔄 Start RunningActivity (custom running timer)
                val intent = Intent(this, RunningActivity::class.java).apply {
                    putExtra(RunningActivity.EXTRA_EXERCISE_ID, exerciseId)
                }
                timerLauncher.launch(intent)
            } else {
                // ⏱ Start regular timer
                val intent = Intent(this, ExerciseDetailAcTimer::class.java).apply {
                    putExtra(ExerciseDetailAcTimer.EXTRA_EXERCISE_ID, exerciseId)
                }
                timerLauncher.launch(intent)
            }
        }

        // Mark done btn
        binding.btnMarkDone.setOnClickListener {
            showMarkDoneDialog(elapsedTime = null)
        }
    }

    // helper class
    object ExerciseVideoHelper {


        val embeds = map_of_embeds
        val frameHeight = 240
        fun getVideoUrlForExercise(context: Context, exerciseName: String): String? {
            return when (exerciseName.lowercase()) {
                "bench press" -> "<iframe height=\"$frameHeight\" src=\"https://www.youtube.com/embed/8k_57QM4jg8?si=Nylf9ae27ZVB8LXb\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                "squat" -> "<iframe height=\"$frameHeight\" src=\"https://www.youtube.com/embed/zcnGWJpSCS8?si=l46Zm1E6bPRmK0d9\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                "deadlift" -> "<iframe height=\"$frameHeight\" src=\"https://www.youtube.com/embed/bbfpCuiGBsI?si=aSFUWySQP0RTm-t1\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                "running" -> "<iframe height=\"$frameHeight\" src=\"https://www.youtube.com/embed/HbBnFx5D_9s?si=DqxKf2e6LITmHto-\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                "bicep curl" -> "<iframe height=\"$frameHeight\" src=\"https://www.youtube.com/embed/6oN92X74wak?si=27zk9I4C65ujgL1C\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                "tricep dip" -> "<iframe height=\"$frameHeight\" src=\"https://www.youtube.com/embed/ahAxEN--nXc?si=rt64N3lkoP3Ot3Ju\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                "pull-up" -> "<iframe height=\"$frameHeight\" src=\"https://www.youtube.com/embed/TMnxKjdYcME?si=5XNn-qA7gytj9v32\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                "neck harness" -> "<iframe height=\"$frameHeight\" src=\"https://www.youtube.com/embed/iRUcLWIri6E?si=5fLS_NLHV-2wOZmV\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                "shoulder press" -> "<iframe height=\"$frameHeight\" src=\"https://www.youtube.com/embed/ZCldXaJJEnk?si=dfN7BTTnWk9XFOnI\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                else -> null
            }
        }
    }



    //TODO change location to a dialogs file, import instead
    private fun showMarkDoneDialog(elapsedTime: Long?) {
        Log.d("IntentDebug","ShowMarkDont Started")
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_mark_done, null)
        val setsInput = dialogView.findViewById<EditText>(R.id.inputSets)
        val repsInput = dialogView.findViewById<EditText>(R.id.inputReps)
        val weightInput = dialogView.findViewById<EditText>(R.id.inputWeight)
        val timeText = dialogView.findViewById<TextView>(R.id.timeElapsed)

        // Show elapsed time if available
        if (elapsedTime != null && elapsedTime > 0) {
            val seconds = (elapsedTime / 1000) % 60
            val minutes = (elapsedTime / 1000) / 60
            timeText.text = "Elapsed Time: ${minutes}m ${seconds}s"
            timeText.visibility = android.view.View.VISIBLE
        } else {
            timeText.visibility = android.view.View.GONE
        }

        AlertDialog.Builder(this)
            .setTitle("Log Exercise")
            .setView(dialogView)
            // save on positive
            .setPositiveButton("Save") { _, _ ->
                val sets = setsInput.text.toString().toIntOrNull() ?: 1
                val reps = repsInput.text.toString().toIntOrNull() ?: 10
                val weight = weightInput.text.toString().toFloatOrNull()

                viewModel.markExerciseDone(sets, reps, weight)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    companion object {
        private const val EXTRA_EXERCISE_ID = "exercise_id"

        //! Background Data Transfer 1
        fun newIntent(context: Context, exerciseId: Long): Intent {
            return Intent(context, ExerciseDetailActivity::class.java).apply {
                putExtra(EXTRA_EXERCISE_ID, exerciseId)
            }
        }
    }
}