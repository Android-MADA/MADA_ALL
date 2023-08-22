package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MyFuction.MySignup1Activity
import com.example.myapplication.databinding.On3Binding
import com.example.myapplication.databinding.On4Binding

class On4Activity : AppCompatActivity() {

    private lateinit var binding: On4Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = On4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.onNextBtn.setOnClickListener {
            val intent = Intent(this@On4Activity, Splash2Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

}