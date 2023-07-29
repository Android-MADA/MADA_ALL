package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.MyRecordWeekBinding

class MyRecordWeekActivity : AppCompatActivity() {
    private lateinit var binding: MyRecordWeekBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyRecordWeekBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}