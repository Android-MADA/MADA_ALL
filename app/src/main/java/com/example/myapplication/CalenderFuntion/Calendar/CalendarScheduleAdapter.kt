package com.example.myapplication.CalenderFuntion.Calendar

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

class CalendarScheduleAdapter(private val myDataArray: ArrayList<AndroidCalendarData>, private val today: Int
                              , private val parentView: View, private val parentDialog: AlertDialog) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
    companion object {
        private const val VIEW_TYPE_WITH_DURATION = 1
        private const val VIEW_TYPE_WITHOUT_DURATION = 2
        private const val VIEW_TYPE_DDAY = 3
    }
    override fun getItemViewType(position: Int): Int {
        val item = myDataArray[position]
        return if (item?.duration == true) {
            VIEW_TYPE_WITH_DURATION
        } else {
            if(item?.dDay=="Y") VIEW_TYPE_DDAY
            else VIEW_TYPE_WITHOUT_DURATION
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
            VIEW_TYPE_DDAY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendar_schedule_cell1, parent, false)
                ItemViewHolderWithDuration(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = myDataArray[position]
        if(item!=null) {
            when (holder.itemViewType) {
                VIEW_TYPE_WITH_DURATION -> {
                    val viewHolderWithDuration = holder as ItemViewHolderWithDuration
                    viewHolderWithDuration.textDay.text = "${today}일"
                    viewHolderWithDuration.textTitle.text = item.title
                    val date1 = LocalDate.parse(item.startDate, formatter)
                    val date2 = LocalDate.parse(item.endDate, formatter)
                    viewHolderWithDuration.textDuration.text = ("${date1.monthValue}월 ${date1.dayOfMonth}일 ~ ${date2.monthValue}월 ${date2.dayOfMonth}일")
                    viewHolderWithDuration.backgroundBar.setColorFilter(Color.parseColor(item.color), PorterDuff.Mode.SRC_IN)
                }
                VIEW_TYPE_WITHOUT_DURATION -> {
                    val viewHolderWithoutDuration = holder as ItemViewHolderWithoutDuration
                    viewHolderWithoutDuration.textTime1.text = convertTo12HourFormat(item.startTime)+"-"
                    viewHolderWithoutDuration.textTime2.text = convertTo12HourFormat(item.endTime)
                    if(viewHolderWithoutDuration.textTime2.text=="00:00")
                        viewHolderWithoutDuration.textTime2.text="24:00"
                    viewHolderWithoutDuration.backgroundPoint.setColorFilter(Color.parseColor(item.color), PorterDuff.Mode.SRC_IN)
                    viewHolderWithoutDuration.textTitle.text = item.title
                }
                VIEW_TYPE_DDAY -> {
                    val viewHolderWithDuration = holder as ItemViewHolderWithDuration
                    viewHolderWithDuration.textDay.text = "${today}일"
                    viewHolderWithDuration.textTitle.text = item.title
                    val date2 = LocalDate.parse(item.endDate, formatter)
                    viewHolderWithDuration.textDuration.text = ("${date2.monthValue}월 ${date2.dayOfMonth}일")
                    viewHolderWithDuration.dday.setImageResource(R.drawable.color_bluepurple_lock)
                    viewHolderWithDuration.backgroundBar.setImageResource(R.drawable.calendar_schedule_background_dday)
                    viewHolderWithDuration.backgroundBar.setColorFilter(Color.parseColor(item.color), PorterDuff.Mode.SRC_IN)
                }
            }
            holder.itemView.setOnClickListener {
                //임시
                val bundle = Bundle()
                bundle.putSerializable("calData",item)
                bundle.putBoolean("edit",true)
                if(item.dDay=="Y")  {
                    bundle.putBoolean("calendar",true)
                    Navigation.findNavController(parentView).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                }
                else Navigation.findNavController(parentView).navigate(R.id.action_fragCalendar_to_calendarAddS,bundle)
                parentDialog.dismiss()

            }
        }

    }
    override fun getItemCount(): Int {
        return myDataArray.size
    }
    inner class ItemViewHolderWithDuration(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views for VIEW_TYPE_WITH_DURATION here
        val textDay: TextView = itemView.findViewById(R.id.textDay)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textDuration: TextView = itemView.findViewById(R.id.textDuration)
        val backgroundBar: ImageView = itemView.findViewById(R.id.backgorundBar)
        val dday: ImageView = itemView.findViewById(R.id.ddayImage)
    }

    inner class ItemViewHolderWithoutDuration(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTime1: TextView = itemView.findViewById(R.id.textTime1)
        val textTime2: TextView = itemView.findViewById(R.id.textTime2)
        val backgroundPoint: ImageView = itemView.findViewById(R.id.backgorundPoint)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
    }


    fun convertTo12HourFormat(timeString: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale("en", "US"))
        val outputFormat = SimpleDateFormat("HH:mm", Locale("en", "US"))
        val calendar = Calendar.getInstance()

        calendar.time = inputFormat.parse(timeString)

        return outputFormat.format(calendar.time)
    }
    fun convertTo12HourFormat2(timeString: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale("en", "US"))
        val outputFormat = SimpleDateFormat("  a h:mm  ", Locale("en", "US"))
        val calendar = Calendar.getInstance()

        calendar.time = inputFormat.parse(timeString)

        return outputFormat.format(calendar.time).replace("AM","오전").replace("PM","오후")

    }

}