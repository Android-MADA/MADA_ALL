package com.example.myapplication.HomeFuction.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentRepeatTodoBinding

class HomeRepeatTodoFragment : Fragment() {

    lateinit var binding : HomeFragmentRepeatTodoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_repeat_todo, container, false)

        binding.ivHomeRepeatBack.setOnClickListener {
            findNavController().navigate(R.id.action_homeRepeatTodoFragment_to_fragHome)
        }
        return binding.root
    }


}