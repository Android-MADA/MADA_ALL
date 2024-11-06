package com.mada.reapp.CalenderFuntion.Calendar

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
import com.mada.reapp.CalenderFuntion.CalendarDdayFragment
import com.mada.reapp.CalenderFuntion.Model.CalendarViewModel
import com.mada.reapp.R

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