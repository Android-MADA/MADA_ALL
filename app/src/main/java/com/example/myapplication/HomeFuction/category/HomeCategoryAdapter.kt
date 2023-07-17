package com.example.myapplication.HomeFuction.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class HomeCategoryAdapter(private val dataSet : ArrayList<sampleCategoryData>) : RecyclerView.Adapter<HomeCategoryAdapter.viewHolder>() {

    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val categoryLayout : LinearLayout
        val categoryTextView : TextView
        //val categoryImageView : ImageView

        init {
            categoryLayout = view.findViewById(R.id.linearLayout_home_add_category)
            categoryTextView = view.findViewById(R.id.tv_home_add_category)
            //categoryImageView = view.findViewById(R.id.iv_home_add_category)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_edit_category_list, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.categoryLayout.setBackgroundColor(dataSet[position].color)
        holder.categoryTextView.text = dataSet[position].name
        //holder.categoryImageView.setImageResource(dataSet[position].icon)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}