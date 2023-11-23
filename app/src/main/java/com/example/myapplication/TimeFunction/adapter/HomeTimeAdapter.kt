package com.example.myapplication.TimeFunction.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.TimeFunction.TimeViewModel

class HomeTimeAdapter(private val dataSet : ArrayList<TimeViewModel.PieChartData>) : RecyclerView.Adapter<HomeTimeAdapter.viewHolder>() {
    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val timeImageView : ImageView
        val timeTextView : TextView
        val timeLayout : ConstraintLayout

        init {
            timeLayout = view.findViewById<ConstraintLayout>(R.id.layout_home_time_list)
            timeImageView = view.findViewById(R.id.iv_home_time_list)
            timeTextView = view.findViewById(R.id.tv_home_time_list)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_time_list, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.timeImageView.setColorFilter(Color.parseColor(dataSet[position].colorCode), PorterDuff.Mode.SRC_IN)
        holder.timeTextView.text = dataSet[position].title
        holder.timeLayout.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
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