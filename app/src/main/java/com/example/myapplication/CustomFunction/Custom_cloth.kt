package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.CustomClothBinding


class custom_cloth {
    lateinit var binding: CustomClothBinding

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomClothBinding.inflate(inflater, container, false)

        return binding.root
    }


}