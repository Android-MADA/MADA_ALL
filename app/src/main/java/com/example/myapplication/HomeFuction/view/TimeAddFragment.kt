package com.example.myapplication.HomeFuction.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentTimeAddBinding

class TimeAddFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : HomeFragmentTimeAddBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_time_add, container, false)

        val iv_color = binding.ivHomeTimeColor
        val colorSelector = binding.layoutColorSelector

        val timeStart = binding.tvHomeTimeStart
        val timeEnd = binding.tvHomeTimeEnd
        val timepickerStart = binding.homeTimepickerStart
        val timepickerEnd = binding.homeTimepickerEnd

        iv_color.setOnClickListener {
            if(colorSelector.isVisible){
                colorSelector.isGone = true
            }
            else {
                colorSelector.isVisible = true
            }
        }

        // 열려 있으면 닫고, 다른 게 열러 있으면 다른 열린 거 닫고 열고, 안 열려 있으면 그냥 열고
        timeStart.setOnClickListener {
            if(timepickerStart.isVisible){
                timepickerStart.isGone = true
            }
            else{
                timepickerEnd.isGone = true
                timepickerStart.isVisible = true
            }
        }

        timeEnd.setOnClickListener {
            if(timepickerEnd.isVisible){
                timepickerEnd.isGone = true
            }
            else{
                timepickerStart.isGone = true
                timepickerEnd.isVisible = true
            }
        }

        timepickerStart.setOnTimeChangedListener { view, hourOfDay, minute ->
            timeStart.text = "${hourOfDay}:${minute}"
        }

        timepickerEnd.setOnTimeChangedListener { view, hourOfDay, minute ->
            timeEnd.text = "${hourOfDay}:${minute}"
        }


        return binding.root
    }



}