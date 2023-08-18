package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.CustomFunction.customPrintDATA
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Model.MyGetNoticesData
import com.example.myapplication.MyFuction.Model.MyGetProfileData
import com.example.myapplication.databinding.MyNoticeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyNoticeActivity : AppCompatActivity() {
    private lateinit var binding: MyNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //서버연결 시작
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM2NTA3OCwiZXhwIjoxNjkyNDAxMDc4fQ.mGHNHLuTpJRc5mFrahf6RCKKVBxfcnvH9B4TDPOA-nEoY-9E8Kl9bw9jH_DjxERx9I3wHg4dwiWqjIImYD1dYQ"
        val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)


        api.myGetNotices(token).enqueue(object : retrofit2.Callback<MyGetNoticesData>{
            override fun onResponse(
                call: Call<MyGetNoticesData>,
                response: Response<MyGetNoticesData>
            ) {
                if(response.isSuccessful){
                    Log.d("getNotice 성공", response.body().toString())

                    // 이렇게 하는 거 아닌거같은디...리스트화 해야함(보류)
                    if(response.body()?.data!!.id == 1){
                        binding.title1.text = response.body()?.data!!.title
                        binding.content1.text = response.body()?.data!!.content
                        binding.date1.text = response.body()?.data!!.content
                    }
                    if(response.body()?.data!!.id == 2){
                        binding.title2.text = response.body()?.data!!.title
                        binding.content2.text = response.body()?.data!!.content
                        binding.date2.text = response.body()?.data!!.content
                    }
                }
                else{
                    Log.d("getNotice 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<MyGetNoticesData>, t: Throwable) {
                Log.d("서버 오류", "getNotice 실패")
            }

        })

        binding.backBtn.setOnClickListener {
            finish()
        }

    }
}