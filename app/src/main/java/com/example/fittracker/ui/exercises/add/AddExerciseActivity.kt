package com.example.fittracker.ui.exercises.add

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fittracker.R
import com.example.fittracker.data.local.db.AppDatabase
import com.example.fittracker.data.local.repo.ExerciseRepo
import com.example.fittracker.domain.model.Exercise
import com.example.fittracker.domain.model.MuscleGroup
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AddExerciseActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var muscleInput: EditText
    private lateinit var descInput: EditText
    private lateinit var addBtn: Button
    private lateinit var listView: ListView

    private lateinit var repo: ExerciseRepo
    private lateinit var adapter: ArrayAdapter<String>
    private val items = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)

        // Init UI
        nameInput = findViewById(R.id.inputExerciseName)
        //muscleInput = findViewById(R.id.inputMuscleGroup)
        descInput = findViewById(R.id.inputDesc)
        addBtn = findViewById(R.id.addExerciseBtn)
        listView = findViewById(R.id.exerciseListView)

        // Setup Repo
        val db = AppDatabase.getInstance(applicationContext)
        repo = ExerciseRepo(db.exerciseDao())

        // Setup Adapter for ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        // muscle Group list
        val muscleGroups = listOf("CHEST", "BACK", "LEGS", "DELTOIDS", "BICEPS", "TRICEPS", "CORE")
        val muscleAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, muscleGroups)

        val muscleGroupInput = findViewById<AutoCompleteTextView>(R.id.inputMuscleGroup)
        muscleGroupInput.setAdapter(muscleAdapter)

        muscleGroupInput.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) (view as AutoCompleteTextView).showDropDown()
        }

        // Load existing exercises
        lifecycleScope.launch {
            repo.getExercises().collectLatest { exercises ->
                items.clear()
                items.addAll(exercises.map { ex -> "${ex.name} (${ex.muscleGroup})" })
                adapter.notifyDataSetChanged()
            }
        }

        // Add new exercise on button click
        addBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val muscleGroupStr = muscleInput.text.toString().trim().uppercase()
            val desc = descInput.text.toString().trim()

            if (name.isEmpty() || muscleGroupStr.isEmpty()) {
                Toast.makeText(this, "Please enter name and muscle group", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Try to parse muscle group enum
            val muscleGroup = try {
                MuscleGroup.valueOf(muscleGroupStr)
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, "Invalid muscle group: $muscleGroupStr", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exercise = Exercise(
                id = 0, // Room will auto-generate
                name = name,
                muscleGroup = muscleGroup,
                isCustom = true, // always true here
                desc = desc
            )

            lifecycleScope.launch {
                repo.addExercise(exercise)
                runOnUiThread {
                    Toast.makeText(this@AddExerciseActivity, "Exercise added!", Toast.LENGTH_SHORT).show()
                    clearForm()
                }
            }
        }
    }

    private fun clearForm() {
        nameInput.text.clear()
        muscleInput.text.clear()
        descInput.text.clear()
    }
}