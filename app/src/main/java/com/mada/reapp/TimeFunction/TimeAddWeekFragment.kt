package com.mada.reapp.TimeFunction

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.mada.reapp.CalenderFuntion.Model.AndroidCalendarData
import com.mada.reapp.HomeFunction.HomeBackCustomDialog
import com.mada.reapp.HomeFunction.HomeCustomDialogListener
import com.mada.reapp.HomeFunction.Model.Schedule
import com.mada.reapp.HomeFunction.Model.ScheduleAdd
import com.mada.reapp.TimeFunction.adapter.HomeTimeColorAdapter
import com.mada.reapp.HomeFunction.viewModel.HomeViewModel
import com.mada.reapp.R
import com.mada.reapp.TimeFunction.adapter.HomeScheduleAndTodoAdapter
import com.mada.reapp.databinding.HomeFragmentTimeWeekAddBinding
import com.mada.reapp.hideBottomNavigation
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class TimeAddWeekFragment : Fragment(), HomeCustomDialogListener {

    private lateinit var binding : HomeFragmentTimeWeekAddBinding
    var timeColorArray = ArrayList<Int>()
    var colorAdapter = HomeTimeColorAdapter(timeColorArray)
    private val viewModel : HomeViewModel by activityViewModels()
    private val viewModelTime: TimeViewModel by activityViewModels()

    lateinit var today: String


    var curColor = "#89A9D9"
    lateinit var token : String
    var haveTodoCalDatas = false

    private lateinit var backDialog: HomeBackCustomDialog
    val week = arrayOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")


    var dayOfWeek = 0
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback : OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                customBackDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initColorArray()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(false,  requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_time_week_add, container, false)
        hideBottomNavigation(true,  requireActivity())
        today = viewModel.homeDate.value.toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*
        val adRequest = AdRequest.Builder()
            .build()
        binding.adView.loadAd(adRequest)*/

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
        val textMemo = binding.edtHomeScheduleMemo

        val btnBack = binding.ivHomeAddTimeBack
        val recievedPieData =  arguments?.getSerializable("pieChartData") as  TimeViewModel.PieChartData?
        today = arguments?.getString("today")?: "2023-06-01"
        var curId = 0

        if(recievedPieData != null) {
            btnSubmit.text = "삭제"
            //btnDelete.isVisible = true

            textTitle.setText(recievedPieData.title)
            ivColor.imageTintList =  ColorStateList.valueOf(Color.parseColor(recievedPieData.colorCode))
            curColor = recievedPieData.colorCode
            //ivColor.setColorFilter(Color.parseColor(recievedPieData.colorCode), PorterDuff.Mode.SRC_IN)
            colorSelector.isGone = true

            times[0].text = viewModelTime.timeChangeReverse(String.format(Locale("en", "US"), "%02d:%02d:00", recievedPieData.startHour, recievedPieData.startMin))
            times[1].text = viewModelTime.timeChangeReverse(String.format(Locale("en", "US"), "%02d:%02d:00", recievedPieData.endHour, recievedPieData.endMin))
            curId = recievedPieData.id
            textMemo.setText(recievedPieData.memo)
        }
        //색상 선택창
        val colorListManager = GridLayoutManager(this.activity, 7)
        colorAdapter.setItemClickListener(object : HomeTimeColorAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                binding.ivHomeTimeColor.imageTintList =
                    ColorStateList.valueOf(timeColorArray[position])
                binding.rvHomeTimeColor.isGone = true
                curColor = String.format("#%06X", 0xFFFFFF and timeColorArray[position])
            }
        })
        var colorRecyclerList = binding.rvHomeTimeColor.apply {
            setHasFixedSize(true)
            layoutManager = colorListManager
            adapter = colorAdapter
        }

        ivColor.setOnClickListener {
            if(btnSubmit.text == "삭제") btnSubmit.text = "수정"
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
            if(btnSubmit.text == "삭제") btnSubmit.text = "수정"
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
            if(btnSubmit.text == "삭제") btnSubmit.text = "수정"
            times[1].setBackgroundResource(R.drawable.calendar_prebackground)
            times[0].setBackgroundColor(Color.TRANSPARENT)
            timepicker.isVisible = true
            scheduleSelect = 1
            val matchResult = regex.find(times[1].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                if (ampm == "오전") {
                    ticker1.value = 0
                }
                else {
                    ticker1.value = 1
                }

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
            if(btnSubmit.text == "삭제") btnSubmit.text = "수정"
            if (newVal == 0) {
                times[scheduleSelect].text =
                    times[scheduleSelect].text.toString().replace("오후", "오전")
            } else {
                times[scheduleSelect].text =
                    times[scheduleSelect].text.toString().replace("오전", "오후")
            }
        }
        var tmpCheck = true
        ticker2.setOnValueChangedListener { picker, oldVal, newVal ->
            if(btnSubmit.text == "삭제") btnSubmit.text = "수정"
            val matchResult = regex.find(times[scheduleSelect].text.toString())
            if (matchResult != null) {
                var (ampm, hour, minute) = matchResult.destructured
                times[scheduleSelect].text = "  " + ampm + " " + newVal + ":" + minute + "  "
                if((oldVal==11&&newVal==12) ||(oldVal==12&&newVal==11)) {
                    if(tmpCheck) {
                        if (ticker1.value == 0)  {
                            ampm = "오후"
                            ticker1.value = 1
                        }
                        else {
                            ampm = "오전"
                            ticker1.value = 0
                        }
                        tmpCheck = false
                    }
                } else {
                    tmpCheck = true
                }
                times[scheduleSelect].text = "  "+ampm+" "+newVal+":"+minute+"  "
            }
        }
        ticker3.setOnValueChangedListener { picker, oldVal, newVal ->
            if(btnSubmit.text == "삭제") btnSubmit.text = "수정"
            val matchResult = regex.find(times[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                times[scheduleSelect].text =
                    "  " + ampm + " " + hour + ":" + data2[newVal] + "  "
            } else {
            }
        }
        val calendar = Calendar.getInstance()
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if (dayOfWeek == Calendar.SUNDAY) {
            dayOfWeek = 6 // 일요일은 6로 매핑
        } else {
            dayOfWeek -=2
        }
        val tmpDayOfWeek =  arguments?.getInt("dayOfWeek")
        if (tmpDayOfWeek != null) {
            dayOfWeek = tmpDayOfWeek
        }
        MobileAds.initialize(this.requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        //등록 btn
        btnSubmit.setOnClickListener {
            if(btnSubmit.text.toString()=="삭제"){
                viewModelTime.delTimeDatas(curId) { result ->
                    when (result) {
                        1 -> {
                            val tmpList = viewModelTime.pieChartDataArrayList[dayOfWeek]
                            for(data in tmpList) {
                                if(data.id==curId) {
                                    tmpList.remove(data)
                                    break
                                }
                            }
                            val bundle = Bundle()
                            bundle.putString("today",today)
                            findNavController().navigate(R.id.action_fragTimeWeekAdd_to_fragTimeTableWeek,bundle)
                        }
                        2 -> {
                            Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                var start = viewModelTime.timePlusMinutes(binding.tvHomeTimeStart.text.toString())
                var end = viewModelTime.timePlusMinutes(binding.tvHomeTimeEnd.text.toString())
                if(end == 0)
                    end =24*60
                var check = true
                if (viewModelTime.pieChartWeekDatas[dayOfWeek] != null) {
                    Log.d("week",viewModelTime.pieChartWeekDatas[dayOfWeek].toString())
                    for (data in viewModelTime.pieChartWeekDatas[dayOfWeek]) {
                        if(data.divisionNumber!=-1) {
                            var tmpStart = data.startHour * 60 + data.startMin
                            var tmpEnd = data.endHour * 60 + data.endMin
                            if(tmpEnd==0)
                                tmpEnd = 24*60
                            if ((start < tmpEnd && start >= tmpStart) || (end > tmpStart && end <= tmpEnd)) {
                                if (data.divisionNumber != recievedPieData?.divisionNumber)
                                    check = false
                            }
                        }
                    }
                }
                if(binding.edtHomeCategoryName.text.toString()=="") {
                    viewModelTime.setPopupOne(requireContext(),"스케줄명을 입력하시오", view)
                } else if(viewModelTime.compareTimes(binding.tvHomeTimeStart.text.toString(),binding.tvHomeTimeEnd.text.toString())) {
                    viewModelTime.setPopupOne(requireContext(),"30분 미만의 시간표는 추가하실수 없습니다", view)
                } else if(!check){            //이미 해당 시간에 일정이 있을 때
                    viewModelTime.setPopupOne(requireContext(),"해당 시간에 이미 일정이 존재합니다", view)
                } else {
                    val tmp = ScheduleAdd(today,binding.edtHomeCategoryName.text.toString(),curColor,viewModelTime.timeChange(binding.tvHomeTimeStart.text.toString()),
                        viewModelTime.timeChange(binding.tvHomeTimeEnd.text.toString()),binding.edtHomeScheduleMemo.text.toString(),false,week[dayOfWeek])
                    if(btnSubmit.text.toString()=="등록") {
                        viewModelTime.addTimeWeekDatas(tmp) { result ->
                            when (result) {
                                1 -> {
                                    val tmpId = viewModelTime.addId
                                    viewModelTime.pieChartDataArrayList[dayOfWeek].add(Schedule(tmpId,today,binding.edtHomeCategoryName.text.toString(),curColor,viewModelTime.timeChange(binding.tvHomeTimeStart.text.toString()),
                                        viewModelTime.timeChange(binding.tvHomeTimeEnd.text.toString()),binding.edtHomeScheduleMemo.text.toString(),false,week[dayOfWeek]))

                                    val bundle = Bundle()
                                    bundle.putString("today",today)
                                    findNavController().navigate(R.id.action_fragTimeWeekAdd_to_fragTimeTableWeek,bundle)
                                }
                                2 -> {
                                    Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else if(btnSubmit.text.toString()=="수정") {
                        viewModelTime.editTimeWeekData(curId,tmp) { result ->
                            when (result) {
                                1 -> {
                                    val tmpList = viewModelTime.pieChartDataArrayList[tmpDayOfWeek!!]
                                    for(data in tmpList) {
                                        if(data.id==curId) {
                                            tmpList.remove(data)
                                            break
                                        }
                                    }
                                    viewModelTime.pieChartDataArrayList[dayOfWeek].add(
                                        Schedule(curId,today,binding.edtHomeCategoryName.text.toString(),curColor,viewModelTime.timeChange(binding.tvHomeTimeStart.text.toString()),
                                            viewModelTime.timeChange(binding.tvHomeTimeEnd.text.toString()),binding.edtHomeScheduleMemo.text.toString(),false,week[dayOfWeek])
                                    )

                                    val bundle = Bundle()
                                    bundle.putString("today",today)
                                    findNavController().navigate(R.id.action_fragTimeWeekAdd_to_fragTimeTableWeek,bundle)
                                }
                                2 -> {
                                    Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }

        }
        binding.ivHomeAddTimeBack.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("today",today)
            findNavController().navigate(R.id.action_fragTimeWeekAdd_to_fragTimeTableWeek,bundle)
        }
        binding.homeFragmentTimeAddLayout.setFocusableInTouchMode(true);
        binding.homeFragmentTimeAddLayout.setOnClickListener {
            binding.homeFragmentTimeAddLayout.requestFocus()
        }

        val datas = ArrayList<AndroidCalendarData>()
        viewModelTime.getTodoCalDatas(today,datas) { result ->
            when (result) {
                1 -> {
                    if(!datas.isEmpty())
                        haveTodoCalDatas = true
                    val adapter = HomeScheduleAndTodoAdapter(datas,
                        LocalDate.parse(today).dayOfMonth,binding.edtHomeCategoryName,binding.tvHomeTimeStart,binding.tvHomeTimeEnd, binding.homeTimeTodoListView)
                    var manager: RecyclerView.LayoutManager = GridLayoutManager(context,1)
                    binding.homeTimeTodoList.layoutManager = manager
                    binding.homeTimeTodoList.adapter = adapter
                }
                2 -> {
                    //Toast.makeText(context, "서버 와의 통신 불안정12", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.edtHomeCategoryName.setOnFocusChangeListener { view, hasFocus ->
            if(btnSubmit.text == "삭제") btnSubmit.text = "수정"
            if (hasFocus&&haveTodoCalDatas) {
                binding.homeTimeTodoListView.visibility = View.VISIBLE
            } else
                binding.homeTimeTodoListView.visibility = View.GONE
        }
        binding.timeLayout.setOnClickListener {
            if(btnSubmit.text == "삭제") btnSubmit.text = "수정"
        }
        binding.edtHomeScheduleMemo.setOnClickListener {
            if(btnSubmit.text == "삭제") btnSubmit.text = "수정"
        }

        val textWeek = arrayOf(
            binding.textWeek1, binding.textWeek2, binding.textWeek3, binding.textWeek4, binding.textWeek5,binding.textWeek6,binding.textWeek7
        )
        val textWeekString = arrayOf(
            "월요일","화요일","수요일","목요일","금요일","토요일","일요일"
        )




        var preTextView = textWeek[dayOfWeek]
        var selectedTextWeek = textWeek[dayOfWeek]



        for ((index, textView) in textWeek.withIndex()) {
            textView.setOnClickListener {
                if(btnSubmit.text == "삭제") btnSubmit.text = "수정"
                preTextView.setTextColor(Color.BLACK)
                preTextView.setBackgroundResource(android.R.color.transparent)
                // 클릭 시 기존에 선택된 TextView의 배경을 투명하게 만듭니다.
                selectedTextWeek?.setBackgroundResource(android.R.color.transparent)
                dayOfWeek = index

                // 클릭한 TextView의 배경을 원하는 배경으로 설정합니다.
                textView.setBackgroundResource(R.drawable.calendar_add_repeat_back1) // 여기서 R.drawable.selected_background는 적절한 배경 리소스로 변경해야 합니다.
                preTextView = textView
                preTextView.setTextColor(Color.WHITE)
                // 선택된 TextView를 업데이트합니다.
                selectedTextWeek = textView
                binding.timeWeekAddDayTv.text = textWeekString[index]
            }
        }
        textWeek[dayOfWeek].callOnClick()
    }
    private fun initColorArray(){
        with(timeColorArray){
            timeColorArray.add(resources.getColor(com.mada.reapp.R.color.sub1))
            timeColorArray.add(android.graphics.Color.parseColor("#F68F30"))
            timeColorArray.add(android.graphics.Color.parseColor("#ED6C64"))
            timeColorArray.add(android.graphics.Color.parseColor("#FDA4B4"))
            timeColorArray.add(android.graphics.Color.parseColor("#F076A2"))
            timeColorArray.add(resources.getColor(com.mada.reapp.R.color.sub2))
            timeColorArray.add(resources.getColor(com.mada.reapp.R.color.sub2))
            timeColorArray.add(resources.getColor(com.mada.reapp.R.color.sub2))

            timeColorArray.add(android.graphics.Color.parseColor("#7FC7D4"))
            timeColorArray.add(resources.getColor(com.mada.reapp.R.color.point_main))
            timeColorArray.add(android.graphics.Color.parseColor("#21C362"))
            timeColorArray.add(android.graphics.Color.parseColor("#0E9746"))
            timeColorArray.add(resources.getColor(com.mada.reapp.R.color.main))
            timeColorArray.add(resources.getColor(com.mada.reapp.R.color.sub4))
            timeColorArray.add(resources.getColor(com.mada.reapp.R.color.sub2))
            timeColorArray.add(resources.getColor(com.mada.reapp.R.color.sub2))
        }
    }
    private fun customBackDialog() {
        backDialog = HomeBackCustomDialog(requireActivity(), this)
        backDialog.show()
    }
    // 커스텀 다이얼로그에서 버튼 클릭 시
    override fun onYesButtonClicked(dialog: Dialog, flag: String) {
            Navigation.findNavController(requireView()).navigate(R.id.action_fragTimeWeekAdd_to_fragTimeTableWeek)
        dialog.dismiss()
    }

    override fun onNoButtonClicked(dialog: Dialog) {
        dialog.dismiss()
    }

}