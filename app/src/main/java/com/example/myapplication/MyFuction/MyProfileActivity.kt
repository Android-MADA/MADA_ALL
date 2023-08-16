package com.example.myapplication.MyFuction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.MyProfileBinding

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: MyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.nextBtn.setOnClickListener {
            val intent = Intent(this@MyProfileActivity, MyProfileNickActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}