package com.example.myapplication.MyFuction.Adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity


class MyListAdapter(val MyList: ArrayList<MyListItem>) : RecyclerView.Adapter<com.example.myapplication.MyFuction.Adapter.MyListAdapter.CustomViewHolder>() {

    //서버연결 시작
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")

    //private lateinit var dialogView: AlertDialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_list_item, parent, false)
        return CustomViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return MyList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val itemData = MyList[position]
        holder.item.text = itemData.item

        holder.item.setOnClickListener {
            val targetActivity = itemData.targetActivity

//            if(holder.item.text == "로그아웃") {
//                val dialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.my_logout_popup, null)
//                val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
//                    .setView(dialogView)
//                    .create()
//
//                val btnNo = dialogView.findViewById<AppCompatButton>(R.id.yesbutton)
//                val btnYes = dialogView.findViewById<AppCompatButton>(R.id.nobutton)
//
//                btnNo.setOnClickListener {
//                    Log.d("로그아웃 취소", "로그아웃 취소")
//                    alertDialogBuilder.dismiss()
//                }
//                btnYes.setOnClickListener {
//                    val intent = Intent(holder.itemView.context, Splash2Activity::class.java)
//                    Log.d("로그아웃 취소", "로그아웃 취소")
//                }
//            }
            //else{
            val intent = Intent(holder.itemView.context, targetActivity)
            holder.itemView.context.startActivity(intent)
            //}
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val item = itemView.findViewById<TextView>(R.id.my_list_item)

    }
}