/*
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.fittracker.R
import com.example.fittracker.data.local.db.AppDatabase
import com.example.fittracker.databinding.FragmentDashboardBinding
import kotlinx.coroutines.launch

class DashboardFragmentBackUp : Fragment() {

    private val viewModel: DashboardViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
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
        "head" to R.drawable.ms_head
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)


        /*val recyclerView = root.findViewById<RecyclerView>(R.id.progressionList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProgressionAdapter(emptyList())
        recyclerView.adapter = adapter*/

        return root


        //! old progression approach
        //viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
//        viewModel.progression.observe(viewLifecycleOwner) { progression ->
//            adapter.updateData(progression.sortedByDescending { it.progression })
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        val container: FrameLayout = view.findViewById(R.id.body_container)
        val bestBodyGroup: TextView = binding.bestMuscleGroups


        setupBodyOverlay(container)
        lifecycleScope.launch {
            val volumes = viewModel.getMuscleGroupVolumes()

            // Add each muscle drawable programmatically
            muscleDrawables.forEach { (muscle, resId) ->
                val imageView = ImageView(requireContext()).apply {
                    setImageResource(resId)

                    val level = getLevelForVolume(volumes[muscle] ?: 0)
                    val colorRes = when (level) {
                        0 -> R.color.gray
                        1 -> R.color.bronze
                        2 -> R.color.silver
                        3 -> R.color.gold
                        4 -> R.color.diamond
                        5 -> R.color.champion
                        else -> R.color.gray
                    }

                    val color = ContextCompat.getColor(requireContext(), colorRes)
                    setColorFilter(color)

                }


                container.addView(imageView)
            }

            // Set top 3 muscle groups text
            val topGroups = volumes.entries.sortedByDescending { it.value }.take(3)
            val top3MG = getString(
                R.string.top_muscle_groups,
                topGroups.getOrNull(0)?.key ?: "-",
                topGroups.getOrNull(1)?.key ?: "-",
                topGroups.getOrNull(2)?.key ?: "-"
            )
            bestBodyGroup.text = top3MG


        }


        /* old setting
            val topGroups = volumes.entries.sortedByDescending { it.value }.take(3)
            val top3MG = buildString {
                appendLine("1. ${topGroups.getOrNull(0)?.key ?: "-"}")
                appendLine("2. ${topGroups.getOrNull(1)?.key ?: "-"}")
                appendLine("3. ${topGroups.getOrNull(2)?.key ?: "-"}")
            }

            binding.bestMuscleGroups.setText(top3MG)
*/
        /*val topGroups = volumes.entries.sortedByDescending { it.value }.take(3)
            binding.bestMuscleGroups.text = getString(
                R.string.top_muscle_groups,
                topGroups.getOrNull(0)?.key ?: "-",
                topGroups.getOrNull(1)?.key ?: "-",
                topGroups.getOrNull(2)?.key ?: "-"
            )*/
    }

    /*private fun setGroupColor(drawable: VectorDrawableCompat, groupName: String, color: Int) {
        val group = drawable.findGroupByName(groupName) ?: return
        for (i in 0 until group.childCount) {
            val child = group.getChildAt(i)
            if (child is VectorDrawableCompat.VFullPath) {
                child.fillColor = ColorStateList.valueOf(color)
            }
        }
    }*/

    fun getLevelForVolume(volume: Int): Int {
        return when {
            volume < 200 -> 1 // Bronze
            volume < 500 -> 2 // Silver
            volume < 1000 -> 3 // Gold
            volume < 1500 -> 4 // Diamond
            else -> 5 // Champion
        }
    }

    /*fun colorMuscleGroup(imageView: ImageView, groupName: String, color: Int) {
        val context = imageView.context
        val drawable = AnimatedVectorDrawableCompat.create(context, R.drawable.bg_muscles_front)

        imageView.setImageDrawable(drawable)
        repeat(10) { // cannot find a way to return number of paths in a group so assume largest number of paths
            val animator = ObjectAnimator.ofArgb(drawable, groupName, "fillColor", color).apply {
                duration = 0
                setEvaluator(ArgbEvaluator())
            }
            animator.start()
        }
    }*/

    // AndroidSVG approach
    /*fun colorSvgGroup(imageView: ImageView, groupId: String, fillColor: Int) {
        try {
            // Load SVG from res/raw
            val svg = SVG.getFromResource(imageView.context, R.raw.bg_muscles)

            // Get the group by its ID
            val group = svg.getElementById(groupId)
            if (group is com.caverock.androidsvg.SVG.SvgConditionalContainer) {
                group.baseStyle.fill = com.caverock.androidsvg.SVG.Colour(fillColor)
            }

            // Render to PictureDrawable and set it to the ImageView
            val picture = svg.renderToPicture()
            val drawable = PictureDrawable(picture)

            imageView.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null) // Disable hardware accel for SVG
            imageView.setImageDrawable(drawable)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

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

    /*private fun setMuscleColor(container: FrameLayout, muscle: String, @ColorRes colorRes: Int) {
        val iv = container.findViewWithTag<ImageView>(muscle) ?: return
        val drawable = iv.drawable.mutate()
        drawable.setTint(ContextCompat.getColor(requireContext(), colorRes))
        iv.setImageDrawable(drawable)
    }*/

    //CSS injection approach
    fun buildSvgCss(volumes: Map<String, Int>, context: Context): String {
        val builder = StringBuilder()
        for ((muscle, volume) in volumes) {
            val level = getLevelForVolume(volume)
            val colorRes = when (level) {
                1 -> R.color.bronze
                2 -> R.color.silver
                3 -> R.color.gold
                4 -> R.color.diamond
                else -> R.color.champion
            }
            val color = ContextCompat.getColor(context, colorRes)
            val hexColor = String.format("#%06X", 0xFFFFFF and color)
            builder.append("#$muscle { fill: $hexColor; }\n")
        }
        return builder.toString()
    }
}
*/