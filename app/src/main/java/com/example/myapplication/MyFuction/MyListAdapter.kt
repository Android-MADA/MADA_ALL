package com.example.myapplication.MyFuction

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


class MyListAdapter(val MyList: ArrayList<MyListItem>) : RecyclerView.Adapter<MyListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return MyList.size
    }

    override fun onBindViewHolder(holder:CustomViewHolder, position: Int) {
        val itemData = MyList[position]
        holder.item.text = itemData.item

        holder.itemView.setOnClickListener {
            val targetActivity = itemData.targetActivity
            val intent = Intent(holder.itemView.context, targetActivity)
            // 다음 액티비티로 넘길 데이터가 있다면 아래와 같이 추가합니다.
            // intent.putExtra("key", value)
            holder.itemView.context.startActivity(intent)
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val item = itemView.findViewById<TextView>(R.id.my_list_item)

    }
}