package com.example.myapplication.HomeFunction.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.HomeFunction.category.HomeCateColorAdapter
import com.example.myapplication.HomeFunction.time.HomeTimeColorAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentTimeAddBinding
import com.example.myapplication.hideBottomNavigation

class TimeAddFragment : Fragment() {

    lateinit var binding : HomeFragmentTimeAddBinding
    var timeColorArray = ArrayList<Int>()
    var colorAdapter = HomeTimeColorAdapter(timeColorArray)

    var bottomFlag = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initColorArray()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_time_add, container, false)
        hideBottomNavigation(bottomFlag, activity)
        //파라미터가 전달된다면(생성이 아니라 수정이라면)
//        if(){
//            //1. 등록 text를 수정 text 로 변경하기
//            btnSubmit.text = "수정"
//            //2. 삭제하기 btn 활설화시키기
//            btnDelete.isVisible = true
//            //3. 받아온 파라미터들을 알맞은 장소에 넣기
//            //4. 이전 시간표 데이터는 삭제하기
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timepickerStart = binding.homeTimepickerStart
        val timepickerEnd = binding.homeTimepickerEnd

        //color selector
        val colorListManager = GridLayoutManager(this.activity, 6)
        colorAdapter.setItemClickListener(object: HomeTimeColorAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                binding.ivHomeTimeColor.imageTintList = ColorStateList.valueOf(timeColorArray[position])
                binding.rvHomeTimeColor.isGone = true
            }
        })
        var colorRecyclerList = binding.rvHomeTimeColor.apply{
            setHasFixedSize(true)
            layoutManager = colorListManager
            adapter = colorAdapter
        }
        binding.ivHomeTimeColor.setOnClickListener{
            if(binding.rvHomeTimeColor.isVisible){
                binding.rvHomeTimeColor.isGone = true
            }
            else {
                binding.rvHomeTimeColor.isVisible = true
            }
        }

        //time selector
        binding.tvHomeTimeStart.setOnClickListener {
            if(timepickerStart.isVisible){
                timepickerStart.isGone = true
            }
            else{
                timepickerEnd.isGone = true
                timepickerStart.isVisible = true
            }
        }

        binding.tvHomeTimeEnd.setOnClickListener {
            if(timepickerEnd.isVisible){
                timepickerEnd.isGone = true
            }
            else{
                timepickerStart.isGone = true
                timepickerEnd.isVisible = true
            }
        }

        timepickerStart.setOnTimeChangedListener { view, hourOfDay, minute ->
            timeClick(hourOfDay, minute, binding.tvHomeTimeStart)
        }

        timepickerEnd.setOnTimeChangedListener { view, hourOfDay, minute ->
            timeClick(hourOfDay, minute, binding.tvHomeTimeEnd)
        }

        //btn clicklistener
        binding.btnHomeTimeAddSubmit.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
        }
        binding.ivHomeAddTimeBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
        }
    }

    private fun initColorArray(){
        with(timeColorArray){
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub5))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.main))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub4))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub6))
            timeColorArray.add(android.graphics.Color.parseColor("#FDA4B4"))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub3))
            timeColorArray.add(android.graphics.Color.parseColor("#D4ECF1"))
            timeColorArray.add(android.graphics.Color.parseColor("#7FC7D4"))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.point_main))
            timeColorArray.add(android.graphics.Color.parseColor("#FDF3CF"))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub1))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub2))

        }
    }

    fun timeClick(hour : Int, minute : Int, view : TextView){
        val AM_PM_start : String
        AM_PM_start = if (hour < 12) {
            "오전"
        } else {
            "오후"
        }

        val hourStart = if (hour <= 12) {
            hour
        } else {
            hour - 12
        }
        view.text = "${AM_PM_start} ${hourStart}:${minute}"
    }


}