package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adpater.Calendar2Adapter
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.CalenderFuntion.OnItemListener
import com.example.myapplication.databinding.CalendarAddBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class CalendarAdd : AppCompatActivity(), OnItemListener {
    private lateinit var calendar: Calendar
    lateinit var binding: CalendarAddBinding
    val preList: ArrayList<Int> = ArrayList()
    val nextList: ArrayList<Int> = ArrayList()
    val weekdays = arrayOf("일" ,"월", "화", "수", "목", "금", "토")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalendarAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preList.add(intent.getIntExtra("Month",0))
        nextList.add(intent.getIntExtra("Month",0))
        preList.add(intent.getIntExtra("Day",0))
        nextList.add(intent.getIntExtra("Day",0))
        preList.add(intent.getIntExtra("Yoil",1))
        nextList.add(intent.getIntExtra("Yoil",1))
        preList.add(0)
        nextList.add(0)

        binding.preScheldule.text = preList[0].toString()+"월 " + preList[1].toString() +"일 ("+weekdays[preList[2]]+")"
        binding.nextScheldule.text = nextList[0].toString()+"월 " + nextList[1].toString() +"일 ("+weekdays[nextList[2]]+")"

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
        binding.cal.visibility= View.GONE
        binding.preScheldule.setOnClickListener {
            binding.preScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.nextScheldule.setBackgroundColor(Color.TRANSPARENT)
            preList[3]=1
            nextList[3]=0
            binding.cal.visibility= View.VISIBLE
        }
        binding.nextScheldule.setOnClickListener {
            binding.nextScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.preScheldule.setBackgroundColor(Color.TRANSPARENT)
            preList[3]=0
            nextList[3]=1
            binding.cal.visibility= View.VISIBLE
        }
        binding.preScheldule2.setOnClickListener {
            binding.preScheldule2.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.nextScheldule2.setBackgroundColor(Color.TRANSPARENT)
        }
        binding.nextScheldule2.setOnClickListener {
            binding.nextScheldule2.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.preScheldule2.setBackgroundColor(Color.TRANSPARENT)
        }
        binding.timePicker.setInitialSelectedTime("36:36")
    }
    private fun setMonthView() {
        var formatter = DateTimeFormatter.ofPattern("yyyy년 M월")
        binding.textCalendar.text = CalendarUtil.selectedDate.format(formatter)
        val dayList = dayInMonthArray()
        val adapter = Calendar2Adapter(dayList,preList,nextList,binding.cal,binding.preScheldule,binding.nextScheldule)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(this,7)
        binding.calendar2.layoutManager = manager
        binding.calendar2.adapter = adapter
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
        Toast.makeText(this, yearMonthDay, Toast.LENGTH_SHORT).show()
    }
}