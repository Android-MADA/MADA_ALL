package com.mada.myapplication.MyFuction


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mada.myapplication.databinding.MyPremiumBinding
class PremiumActivity : AppCompatActivity() {

    private lateinit var binding: MyPremiumBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  MyPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}