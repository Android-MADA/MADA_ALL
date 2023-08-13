package com.example.myapplication.MyFuction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Fragment.FragMy
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.MyProfileNickBinding

class MyProfileNickActivity : AppCompatActivity() {
    private lateinit var binding: MyProfileNickBinding
    //private var isButtonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyProfileNickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.myNickBtn.setOnClickListener {
            val intent = Intent(this, FragMy::class.java)
            startActivity(intent)
            finish()
        }

    }
}