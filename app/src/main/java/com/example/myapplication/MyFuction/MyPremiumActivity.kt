package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.databinding.MyPremiumBinding

class MyPremiumActivity : AppCompatActivity() {
    private lateinit var binding: MyPremiumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}