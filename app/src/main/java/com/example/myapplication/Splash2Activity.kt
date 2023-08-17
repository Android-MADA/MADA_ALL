package com.example.myapplication

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.myapplication.MyFuction.MySignup1Activity
import com.example.myapplication.MyFuction.MyWebviewActivity
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.databinding.Splash2Binding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Splash2Activity : AppCompatActivity() {

    private lateinit var binding: Splash2Binding

    // retrofit
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceMy::class.java)
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjI2NTQzMywiZXhwIjoxNjkyMzAxNDMzfQ.5ZNMyY3191Th0_Ci3qRw4xPlavx6vFD5VgXJyJC_mBvX1gV1F5-D6q4d7Be6_-gm6clqoJd-3e3eKvxYEAcJ1g"

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Splash2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kakaoBtn.setOnClickListener{
            val intent = Intent(this, MySignup1Activity::class.java)
            startActivity(intent)
            finish()
        }

        binding.naverBtn.setOnClickListener{
            val intent = Intent(this, MyWebviewActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.googleBtn.setOnClickListener{
            val intent = Intent(this, MySignup1Activity::class.java)
            startActivity(intent)
            finish()
        }

    }
}