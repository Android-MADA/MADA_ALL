package com.mada.myapplication.StartFuction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mada.myapplication.databinding.On2Binding

class FragOnBoarding_2 : Fragment() {
    private lateinit var binding: On2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = On2Binding.inflate(inflater, container, false)
        return binding.root
    }
}
