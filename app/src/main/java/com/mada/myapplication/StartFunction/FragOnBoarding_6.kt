package com.mada.myapplication.StartFunction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mada.myapplication.databinding.On6Binding

class FragOnBoarding_6 : Fragment() {
    private lateinit var binding: On6Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = On6Binding.inflate(inflater, container, false)
        return binding.root
    }
}
