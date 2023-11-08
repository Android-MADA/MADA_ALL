package com.example.myapplication.TimeFunction.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.tiles.LayoutElementBuilders.Image
import com.example.myapplication.R
import com.example.myapplication.TimeFunction.TimeViewModel

class TimeTableAdpater(private val data: ArrayList<TimeViewModel.PieChartData>) : RecyclerView.Adapter<TimeTableAdpater.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.timetable_title_tv)
        val memo: TextView = itemView.findViewById(R.id.timetable_memo_tv)
        val image : ImageView = itemView.findViewById(R.id.timetable_bg)
        // Add other views for your data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_timetable_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        // Populate the views with data from the item
        holder.title.text = "${item.title}"
        holder.memo.text = "${item.memo}"
        holder.image.setColorFilter(Color.parseColor(item.colorCode))
        val layoutParams = holder.image.layoutParams
        layoutParams.height = dpToPx(holder.image.context, (item.endHour-item.startHour))
        holder.image.layoutParams = layoutParams
        // Set other data to other views
    }

    override fun getItemCount(): Int {
        return data.size
    }
    private fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}
