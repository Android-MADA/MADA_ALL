package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.On1Binding
import com.example.myapplication.databinding.On2Binding

class On2Activity : AppCompatActivity() {

    private lateinit var binding: On2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = On2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.onNextBtn.setOnClickListener {
            val intent = Intent(this@On2Activity, On3Activity::class.java)
            startActivity(intent)
            finish()
        }

        binding.onSkipBtn.setOnClickListener {
            val intent = Intent(this@On2Activity, On4Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

}