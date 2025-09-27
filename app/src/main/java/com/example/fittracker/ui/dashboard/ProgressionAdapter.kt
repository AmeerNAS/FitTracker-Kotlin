package com.example.fittracker.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fittracker.R
import com.example.fittracker.domain.usecase.GetMuscleProgressionUseCase

class ProgressionAdapter(
    private var items: List<GetMuscleProgressionUseCase.MuscleProgression>
) : RecyclerView.Adapter<ProgressionAdapter.ProgressionViewHolder>() {

    class ProgressionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val muscleName: TextView = view.findViewById(R.id.muscleGroupName)
        val progressBar: ProgressBar = view.findViewById(R.id.muscleProgressBar)
        val score: TextView = view.findViewById(R.id.muscleScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progression, parent, false)
        return ProgressionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgressionViewHolder, position: Int) {
        val item = items[position]
        holder.muscleName.text = item.muscleGroup.name
        holder.score.text = "Score: ${item.progression}"
        holder.progressBar.progress = item.progression.coerceAtMost(100)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<GetMuscleProgressionUseCase.MuscleProgression>) {
        items = newItems
        notifyDataSetChanged()
    }
}