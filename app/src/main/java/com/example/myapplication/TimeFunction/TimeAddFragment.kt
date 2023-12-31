package com.example.myapplication.TimeFunction

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
import com.example.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.example.myapplication.HomeFunction.HomeBackCustomDialog
import com.example.myapplication.HomeFunction.HomeCustomDialogListener
import com.example.myapplication.HomeFunction.Model.Schedule
import com.example.myapplication.HomeFunction.Model.ScheduleAdd
import com.example.myapplication.TimeFunction.adapter.HomeTimeColorAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.TimeFunction.adapter.HomeScheduleAndTodoAdapter
import com.example.myapplication.databinding.HomeFragmentTimeAddBinding
import com.example.myapplication.hideBottomNavigation
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.time.LocalDate
import java.util.Locale

class TimeAddFragment : Fragment(), HomeCustomDialogListener {

    private lateinit var binding : HomeFragmentTimeAddBinding
    var timeColorArray = ArrayList<Int>()
    var colorAdapter = HomeTimeColorAdapter(timeColorArray)
    private val viewModel : HomeViewModel by activityViewModels()
    private val viewModelTime: TimeViewModel by activityViewModels()

    lateinit var today: String

    var preFragment : Int = 0

    var curColor = "#89A9D9"
    lateinit var token : String
    var haveTodoCalDatas = false

    private lateinit var backDialog: HomeBackCustomDialog

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
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_time_add, container, false)
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
        // 데이터 받기
        val receivedData = arguments?.getSerializable("pieChartDataArray") as?  ArrayList<TimeViewModel.PieChartData>?: null
        Log.d("reciedvd",receivedData.toString())
        val recievedPieData =  arguments?.getSerializable("pieChartData") as  TimeViewModel.PieChartData?
        today = arguments?.getString("today")?: "2023-06-01"
        preFragment = arguments?.getInt("frag")?:R.id.action_fragTimeAdd_to_fragTime
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

        //등록 btn
        btnSubmit.setOnClickListener {
            if(btnSubmit.text.toString()=="삭제"){
                viewModelTime.delTimeDatas(curId) { result ->
                    when (result) {
                        1 -> {
                            val tmpList = viewModelTime.hashMapArraySchedule.get(today)!!
                            for(data in tmpList) {
                                if(data.id==curId) {
                                    tmpList.remove(data)
                                    break
                                }
                            }
                            val bundle = Bundle()
                            bundle.putString("today",today)
                            findNavController().navigate(preFragment,bundle)
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
                if (receivedData != null) {
                    for (data in receivedData) {
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
                if(binding.edtHomeCategoryName.text.toString()=="") {
                    viewModelTime.setPopupOne(requireContext(),"스케줄명을 입력하시오", view)
                } else if(viewModelTime.compareTimes(binding.tvHomeTimeStart.text.toString(),binding.tvHomeTimeEnd.text.toString())) {
                    viewModelTime.setPopupOne(requireContext(),"올바른 시간을 입력하시오", view)
                } else if(!check){            //이미 해당 시간에 일정이 있을 때
                    viewModelTime.setPopupOne(requireContext(),"해당 시간에 이미 일정이 존재합니다", view)
                } else {
                    val tmp = ScheduleAdd(today,binding.edtHomeCategoryName.text.toString(),curColor,viewModelTime.timeChange(binding.tvHomeTimeStart.text.toString()),
                        viewModelTime.timeChange(binding.tvHomeTimeEnd.text.toString()),binding.edtHomeScheduleMemo.text.toString(),"d",0,"DAILY")
                    if(btnSubmit.text.toString()=="등록") {
                        viewModelTime.addTimeDatas(tmp) { result ->
                            when (result) {
                                1 -> {
                                    val tmpId = viewModelTime.addId
                                    if(viewModelTime.hashMapArraySchedule.get(today)==null) {
                                        viewModelTime.hashMapArraySchedule.put(today,ArrayList<Schedule>())
                                    }
                                    viewModelTime.hashMapArraySchedule.get(today)!!.add(
                                        Schedule(tmpId,today,binding.edtHomeCategoryName.text.toString(),curColor,viewModelTime.timeChange(binding.tvHomeTimeStart.text.toString()),
                                            viewModelTime.timeChange(binding.tvHomeTimeEnd.text.toString()),binding.edtHomeScheduleMemo.text.toString(),"",0,"DAILY")
                                    )

                                    val bundle = Bundle()
                                    bundle.putString("today",today)
                                    findNavController().navigate(preFragment,bundle)
                                }
                                2 -> {
                                    Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else if(btnSubmit.text.toString()=="수정") {
                        viewModelTime.editTimeData(curId,tmp) { result ->
                            when (result) {
                                1 -> {
                                    val tmpList = viewModelTime.hashMapArraySchedule.get(today)!!
                                    for(data in tmpList) {
                                        if(data.id==curId) {
                                            tmpList.remove(data)
                                            break
                                        }
                                    }
                                    viewModelTime.hashMapArraySchedule.get(today)!!.add(
                                        Schedule(curId,today,binding.edtHomeCategoryName.text.toString(),curColor,viewModelTime.timeChange(binding.tvHomeTimeStart.text.toString()),
                                            viewModelTime.timeChange(binding.tvHomeTimeEnd.text.toString()),binding.edtHomeScheduleMemo.text.toString(),"",0,"DAILY")
                                    )

                                    val bundle = Bundle()
                                    bundle.putString("today",today)
                                    findNavController().navigate(preFragment,bundle)
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
            findNavController().navigate(preFragment,bundle)
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
                    Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
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
    }
    private fun initColorArray(){
        with(timeColorArray){
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub1))
            timeColorArray.add(android.graphics.Color.parseColor("#F68F30"))
            timeColorArray.add(android.graphics.Color.parseColor("#ED6C64"))
            timeColorArray.add(android.graphics.Color.parseColor("#FDA4B4"))
            timeColorArray.add(android.graphics.Color.parseColor("#F076A2"))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub2))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub2))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub2))

            timeColorArray.add(android.graphics.Color.parseColor("#7FC7D4"))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.point_main))
            timeColorArray.add(android.graphics.Color.parseColor("#21C362"))
            timeColorArray.add(android.graphics.Color.parseColor("#0E9746"))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.main))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub4))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub2))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub2))
        }
    }
    private fun customBackDialog() {
        backDialog = HomeBackCustomDialog(requireActivity(), this)
        backDialog.show()
    }
    // 커스텀 다이얼로그에서 버튼 클릭 시
    override fun onYesButtonClicked(dialog: Dialog, flag: String) {
            Navigation.findNavController(requireView()).navigate(R.id.action_fragTimeAdd_to_fragTime)
        dialog.dismiss()
    }

    override fun onNoButtonClicked(dialog: Dialog) {
        dialog.dismiss()
    }

}