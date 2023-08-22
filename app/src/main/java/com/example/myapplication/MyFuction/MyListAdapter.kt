package com.example.myapplication.MyFuction

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.nickName
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyListAdapter(val MyList: ArrayList<MyListItem>) : RecyclerView.Adapter<MyListAdapter.CustomViewHolder>() {

    // retrofit
    var token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM2NTA3OCwiZXhwIjoxNjkyNDAxMDc4fQ.mGHNHLuTpJRc5mFrahf6RCKKVBxfcnvH9B4TDPOA-nEoY-9E8Kl9bw9jH_DjxERx9I3wHg4dwiWqjIImYD1dYQ"

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
            if(holder.item.text == "로그아웃") {
                var mDialogView = LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.my_logout_popup, null)
                var mBuilder = AlertDialog.Builder(holder.itemView.context)
                    .setView(mDialogView)
                    .create()

                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)

                val alertDialog = mBuilder.show()
                val display =
                    (holder.itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                mBuilder?.window?.setGravity(Gravity.CENTER)
            }
            val targetActivity = itemData.targetActivity
            val intent = Intent(holder.itemView.context, targetActivity)
            // 다음 액티비티로 넘길 데이터가 있다면 아래와 같이 추가
            // intent.putExtra("key", value)
            holder.itemView.context.startActivity(intent)
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val item = itemView.findViewById<TextView>(R.id.my_list_item)

    }
}