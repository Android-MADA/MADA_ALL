package com.mada.reapp.HomeFunction.adapter.category

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mada.reapp.R

class HomeCateColorAdapter(private val dataList : ArrayList<String>) : RecyclerView.Adapter<HomeCateColorAdapter.viewHolder>() {

    var selecetedColor = "#89A9D9"

    class viewHolder(view : View) : RecyclerView.ViewHolder(view){
        val categoryColor : ImageView

        init {
            categoryColor = view.findViewById(R.id.iv_home_color_list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_cate_color_list, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.categoryColor.imageTintList = ColorStateList.valueOf(Color.parseColor(dataList[position]))

        holder.categoryColor.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
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