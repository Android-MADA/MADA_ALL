package com.mada.myapplication.HomeFunction.adapter.repeatTodo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mada.myapplication.R

class RepeatWeeklyAdapter(val dataSet : ArrayList<String>) : RecyclerView.Adapter<RepeatWeeklyAdapter.ViewHolder>() {

    var selectedDay : String? = null

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val dayText : TextView

        init {
            dayText = view.findViewById(R.id.week_list_tv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repeat_weekly_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if(dataSet[position] == selectedDay){
            holder.dayText.background.setTint(Color.parseColor("#486DA3"))
            holder.dayText.setTextColor(Color.parseColor("#ffffff"))
        }
        else {
            holder.dayText.background.setTint(Color.parseColor("#F5F5F5"))
            holder.dayText.setTextColor(Color.parseColor("#000000"))
        }
        holder.dayText.text = dataSet[position]
        holder.dayText.setOnClickListener {
            selectedDay = dataSet[position]
            itemClickListener.onClick(it, position)
            notifyDataSetChanged()
        }
    }
}