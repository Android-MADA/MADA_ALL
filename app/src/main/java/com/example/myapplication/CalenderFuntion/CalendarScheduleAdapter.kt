package com.example.myapplication.CalenderFuntion

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Fragment.FragCalendar
import com.example.myapplication.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarScheduleAdapter(private val calendarDataArray:  ArrayList<FragCalendar.CalendarDATA?>,private val today: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
    companion object {
        private const val VIEW_TYPE_WITH_DURATION = 1
        private const val VIEW_TYPE_WITHOUT_DURATION = 2
    }
    override fun getItemViewType(position: Int): Int {
        val item = calendarDataArray[position]
        return if (item?.duration == true) {
            VIEW_TYPE_WITH_DURATION
        } else {
            VIEW_TYPE_WITHOUT_DURATION
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_WITH_DURATION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendar_schedule_cell1, parent, false)
                ItemViewHolderWithDuration(view)
            }
            VIEW_TYPE_WITHOUT_DURATION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendar_schedule_cell2, parent, false)
                ItemViewHolderWithoutDuration(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = calendarDataArray[position]
        if(item!=null) {
            when (holder.itemViewType) {
                VIEW_TYPE_WITH_DURATION -> {
                    val viewHolderWithDuration = holder as ItemViewHolderWithDuration
                    viewHolderWithDuration.textDay.text = "${today}일"
                    viewHolderWithDuration.textTitle.text = item.memo
                    val date1 = LocalDate.parse(item.startDate, formatter)
                    val date2 = LocalDate.parse(item.endDate, formatter)
                    viewHolderWithDuration.textDuration.text = ("${date1.monthValue}월 ${date1.dayOfMonth}일 ~ ${date2.monthValue}월 ${date2.dayOfMonth}일")
                    viewHolderWithDuration.backgroundBar.setColorFilter(Color.parseColor(item.color), PorterDuff.Mode.SRC_IN)
                }
                VIEW_TYPE_WITHOUT_DURATION -> {
                    val viewHolderWithoutDuration = holder as ItemViewHolderWithoutDuration
                    viewHolderWithoutDuration.textTime1.text = convertTo12HourFormat(item.startTime)+"-"
                    viewHolderWithoutDuration.textTime2.text = convertTo12HourFormat(item.endTime)
                    viewHolderWithoutDuration.backgroundPoint.setColorFilter(Color.parseColor(item.color), PorterDuff.Mode.SRC_IN)
                }
            }
        }

    }
    override fun getItemCount(): Int {
        return calendarDataArray.size
    }
    inner class ItemViewHolderWithDuration(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views for VIEW_TYPE_WITH_DURATION here
        val textDay: TextView = itemView.findViewById(R.id.textDay)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textDuration: TextView = itemView.findViewById(R.id.textDuration)
        val backgroundBar: ImageView = itemView.findViewById(R.id.backgorundBar)
    }

    inner class ItemViewHolderWithoutDuration(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTime1: TextView = itemView.findViewById(R.id.textTime1)
        val textTime2: TextView = itemView.findViewById(R.id.textTime2)
        val backgroundPoint: ImageView = itemView.findViewById(R.id.backgorundPoint)
    }

    fun convertTo12HourFormat(timeString: String): String {
        val parts = timeString.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1]
        val convertedHour = if (hour > 12) hour - 12 else hour

        return "$convertedHour:$minute"
    }
}