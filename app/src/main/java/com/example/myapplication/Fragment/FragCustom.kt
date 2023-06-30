package com.example.myapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragCustomBinding

class FragCustom : Fragment() {

    lateinit var binding: FragCustomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragCustomBinding.inflate(inflater, container, false)

        return binding.root
    }

}