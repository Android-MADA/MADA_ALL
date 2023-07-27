package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.MyProfileNickBinding

class MyProfileNickActivity : AppCompatActivity() {
    private lateinit var binding: MyProfileNickBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyProfileNickBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}