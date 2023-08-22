package com.example.myapplication.MyFuction

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity


class MyListAdapter(val MyList: ArrayList<MyListItem>) : RecyclerView.Adapter<MyListAdapter.CustomViewHolder>() {

    // retrofit
    var token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM2NTA3OCwiZXhwIjoxNjkyNDAxMDc4fQ.mGHNHLuTpJRc5mFrahf6RCKKVBxfcnvH9B4TDPOA-nEoY-9E8Kl9bw9jH_DjxERx9I3wHg4dwiWqjIImYD1dYQ"
    private lateinit var dialogView: AlertDialog

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

        holder.item.setOnClickListener {
            val targetActivity = itemData.targetActivity

            if(holder.item.text == "로그아웃") {
                val dialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.my_logout_popup, null)
                val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
                    .setView(dialogView)
                    .create()

                val btnNo = dialogView.findViewById<AppCompatButton>(R.id.yesbutton)
                val btnYes = dialogView.findViewById<AppCompatButton>(R.id.nobutton)

                btnNo.setOnClickListener {
                    Log.d("로그아웃 취소", "로그아웃 취소")
                    alertDialogBuilder.dismiss()
                }
                btnYes.setOnClickListener {
                    val intent = Intent(holder.itemView.context, Splash2Activity::class.java)
                    Log.d("로그아웃 취소", "로그아웃 취소")
                }
            }
            else{
                val intent = Intent(holder.itemView.context, targetActivity)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val item = itemView.findViewById<TextView>(R.id.my_list_item)

    }
}