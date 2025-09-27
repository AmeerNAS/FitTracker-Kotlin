package com.example.fittracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import com.example.fittracker.R
import com.example.fittracker.data.local.db.AppDatabase
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        val ctx = requireContext().applicationContext
        val db = AppDatabase.getInstance(ctx)
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(db.dailySummaryDao()) as T
            }
        }
    }

    private var summaryText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val todaySummary = view.findViewById<TextView>(R.id.todaySummary)
        val statsSummary = view.findViewById<TextView>(R.id.statsSummary)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        // for Empty DB call
        val emptyLayout = view.findViewById<View>(R.id.emptyStateLayout)

        // Today’s workout
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTodaySummary().collect { summary ->
                todaySummary.text = if (summary != null) {
                    "Exercises: ${summary.totalExercises}, Volume: ${summary.totalVolume}, Muscles: ${summary.musclesTrained}"
                } else {
                    "No workouts yet today."
                }
            }
        }

        // Lifetime stats
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forUI.collectLatest { state: ForUI ->
                    when (state) {
                        is ForUI.Empty -> {
                            statsSummary.visibility = View.GONE
                            emptyLayout.visibility = View.VISIBLE
                        }
                        is ForUI.HasData -> {
                            statsSummary.visibility = View.VISIBLE
                            emptyLayout.visibility = View.GONE

                            statsSummary.text = getString(
                                R.string.lifetime_stats,
                                state.stats.totalWorkouts,
                                state.stats.totalVolume,
                                state.stats.totalExercises
                            )
                        }
                    }
                }
            }
        }


        // Highlight
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // TODO let user tap a date to see workouts
        }

    }
}