package com.example.myapplication.MyFuction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.myapplication.CalenderFuntion.Model.CalendarData2
import com.example.myapplication.CalenderFuntion.Model.nickName
import com.example.myapplication.Fragment.FragMy
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Model.MyChangeNicknameData
import com.example.myapplication.MyFuction.Model.MyChangeNicknameData2
import com.example.myapplication.MyFuction.Model.MyGetProfileData
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.MyProfileNickBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileNickActivity : AppCompatActivity() {
    private lateinit var binding: MyProfileNickBinding
    //private var isButtonClicked = false

    //서버연결 시작
    //val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM2NTA3OCwiZXhwIjoxNjkyNDAxMDc4fQ.mGHNHLuTpJRc5mFrahf6RCKKVBxfcnvH9B4TDPOA-nEoY-9E8Kl9bw9jH_DjxERx9I3wHg4dwiWqjIImYD1dYQ"
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = MyWebviewActivity.prefs.getString("token", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyProfileNickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.myNickBtn.setOnClickListener {
            changeNickname(binding.editNickname.text.toString())
//            val intent = Intent(this, FragMy::class.java)
//            startActivity(intent)
//            finish()
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
        finish()
    }
}