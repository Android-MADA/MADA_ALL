package com.example.myapplication.MyFuction.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class MyCalendarMonthAdapter(private val dayList: ArrayList<Date>, private val realMonth : String)
        : RecyclerView.Adapter<MyCalendarMonthAdapter.ItemViewHolder>()  {
    var m = LocalDate.now().monthValue
    var y = LocalDate.now().year
    var d = LocalDate.now().dayOfMonth

    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCalendar::class.java)
    //임시 데이터
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDay : TextView = itemView.findViewById(R.id.textDay)
    }
    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar2_cell,parent,false)

        return ItemViewHolder(view)
    }
    //데이터 설정
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var monthDate = dayList[holder.adapterPosition]
        var dateCalendar = Calendar.getInstance()
        dateCalendar.time = monthDate
        if((dateCalendar.get(Calendar.MONTH)+1).toString()==realMonth) {
            var iYear = dateCalendar.get(Calendar.YEAR)
            var iMonth = dateCalendar.get(Calendar.MONTH) + 1
            var iDay = dateCalendar.get(Calendar.DAY_OF_MONTH)
            holder.textDay.text = iDay.toString()

            if(iYear == y && iMonth == m && iDay == d) {
                holder.textDay.setBackgroundResource(R.drawable.calendar_cell_background2)
                holder.textDay.setTextColor(Color.WHITE)
            }

            holder.itemView.setOnClickListener {
            }
        }


    }

    override fun getItemCount(): Int {
        return dayList.size
    }


}