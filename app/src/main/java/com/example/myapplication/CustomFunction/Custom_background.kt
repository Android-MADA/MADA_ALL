package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.CustomBackgroundBinding

class custom_background {
    lateinit var binding: CustomBackgroundBinding

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomBackgroundBinding.inflate(inflater, container, false)

        return binding.root
    }
}
