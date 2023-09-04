package com.example.myapplication.MyFuction.Activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
            Log.d("로그아웃 성공", "로그아웃 성공")
//            val intent = Intent(this, Splash2Activity::class.java)
//            startActivity(intent)
        }

    }
}