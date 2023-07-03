package com.example.myapplication.Adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Listener.OnItemListener
import com.example.myapplication.R

class CalendarAdapter(private val dayList: ArrayList<String>, private  val onItemListener: OnItemListener)
        : RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>()  {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDay : TextView = itemView.findViewById(R.id.textDay)
    }
    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell,parent,false)
        return ItemViewHolder(view)
    }
    //데이터 설정
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var day = dayList[holder.adapterPosition]
        holder.textDay.text = day
        holder.itemView.setOnClickListener {
            onItemListener.onItemClick(day)
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
    }
}