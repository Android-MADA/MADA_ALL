package com.mada.myapplication.HomeFunction.adapter.repeatTodo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mada.myapplication.R

class RepeatTypeAdapter(val dataSet : ArrayList<String>) : RecyclerView.Adapter<RepeatTypeAdapter.ViewHolder>() {

    //변수 선언
    var selectedType = "매일"


    //클릭리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepeatTypeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repeat_type_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: RepeatTypeAdapter.ViewHolder, position: Int) {
        holder.typeText.text = dataSet[position]
        holder.typeText.setOnClickListener {
            itemClickListener.onClick(it, position)
            selectedType = dataSet[position]
            notifyDataSetChanged()
        }
        if(dataSet[position] == selectedType){
            holder.typeText.setBackgroundResource(R.drawable.background_10_strokex)
            holder.typeText.background.setTint(Color.parseColor("#E1E9F5"))
        }
        else{
            holder.typeText.setBackgroundResource(R.drawable.background_10)
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val typeText : TextView

        init{
            typeText = view.findViewById(R.id.repeat_type_list_tv)
        }
    }
}