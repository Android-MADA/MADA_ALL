package com.example.myapplication.MyFuction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.MyLogoutPopupBinding

class MyLogoutPopupActivity: AppCompatActivity() {
    private lateinit var binding: MyLogoutPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyLogoutPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nobutton.setOnClickListener {
            finish()
        }
        binding.yesbutton.setOnClickListener{
            val intent = Intent(this, Splash2Activity::class.java)
            startActivity(intent)
        }

    }
}