package com.example.myapplication.HomeFunction.view

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.HomeFunction.time.HomeTimeColorAdapter
import com.example.myapplication.HomeFunction.view.viewpager2.HomeViewpagerTimetableFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentTimeAddBinding
import com.example.myapplication.hideBottomNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView

class TimeAddFragment : Fragment() {

    private lateinit var binding : HomeFragmentTimeAddBinding
    private var bottomFlag = true
    var timeColorArray = ArrayList<Int>()
    var colorAdapter = HomeTimeColorAdapter(timeColorArray)


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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivColor = binding.ivHomeTimeColor
        val colorSelector = binding.rvHomeTimeColor

        val times = arrayOf<TextView>(
            binding.tvHomeTimeStart,
            binding.tvHomeTimeEnd
        )
        val timepicker = binding.timePicker
        val ticker1 = binding.numberPicker1
        val ticker2 = binding.numberPicker2
        val ticker3 = binding.numberPicker3

        val textTitle = binding.edtHomeCategoryName
        val btnSubmit = binding.btnHomeTimeAddSubmit
        val btnDelete = binding.btnHomeTimeEditDelete
        val textMemo = binding.edtHomeScheduleMemo

        val btnBack = binding.ivHomeAddTimeBack
        // 데이터 받기
        val receivedData = arguments?.getSerializable("pieChartDataArray") as  Array<HomeViewpagerTimetableFragment.PieChartData>?
        val recievedPieData =  arguments?.getSerializable("pieChartData") as  HomeViewpagerTimetableFragment.PieChartData?

        fun convertTo12HourFormat(hour24: Int, minute: Int): String {
            val amPm: String
            val hour12: Int

            if (hour24 >= 12) {
                amPm = "오후"
                hour12 = if (hour24 > 12) hour24 - 12 else hour24
            } else {
                amPm = "오전"
                hour12 = if (hour24 == 0) 12 else hour24
            }

            val minuteStr = if (minute < 10) "0$minute" else minute.toString()
            return "  $amPm $hour12:$minuteStr  "
        }


        if(recievedPieData != null) {
            btnSubmit.text = "수정"
            btnDelete.isVisible = true

            textTitle.setText(recievedPieData.title)
            ivColor.setColorFilter(Color.parseColor(recievedPieData.colorCode), PorterDuff.Mode.SRC_IN)
            colorSelector.isGone = true

            times[0].text = convertTo12HourFormat(recievedPieData.startHour,recievedPieData.startMin)
            times[1].text = convertTo12HourFormat(recievedPieData.endHour,recievedPieData.endMin)
            textMemo.setText(recievedPieData.memo)
        }

        //파라미터가 전달된다면(생성이 아니라 수정이라면)
//        if(){
//            //3. 받아온 파라미터들을 알맞은 장소에 넣기
//            //4. 이전 시간표 데이터는 삭제하기
//        }

        //색상 선택창
        val colorListManager = GridLayoutManager(this.activity, 6)
        colorAdapter.setItemClickListener(object : HomeTimeColorAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                binding.ivHomeTimeColor.imageTintList =
                    ColorStateList.valueOf(timeColorArray[position])
                binding.rvHomeTimeColor.isGone = true
            }
        })
        var colorRecyclerList = binding.rvHomeTimeColor.apply {
            setHasFixedSize(true)
            layoutManager = colorListManager
            adapter = colorAdapter
        }

        ivColor.setOnClickListener {
            if (colorSelector.isVisible) {
                colorSelector.isGone = true
            } else {
                colorSelector.isVisible = true
            }
        }

        // 데이터 클래스 작성해서 옮기기


        var scheduleSelect = 0
        val data1 = arrayOf("오전", "오후")
        val data2 = arrayOf("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55")
        val regex = """\s*(오전|오후)\s+(\d{1,2}):(\d{2})\s*""".toRegex()

        times[0].setOnClickListener {
            times[0].setBackgroundResource(R.drawable.calendar_prebackground)
            times[1].setBackgroundColor(Color.TRANSPARENT)
            timepicker.isVisible = true
            scheduleSelect = 0
            val matchResult = regex.find(times[0].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured

                if (ampm == "오전") {
                    ticker1.value = 0
                } else {
                    ticker1.value = 1
                }

                ticker2.value = hour.toInt()
                ticker3.value = minute.toInt() / 5
            } else {
                ticker1.value = 0
                ticker2.value = 10
                ticker3.value = 0
            }
        }

        times[1].setOnClickListener {
            times[1].setBackgroundResource(R.drawable.calendar_prebackground)
            times[0].setBackgroundColor(Color.TRANSPARENT)
            timepicker.isVisible = true
            scheduleSelect = 1
            val matchResult = regex.find(times[1].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                if (ampm == "오전")
                    ticker1.value = 0
                else
                    ticker1.value = 1
                ticker2.value = hour.toInt()
                ticker3.value = minute.toInt() / 5
            } else {
                ticker1.value = 0
                ticker2.value = 11
                ticker3.value = 0
            }
        }


        ticker1.minValue = 0
        ticker1.maxValue = 1
        ticker1.displayedValues = data1
        ticker3.minValue = 0
        ticker3.maxValue = 11
        ticker3.displayedValues = data2
        ticker1.setOnValueChangedListener { picker, oldVal, newVal ->
            if (newVal == 0) {
                times[scheduleSelect].text =
                    times[scheduleSelect].text.toString().replace("오후", "오전")
            } else {
                times[scheduleSelect].text =
                    times[scheduleSelect].text.toString().replace("오전", "오후")
            }
        }
        ticker2.setOnValueChangedListener { picker, oldVal, newVal ->
            val matchResult = regex.find(times[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                times[scheduleSelect].text = "  " + ampm + " " + newVal + ":" + minute + "  "
            } else {
            }
        }
        ticker3.setOnValueChangedListener { picker, oldVal, newVal ->
            val matchResult = regex.find(times[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                times[scheduleSelect].text =
                    "  " + ampm + " " + hour + ":" + data2[newVal - 1] + "  "
            } else {
            }
        }

        //등록 btn
        btnSubmit.setOnClickListener {
            var matchResult = regex.find(times[0].text.toString())
            var start: Int = 0
            var end: Int = 0
            if (matchResult != null) {
                val (ampm, tmpHour, tmpMin) = matchResult.destructured
                if (ampm == "오전") {
                    start = tmpHour.toInt() * 60 + tmpMin.toInt()
                } else {
                    start = tmpHour.toInt() * 60 + tmpMin.toInt() + 12 * 60
                }

            }
            matchResult = regex.find(times[1].text.toString())
            if (matchResult != null) {
                val (ampm, tmpHour, tmpMin) = matchResult.destructured
                if (ampm == "오전") {
                    end = tmpHour.toInt() * 60 + tmpMin.toInt()
                } else {
                    end = tmpHour.toInt() * 60 + tmpMin.toInt() + 12 * 60
                }
            }
            var check = true
            if (receivedData != null) {
                for (data in receivedData) {

                    var tmpStart = data.startHour * 60 + data.startMin
                    var tmpEnd = data.endHour * 60 + data.endMin
                    if ((start < tmpEnd && start >= tmpStart) || (end > tmpStart && end <= tmpEnd)) {
                        if (data.divisionNumber != recievedPieData?.divisionNumber)
                            check = false
                    }
                }
                Log.d("receivedData", "!null")
            } else {

            }
            if (check) {         //이상 x
                findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
                //만약 수정 상태라면 해당 데이터 수정 해야함!!!
                //등록 상태라면 데이터 등록

            } else {            //이미 해당 시간에 일정이 있을 때
                val mDialogView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.home_fragment_time_add_warningsign, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener({
                    mBuilder.dismiss()
                })
            }

            // list 만들고 그 안에 파라미터를 받고(데이터를 저장해서) db에 넘기기
            //2. 시간표 화면으로 이동

        }
        binding.ivHomeAddTimeBack.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
        }
        btnDelete.setOnClickListener {
            //데이터 삭제 해야함
            findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
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


}