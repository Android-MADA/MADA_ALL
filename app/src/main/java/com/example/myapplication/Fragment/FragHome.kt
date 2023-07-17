package com.example.myapplication.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragHomeBinding
import com.example.myapplication.databinding.HomeFragmentHomeBinding

class FragHome : Fragment() {

    lateinit var binding: HomeFragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

}