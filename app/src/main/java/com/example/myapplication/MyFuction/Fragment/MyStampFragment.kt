package com.example.myapplication.MyFuction.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.MyStampBinding

class MyStampFragment : Fragment() {
    private lateinit var binding: MyStampBinding
    lateinit var navController: NavController
    private var isButtonClicked = false

    // retrofit
    private var token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM3NDYwOCwiZXhwIjoxNjkyNDEwNjA4fQ.FWaurv6qy-iiha07emFxGIZjAnwL3fluFsZSQY-AvlmBBsHe5ZtfRL69l6zP1ntOGIWEGb5IbCLd5JP4MjWu4w"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyStampBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        binding.cancelBtn.setOnClickListener {
            navController.navigate(R.id.action_myStampFragment_to_fragMy)
        }
    }
}