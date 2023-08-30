package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash1)

        // 3초 후 화면 자동 전환하기 위한 핸들러 생성
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            val intent = Intent(this, On1Activity::class.java)
            startActivity(intent)
            finish()
        }
        handler.postDelayed(runnable, 1000)
    }
}


