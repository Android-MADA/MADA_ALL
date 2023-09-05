package com.example.myapplication.MyFuction.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.MyFuction.Data.MyRecordCategoryData
import com.example.myapplication.R

class MyRecordCategoryAdapter(private val context: Context) : RecyclerView.Adapter<MyRecordCategoryAdapter.ViewHolder>() {

    var datas = mutableListOf<MyRecordCategoryData>()
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
        private val imgColor: ImageView = itemView.findViewById(R.id.record_category_color)

        fun bind(item: MyRecordCategoryData) {
            txtPercent.text = item.percent
            Glide.with(itemView).load(item.colorImage).into(imgColor)
            txtCategory.text = item.category
        }
    }

}