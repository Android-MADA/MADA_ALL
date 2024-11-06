package com.mada.reapp.StartFunction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mada.reapp.databinding.On1Binding

class FragOnBoarding_1 : Fragment() {
    private lateinit var binding: On1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = On1Binding.inflate(inflater, container, false)

        return binding.root
    }
}
