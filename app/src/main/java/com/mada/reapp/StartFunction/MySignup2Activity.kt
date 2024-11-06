package com.mada.reapp.StartFunction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mada.reapp.MainActivity
import com.mada.reapp.databinding.MySignup2Binding


class MySignup2Activity : AppCompatActivity() {

    private lateinit var binding: MySignup2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MySignup2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signup2Btn.setOnClickListener {
            val intent = Intent(this@MySignup2Activity, MainActivity::class.java)


            startActivity(intent)
            finish()
        }
    }

}