package com.example.myapplication.HomeFunction.category

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class HomeCateIconAdapter(private val dataList : ArrayList<String>) : RecyclerView.Adapter<HomeCateIconAdapter.viewHolder>(){

    var selectedIcon : String = R.drawable.ic_home_cate_study.toString()

    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val categoryIcon : ImageView

        init {
            categoryIcon = view.findViewById(R.id.iv_home_icon_list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCateIconAdapter.viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_cate_icon_list, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeCateIconAdapter.viewHolder, position: Int) {
        holder.categoryIcon.setImageResource(dataList[position].toInt())
        holder.categoryIcon.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
}