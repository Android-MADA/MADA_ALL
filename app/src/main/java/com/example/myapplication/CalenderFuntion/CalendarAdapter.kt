package com.example.myapplication.CalenderFuntion

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class CalendarAdapter(private val dayList: ArrayList<Date>)
        : RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>()  {
    var m = LocalDate.now().monthValue
    var y = LocalDate.now().year
    var d = LocalDate.now().dayOfMonth
    val weekdays = arrayOf("일" ,"월", "화", "수", "목", "금", "토")
    data class MADA(val title: String, val startDate: String, val endDate: String, val colorCode: String)
    //임시 데이터
    val tmpDATA = listOf(
        MADA("기말 강의 평가 기간", "7월 2일", "7월 6일", "#2AA1B7"),
        MADA("데이터 분석 기초 기말고사", "7월 4일", "7월 4일", "#F8D141")
    )
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDay : TextView = itemView.findViewById(R.id.textDay)
        val point : ImageView = itemView.findViewById(R.id.calenadar_point)
    }
    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell,parent,false)

        return ItemViewHolder(view)
    }
    //데이터 설정
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
            holder.itemView.setBackgroundResource(R.drawable.etc_calender_today)
            holder.textDay.setTextColor(Color.WHITE)
            holder.point.setBackgroundResource(R.drawable.calendar_point)     //일단 임시로 일정 있는 거 표시
        }

        holder.itemView.setOnClickListener {
            var yearMonDay = "$iYear 년 $iMonth 월 $iDay 일"
            var mDialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.calendar_popup,null)
            var mBuilder = AlertDialog.Builder(holder.itemView.context)
                .setView(mDialogView)
            val alertDialog = mBuilder.show()
            mDialogView.findViewById<TextView>(R.id.textDay2).text = iDay.toString() +"일"
            mDialogView.findViewById<TextView>(R.id.textPosition).text = weekdays[position%7] + "요일"
            mDialogView.findViewById<AppCompatImageButton>(R.id.addBtn).setOnClickListener( {
                val intent = Intent(holder.itemView.context, CalendarAdd::class.java)
                intent.putExtra("Month",iMonth)
                intent.putExtra("Day",iDay)
                intent.putExtra("Yoil",position%7)
                holder.itemView.context.startActivity(intent)
                alertDialog.dismiss()
            })


            //임시 데이터

            val adapter = CalendarScheduleAdapter(iDay,position,tmpDATA)
            var manager: RecyclerView.LayoutManager = GridLayoutManager(holder.itemView.context,1)
            mDialogView.findViewById<RecyclerView>(R.id.scheduleList).layoutManager = manager
            mDialogView.findViewById<RecyclerView>(R.id.scheduleList).adapter = adapter
        }

    }

    override fun getItemCount(): Int {
        return dayList.size
    }
}