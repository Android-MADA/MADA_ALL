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
    var preStartToEnd : sche = sche(0, 0, 0, 0)
    var nextStartToEnd : sche = sche(0, 0, 0, 0)

    private val viewModel: CustomViewModel by viewModels()      //쥐새끼

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
        setMonthView()
        binding.preBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1)
            calendar.add(Calendar.MONTH, -1)
            setMonthView()
        }
        binding.nextBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1)
            calendar.add(Calendar.MONTH, 1)
            setMonthView()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedData = viewModel.getSavedButtonInfo()

        val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(RetrofitServiceCalendar::class.java)
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDVWJlYWF6cDhBem9mWDJQQUlxVHN0NmVxUTN4T1JfeXBWR1VuQUlqZU40IiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjA5MjQ0OCwiZXhwIjoxNjkyMTI4NDQ4fQ.H9X0jEZVqG9FMzwhDh8I05ov6KRVlGfI8C5bXUwoEWB1lrcQQZzVC9shykYX2_4r-IL51KBhA45Qru0zLf5YhA"
        val month = "8"
        val tmp = CalendarData2("이건 되었나???","2023-08-09","2023-08-23","red","N","N","제발되라!!!!!!!!")
        val call1 = service.addCal(token,tmp.toJson())
        Log.d("000",tmp.toJson())
        call1.enqueue(object : Callback<ResponseSample> {
            override fun onResponse(call: Call<ResponseSample>, response: Response<ResponseSample>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) {
                        Log.d("status",responseBody.status.toString())
                        Log.d("success",responseBody.success.toString())
                        Log.d("message",responseBody.message.toString())
                    }else
                        Log.d("777","777")

                } else {
                    Log.d("666","itemType: ${response.code()} ")
                    Log.d("666","itemType: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<ResponseSample>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })

        Log.d("555","555")
        val call2 = service.monthCalRequest(token,month)
        Log.d("000","000")

        call2.enqueue(object : Callback<CalendarDatas> {
            override fun onResponse(call2: Call<CalendarDatas>, response: Response<CalendarDatas>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas
                        if(datas != null) {
                            for (data in datas) {
                                // 각각의 작업을 처리하는 로직을 여기에 작성하세요.
                                Log.d("111","datas: ${data.calendarName}")
                                // ...
                            }
                        } else {
                            Log.d("2222","Request was not successful. Message: hi")
                        }
                    } else {
                        Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                    Log.d("333","itemType: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CalendarDatas>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })

        Log.d("555","555")


        val datasDday = arrayOf(        //임시 데이터, 끝나는 날짜 순서대로 정렬해야함
            CalendarDATA("2023-7-21","2023-7-21","2023-8-31","","",
                "#FFE7EB","",'Y',"방학",-1,true,"방학이 끝나간다...","CAL"),
            CalendarDATA("2023-7-2","2023-7-2","2023-9-2","","",
                "#E1E9F5","",'Y',"UMC 데모데이",-1,true,"메모는 여기에 뜨게 하면 될것 같습니다!","CAL")

        )

        for (i in 0 until min(datasDday.size, 3)) {
            val color = datasDday[i].color
            val imageResource = when (color) {
                "#E1E9F5" -> R.drawable.calendar_ddayblue_smallbackground
                "#FFE7EB" -> R.drawable.calendar_ddaypink_smallbackground
                "#F5EED1" -> R.drawable.calendar_ddayyellow_smallbackground
                else -> R.drawable.calendar_dday_plus
            }
            when (i) {
                0 -> {
                    binding.dday1.setImageResource(imageResource)
                    binding.dday1Text.text = "D-${daysRemainingToDate(datasDday[i].endDate)}"
                    binding.dday1TextInfo.text = datasDday[i].title
                }
                1 -> {
                    binding.dday2.setImageResource(imageResource)
                    binding.dday2Text.text = "D-${daysRemainingToDate(datasDday[i].endDate)}"
                    binding.dday2TextInfo.text = datasDday[i].title
                }
                2 -> {
                    binding.dday3.setImageResource(imageResource)
                    binding.dday3Text.text = "D-${daysRemainingToDate(datasDday[i].endDate)}"
                    binding.dday3TextInfo.text = datasDday[i].title
                }
            }
        }
        binding.dday1.setOnClickListener {
            if(datasDday.size>=1) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                if(datasDday[0].color == "#E1E9F5") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_blue_popup)
                } else if(datasDday[0].color == "#FFE7EB") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_pink_popup)
                } else if(datasDday[0].color == "#F5EED1"){
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_yellow_popup)
                }
                mDialogView.findViewById<TextView>(R.id.textTitle).text = datasDday[0].title
                mDialogView.findViewById<TextView>(R.id.textDay).text =convertToDateKoreanFormat(datasDday[0].endDate)
                mDialogView.findViewById<TextView>(R.id.textDday).text =binding.dday1Text.text.toString()
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("title",datasDday[0].title)
                    bundle.putString("startDate",datasDday[0].startDate)
                    bundle.putString("endDate",datasDday[0].endDate)
                    bundle.putString("memo",datasDday[0].memo)
                    bundle.putString("color",datasDday[0].color)
                    bundle.putInt("dday",daysRemainingToDate(datasDday[0].endDate))
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

            if(datasDday.size>=2) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                if(datasDday[1].color == "#E1E9F5") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_blue_popup)
                } else if(datasDday[1].color == "#F0768C") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_pink_popup)
                } else if(datasDday[1].color == "#F5EED1"){
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_yellow_popup)
                }
                mDialogView.findViewById<TextView>(R.id.textTitle).text = datasDday[1].title
                mDialogView.findViewById<TextView>(R.id.textDay).text =convertToDateKoreanFormat(datasDday[1].endDate)
                mDialogView.findViewById<TextView>(R.id.textDday).text =binding.dday2Text.text.toString()
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("title",datasDday[1].title)
                    bundle.putString("startDate",datasDday[1].startDate)
                    bundle.putString("endDate",datasDday[1].endDate)
                    bundle.putString("memo",datasDday[1].memo)
                    bundle.putString("color",datasDday[1].color)
                    bundle.putInt("dday",daysRemainingToDate(datasDday[1].endDate))
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
            if(datasDday.size==3) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                if(datasDday[2].color == "#E1E9F5") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_blue_popup)
                } else if(datasDday[2].color == "#F0768C") {
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_pink_popup)
                } else if(datasDday[2].color == "#F5EED1"){
                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_yellow_popup)
                }
                mDialogView.findViewById<TextView>(R.id.textTitle).text = datasDday[2].title
                mDialogView.findViewById<TextView>(R.id.textDay).text =convertToDateKoreanFormat(datasDday[2].endDate)
                mDialogView.findViewById<TextView>(R.id.textDday).text =binding.dday3Text.text.toString()
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("title",datasDday[2].title)
                    bundle.putString("startDate",datasDday[2].startDate)
                    bundle.putString("endDate",datasDday[2].endDate)
                    bundle.putString("memo",datasDday[2].memo)
                    bundle.putString("color",datasDday[2].color)
                    bundle.putInt("dday",daysRemainingToDate(datasDday[2].endDate))
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
            bundle.putInt("Month",calendar.get(Calendar.MONTH) + 1)
            bundle.putInt("Day",calendar.get(Calendar.DAY_OF_MONTH))
            bundle.putInt("Yoil",calendar.get(Calendar.DAY_OF_WEEK) - 1)
            Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAdd,bundle)
        }


    }

    private fun fetchDataFromUrl(urlString: String): JSONObject {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        try {
            val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var inputLine: String?
            while (bufferedReader.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            bufferedReader.close()
            return JSONObject(response.toString())
        } finally {
            connection.disconnect()
        }
    }

    fun daysRemainingToDate(targetDate: String): Int {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        val today = LocalDate.now()
        val target = LocalDate.parse(targetDate, dateFormatter)
        val daysRemaining = target.toEpochDay() - today.toEpochDay()
        return daysRemaining.toInt()
    }
    private fun setMonthView() {
        val dataArray = Array<ArrayList<CalendarDATA?>>(42) { ArrayList() }

        var formatter = DateTimeFormatter.ofPattern("M월")
        binding.textMonth.text = CalendarUtil.selectedDate.format(formatter)
        formatter = DateTimeFormatter.ofPattern("yyyy년")
        binding.textYear.text = CalendarUtil.selectedDate.format(formatter)

        val dayList = dayInMonthArray()

        //임시 데이터 정보 받아오기
        val datas = arrayOf(        //임시 데이터, 수정 날짜 순서대로 정렬해야하며 점 일정은 나중으로 넣어야함
            CalendarDATA("2023-7-2","2023-7-2","2023-7-6","","",
                "#2AA1B7","반복 안함",'N',"데이터분석기초 기말고사",-1,true,"","CAL"),
            CalendarDATA("2023-7-21","2023-7-21","2023-8-5","","",
                "#89A9D9","매일",'N',"방학",-1,true,"","CAL"),
            CalendarDATA("2023-7-13","2023-7-13","2023-7-15","","",
                "#2AA1B7","매주",'N',"이건 무슨 일정일까",-1,true,"","CAL"),
            CalendarDATA("2023-7-6","2023-7-6","2023-7-6","12:00","13:30",
                "#F8D141","매월",'N',"기말 강의평가 기간",-1,false,"메모 ","CAL")
        )
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


            }
        }
        val adapter = CalendarAdapter(dayList,dataArray)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(context,7)
        binding.calendar.layoutManager = manager
        binding.calendar.adapter = adapter
    }
    private fun dayInMonthArray() : ArrayList<Date> {
        var dayList = ArrayList<Date>()
        var monthCalendar = calendar.clone() as Calendar

        monthCalendar[Calendar.DAY_OF_MONTH] = 1        //달의 첫 번째 날짜
        var firstDayofMonth = monthCalendar[Calendar.DAY_OF_WEEK]-1

        monthCalendar.add(Calendar.DAY_OF_MONTH,-firstDayofMonth)
        var i = 0
        while(i<43) {
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val date = dateFormat.parse(monthCalendar.time.toString())
            calendarDayArray[i++] = SimpleDateFormat("yyyy-M-d", Locale.ENGLISH).format(date)
            dayList.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)
        }
        if(true) {      //월요일 부터 시작이라면
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

}