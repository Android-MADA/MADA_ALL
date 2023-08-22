package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.On2Activity
import com.example.myapplication.On4Activity
import com.example.myapplication.databinding.On1Binding

class On1Activity : AppCompatActivity() {

    private lateinit var binding: On1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = On1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.onNextBtn.setOnClickListener {
            val intent = Intent(this@On1Activity, On2Activity::class.java)
            startActivity(intent)
            finish()
        }

        binding.onSkipBtn.setOnClickListener {
            val intent = Intent(this@On1Activity, On4Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

}