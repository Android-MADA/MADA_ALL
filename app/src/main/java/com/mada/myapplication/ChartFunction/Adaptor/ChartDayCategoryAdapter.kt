package com.mada.myapplication.ChartFunction.Adaptor

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mada.myapplication.ChartFunction.Data.MonthPieData
import com.mada.myapplication.R

class ChartDayCategoryAdapter(private val context: Context) : RecyclerView.Adapter<ChartDayCategoryAdapter.ViewHolder>() {

    var datas = mutableListOf<MonthPieData>()
        set(value) {
            field = value
            notifyDataSetChanged() // 데이터가 변경되었음을 알려 리사이클러뷰를 다시 그립니다.
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.my_record_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtPercent: TextView = itemView.findViewById(R.id.record_category_percent)
        private val txtCategory: TextView = itemView.findViewById(R.id.record_category_name)
        private val viewColor: CardView = itemView.findViewById(R.id.record_category_color)

        fun bind(item: MonthPieData) {
            txtPercent.text = item.rate.toString()
            viewColor.setCardBackgroundColor(Color.parseColor("#"+item.color.replace("#","")))
            txtCategory.text = item.categoryName
        }
    }

}