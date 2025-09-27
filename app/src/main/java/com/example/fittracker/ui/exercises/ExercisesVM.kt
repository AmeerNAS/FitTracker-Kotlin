package com.example.fittracker.ui.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittracker.data.local.repo.ExerciseRepo
import com.example.fittracker.domain.model.Exercise
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExercisesVM(private val repo: ExerciseRepo) : ViewModel() {

    val exercises: StateFlow<List<Exercise>> = repo.getExercises().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun ensureSeeded() {
        viewModelScope.launch {
            repo.seedExercises()
        }
    }
}