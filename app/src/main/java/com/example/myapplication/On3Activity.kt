package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.On1Binding
import com.example.myapplication.databinding.On3Binding

class On3Activity : AppCompatActivity() {

    private lateinit var binding: On3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = On3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.onNextBtn.setOnClickListener {
            val intent = Intent(this@On3Activity, On4Activity::class.java)
            startActivity(intent)
            finish()
        }

        binding.onSkipBtn.setOnClickListener {
            val intent = Intent(this@On3Activity, On4Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

}