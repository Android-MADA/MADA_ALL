package com.example.myapplication.MyFuction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.MyPremiumBinding

class MyPremiumActivity : AppCompatActivity() {
    private lateinit var binding: MyPremiumBinding
    //private var isButtonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.myPremiumBtn.setOnClickListener {
            finish()
        }

    }
}