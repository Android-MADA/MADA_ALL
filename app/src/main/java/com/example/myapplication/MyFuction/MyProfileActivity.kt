package com.example.myapplication.MyFuction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Model.MyGetProfileData
import com.example.myapplication.databinding.MyProfileBinding
import retrofit2.Call
import retrofit2.Response

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: MyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //서버연결 시작
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM2NTA3OCwiZXhwIjoxNjkyNDAxMDc4fQ.mGHNHLuTpJRc5mFrahf6RCKKVBxfcnvH9B4TDPOA-nEoY-9E8Kl9bw9jH_DjxERx9I3wHg4dwiWqjIImYD1dYQ"
        val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)


        api.myGetProfile(token).enqueue(object : retrofit2.Callback<MyGetProfileData>{
            override fun onResponse(
                call: Call<MyGetProfileData>,
                response: Response<MyGetProfileData>
            ) {
                if(response.isSuccessful){
                    Log.d("getProfile 성공", response.body().toString())
                    val response = response.body()
                    binding.nickname.text = response.data.nickname
                    binding.email.text = response.body()?.data!!.email
                }
                else{
                    Log.d("getProfile 실패", response.body().toString())
                }

            }
            override fun onFailure(call: Call<MyGetProfileData>, t: Throwable) {
                Log.d("서버 오류", "getProfile 실패")
            }

        })

        binding = MyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.nextBtn.setOnClickListener {
            val intent = Intent(this@MyProfileActivity, MyProfileNickActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}