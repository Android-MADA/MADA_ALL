package com.example.myapplication.MyFuction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.MySignup1Binding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MySignup1Activity : AppCompatActivity() {

    private lateinit var binding: MySignup1Binding
    private var isButtonClicked = false

    // retrofit
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceMy::class.java)
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjI2NTQzMywiZXhwIjoxNjkyMzAxNDMzfQ.5ZNMyY3191Th0_Ci3qRw4xPlavx6vFD5VgXJyJC_mBvX1gV1F5-D6q4d7Be6_-gm6clqoJd-3e3eKvxYEAcJ1g"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MySignup1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signup1Btn.setOnClickListener {
            isButtonClicked = !isButtonClicked
            binding.signup1Btn.setBackgroundResource(R.drawable.my_btn_ok)

            val intent = Intent(this@MySignup1Activity, MySignup2Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

}

