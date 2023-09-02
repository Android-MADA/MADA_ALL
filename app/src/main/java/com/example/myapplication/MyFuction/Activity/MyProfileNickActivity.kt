package com.example.myapplication.MyFuction.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.Fragment.FragMy
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Data.MyChangeNicknameData
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.MyProfileNickBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileNickActivity : AppCompatActivity() {
    private lateinit var binding: MyProfileNickBinding
    //private var isButtonClicked = false

    //서버연결 시작
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyProfileNickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.myNickBtn.setOnClickListener {
            changeNickname(binding.editNickname.text.toString())
            val intent = Intent(this, FragMy::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun changeNickname(nickName: String){
        val call1 = api.changeNickname(token,nickName)
        call1.enqueue(object : Callback<MyChangeNicknameData> {

            override fun onResponse(
                call: Call<MyChangeNicknameData>,
                response: Response<MyChangeNicknameData>
            ) {
                val responseCode = response.code()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) {
                        Log.d("닉네임 ${response.body()?.data!!.nickname}으로 변경 성공", "Response Code: $responseCode")
                    }else
                        Log.d("닉네임 ${response.body()?.data!!.nickname}으로 변경 실패", "Response Code: $responseCode")
                } else {
                    Log.d("닉네임 변경 실패","Response Code: ${response.code()} ")
                }
            }
            override fun onFailure(call: Call<MyChangeNicknameData>, t: Throwable) {
                Log.d("서버 오류","닉네임 변경 실패")
            }
        })

    }
}