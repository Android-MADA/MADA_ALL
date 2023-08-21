package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Model.MyGetNoticesData
import com.example.myapplication.MyFuction.Model.MyGetProfileData
import com.example.myapplication.MyFuction.Model.MyNoticesData
import com.example.myapplication.MyFuction.Model.MyNoticesList
import com.example.myapplication.databinding.MyNoticeBinding
import retrofit2.Call
import retrofit2.Response

class MyNoticeActivity : AppCompatActivity() {
    private lateinit var binding: MyNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        //서버연결 시작
        val token =
            "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM2NTA3OCwiZXhwIjoxNjkyNDAxMDc4fQ.mGHNHLuTpJRc5mFrahf6RCKKVBxfcnvH9B4TDPOA-nEoY-9E8Kl9bw9jH_DjxERx9I3wHg4dwiWqjIImYD1dYQ"
        val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)


        // 서버에서 공지사항 불러오기
        api.myGetNotices(token).enqueue(object : retrofit2.Callback<List<MyGetNoticesData>> {
            override fun onResponse(
                call: Call<List<MyGetNoticesData>>,
                response: Response<List<MyGetNoticesData>>
            ) {
                if (response.isSuccessful) {
                    Log.d("GetNotices 성공", response.body().toString())
                    //binding.date1.text = response.body()

                } else {
                    Log.d("GetNotices 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<List<MyGetNoticesData>>, t: Throwable) {
                Log.d("서버 오류", "GetNotices 실패")
            }


        })

    }
}