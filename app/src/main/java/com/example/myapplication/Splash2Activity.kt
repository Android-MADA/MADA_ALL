package com.example.myapplication

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.MyFuction.MySignup1Activity
import com.example.myapplication.MyFuction.MySignup2Activity
import com.example.myapplication.MyFuction.MyWebviewActivity
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.databinding.Splash2Binding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Splash2Activity : AppCompatActivity() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    private lateinit var binding: Splash2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Splash2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PreferenceUtil(this)

        binding.kakaoBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.naverBtn.setOnClickListener{
            val intent = Intent(this, MyWebviewActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.googleBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}