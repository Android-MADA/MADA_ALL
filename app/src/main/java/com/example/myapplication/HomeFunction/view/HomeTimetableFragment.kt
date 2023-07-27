package com.example.myapplication.HomeFunction.view

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.time.HomeTimeAdapter
import com.example.myapplication.HomeFunction.time.SampleTimeData
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentTimetableBinding
import com.example.myapplication.hideBottomNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeTimetableFragment : Fragment() {

    lateinit var binding : HomeFragmentTimetableBinding
    val sampleTimeArray = ArrayList<SampleTimeData>()
    val timeAdapter = HomeTimeAdapter(sampleTimeArray)
    var bottomFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_timetable, container, false)
        hideBottomNavigation(bottomFlag, activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivHomeTimetableBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_fragHome)
            bottomFlag = false
        }

        binding.tvHomeTimetableSave.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_fragHome)
            bottomFlag = false
        }

        binding.fabHomeTime.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_timeAddFragment)
        }

        //rv adpter 연결
        timeAdapter.setItemClickListener(object: HomeTimeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //페이지 이동 + 데이터 전달
                Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_timeAddFragment)
            }
        })

        binding.rvHomeTimeSchedule.adapter = timeAdapter
        binding.rvHomeTimeSchedule.layoutManager = LinearLayoutManager(this.activity)

        //edt처리

        //pie chart 추가


    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }



    private fun initArrayList(){
        with(sampleTimeArray){
            sampleTimeArray.add(SampleTimeData("잠", resources.getColor(R.color.sub2), "오전 10:30", "오전 11:00", "2023-07-20"))
            sampleTimeArray.add(SampleTimeData("오전수업", Color.parseColor("#FDA4B4"), "오전 10:00", "오전 10:30", "2023-07-20"))
        }
    }


}

