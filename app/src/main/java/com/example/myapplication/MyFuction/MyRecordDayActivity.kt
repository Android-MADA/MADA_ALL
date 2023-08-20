package com.example.myapplication.MyFuction

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarAdapter
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.CalenderFuntion.Model.CalendarDATA
import com.example.myapplication.Fragment.FragHome
import com.example.myapplication.HomeFunction.Model.ScheduleListData
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.time.SampleTimeData
import com.example.myapplication.HomeFunction.view.HomeViewpagerTimetableFragment
import com.example.myapplication.R
import com.example.myapplication.YourMarkerView
import com.example.myapplication.databinding.HomeFragmentBinding
import com.example.myapplication.databinding.MyRecordDayBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MyRecordDayActivity : AppCompatActivity() {
    private lateinit var binding: MyRecordDayBinding
    lateinit var homeFragmentBinding: HomeFragmentBinding
    private lateinit var calendar: Calendar

    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(HomeApi::class.java)
    var token = ""
    var today = "2023-08-18"        //default값

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyRecordDayBinding.inflate(layoutInflater)
        homeFragmentBinding = HomeFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        token = MyWebviewActivity.prefs.getString("token","")

        binding.backBtn.setOnClickListener {
            finish()
        }

        //달력 부분
        CalendarUtil.selectedDate = LocalDate.now()

        binding.todayInfo.text = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        calendar = Calendar.getInstance()
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

        // 일 주 월 버튼 클릭 이동
        binding.dayWeekMonthBtn.setOnClickListener {
            finish()
            val intent = Intent(this, MyRecordWeekActivity::class.java)
            startActivity(intent)
        }
        binding.todayInfo.setOnClickListener {
            getTimeDatas(binding.todayInfo.text.toString())
        }

        // 홈 투두 받아오기
        val homeViewPager = homeFragmentBinding.homeViewpager2

    }
    private fun setMonthView() {
        var formatter = DateTimeFormatter.ofPattern("yyyy년 M월")
        binding.textCalendar.text = CalendarUtil.selectedDate.format(formatter)

        val dayList = dayInMonthArray()

        val formatter2 = DateTimeFormatter.ofPattern("M")
        val adapter = MyCalendarAdapter(dayList,CalendarUtil.selectedDate.format(formatter2),binding.record,binding.todayInfo)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(this,7)
        binding.calendar2.layoutManager = manager
        binding.calendar2.adapter = adapter

    }
    private fun dayInMonthArray() : ArrayList<Date> {
        var dayList = ArrayList<Date>()
        var monthCalendar = calendar.clone() as Calendar

        monthCalendar[Calendar.DAY_OF_MONTH] = 1
        var firstDayofMonth = monthCalendar[Calendar.DAY_OF_WEEK]-1
        monthCalendar.add(Calendar.DAY_OF_MONTH,-firstDayofMonth)

        var i = 0
        var curMon = calendar.get(Calendar.MONTH)+1
        while(i<38) {
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val date = dateFormat.parse(monthCalendar.time.toString())
            if(curMon< SimpleDateFormat("M", Locale.ENGLISH).format(date).toInt()) {
                break
            }
            dayList.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)

        }

        return dayList


    }
    private fun extractTime(timeString: String,hourOrMin : Boolean): Int {
        val timeParts = timeString.split(":")
        if (timeParts.size == 3) {
            try {
                val hour = timeParts[0].toInt()
                val minute = timeParts[1].toInt()
                if(hourOrMin)
                    return hour
                else
                    return minute
            } catch (e: NumberFormatException) {
                // 숫자로 변환할 수 없는 경우 또는 잘못된 형식인 경우
                return 0
            }
        }
        return 0
    }
    private fun getTimeDatas(date : String) {
        val call = service.getTimetable(token,date)
        val arrays = ArrayList<HomeViewpagerTimetableFragment.PieChartData>()
        val sampleTimeArray = ArrayList<SampleTimeData>()
        call.enqueue(object : Callback<ScheduleListData> {
            override fun onResponse(call2: Call<ScheduleListData>, response: Response<ScheduleListData>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas2.datas
                        if(datas != null) {
                            var i = 0
                            for (data in datas) {
                                var end00 = 0
                                if(data.endTime=="00:00:00")
                                    end00 = 24
                                val tmp = HomeViewpagerTimetableFragment.PieChartData(data.scheduleName,data.memo,extractTime(data.startTime,true),extractTime(data.startTime,false),
                                    extractTime(data.endTime,true)+end00,extractTime(data.endTime,false),data.color,i++,data.id)
                                arrays.add(tmp)
                                sampleTimeArray.add(SampleTimeData(data.scheduleName,data.color))
                            }
                        }
                        pirChartOn(arrays)

                    }
                }
            }
            override fun onFailure(call: Call<ScheduleListData>, t: Throwable) {
            }
        })
    }
    private fun pirChartOn(arrays : ArrayList<HomeViewpagerTimetableFragment.PieChartData>) {
        val tmp2 = arrays.sortedWith(compareBy(
            { it.startHour },
            { it.startMin }
        ))


        var tmp = 0     //시작 시간

        var dataArray = tmp2.toMutableList() as ArrayList<HomeViewpagerTimetableFragment.PieChartData>
        val pieChartDataArray = dataArray
        //Pi Chart
        var chart = binding.chart


        val marker_ = YourMarkerView(this, R.layout.home_time_custom_label,pieChartDataArray)
        val entries = ArrayList<PieEntry>()
        val colorsItems = ArrayList<Int>()

        if(pieChartDataArray.size==0) {     //그날 정보가 없다면
            entries.add(PieEntry(10f, "999"))
            colorsItems.add(Color.parseColor("#F0F0F0"))
        }
        for(data in pieChartDataArray) {
            val start = data.startHour.toString().toInt() * 60 + data.startMin.toString().toInt()
            val end = data.endHour.toString().toInt() * 60 + data.endMin.toString().toInt()
            if(tmp==start) {      //이전 일정과 사이에 빈틈이 없을때
                entries.add(PieEntry((end-start).toFloat(), data.divisionNumber.toString()))
                colorsItems.add(Color.parseColor(data.colorCode.toString()))
                tmp = end
            } else {
                val noScheduleDuration = start - tmp
                entries.add(PieEntry(noScheduleDuration.toFloat(), "999"))      // 스케줄 없는 시간
                colorsItems.add(Color.parseColor("#FFFFFF"))
                entries.add(PieEntry((end-start).toFloat(), data.divisionNumber.toString()))
                colorsItems.add(Color.parseColor(data.colorCode.toString()))
                tmp = end
            }
        }
        if(pieChartDataArray.size>0&&pieChartDataArray[pieChartDataArray.size-1].endHour!=24) {
            val h = 23 - pieChartDataArray[pieChartDataArray.size-1].endHour
            val m = 60 - pieChartDataArray[pieChartDataArray.size-1].endMin
            entries.add(PieEntry((h*60+m).toFloat(), "999"))
            colorsItems.add(Color.parseColor("#FFFFFF"))
        }
        // 왼쪽 아래 설명 제거
        val legend = chart.legend
        legend.isEnabled = false
        chart.invalidate()

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.apply {
            colors = colorsItems
            setDrawValues(false) // 비율 숫자 없애기

        }

        val pieData = PieData(pieDataSet)
        val smallXY = if(chart.width > chart.height) chart.height else chart.width
        val range = smallXY/60f

        chart.apply {
            data = pieData
            isRotationEnabled = false                               //드래그로 회전 x
            isDrawHoleEnabled = false                               //중간 홀 그리기 x
            setExtraOffsets(range,range,range,range)    //크기 조절
            setUsePercentValues(false)
            setEntryLabelColor(Color.BLACK)
            marker = marker_
            setDrawEntryLabels(false) //라벨 끄기
            //rotationAngle = 30f // 회전 각도, 굳이 필요 없을듯
            description.isEnabled = false   //라벨 끄기 (오른쪽아래 간단한 설명)
        }

        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e is PieEntry) {
                    val pieEntry = e as PieEntry
                    val label = pieEntry.label
                    if (label == "999") {
                        pieDataSet.selectionShift = 0f //하이라이트 크기
                    } else {
                        pieDataSet.selectionShift = 30f// 다른 라벨의 경우 선택 시 하이라이트 크기 설정
                    }

                }
            }
            override fun onNothingSelected() {
            }
        })

    }



}