package com.example.myapplication.CalenderFuntion.Calendar

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarDdayFragment
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.HomeFunction.view.HomeViewpagerDdayFragment
import com.example.myapplication.R
import java.util.ArrayList

class DdayAdapter(val CalendarViewModel: CalendarViewModel, val parent2: CalendarDdayFragment)  : RecyclerView.Adapter<DdayAdapter.ItemViewHolder>()  {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dday1 : ImageView = itemView.findViewById(R.id.dday1)
        val ddayRemain : TextView = itemView.findViewById(R.id.dday_remain)
        val ddayText : TextView = itemView.findViewById(R.id.dday_text)
        val day : TextView = itemView.findViewById(R.id.dday_day)
    }
    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dday_layout,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = CalendarViewModel.ddayArrayList[position]
        holder.dday1.setColorFilter(Color.parseColor(item.color), PorterDuff.Mode.SRC_IN)
        holder.ddayRemain.text = "D-${CalendarViewModel.daysRemainingToDate(item.endDate)}"
        holder.ddayRemain.setTextColor(Color.parseColor(item.color))
        holder.ddayText.text = item.title
        holder.day.text = CalendarViewModel.convertToDateKoreanFormat123(item.endDate)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("calData",item)
            Navigation.findNavController(holder.itemView).navigate(R.id.action_calendarDday_to_calendarAddDday,bundle)

        }
    }
    override fun getItemCount(): Int {
        return CalendarViewModel.ddayArrayList.size
    }
    private fun refreshAdapter() {
        // 어댑터의 데이터 갱신 로직 등을 수행
        notifyDataSetChanged()
    }
}