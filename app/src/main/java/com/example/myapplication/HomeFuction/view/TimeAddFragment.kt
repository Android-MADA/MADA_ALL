package com.example.myapplication.HomeFuction.view

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
import androidx.navigation.fragment.findNavController
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

        val ivColor = binding.ivHomeTimeColor
        val colorSelector = binding.layoutColorSelector

        val timeStart = binding.tvHomeTimeStart
        val timeEnd = binding.tvHomeTimeEnd
        val timepickerStart = binding.homeTimepickerStart
        val timepickerEnd = binding.homeTimepickerEnd

        val btnSubmit = binding.btnHomeTimeAddSubmit
        val btnDelete = binding.btnHomeTimeEditDelete

        val btnBack = binding.ivHomeAddTimeBack

        //파라미터가 전달된다면(생성이 아니라 수정이라면)
//        if(){
//            //1. 등록 text를 수정 text 로 변경하기
//            btnSubmit.text = "수정"
//            //2. 삭제하기 btn 활설화시키기
//            btnDelete.isVisible = true
//            //3. 받아온 파라미터들을 알맞은 장소에 넣기
//            //4. 이전 시간표 데이터는 삭제하기
//        }


        //색상 선택창
        // radio group 처리하기
        // 클릭 시 아이콘 색도 바꾸기
        ivColor.setOnClickListener {
            if(colorSelector.isVisible){
                colorSelector.isGone = true
            }
            else {
                colorSelector.isVisible = true
            }
        }

        // 데이터 클래스 작성해서 옮기기
        var selectedColor = resources.getColor(R.color.sub2)

        fun homeColorClick(color : Int){
            ivColor.imageTintList = ColorStateList.valueOf(color)
            selectedColor = color
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor1.setOnClickListener {
            homeColorClick(resources.getColor(R.color.sub5))
        }

        binding.ivHomeTimeColor2.setOnClickListener {
            homeColorClick(resources.getColor(R.color.main))
        }

        binding.ivHomeTimeColor3.setOnClickListener {
            homeColorClick(resources.getColor(R.color.sub4))
        }

        binding.ivHomeTimeColor4.setOnClickListener {
            homeColorClick(resources.getColor(R.color.sub6))
        }

        binding.ivHomeTimeColor5.setOnClickListener {
            homeColorClick(Color.parseColor("#FDA4B4"))
        }

        binding.ivHomeTimeColor6.setOnClickListener {
            homeColorClick(resources.getColor(R.color.sub3))
        }

        binding.ivHomeTimeColor7.setOnClickListener {
            homeColorClick(Color.parseColor("#D4ECF1"))
        }

        binding.ivHomeTimeColor8.setOnClickListener {
            homeColorClick(Color.parseColor("#7FC7D4"))
        }

        binding.ivHomeTimeColor9.setOnClickListener {
            homeColorClick(resources.getColor(R.color.point_main))
        }

        binding.ivHomeTimeColor10.setOnClickListener {
            homeColorClick(Color.parseColor("#FDF3CF"))
        }

        binding.ivHomeTimeColor11.setOnClickListener {
            homeColorClick(resources.getColor(R.color.sub1))
        }

        binding.ivHomeTimeColor12.setOnClickListener {
            homeColorClick(resources.getColor(R.color.sub2))
        }



        //시간 선택
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
            timeClick(hourOfDay, minute, timeStart)
        }

        timepickerEnd.setOnTimeChangedListener { view, hourOfDay, minute ->
            timeClick(hourOfDay, minute, timeEnd)
        }

        //등록 btn
        btnSubmit.setOnClickListener {
            //1. db에 저장(생성) 또는 수정(수정)
            // 수정버튼을 누르면 시간표가 생성되는 게 아니라 수정됨
            // list 만들고 그 안에 파라미터를 받고(데이터를 저장해서) db에 넘기기
            //2. 시간표 화면으로 이동
            findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
        }
        //back btn
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
        }

        return binding.root
    }

}