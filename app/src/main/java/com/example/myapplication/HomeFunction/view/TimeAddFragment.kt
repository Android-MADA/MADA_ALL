package com.example.myapplication.HomeFunction.view

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.CalendarDATA
import com.example.myapplication.HomeFunction.Model.Schedule
import com.example.myapplication.HomeFunction.Model.ScheduleAdd
import com.example.myapplication.HomeFunction.Model.ScheduleResponse
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.time.HomeScheduleAndTodoAdapter
import com.example.myapplication.HomeFunction.time.HomeTimeColorAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentTimeAddBinding
import com.example.myapplication.hideBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate

class TimeAddFragment : Fragment() {

    private lateinit var binding : HomeFragmentTimeAddBinding
    private var bottomFlag = true
    var timeColorArray = ArrayList<Int>()
    var colorAdapter = HomeTimeColorAdapter(timeColorArray)

    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(HomeApi::class.java)
    var token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDVWJlYWF6cDhBem9mWDJQQUlxVHN0NmVxUTN4T1JfeXBWR1VuQUlqZU40IiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjE1OTAzNiwiZXhwIjoxNjkyMTk1MDM2fQ.ymm4YwjLcsFOxtvKbz_ygKc3tNREpBFtsdFxcaOSB2WNzbxMx281BXQQomvn3kAU8tPJ34RcqzSeoA54vd57ug"

    var today ="2023-08-16"
    var curColor = "#89A9D9"
    val curA = arrayOf<Boolean>(
        false, false
    )
    val curH = arrayOf<String>(
        "10","11"
    )
    val curM = arrayOf<String>(
        "00","00"
    )

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
        val receivedData = arguments?.getSerializable("pieChartDataArray") as?  ArrayList<HomeViewpagerTimetableFragment.PieChartData>?: null
        Log.d("reciedvd",receivedData.toString())
        val recievedPieData =  arguments?.getSerializable("pieChartData") as  HomeViewpagerTimetableFragment.PieChartData?
        today = arguments?.getString("today")?: "2023-06-01"
        token= arguments?.getString("Token")?: ""
        var id = 0

        if(recievedPieData != null) {
            btnSubmit.text = "수정"
            btnDelete.isVisible = true

            textTitle.setText(recievedPieData.title)
            ivColor.imageTintList =  ColorStateList.valueOf(Color.parseColor(recievedPieData.colorCode))
            //ivColor.setColorFilter(Color.parseColor(recievedPieData.colorCode), PorterDuff.Mode.SRC_IN)
            colorSelector.isGone = true

            times[0].text = convertTo12HourFormat(recievedPieData.startHour,recievedPieData.startMin)
            times[1].text = convertTo12HourFormat(recievedPieData.endHour,recievedPieData.endMin)
            id = recievedPieData.id
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
                curColor = String.format("#%06X", 0xFFFFFF and timeColorArray[position])
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
                curH[0] = hour.toString().padStart(2, '0')
                curM[0] = minute.toString().padStart(2, '0')
                if (ampm == "오전") {
                    curA[0] = false
                    ticker1.value = 0
                } else {
                    curA[0] = true
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
                curH[1] = hour.toString().padStart(2, '0')
                curM[1] = minute.toString().padStart(2, '0')
                if (ampm == "오전") {
                    curA[1] = false
                    ticker1.value = 0
                }
                else {
                    curA[1] = true
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
            if (newVal == 0) {
                curA[scheduleSelect] = false
                times[scheduleSelect].text =
                    times[scheduleSelect].text.toString().replace("오후", "오전")
            } else {
                curA[scheduleSelect] = true
                times[scheduleSelect].text =
                    times[scheduleSelect].text.toString().replace("오전", "오후")
            }

        }
        ticker2.setOnValueChangedListener { picker, oldVal, newVal ->
            curH[scheduleSelect] = newVal.toString().padStart(2, '0')
            val matchResult = regex.find(times[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                times[scheduleSelect].text = "  " + ampm + " " + newVal + ":" + minute + "  "
            } else {
            }
        }
        ticker3.setOnValueChangedListener { picker, oldVal, newVal ->
            curM[scheduleSelect] = newVal.toString().padStart(2, '0')
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
                Log.d("receivedData", "null!")
            }
            if (check) {         //이상 x
                if(curA[0]&&curH[0].toInt()==12)
                    curH[0] = "12"
                else if(!curA[0]&&curH[0].toInt()==12)
                    curH[0] = "00"
                else if(curA[0])
                    curH[0] = (curH[0].toInt() + 12).toString()

                if(curA[1]&&curH[1].toInt()==12)
                    curH[1] = "12"
                else if(!curA[1]&&curH[1].toInt()==12)
                    curH[1] = "24"
                else if(curA[1])
                    curH[1] = (curH[1].toInt() + 12).toString()
                val tmp = ScheduleAdd(today,binding.edtHomeCategoryName.text.toString(),curColor,"${curH[0]}:${curM[0]}:00",
                    "${curH[1]}:${curM[1]}:00",binding.edtHomeScheduleMemo.text.toString())
                Log.d("time","${curH[0]}:${curM[0]}:00 ${curH[1]}:${curM[1]}:00")
                if(binding.btnHomeTimeAddSubmit.text.toString()=="등록") {
                    addTimeDatas(tmp)
                } else {
                    editTimeData(id,tmp)
                }


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
            delTimeDatas(id)

        }


        val datas = arrayOf(        //임시 데이터, 수정 날짜 순서대로 정렬해야하며 점 일정은 나중으로 넣어야함
            CalendarDATA(
                "2023-7-2", "2023-7-2", "2023-7-6", "00:00", "24:00",
                "#2AA1B7", "반복 안함", "N", "데이터분석기초 기말고사", -1, true, "","CAL",7
            ),
            CalendarDATA(
                "2023-7-6", "2023-7-6", "2023-7-6", "12:00", "13:30",
                "#F8D141", "매월", "N", "친구랑 약속", 0, false, "메모 ","TODO",7
            )
        )

        if(datas.size>0) {
            val adapter = HomeScheduleAndTodoAdapter(datas,LocalDate.parse(today).dayOfMonth,binding.edtHomeCategoryName,binding.tvHomeTimeStart,binding.tvHomeTimeEnd, binding.homeTimeTodoListView)
            var manager: RecyclerView.LayoutManager = GridLayoutManager(view.context,1)
            binding.homeTimeTodoList.layoutManager = manager
            binding.homeTimeTodoList.adapter = adapter
        }

        binding.homeFragmentTimeAddLayout.setFocusableInTouchMode(true);
        binding.homeFragmentTimeAddLayout.setOnClickListener {
            binding.homeFragmentTimeAddLayout.requestFocus()
        }
        binding.edtHomeCategoryName.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.homeTimeTodoListView.visibility = View.VISIBLE
            } else
                binding.homeTimeTodoListView.visibility = View.GONE
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
    private fun addTimeDatas(data : ScheduleAdd) {
        val call = service.addTime(token,data)
        Log.d("data",data.toJson())
        call.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call2: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse
                        Log.d("success","${datas.status} ${datas.message} ${datas.succcess} ${datas.data.date} ${datas.data.scheduleName} ${datas.data.color}")
                    } else {
                        Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                    Log.d("333213","itemType: ${response.code()}")
                }
                findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
            }
            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }
    private fun delTimeDatas(id : Int) {
        val call = service.deleteTime(token, id)

        call.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call2: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse
                        Log.d("success","${datas.status} ${datas.message} ${datas.succcess} ")
                    } else {
                        Log.d("222","Request was not successful. Message: hi")
                    }
                    findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
                } else {
                    Log.d("333213","itemType: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }
    private fun editTimeData(id : Int,data : ScheduleAdd) {
        val call = service.editTime(token, id,data)
        call.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call2: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse
                        Log.d("success","${datas.status} ${datas.message} ${datas.succcess} ")
                    } else {
                        Log.d("222","Request was not successful. Message: hi")
                    }
                    findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
                } else {
                    Log.d("333213","itemType: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }
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

}