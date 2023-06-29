package com.example.myapplication.Fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adpater.CalendarAdapter
import com.example.myapplication.databinding.FragCalendarBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class FragCalendar : Fragment() {

    lateinit var binding: FragCalendarBinding
    lateinit var selectedDate: LocalDate
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        binding = FragCalendarBinding.inflate(inflater, container, false)

        selectedDate = LocalDate.now()
        setMonthView()
        binding.preBtn.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }
        binding.nextBtn.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }
        return binding.root
    }
    private fun setMonthView() {
        var formatter = DateTimeFormatter.ofPattern("M월")
        binding.textMonth.text = selectedDate.format(formatter)
        formatter = DateTimeFormatter.ofPattern("yyyy년")
        binding.textYear.text = selectedDate.format(formatter)

        val dayList = dayInMonthArray(selectedDate)
        val adapter = CalendarAdapter(dayList)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(context,7)
        binding.calendar.layoutManager = manager
        binding.calendar.adapter = adapter
    }
    private fun dayInMonthArray(date: LocalDate) : ArrayList<String> {
        var dayList = ArrayList<String>()
        var yearMonth = YearMonth.from(date)
        var lastDay = yearMonth.lengthOfMonth()
        var firstDay = selectedDate.withDayOfMonth(1)
        var dayOfWeek = firstDay.dayOfWeek.value
        for(i in 1..41) {
            if(i<=dayOfWeek || i> lastDay+dayOfWeek) {
                dayList.add("")
            } else {
                dayList.add((i-dayOfWeek).toString())
            }
        }
        return dayList
    }
}