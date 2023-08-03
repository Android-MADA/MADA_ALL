package com.example.myapplication.MyFuction

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.MySignup1Binding
import com.example.myapplication.databinding.MySignup2Binding


class MySignup2 : AppCompatActivity() {

    private lateinit var binding: MySignup2Binding
    private var isButtonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MySignup2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signup2Btn.setOnClickListener {
            isButtonClicked = !isButtonClicked
            binding.signup2Btn.setBackgroundResource(R.drawable.my_btn_ok)

            val intent = Intent(this@MySignup2, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}