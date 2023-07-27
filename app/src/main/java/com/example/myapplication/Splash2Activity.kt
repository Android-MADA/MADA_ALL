package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.MyFuction.MySignup1
import com.example.myapplication.databinding.Splash2Binding

class Splash2Activity : AppCompatActivity() {

    private lateinit var binding: Splash2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Splash2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton6.setOnClickListener{
            val intent = Intent(this, MySignup1::class.java)
            startActivity(intent)
            finish()
        }
        binding.imageButton7.setOnClickListener{
            val intent = Intent(this, MySignup1::class.java)
            startActivity(intent)
            finish()
        }
        binding.imageButton8.setOnClickListener{
            val intent = Intent(this, MySignup1::class.java)
            startActivity(intent)
            finish()
        }

    }
}