package com.example.fittracker.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fittracker.R
import com.example.fittracker.data.local.db.AppDatabase
import com.example.fittracker.databinding.FragmentDashboardBinding
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return DashboardViewModel(db.exerciseDao(),db.workoutLogDao()) as T
            }
        }
    }

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var adapter: ProgressionAdapter

    private lateinit var container: FrameLayout

    //EXTRA
    private val muscleDrawables = mapOf(
        "chest" to R.drawable.ms_chest,
        "biceps" to R.drawable.ms_biceps,
        "legs" to R.drawable.ms_legs,
        "shoulders" to R.drawable.ms_deltoids,
        "core" to R.drawable.ms_core,
        "thighs" to R.drawable.ms_thighs,
        "triceps" to R.drawable.ms_triceps,
        "neck" to R.drawable.ms_neck,
        "head" to R.drawable.ms_head,
        "knees" to R.drawable.ms_knees,
        "feet" to R.drawable.ms_feet,
        "hands" to R.drawable.ms_hands
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding  = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val container: FrameLayout = view.findViewById(R.id.body_container)
        //val bestBodyGroup: TextView = binding.bestMuscleGroups

        setupBodyOverlay(binding.bodyContainer)


        // Collect volumes and update UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.muscleGroupVolumes.collect { volumes ->
                // reset all to gray
                muscleDrawables.keys.forEach { muscle ->
                    setMuscleColor(binding.bodyContainer, muscle, R.color.gray)
                }

                // update based on volumes
                volumes.forEach { (muscle, volume) ->
                    val level = getLevelForVolume(volume)
                    val colorRes = when (level) {
                        1 -> R.color.bronze
                        2 -> R.color.silver
                        3 -> R.color.gold
                        4 -> R.color.diamond
                        5 -> R.color.champion
                        else -> R.color.gray
                    }
                    setMuscleColor(binding.bodyContainer, muscle, colorRes)
                }

                // top 3 groups
                val topGroups = volumes.entries.sortedByDescending { it.value }.take(3)
                binding.bestMuscleGroups.text = getString(
                    R.string.top_muscle_groups,
                    topGroups.getOrNull(0)?.key ?: "-",
                    topGroups.getOrNull(1)?.key ?: "-",
                    topGroups.getOrNull(2)?.key ?: "-"
                )
            }
        }
    }

    fun getLevelForVolume(volume: Int): Int {
        return when {
            volume < 200 -> 1 // Bronze
            volume < 500 -> 2 // Silver
            volume < 1000 -> 3 // Gold
            volume < 1500 -> 4 // Diamond
            else -> 5 // Champion
        }
    }

    private fun setupBodyOverlay(container: FrameLayout) {
        muscleDrawables.forEach { (muscle, drawableRes) ->
            val iv = ImageView(requireContext()).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                setImageResource(drawableRes)
                adjustViewBounds = true
                scaleType = ImageView.ScaleType.FIT_CENTER
                tag = muscle // store the muscle name so we can find later
            }
            container.addView(iv)
        }
    }

    private fun setMuscleColor(container: FrameLayout, muscle: String, colorRes: Int) {
        val iv = container.findViewWithTag<ImageView>(muscle) ?: return
        val drawable = iv.drawable.mutate()
        drawable.setTint(ContextCompat.getColor(requireContext(), colorRes))
        iv.setImageDrawable(drawable)
    }

    /*private fun setMuscleColor(container: FrameLayout, muscle: String, @ColorRes colorRes: Int) {
        val iv = container.findViewWithTag<ImageView>(muscle) ?: return
        val drawable = iv.drawable.mutate()
        drawable.setTint(ContextCompat.getColor(requireContext(), colorRes))
        iv.setImageDrawable(drawable)
    }*/

    //dev func
    /*override fun onDestroyView() {
        super.onDestroyView()
        binding= null
    }*/
}
