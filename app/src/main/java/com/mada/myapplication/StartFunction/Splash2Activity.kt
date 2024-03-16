package com.mada.myapplication.StartFunction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mada.myapplication.MainActivity
import com.mada.myapplication.PreferenceUtil
import com.mada.myapplication.databinding.Splash2Binding

class Splash2Activity : AppCompatActivity() {
    companion object {
        lateinit var prefs: PreferenceUtil

    }

    private lateinit var binding: Splash2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Splash2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PreferenceUtil(this)

        binding.kakaoBtn.setOnClickListener{
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
            //finish()
        }

        binding.naverBtn.setOnClickListener{
            val intent = Intent(this, MyWebviewActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.googleBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}