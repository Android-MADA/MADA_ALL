package com.example.myapplication.HomeFuction.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

        //리팩토링 -> 인터페이스 작성하기
        binding.ivHomeTimeColor1.setOnClickListener {
            ivColor.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.sub5))
            selectedColor = resources.getColor(R.color.sub5)
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor2.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.main))
            selectedColor = resources.getColor(R.color.main)
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor3.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.sub4))
            selectedColor = resources.getColor(R.color.sub4)
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor4.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.sub6))
            selectedColor = resources.getColor(R.color.sub6)
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor5.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(Color.parseColor("#FDA4B4"))
            selectedColor = Color.parseColor("#FDA4B4")
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor6.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.sub3))
            selectedColor = resources.getColor(R.color.sub3)
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor7.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(Color.parseColor("#D4ECF1"))
            selectedColor = Color.parseColor("#D4ECF1")
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor8.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(Color.parseColor("#7FC7D4"))
            selectedColor = Color.parseColor("#7FC7D4")
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor9.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.point_main))
            selectedColor = resources.getColor(R.color.point_main)
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor10.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(Color.parseColor("#FDF3CF"))
            selectedColor = Color.parseColor("#FDF3CF")
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor11.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.sub1))
            selectedColor = resources.getColor(R.color.sub1)
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }

        binding.ivHomeTimeColor12.setOnClickListener {

            ivColor.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.sub2))
            selectedColor = resources.getColor(R.color.sub2)
            Log.d("color", selectedColor.toString())
            colorSelector.isGone = true
        }



        //날짜 선택 창
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
            val AM_PM_start : String
            AM_PM_start = if (hourOfDay < 12) {
                "오전"
            } else {
                "오후"
            }

            val hourStart = if (hourOfDay <= 12) {
                hourOfDay
            } else {
                hourOfDay - 12
            }

            timeStart.text = "${AM_PM_start} ${hourStart}:${minute}"
        }

        timepickerEnd.setOnTimeChangedListener { view, hourOfDay, minute ->
            val AM_PM_end : String
            AM_PM_end = if (hourOfDay < 12) {
                "오전"
            } else {
                "오후"
            }

            val hourEnd = if (hourOfDay <= 12) {
                hourOfDay
            } else {
                hourOfDay - 12
            }


            timeEnd.text = "${AM_PM_end} ${hourEnd}:${minute}"
        }

        //등록 btn
        btnSubmit.setOnClickListener {
            //1. db에 저장(생성) 또는 수정(수정)
            // 수정버튼을 누르면 시간표가 생성되는 게 아니라 수정됨
            // list 만들고 그 안에 파라미터를 받고(데이터를 저장해서) db에 넘기기
            //2. 시간표 화면으로 이동
        }

        return binding.root
    }

}