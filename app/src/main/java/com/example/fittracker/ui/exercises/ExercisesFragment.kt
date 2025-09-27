package com.example.fittracker.ui.exercises

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fittracker.R
import com.example.fittracker.data.local.db.AppDatabase
import com.example.fittracker.data.local.repo.ExerciseRepo
import com.example.fittracker.ui.exercises.add.AddExerciseActivity
import com.example.fittracker.ui.exercises.detail.ExerciseDetailActivity
import com.example.fittracker.ui.util.applySystemBarPadding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ExercisesFragment
 *
 * A Fragment that displays a list of available exercises.
 *
 * Responsibilities:
 * - Loads exercise data from the [ExercisesVM] ViewModel.
 * - Displays the exercises in a RecyclerView using [ExercisesAdapter].
 *
 * Navigates to:
 *   - [ExerciseDetailActivity] when an exercise is selected.
 *   - [AddExerciseActivity] to create a new exercise.
 * - Seeds default exercises into the database if none exist.
 *
 * Features:
 * Uses Kotlin Flows to observe exercise data reactively.
 * Handles lifecycle-aware collection via [lifecycleScope].
 * Applies padding to the RecyclerView to accommodate system bars.
 *
 * Dependencies:
 * - [ExerciseRepo]: for retrieving and seeding exercises.
 * - [AppDatabase]: for accessing local database DAOs.
 *
 * Layout: `fragment_exercises.xml`
 */
class ExercisesFragment : Fragment() {

    private val viewModel: ExercisesVM by viewModels {
        val ctx = requireContext().applicationContext
        val db = AppDatabase.getInstance(ctx)
        val repo = ExerciseRepo(db.exerciseDao())
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ExercisesVM(repo) as T
            }
        }
    }

    private lateinit var adapter: ExercisesAdapter
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // inflates the layout
        return inflater.inflate(R.layout.fragment_exercises, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)



        // Hook
        adapter = ExercisesAdapter(onItemClick = { exercise ->
            val intent = ExerciseDetailActivity.newIntent(requireContext(), exercise.id)
            startActivity(intent)
        })

        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = adapter

        recyclerView?.applySystemBarPadding(applyTop = true, applyBottom = true)
        // Add exercise button
        val addButton: Button = view.findViewById(R.id.btnAddExercise)
        addButton.setOnClickListener {
            val intent = Intent(requireContext(), AddExerciseActivity::class.java)
            startActivity(intent)
        }

        // Seed exercises if empty
        viewModel.ensureSeeded()

        // Observe and display exercises
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exercises.collectLatest { list ->
                adapter.submitList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
    }
}