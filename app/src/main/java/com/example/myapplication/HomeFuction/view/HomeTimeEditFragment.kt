package com.example.myapplication.HomeFuction.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFuction.time.HomeTimeAdapter
import com.example.myapplication.HomeFuction.time.SampleTimeData
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentHomeTimeEditBinding


class HomeTimeEditFragment : Fragment() {

    lateinit var binding : HomeFragmentHomeTimeEditBinding
    val sampleTimeArray = ArrayList<SampleTimeData>()
    val timeAdapter = HomeTimeAdapter(sampleTimeArray)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_home_time_edit, container, false)

        //rv adpter 연결
        initArrayList()

        timeAdapter.setItemClickListener(object :HomeTimeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //페이지 이동
            }
        })

        binding.rvHomeTimeSchedule.adapter = timeAdapter
        binding.rvHomeTimeSchedule.layoutManager = LinearLayoutManager(this.activity)

        //setonclickevent

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