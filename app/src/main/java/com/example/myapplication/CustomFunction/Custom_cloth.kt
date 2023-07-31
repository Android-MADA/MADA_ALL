package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.CustomClothBinding
import com.example.myapplication.databinding.FragCustomBinding


class custom_cloth {
    lateinit var binding: CustomClothBinding
    lateinit var fragbinding: FragCustomBinding

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomClothBinding.inflate(inflater, container, false)
        fragbinding = FragCustomBinding.inflate(inflater)

        var btnclothbasic = binding.btnClothBasic
        var btnclothAstronauts = binding.btnClothAstronauts
        var btnclothcaffk = binding.btnClothCaffK
        var btnclothdev = binding.btnClothDev
        var btnclothhanbokf = binding.btnClothHanbokF
        var btnclothhanbokm = binding.btnClothHanbokM
        var btnclothmovie = binding.btnClothMovie
        var btnclothsnowman = binding.btnClothSnowman
        var btnclothv = binding.btnClothV
        var btnclothzzim = binding.btnClothZzim

        btnclothbasic.setOnClickListener{
            binding.btnClothBasic.setImageResource(R.drawable.color_rblue_choice)
            fragbinding.imgCustomCloth.setImageResource(R.drawable.c_ramdi)

        }


        return binding.root
    }


}