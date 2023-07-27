package com.example.myapplication.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarAdapter
import com.example.myapplication.CalenderFuntion.CalendarAdd
import com.example.myapplication.CalenderFuntion.CalendarAddDday
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.CalenderFuntion.OnItemListener
import com.example.myapplication.HomeFunction.view.viewpager2.HomeViewpagerTimetableFragment
import com.example.myapplication.databinding.FragCalendarBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import com.example.myapplication.R
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
        val endDate: String,
        val color: String,
        //val repeat: Char,
        val dDay: Char,
        val memo: String,
       //val createAt: String,
        //val updateAt: String
        var floor : Int,
        val duration : Boolean
    )

    val calendarDayArray = Array(42) {""}
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragCalendarBinding.inflate(inflater, container, false)
        CalendarUtil.selectedDate = LocalDate.now()
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
        binding.dday1.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_blank, null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
                .create()
            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            mBuilder.show()
            mDialogView.findViewById<AppCompatImageButton>(R.id.blank).setOnClickListener( {
                val intent = Intent(requireContext(), CalendarAddDday::class.java)
                requireContext().startActivity(intent)
            })

        }
        binding.dday2.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_blank, null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
                .create()
            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            mBuilder.show()
            mDialogView.findViewById<AppCompatImageButton>(R.id.blank).setOnClickListener( {
                val intent = Intent(requireContext(), CalendarAddDday::class.java)
                requireContext().startActivity(intent)
            })
        }
        binding.dday3.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_blank, null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
                .create()
            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            mBuilder.show()
            mDialogView.findViewById<AppCompatImageButton>(R.id.blank).setOnClickListener( {
                val intent = Intent(requireContext(), CalendarAddDday::class.java)
                requireContext().startActivity(intent)
            })
        }


        return binding.root
    }
    private fun setMonthView() {
        val dataArray = Array<ArrayList<CalendarDATA?>>(42) { ArrayList() }

        var formatter = DateTimeFormatter.ofPattern("M월")
        binding.textMonth.text = CalendarUtil.selectedDate.format(formatter)
        formatter = DateTimeFormatter.ofPattern("yyyy년")
        binding.textYear.text = CalendarUtil.selectedDate.format(formatter)

        val dayList = dayInMonthArray()

        //임시 데이터 정보 받아오기
        val datas = arrayOf(        //임시 데이터
            CalendarDATA("2023-7-2","2023-7-6","#2AA1B7",'N',"데이터분석기초 기말고사",-1,true),
            CalendarDATA("2023-7-6","2023-7-6","#F8D141",'N',"기말 강의평가 기간",-1,false),
            CalendarDATA("2023-7-6","2023-7-30","#89A9D9",'N',"방학",-1,true)

        )
        for(data in datas) {
            var start = calendarDayArray.indexOf(data.startDate)
            var end = calendarDayArray.indexOf(data.endDate)

            if(dataArray[start].size<2) {
                data.floor = dataArray[start].size
            } else {
                data.floor=-1
            }
            Log.d("size","==="+data.memo)
            for(i in start.. end) {
                if(dataArray[i].size<2&&data.floor==-1) {
                    data.floor = dataArray[i].size
                }
                dataArray[i].add(data.copy())
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