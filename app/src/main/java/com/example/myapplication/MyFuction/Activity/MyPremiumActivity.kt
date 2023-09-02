package com.example.myapplication.MyFuction.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Data.MyPremiumData
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.MyPremiumBinding
import retrofit2.Call
import retrofit2.Response

class MyPremiumActivity : AppCompatActivity() {
    private lateinit var binding: MyPremiumBinding
    //private var isButtonClicked = false

    //서버연결 시작
    //val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM2NTA3OCwiZXhwIjoxNjkyNDAxMDc4fQ.mGHNHLuTpJRc5mFrahf6RCKKVBxfcnvH9B4TDPOA-nEoY-9E8Kl9bw9jH_DjxERx9I3wHg4dwiWqjIImYD1dYQ"
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.myPremiumBtn.setOnClickListener {
            patchPremiumSetting(binding.myPremiumBtn.isSelected)
            finish()
        }


    }
    private fun patchPremiumSetting(isSetting: Boolean){
        api.mySetPremium(token, isSetting).enqueue(object : retrofit2.Callback<MyPremiumData>{
            override fun onResponse(
                call: Call<MyPremiumData>, response: Response<MyPremiumData>
            ) {
                val responseCode = response.code()

                if(response.isSuccessful){
                    Log.d("프리미엄 변경 성공, ${response.body()?.is_subscribe}", "Response Code: $responseCode")
                }
                else{
                    Log.d("프리미엄 변경 실패", "Response Code: $responseCode")
                }
            }
            override fun onFailure(call: Call<MyPremiumData>, t: Throwable) {
                Log.d("서버 오류", "프리미엄 변경 실패")
            }
        })
    }
}