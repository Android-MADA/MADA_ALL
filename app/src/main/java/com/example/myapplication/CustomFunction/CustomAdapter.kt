package com.example.myapplication.Adpater


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

open class CustomAdapter () : RecyclerView.Adapter<CustomAdapter.ItemViewHolder>()  {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //오류로 잠시 넣어둠(버루)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_color,parent,false)
        return CustomAdapter.ItemViewHolder(view)
        //
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    }


    override fun getItemCount(): Int {
        return 1
    }
}