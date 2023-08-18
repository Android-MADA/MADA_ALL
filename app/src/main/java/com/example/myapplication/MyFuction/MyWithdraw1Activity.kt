package com.example.myapplication.MyFuction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.MyWithdraw1Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyWithdraw1Activity : AppCompatActivity() {
    private lateinit var binding: MyWithdraw1Binding
    private var isButtonClicked = false

    // retrofit
    var token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM3NDYwOCwiZXhwIjoxNjkyNDEwNjA4fQ.FWaurv6qy-iiha07emFxGIZjAnwL3fluFsZSQY-AvlmBBsHe5ZtfRL69l6zP1ntOGIWEGb5IbCLd5JP4MjWu4w"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyWithdraw1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.myWithdrawBtn.setOnClickListener {
            if(binding.checkBox.isChecked){
                // 버튼 색 변경
                isButtonClicked = !isButtonClicked
                binding.myWithdrawBtn.setBackgroundResource(R.drawable.my_withdrwaw_btn_ok)

                //서버 연결
                val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                val api = retrofit.create(RetrofitServiceMy::class.java)
                token = MyWebviewActivity.prefs.getString("token","")
                val call = api.withdraw(token)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful) {
                            Log.d("회원탈퇴 성공", response.body().toString())
                        }
                        else{
                            Log.d("회원탈퇴 실패", response.body().toString())
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("회원탈퇴 실패 ", "서버 오류")
                    }
                })

                // 처음 화면으로 이동
                val intent = Intent(this, Splash2Activity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}