package com.example.rambler.Activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rambler.Modules.StepCount
import com.example.rambler.R


class StepsCountAdapter : RecyclerView.Adapter<SummaryViewHolder>() {
    private val dataInfo = mutableListOf<StepCount>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val li = LayoutInflater.from(parent.context)
        return SummaryViewHolder(
            li.inflate(
                R.layout.item_summary,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val summary = dataInfo[position]
        holder.dateTextView?.text = summary.getDateFormatted()
        holder.stepsTextView?.text = summary.steps.toString()
    }

    override fun getItemCount(): Int {
        return dataInfo.size
    }

    fun setList(summaryList: List<StepCount>?) {
        this.dataInfo.clear()

        summaryList?.let {
            this.dataInfo.addAll(summaryList!!)
        }

        notifyDataSetChanged()
    }
}

class SummaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var dateTextView: TextView? = view.findViewById(R.id.date_textview)
    var stepsTextView: TextView? = view.findViewById(R.id.steps_textview)
}