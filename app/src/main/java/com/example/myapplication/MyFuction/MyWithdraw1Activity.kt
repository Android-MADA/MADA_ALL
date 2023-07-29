package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.MyWithdraw1Binding

class MyWithdraw1Activity : AppCompatActivity() {
    private lateinit var binding: MyWithdraw1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyWithdraw1Binding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}