package com.example.myapplication.MyFuction

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.MySetBinding
import com.example.myapplication.databinding.MySignup1Binding


class MySignup1 : AppCompatActivity() {

    private lateinit var binding: MySignup1Binding
    private var isButtonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MySignup1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signup1Btn.setOnClickListener {
            isButtonClicked = !isButtonClicked
            updateButtonState(binding.signup1Btn)

            val intent = Intent(this@MySignup1, MySignup2::class.java)
            startActivity(intent)
            finish()

            updateButtonState(binding.signup1Btn)
        }
    }

    private fun updateButtonState(customButton: Button) {
        if (isButtonClicked) {
            // 버튼이 클릭되었을 때 색상 설정
            customButton.setBackgroundResource(R.drawable.my_btn_ok)
        } else {
            // 버튼이 클릭되지 않았을 때 색상 설정
            customButton.setBackgroundResource(R.drawable.my_btn_nomal)
        }
    }
}

