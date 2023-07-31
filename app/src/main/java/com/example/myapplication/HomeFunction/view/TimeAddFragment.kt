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
import androidx.navigation.fragment.findNavController
import com.example.myapplication.HomeFunction.view.viewpager2.HomeViewpagerTimetableFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentTimeAddBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        val times = arrayOf<TextView>(
            binding.tvHomeTimeStart,
            binding.tvHomeTimeEnd
        )
        val timepicker = binding.timePicker
        val ticker1 = binding.numberPicker1
        val ticker2 = binding.numberPicker2
        val ticker3 = binding.numberPicker3

        val btnSubmit = binding.btnHomeTimeAddSubmit
        val btnDelete = binding.btnHomeTimeEditDelete

        val btnBack = binding.ivHomeAddTimeBack
        // 데이터 받기
        val receivedData = arguments?.getSerializable("pieChartDataArray") as  Array<HomeViewpagerTimetableFragment.PieChartData>?
        if( receivedData != null) {

        }


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


        var scheduleSelect = 0
        val data1 = arrayOf("오전", "오후")
        val data2 = arrayOf("00","05","10","15","20","25","30","35","40","45","50","55")
        val regex = """\s*(오전|오후)\s+(\d{1,2}):(\d{2})\s*""".toRegex()

        times[0].setOnClickListener {
            times[0].setBackgroundResource(R.drawable.calendar_prebackground)
            times[1].setBackgroundColor(Color.TRANSPARENT)
            timepicker.isVisible = true
            scheduleSelect=0
            val matchResult = regex.find(times[0].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured

                if(ampm=="오전") {
                    ticker1.value=0
                }
                else {
                    ticker1.value=1
                }

                ticker2.value=hour.toInt()
                ticker3.value=minute.toInt()/5
            } else {
                ticker1.value=0
                ticker2.value=10
                ticker3.value=0
            }
        }

        times[1].setOnClickListener {
            times[1].setBackgroundResource(R.drawable.calendar_prebackground)
            times[0].setBackgroundColor(Color.TRANSPARENT)
            timepicker.isVisible = true
            scheduleSelect=1
            val matchResult = regex.find(times[1].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                if(ampm=="오전")
                    ticker1.value=0
                else
                    ticker1.value=1
                ticker2.value=hour.toInt()
                ticker3.value=minute.toInt()/5
            } else {
                ticker1.value=0
                ticker2.value=11
                ticker3.value=0
            }
        }


        ticker1.minValue = 0
        ticker1.maxValue = 1
        ticker1.displayedValues  = data1
        ticker3.minValue = 0
        ticker3.maxValue = 11
        ticker3.displayedValues  = data2
        ticker1.setOnValueChangedListener { picker, oldVal, newVal ->
            if(newVal==0) {
                times[scheduleSelect].text = times[scheduleSelect].text.toString().replace("오후","오전")
            } else {
                times[scheduleSelect].text = times[scheduleSelect].text.toString().replace("오전","오후")
            }
        }
        ticker2.setOnValueChangedListener { picker, oldVal, newVal ->
            val matchResult = regex.find(times[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                times[scheduleSelect].text = "  "+ampm+" "+newVal+":"+minute+"  "
            } else {
            }
        }
        ticker3.setOnValueChangedListener { picker, oldVal, newVal ->
            val matchResult = regex.find(times[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                times[scheduleSelect].text = "  "+ampm+" "+hour+":"+data2[newVal-1]+"  "
            } else {
            }
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

    fun hideBootomNavigation(bool : Boolean){
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if(bool){
            bottomNavigation?.isGone = true
        }
        else {
            bottomNavigation?.isVisible = true
        }
    }

}