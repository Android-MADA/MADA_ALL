package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.MyNoticeBinding

class MyNoticeActivity : AppCompatActivity() {
    private lateinit var binding: MyNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

    }
}