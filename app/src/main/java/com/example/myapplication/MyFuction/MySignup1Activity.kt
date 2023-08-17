package com.example.myapplication.MyFuction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.MySignup1Binding

class MySignup1Activity : AppCompatActivity() {

    private lateinit var binding: MySignup1Binding
    private var isButtonClicked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MySignup1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signup1Btn.setOnClickListener {
            isButtonClicked = !isButtonClicked
            binding.signup1Btn.setBackgroundResource(R.drawable.my_btn_ok)

            val intent = Intent(this@MySignup1Activity, MySignup2Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

}

