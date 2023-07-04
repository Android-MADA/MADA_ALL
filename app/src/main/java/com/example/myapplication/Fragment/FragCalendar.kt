package com.example.myapplication.Fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adpater.CalendarAdapter
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.CalenderFuntion.OnItemListener
import com.example.myapplication.databinding.FragCalendarBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class FragCalendar : Fragment(), OnItemListener {

    lateinit var binding: FragCalendarBinding
    private lateinit var calendar: Calendar

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
        return binding.root
    }
    private fun setMonthView() {
        var formatter = DateTimeFormatter.ofPattern("M월")
        binding.textMonth.text = CalendarUtil.selectedDate.format(formatter)
        formatter = DateTimeFormatter.ofPattern("yyyy년")
        binding.textYear.text = CalendarUtil.selectedDate.format(formatter)

        val dayList = dayInMonthArray()
        val adapter = CalendarAdapter(dayList)
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
        while(dayList.size<42) {
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