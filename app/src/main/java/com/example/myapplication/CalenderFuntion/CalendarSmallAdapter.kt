package com.example.myapplication.CalenderFuntion

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class CalendarSmallAdapter(private val dayList: ArrayList<Date>, private val cal : LinearLayout,
                           private val preNexttext: TextView,)
    : RecyclerView.Adapter<CalendarSmallAdapter.ItemViewHolder>() {
    var m = LocalDate.now().monthValue
    var y = LocalDate.now().year
    var d = LocalDate.now().dayOfMonth
    private val weekdays = arrayOf("일" ,"월", "화", "수", "목", "금", "토")

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDay : TextView = itemView.findViewById(R.id.textDay)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar2_cell,parent,false)
        return ItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var monthDate = dayList[holder.adapterPosition]
        var dateCalendar = Calendar.getInstance()
        dateCalendar.time = monthDate
        var dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH)
        holder.textDay.text = dayNo.toString()
        var iYear = dateCalendar.get(Calendar.YEAR)
        var iMonth = dateCalendar.get(Calendar.MONTH) + 1
        var iDay = dateCalendar.get(Calendar.DAY_OF_MONTH)


        if(CalendarUtil.selectedDate.monthValue != iMonth) {
            holder.textDay.setTextColor(Color.LTGRAY)
        }
        if(iYear == y && iMonth == m && iDay == d) {
            holder.textDay.setBackgroundResource(R.drawable.calendar_smallbackground)
            holder.textDay.setTextColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener {
            preNexttext.text ="  ${iMonth}월 ${iDay}일 (${weekdays[position]})  "
        }

    }

    override fun getItemCount(): Int {
        return dayList.size
    }
}