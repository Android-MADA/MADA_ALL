package com.example.myapplication.MyFuction

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.MyLogoutPopupBinding

class MyLogoutPopupActivity(context: Context) : Dialog(context) {
    private lateinit var binding: MyLogoutPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyLogoutPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 배경을 투명하게함
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.nobutton.setOnClickListener {
            Log.d("로그아웃 취소", "로그아웃 취소")
        }
        binding.yesbutton.setOnClickListener{
//            val intent = Intent(this, Splash2Activity::class.java)
//            startActivity(intent)
        }

    }
}