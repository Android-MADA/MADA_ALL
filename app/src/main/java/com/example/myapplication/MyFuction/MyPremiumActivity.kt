package com.example.myapplication.MyFuction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.MyPremiumBinding

class MyPremiumActivity : AppCompatActivity() {
    private lateinit var binding: MyPremiumBinding
    //private var isButtonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myPremiumBtn.setOnClickListener {
            //isButtonClicked = !isButtonClicked
            //binding.myPremiumBtn.setBackgroundResource(R.drawable.my_withdrwaw_btn_ok)

            val intent = Intent(this, Splash2Activity::class.java)
            startActivity(intent)
            finish()
        }

    }
}