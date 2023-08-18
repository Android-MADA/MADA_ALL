package com.example.myapplication.MyFuction

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            if(holder.item.text == "로그아웃"){
                // 로그아웃 서버 연결(팝업X 상태)
                val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                val api = retrofit.create(RetrofitServiceMy::class.java)
                token = MyWebviewActivity.prefs.getString("token","")
                val call = api.logout(token)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful) {
                            Log.d("logout 성공", response.body().toString())
                        }
                        else{
                            Log.d("logout 실패", response.body().toString())
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("logout 실패 ", "서버 오류")
                    }
                })
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