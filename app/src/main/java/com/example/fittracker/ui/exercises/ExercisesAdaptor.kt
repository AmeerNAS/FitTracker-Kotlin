package com.example.fittracker.ui.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fittracker.R
import com.example.fittracker.domain.model.Exercise

class ExercisesAdapter(
    private var items: List<Exercise> = emptyList(),
    private val onItemClick: ((Exercise) -> Unit)? = null
) : RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.exerciseName)
        private val muscleGroupTV: TextView = itemView.findViewById(R.id.muscleGroup)
        private val descTV: TextView = itemView.findViewById(R.id.exerciseDesc)
        private val customTV: TextView = itemView.findViewById(R.id.isCustom)

        fun bind(ex: Exercise) {
            nameTV.text = ex.name
            muscleGroupTV.text = ex.muscleGroup.name
            descTV.text = ex.desc ?: ""
            customTV.text = if (ex.isCustom) "Custom" else "Built-in"
            // Handle click
            itemView.setOnClickListener {
                onItemClick?.invoke(ex)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(v)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Exercise>) {
        items = newItems
        notifyDataSetChanged()
    }
}