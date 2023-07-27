package com.example.myapplication.HomeFunction.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.CustomCircleBarView
import com.example.myapplication.HomeFunction.time.HomeTimeAdapter
import com.example.myapplication.HomeFunction.time.SampleTimeData
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentTimetableBinding


class HomeTimetableFragment : Fragment() {

    lateinit var binding : HomeFragmentTimetableBinding
    val sampleTimeArray = ArrayList<SampleTimeData>()
    val timeAdapter = HomeTimeAdapter(sampleTimeArray)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_timetable, container, false)

        val btnBack = binding.ivHomeTimetableBack
        val btnSave = binding.tvHomeTimetableSave
        val btnAdd = binding.fabHomeTime

        //rv adpter 연결
        timeAdapter.setItemClickListener(object: HomeTimeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //페이지 이동 + 데이터 전달
                findNavController().navigate(R.id.action_homeTimetableFragment_to_timeAddFragment)
            }
        })

        binding.rvHomeTimeSchedule.adapter = timeAdapter
        binding.rvHomeTimeSchedule.layoutManager = LinearLayoutManager(this.activity)

        //setonclickevent

        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_homeTimetableFragment_to_fragHome)
        }

        btnSave.setOnClickListener {
            findNavController().navigate(R.id.action_homeTimetableFragment_to_fragHome)
        }

        btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeTimetableFragment_to_timeAddFragment)
        }

        // pie chart

        //edt
        return binding.root
    }

    private fun initArrayList(){
        with(sampleTimeArray){
            sampleTimeArray.add(SampleTimeData("잠", resources.getColor(R.color.sub2), "오전 10:30", "오전 11:00", "2023-07-20"))
            sampleTimeArray.add(SampleTimeData("오전수업", Color.parseColor("#FDA4B4"), "오전 10:00", "오전 10:30", "2023-07-20"))
        }
    }
}