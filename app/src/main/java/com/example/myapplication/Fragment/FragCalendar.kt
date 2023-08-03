package com.example.myapplication.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarAdapter
import com.example.myapplication.CalenderFuntion.CalendarAddDdayFragment
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.CalenderFuntion.OnItemListener
import com.example.myapplication.databinding.FragCalendarBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import com.example.myapplication.R
import java.lang.Integer.min
import java.text.SimpleDateFormat
import java.util.Locale


class FragCalendar : Fragment(), OnItemListener {

    lateinit var binding: FragCalendarBinding
    private lateinit var calendar: Calendar
    data class sche(var startMonth: Int, var endMonth: Int, var startDay: Int, var endDay: Int)
    var preStartToEnd : sche = sche(0, 0, 0, 0)
    var nextStartToEnd : sche = sche(0, 0, 0, 0)

    data class CalendarDATA(
        val startDate: String,
        var startDate2: String,
        val endDate: String,
        val startTime : String,
        val endTime : String,
        val color: String,
        val repeat: String,
        val dDay: Char,
        val title: String,
       //val createAt: String,
        //val updateAt: String
        var floor : Int,
        val duration : Boolean,
        val memo : String

    )

    val calendarDayArray = Array(42) {""}
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

        val datasDday = arrayOf(        //임시 데이터, 끝나는 날짜 순서대로 정렬해야함
            CalendarDATA("2023-7-21","2023-7-21","2023-8-31","","",
                "#FFE7EB","",'Y',"방학",-1,true,"방학이 끝나간다..."),
            CalendarDATA("2023-7-2","2023-7-2","2023-9-2","","",
                "#E1E9F5","",'Y',"UMC 데모데이",-1,true,"메모는 여기에 뜨게 하면 될것 같습니다!")

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
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
            } else {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_blank, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<AppCompatImageButton>(R.id.blank).setOnClickListener( {
                    val bundle = Bundle()
                    bundle.putBoolean("dday", true)
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                    mBuilder.dismiss()
                })
            }
        }
        binding.dday2.setOnClickListener {

            if(datasDday.size>=2) {

                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
            } else {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_blank, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<AppCompatImageButton>(R.id.blank).setOnClickListener( {
                    val bundle = Bundle()
                    bundle.putBoolean("dday", true)
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                    mBuilder.dismiss()
                })
            }

        }
        binding.dday3.setOnClickListener {
            if(datasDday.size==3) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
            } else {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_blank, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                mDialogView.findViewById<AppCompatImageButton>(R.id.blank).setOnClickListener( {
                    val bundle = Bundle()
                    bundle.putBoolean("dday", true)
                    Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
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
                "#2AA1B7","반복 안함",'N',"데이터분석기초 기말고사",-1,true,""),
            CalendarDATA("2023-7-21","2023-7-21","2023-8-5","","",
                "#89A9D9","매일",'N',"방학",-1,true,""),
            CalendarDATA("2023-7-13","2023-7-13","2023-7-15","","",
                "#2AA1B7","매주",'N',"이건 무슨 일정일까",-1,true,""),
            CalendarDATA("2023-7-6","2023-7-6","2023-7-6","12:00","13:30",
                "#F8D141","매월",'N',"기말 강의평가 기간",-1,false,"메모 ")
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
        while(i<42) {
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val date = dateFormat.parse(monthCalendar.time.toString())
            calendarDayArray[i++] = SimpleDateFormat("yyyy-M-d", Locale.ENGLISH).format(date)
            dayList.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)
        }
        return dayList
    }
    private fun yearMonthFromDate(date : LocalDate) :String{
        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        return date.format(formatter)
    }
    override fun onItemClick(dayText: String) {
        var yearMonthDay = yearMonthFromDate(CalendarUtil.selectedDate) + " " + dayText + "일"
        Toast.makeText(context, yearMonthDay, Toast.LENGTH_SHORT).show()
    }

}