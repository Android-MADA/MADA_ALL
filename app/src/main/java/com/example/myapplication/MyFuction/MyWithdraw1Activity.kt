package com.example.myapplication.MyFuction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.MyWithdraw1Binding

class MyWithdraw1Activity : AppCompatActivity() {
    private lateinit var binding: MyWithdraw1Binding
    private var isButtonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyWithdraw1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.myWithdrawBtn.setOnClickListener {
            if(binding.checkBox.isChecked){
                isButtonClicked = !isButtonClicked
                binding.myWithdrawBtn.setBackgroundResource(R.drawable.my_withdrwaw_btn_ok)

                val intent = Intent(this, Splash2Activity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}