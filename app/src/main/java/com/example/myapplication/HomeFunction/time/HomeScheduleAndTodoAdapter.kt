package com.example.myapplication.HomeFunction.time

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.example.myapplication.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeScheduleAndTodoAdapter(private val dataArray: ArrayList<AndroidCalendarData>, private  val today : Int, private val editTextTitle : EditText,
                                 private val textPreClock : TextView, private val textNextClock : TextView, private val view : View) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
    companion object {
        private const val VIEW_TYPE_CAL_DURATION = 1
        private const val VIEW_TYPE_CAL_NO_DURATION = 2
        private const val VIEW_TYPE_TODO = 3
    }
    override fun getItemViewType(position: Int): Int {
        val item = dataArray[position]
        return if (item?.what == "CAL") {
            return if(item?.duration == true)
                VIEW_TYPE_CAL_DURATION
            else
                VIEW_TYPE_CAL_NO_DURATION
        } else {
            VIEW_TYPE_TODO
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CAL_DURATION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendar_schedule_cell1, parent, false)
                ItemViewHolderCalDuration(view)
            }
            VIEW_TYPE_CAL_NO_DURATION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendar_schedule_cell2, parent, false)
                ItemViewHolderCalNoDuration(view)
            }
            VIEW_TYPE_TODO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.home_shedule_cell_todo, parent, false)
                ItemViewHolderTodo(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataArray[position]
        if(item!=null) {
            when (holder.itemViewType) {
                VIEW_TYPE_CAL_DURATION -> {
                    val viewHolderWithDuration = holder as ItemViewHolderCalDuration
                    viewHolderWithDuration.textDay.text = "D-Day"
                    viewHolderWithDuration.textTitle.text = item.title
                    val date2 = LocalDate.parse(item.endDate, formatter)
                    viewHolderWithDuration.textDuration.text = ("~ ${date2.monthValue}월 ${date2.dayOfMonth}일")
                    viewHolderWithDuration.backgroundBar.setColorFilter(Color.parseColor(item.color), PorterDuff.Mode.SRC_IN)
                }
                VIEW_TYPE_CAL_NO_DURATION -> {
                    val viewHolderWithoutDuration = holder as ItemViewHolderCalNoDuration
                    viewHolderWithoutDuration.textTime1.text = convertTo12HourFormat(item.startTime)+"-"
                    viewHolderWithoutDuration.textTime2.text = convertTo12HourFormat(item.endTime)
                    viewHolderWithoutDuration.backgroundPoint.setColorFilter(Color.parseColor(item.color), PorterDuff.Mode.SRC_IN)
                    viewHolderWithoutDuration.textTitle.text = item.title
                }
                VIEW_TYPE_TODO -> {
                    val viewHolderTodo = holder as ItemViewHolderTodo

                    viewHolderTodo.catImage.setImageResource(findIcon(item.floor))
                    viewHolderTodo.textTitle.text = item.title
                }
            }
            holder.itemView.setOnClickListener {
                editTextTitle.setText(item.title)
                if(item.startTime!="") {
                    textPreClock.text = convertTo12HourFormat2(item.startTime)
                    textNextClock.text = convertTo12HourFormat2(item.endTime)
                }
                view.visibility = View.GONE
            }

        }

    }
    override fun getItemCount(): Int {
        return dataArray.size
    }
    inner class ItemViewHolderCalDuration(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDay: TextView = itemView.findViewById(R.id.textDay)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textDuration: TextView = itemView.findViewById(R.id.textDuration)
        val backgroundBar: ImageView = itemView.findViewById(R.id.backgorundBar)
    }
    inner class ItemViewHolderCalNoDuration(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTime1: TextView = itemView.findViewById(R.id.textTime1)
        val textTime2: TextView = itemView.findViewById(R.id.textTime2)
        val backgroundPoint: ImageView = itemView.findViewById(R.id.backgorundPoint)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
    }

    inner class ItemViewHolderTodo(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catImage: ImageView = itemView.findViewById(R.id.catImage)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
    }

    fun convertTo12HourFormat(timeString: String): String {
        val parts = timeString.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1]
        val convertedHour = if (hour > 12) hour - 12 else hour

        return "$convertedHour:$minute"
    }
    fun convertTo12HourFormat2(timeString: String): String {
        val parts = timeString.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1]
        val convertedHour = if (hour > 12) hour - 12 else if(hour >0) hour else 12
        val ampm = if (hour > 12&&hour<24) "오후" else if(hour <12 && hour>0) "오전" else if(hour ==12) "오후" else "오전"

        return "  $ampm $convertedHour:$minute  "
    }
    fun findIcon(iconId : Int) : Int {
        val icon = when(iconId){
            1 -> {R.drawable.ic_home_cate_burn}
            2 -> {R.drawable.ic_home_cate_chat1}
            3 -> {R.drawable.ic_home_cate_health}
            4 -> {R.drawable.ic_home_cate_heart}
            5 -> {R.drawable.ic_home_cate_laptop}
            6 -> {R.drawable.ic_home_cate_lightout}
            7 -> {R.drawable.ic_home_cate_lightup}
            8 -> {R.drawable.ic_home_cate_meal2}
            9 -> {R.drawable.ic_home_cate_meal1}
            10 -> {R.drawable.ic_home_cate_mic}
            11 -> {R.drawable.ic_home_cate_music}
            12 -> {R.drawable.ic_home_cate_pen}
            13 -> {R.drawable.ic_home_cate_phone}
            14 -> {R.drawable.ic_home_cate_plan}
            15 -> {R.drawable.ic_home_cate_rest}
            16 -> {R.drawable.ic_home_cate_sony}
            17 -> {R.drawable.ic_home_cate_study}
            else -> {R.drawable.ic_home_cate_work}
        }
        return icon
    }

}