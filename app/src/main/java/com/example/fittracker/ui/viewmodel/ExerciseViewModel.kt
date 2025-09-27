package com.example.fittracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittracker.domain.model.Exercise
import com.example.fittracker.domain.model.MuscleGroup
import com.example.fittracker.domain.usecase.AddExerciseUseCase
import com.example.fittracker.domain.usecase.GetExercisesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//! Dead class
class ExerciseViewModel(
    private val addExercise: AddExerciseUseCase,
    private val getExercises: GetExercisesUseCase
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises

    init {
        viewModelScope.launch {
            getExercises().collect { _exercises.value = it }
        }
    }

    fun addExercise(name: String, muscleGroup: MuscleGroup, isCustom: Boolean, desc: String? = null) {
        viewModelScope.launch {
            addExercise(Exercise(name = name, muscleGroup = muscleGroup, isCustom = isCustom, desc = desc))
        }
    }
}