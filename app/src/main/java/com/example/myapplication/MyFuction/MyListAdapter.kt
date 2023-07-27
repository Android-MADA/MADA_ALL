package com.example.myapplication.MyFuction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class MyListAdapter(val MyList: ArrayList<MyListItem>) : RecyclerView.Adapter<MyListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_list_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = absoluteAdapterPosition
                val myListItem: MyListItem = MyList.get(curPos)
                Toast.makeText(parent.context, "${myListItem}이 클릭됨", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return MyList.size
    }

    override fun onBindViewHolder(holder: MyListAdapter.CustomViewHolder, position: Int) {
        holder.item.text = MyList.get(position).item
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val item = itemView.findViewById<TextView>(R.id.my_list_item)
    }
}

