package com.example.fittracker.ui.activity

import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.fittracker.R
import com.example.fittracker.domain.model.MuscleGroup
import com.example.fittracker.ui.viewmodel.ExerciseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
//TODO Dead Class - Delete later

class ExerciseActivity : ComponentActivity() {

    private lateinit var viewModel: ExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)

        viewModel = ViewModelProvider(this)[ExerciseViewModel::class.java]

        // Inputs
        val nameInput = findViewById<EditText>(R.id.inputExerciseName)
        val muscleGroupInput = findViewById<EditText>(R.id.inputMuscleGroup)
        //val isCustomInput = findViewById<CheckBox>(R.id.inputCustom)
        val descInput = findViewById<EditText>(R.id.inputDesc)

        // Listeners
        val button = findViewById<Button>(R.id.addExerciseBtn)

        // Presenters
        val listView = findViewById<ListView>(R.id.exerciseListView)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        listView.adapter = adapter

        button.setOnClickListener {
            val name = nameInput.text.toString()
            val muscleGroup = muscleGroupInput.text.toString()
            //val isCustom = isCustomInput.isChecked
            val desc = descInput.text.toString()

            if (name.isNotEmpty() && muscleGroup.isNotEmpty() && desc.isNotEmpty()) {
                viewModel.addExercise(name, MuscleGroup.valueOf(muscleGroup), true, desc)
                nameInput.text.clear()
                muscleGroupInput.text.clear()
                //isCustomInput.isChecked = false
                descInput.text.clear()
            }
            if (name.isNotEmpty() && muscleGroup.isNotEmpty() && desc.isEmpty()) {
                viewModel.addExercise(name, MuscleGroup.valueOf(muscleGroup), true, null)
                nameInput.text.clear()
                muscleGroupInput.text.clear()
                //isCustomInput.isChecked = false
            }
            else {
                Toast.makeText(this, "Please Write a suitable name and muscle group!", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.exercises.collectLatest { exercises ->
                adapter.clear()
                adapter.addAll(exercises.map { "${it.name} - ${it.muscleGroup.name}" })
            }
        }
    }
}