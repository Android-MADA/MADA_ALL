package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Model.MyChangeNicknameData
import com.example.myapplication.MyFuction.Model.MySetPageData
import com.example.myapplication.databinding.MySetBinding
import retrofit2.Call
import retrofit2.Response

class MySetActivity : AppCompatActivity() {
    private lateinit var binding: MySetBinding

    //서버연결 시작
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = MyWebviewActivity.prefs.getString("token", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MySetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.mySetSwitch1.isChecked = true
        binding.mySetSwitch2.isChecked  = false
        binding.mySetSwitch3.isChecked = true

        api.setPage(token).enqueue(object : retrofit2.Callback<MySetPageData>{
            override fun onResponse(
                call: Call<MySetPageData>,
                response: Response<MySetPageData>
            ) {
                val responseCode = response.code()
                if(response.isSuccessful){
                    Log.d("화면 설정 변경 성공", "Response Code: $responseCode")
                    binding.mySetSwitch1.setOnClickListener {
                        binding.mySetSwitch1.isChecked = response.body()!!.endTodobackString
                    }
                    binding.mySetSwitch2.setOnClickListener {
                        binding.mySetSwitch2.isChecked = response.body()!!.newTodoStartSetting
                    }
                    binding.mySetSwitch3.setOnClickListener {
                        binding.mySetSwitch3.isChecked = response.body()!!.startTodoAtMonday
                    }
                }
                else{
                    Log.d("화면 설정 변경 실패", "Response Code: $responseCode")
                }
            }
            override fun onFailure(call: Call<MySetPageData>, t: Throwable) {
                Log.d("서버 오류", "화면 설정 변경 실패")
            }
        })
    }
}