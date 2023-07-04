package com.example.myapplication.Adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class CalendarScheduleAdapter(private val day : Int,private val position : Int ,private val MADAList: List<CalendarAdapter.MADA>) : RecyclerView.Adapter<CalendarScheduleAdapter.ItemViewHolder>()  {

    data class MADA(val title: String, val startDate: String, val endDate: String, val colorCode: String)

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDay : TextView = itemView.findViewById(R.id.textDay)
        val textTitle : TextView = itemView.findViewById(R.id.textTitle)
        val textDuration : TextView = itemView.findViewById(R.id.textDuration)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_schedule,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val tmpMADA = MADAList[holder.adapterPosition]
        //holder.textDay.text = tmpMADA.
        holder.textTitle.text = tmpMADA.title
        holder.textDuration.text = tmpMADA.startDate +" ~ "+ tmpMADA.endDate
    }


    override fun getItemCount(): Int {
        return MADAList.size
    }

}