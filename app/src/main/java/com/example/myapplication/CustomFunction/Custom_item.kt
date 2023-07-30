package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.CustomItemBinding

class custom_item {
    lateinit var binding: CustomItemBinding

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomItemBinding.inflate(inflater, container, false)

        return binding.root
    }
}