package com.example.myapplication.Fragment

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarAdapter
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.CalenderFuntion.Model.CalendarDATA
import com.example.myapplication.CalenderFuntion.Model.CalendarData2
import com.example.myapplication.CalenderFuntion.Model.CalendarDatas
import com.example.myapplication.CalenderFuntion.Model.CharacterResponse
import com.example.myapplication.CalenderFuntion.Model.ResponseSample
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.CustomFunction.CustomViewModel
import com.example.myapplication.databinding.FragCalendarBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import com.example.myapplication.R
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Integer.min
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale


class FragCalendar : Fragment(){

    lateinit var binding: FragCalendarBinding
    private lateinit var calendar: Calendar
    data class sche(var startMonth: Int, var endMonth: Int, var startDay: Int, var endDay: Int)
    val weekdays = arrayOf("일" ,"월", "화", "수", "목", "금", "토")
    var preStartToEnd : sche = sche(0, 0, 0, 0)
    var nextStartToEnd : sche = sche(0, 0, 0, 0)

    private val viewModel: CustomViewModel by viewModels()      //쥐새끼


    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCalendar::class.java)
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDVWJlYWF6cDhBem9mWDJQQUlxVHN0NmVxUTN4T1JfeXBWR1VuQUlqZU40IiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjE3MTQ1NywiZXhwIjoxNjkyMjA3NDU3fQ.qt1hfpsN8E1qoN71m7VobnmhkmLNv6y_i23ULkNbYDBDYYJTqsDpY3MLbhvBA6yzb1N5gJ_sjlDh7LpvS0DyTA"



    var calendarDayArray = Array(43) {""}
    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CalendarUtil.selectedDate = LocalDate.now()
        calendar = Calendar.getInstance()
        binding = FragCalendarBinding.inflate(inflater, container, false)
        val datas = ArrayList<CalendarDATA>()
        var formatterM = DateTimeFormatter.ofPattern("M")
        var formatterY = DateTimeFormatter.ofPattern("YYYY")
        getMonthDataArray(CalendarUtil.selectedDate.format(formatterM),CalendarUtil.selectedDate.format(formatterY),datas)

        binding.preBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1)
            calendar.add(Calendar.MONTH, -1)
            datas.clear()
            getMonthDataArray(CalendarUtil.selectedDate.format(formatterM),CalendarUtil.selectedDate.format(formatterY),datas)
            Log.d("pre","pre")
        }
        binding.nextBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1)
            calendar.add(Calendar.MONTH, 1)
            datas.clear()
            getMonthDataArray(CalendarUtil.selectedDate.format(formatterM),CalendarUtil.selectedDate.format(formatterY),datas)
            Log.d("next","next")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedData = viewModel.getSavedButtonInfo()

        getCustomChar()

        val ddayDatas = ArrayList<CalendarDATA>()
        getDdayDataArray(ddayDatas)

        for (i in 0 until min(ddayDatas.size, 3)) {
            val color = ddayDatas[i].color
            val imageResource = when (color) {
                "#E1E9F5" -> R.drawable.calendar_ddayblue_smallbackground
                "#FFE7EB" -> R.drawable.calendar_ddaypink_smallbackground
                "#F5EED1" -> R.drawable.calendar_ddayyellow_smallbackground
                else -> R.drawable.calendar_dday_plus
            }
            when (i) {
                0 -> {
                    binding.dday1.setImageResource(imageResource)
                    binding.dday1Text.text = "D-${daysRemainingToDate(ddayDatas[i].endDate)}"
                    binding.dday1TextInfo.text = ddayDatas[i].title
                }
                1 -> {
                    binding.dday2.setImageResource(imageResource)
                    binding.dday2Text.text = "D-${daysRemainingToDate(ddayDatas[i].endDate)}"
                    binding.dday2TextInfo.text = ddayDatas[i].title
                }
                2 -> {
                    binding.dday3.setImageResource(imageResource)
                    binding.dday3Text.text = "D-${daysRemainingToDate(ddayDatas[i].endDate)}"
                    binding.dday3TextInfo.text = ddayDatas[i].title
                }
            }
        }
        binding.dday1.setOnClickListener {
            if(ddayDatas.size>=1) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                if(ddayDatas[0].color == "#E1E9F5") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_blue_popup)
                } else if(ddayDatas[0].color == "#FFE7EB") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_pink_popup)
                } else if(ddayDatas[0].color == "#F5EED1"){
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_yellow_popup)
                }
                mDialogView.findViewById<TextView>(R.id.textTitle).text = ddayDatas[0].title
                mDialogView.findViewById<TextView>(R.id.textDay).text =convertToDateKoreanFormat(ddayDatas[0].endDate)
                mDialogView.findViewById<TextView>(R.id.textDday).text =binding.dday1Text.text.toString()
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("title",ddayDatas[0].title)
                    bundle.putString("startDate",ddayDatas[0].startDate)
                    bundle.putString("endDate",ddayDatas[0].endDate)
                    bundle.putString("memo",ddayDatas[0].memo)
                    bundle.putString("color",ddayDatas[0].color)
                    bundle.putInt("dday",daysRemainingToDate(ddayDatas[0].endDate))
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                    mBuilder.dismiss()
                }
                mDialogView.findViewById<ImageButton>(R.id.delbutton).setOnClickListener {
                    //일정 삭제 코드 추가
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_fragCalendar)
                    mBuilder.dismiss()
                }
                //mDialogView.findViewById<ImageButton>(R.id.plus).setImageResource(R.drawable.)

            } else {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_blank, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<AppCompatImageButton>(R.id.blank).setOnClickListener( {
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAddDday)
                    mBuilder.dismiss()
                })
            }
        }
        binding.dday2.setOnClickListener {

            if(ddayDatas.size>=2) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                if(ddayDatas[1].color == "#E1E9F5") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_blue_popup)
                } else if(ddayDatas[1].color == "#F0768C") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_pink_popup)
                } else if(ddayDatas[1].color == "#F5EED1"){
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_yellow_popup)
                }
                mDialogView.findViewById<TextView>(R.id.textTitle).text = ddayDatas[1].title
                mDialogView.findViewById<TextView>(R.id.textDay).text =convertToDateKoreanFormat(ddayDatas[1].endDate)
                mDialogView.findViewById<TextView>(R.id.textDday).text =binding.dday2Text.text.toString()
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("title",ddayDatas[1].title)
                    bundle.putString("startDate",ddayDatas[1].startDate)
                    bundle.putString("endDate",ddayDatas[1].endDate)
                    bundle.putString("memo",ddayDatas[1].memo)
                    bundle.putString("color",ddayDatas[1].color)
                    bundle.putInt("dday",daysRemainingToDate(ddayDatas[1].endDate))
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                    mBuilder.dismiss()
                }
                mDialogView.findViewById<ImageButton>(R.id.delbutton).setOnClickListener {
                    //일정 삭제 코드 추가
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_fragCalendar)
                    mBuilder.dismiss()
                }
            } else {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_blank, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<AppCompatImageButton>(R.id.blank).setOnClickListener( {
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAddDday)
                    mBuilder.dismiss()
                })
            }

        }
        binding.dday3.setOnClickListener {
            if(ddayDatas.size==3) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                if(ddayDatas[2].color == "#E1E9F5") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_blue_popup)
                } else if(ddayDatas[2].color == "#F0768C") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_pink_popup)
                } else if(ddayDatas[2].color == "#F5EED1"){
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_yellow_popup)
                }
                mDialogView.findViewById<TextView>(R.id.textTitle).text = ddayDatas[2].title
                mDialogView.findViewById<TextView>(R.id.textDay).text =convertToDateKoreanFormat(ddayDatas[2].endDate)
                mDialogView.findViewById<TextView>(R.id.textDday).text =binding.dday3Text.text.toString()
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("title",ddayDatas[2].title)
                    bundle.putString("startDate",ddayDatas[2].startDate)
                    bundle.putString("endDate",ddayDatas[2].endDate)
                    bundle.putString("memo",ddayDatas[2].memo)
                    bundle.putString("color",ddayDatas[2].color)
                    bundle.putInt("dday",daysRemainingToDate(ddayDatas[2].endDate))
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                    mBuilder.dismiss()
                }
                mDialogView.findViewById<ImageButton>(R.id.delbutton).setOnClickListener {
                    //일정 삭제 코드 추가
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_fragCalendar)
                    mBuilder.dismiss()
                }
            } else {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_blank, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<AppCompatImageButton>(R.id.blank).setOnClickListener( {
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAddDday)
                    mBuilder.dismiss()
                })
            }

        }
        binding.calendarDdayPlusBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("Today","  ${calendar.get(Calendar.MONTH) + 1}월 ${calendar.get(Calendar.DAY_OF_MONTH)}일 (${weekdays[calendar.get(Calendar.DAY_OF_WEEK) - 1]})  ")
            bundle.putString("Token",token)
            //Log.d("aaa","  ${calendar.get(Calendar.MONTH) + 1}월 ${calendar.get(Calendar.DAY_OF_MONTH)}일 (${weekdays[calendar.get(Calendar.DAY_OF_WEEK) - 1]})  ")
            Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAdd,bundle)
        }


    }


    fun daysRemainingToDate(targetDate: String): Int {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        val today = LocalDate.now()
        val target = LocalDate.parse(targetDate, dateFormatter)
        val daysRemaining = target.toEpochDay() - today.toEpochDay()
        return daysRemaining.toInt()
    }
    private fun setMonthView(datas : ArrayList<CalendarDATA>, startMon : Boolean) {
        val dataArray = Array<ArrayList<CalendarDATA?>>(42) { ArrayList() }
        var formatter = DateTimeFormatter.ofPattern("M")
        binding.textMonth.text = CalendarUtil.selectedDate.format(formatter)+"월"
        formatter = DateTimeFormatter.ofPattern("yyyy년")
        binding.textYear.text = CalendarUtil.selectedDate.format(formatter)

        val dayList = dayInMonthArray(startMon)

        //임시 데이터 정보 받아오기
                                                                       //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 년도랑 월 받아와야함
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-M-d")
        for(data in datas) {
            if(data!=null) {
                var start = calendarDayArray.indexOf(data.startDate)
                var end = calendarDayArray.indexOf(data.endDate)
                if(end>=0 && start < 0)
                    start = 0
                else if(end<0 && start >=0)
                    end = 41

                if (start >= 0 && end >= 0) {
                    // start와 end가 유효한 경우에만 dataArray 배열에 데이터 추가
                    if(dataArray[start].size<2) {
                        data.floor = dataArray[start].size
                    } else {
                        data.floor=-1
                    }
                    for(i in start.. end) {
                        if(dataArray[i].size<2&&data.floor==-1) {
                            data.floor = dataArray[i].size
                            data.startDate2 = LocalDate.parse(data.startDate, formatter2).plusDays((i-start).toLong()).format(formatter2)
                        }

                        dataArray[i].add(data.copy())
                    }
                } else {
                    // start와 end가 유효하지 않은 경우, 오류 처리 또는 다른 로직 수행
                }


            } else {

            }
        }
        val adapter = CalendarAdapter(dayList,dataArray,token)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(context,7)
        binding.calendar.layoutManager = manager
        binding.calendar.adapter = adapter
    }

    private fun dayInMonthArray(startMon : Boolean) : ArrayList<Date> {
        var dayList = ArrayList<Date>()
        var monthCalendar = calendar.clone() as Calendar

        monthCalendar[Calendar.DAY_OF_MONTH] = 1        //달의 첫 번째 날짜
        var firstDayofMonth = monthCalendar[Calendar.DAY_OF_WEEK]-1
        if(startMon&&firstDayofMonth==0) {
            firstDayofMonth = 7
        }
        monthCalendar.add(Calendar.DAY_OF_MONTH,-firstDayofMonth)
        var i = 0
        while(i<43) {
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val date = dateFormat.parse(monthCalendar.time.toString())
            calendarDayArray[i++] = SimpleDateFormat("yyyy-M-d", Locale.ENGLISH).format(date)
            dayList.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)
        }
        if(startMon) {      //월요일 부터 시작이라면
            dayList.removeAt(0)
            for (i in 0 until calendarDayArray.size - 1) {
                calendarDayArray[i] = calendarDayArray[i + 1]

            }
            val textView = binding.textSun
            binding.textYoil.removeView(binding.textSun)
            binding.textYoil.addView(textView)
        }
        return dayList
    }
    private fun yearMonthFromDate(date : LocalDate) :String{
        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        return date.format(formatter)
    }
    fun convertToDateKoreanFormat(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val outputFormat = SimpleDateFormat("M월 d일", Locale("ko", "KR"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    fun convertToDate2(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    private fun getMonthDataArray(month : String,year : String, arrays : ArrayList<CalendarDATA>) {
        //임시 데이터, 수정 날짜 순서대로 정렬해야하며 점 일정은 나중으로 넣어야함
        val call2 = service.monthCalRequest(token,year,month)
        var startMon = true
        call2.enqueue(object : Callback<CalendarDatas> {
            override fun onResponse(call2: Call<CalendarDatas>, response: Response<CalendarDatas>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas
                        startMon = apiResponse.startMon
                        if(datas != null) {
                            for (data in datas) {
                                val dura : Boolean
                                if(data.start_date==data.end_date) dura = false
                                else dura = true
                                val tmp = CalendarDATA("${convertToDate2(data.start_date)}","${convertToDate2(data.start_date)}","${convertToDate2(data.end_date)}",
                                    "10:00","11:00","${data.color}","${data.repeat}","${data.d_day}","${data.name}",
                                    -1,dura,"${data.memo}","CAL")
                                if(dura) {
                                    arrays.add(0,tmp)
                                } else {
                                    arrays.add(tmp)
                                }


                                Log.d("111","datas: ${tmp.startDate} ${tmp.endDate} ${tmp.title} ${tmp.color} ${tmp.repeat} ${tmp.dDay} ${tmp.memo}")

                                // ...
                            }
                        } else {
                           Log.d("2222","Request was not successful. Message: hi")
                        }
                        setMonthView(arrays,startMon)
                    } else {
                        //Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                   // Log.d("333","itemType: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<CalendarDatas>, t: Throwable) {
                //Log.d("444","itemType: ${t.message}")
            }
        })
    }
    private fun getDdayDataArray(arrays : ArrayList<CalendarDATA>) {
        val call2 = service.getAllDday(token)
        call2.enqueue(object : Callback<CalendarDatas> {
            override fun onResponse(call2: Call<CalendarDatas>, response: Response<CalendarDatas>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas
                        if(datas != null) {
                            for (data in datas) {
                                //arrays.add(data)                                                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@이거 수정해야함
                                //Log.d("111","datas: ${data.calendarName}")
                                // ...
                            }
                        } else {
                            //Log.d("2222","Request was not successful. Message: hi")
                        }
                    } else {
                        //Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                    //Log.d("333","itemType: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<CalendarDatas>, t: Throwable) {
                //Log.d("444","itemType: ${t.message}")
            }
        })
    }
    private fun getCustomChar() {
        val call2 = service.characterRequest(token)
        call2.enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(call2: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas
                        if(datas != null) {
                            for (data in datas) {
                                //arrays.add(data)                                                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@이거 수정해야함
                                Log.d("111","datas: ${data.id} ${data.itemType} ${data.filePath}")
                                // ...
                            }
                        } else {
                            Log.d("2221","${response.code()}")
                        }
                    } else {
                        Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                    Log.d("3331","itemType: ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }
}