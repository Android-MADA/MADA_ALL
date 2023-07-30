package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.CustomColorBinding

class custom_color {

    lateinit var binding: CustomColorBinding

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomColorBinding.inflate(inflater, container, false)

        return binding.root
    }
}