package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.MyRecordMonthBinding

class MyRecordMonthActivity : AppCompatActivity() {
    private lateinit var binding: MyRecordMonthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyRecordMonthBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}