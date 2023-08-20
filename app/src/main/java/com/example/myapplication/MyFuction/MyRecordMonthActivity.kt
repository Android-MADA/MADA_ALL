package com.example.myapplication.MyFuction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.databinding.MyRecordMonthBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MyRecordMonthActivity : AppCompatActivity() {
    private lateinit var binding: MyRecordMonthBinding
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyRecordMonthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        //달력 부분
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
        binding.dayWeekMonthBtn.setOnClickListener {
            finish()
            val intent = Intent(this, MyRecordDayActivity::class.java)
            startActivity(intent)
        }

    }
    private fun setMonthView() {
        var formatter = DateTimeFormatter.ofPattern("yyyy년 M월")
        binding.textCalendar.text = CalendarUtil.selectedDate.format(formatter)

        val dayList = dayInMonthArray()

        val formatter2 = DateTimeFormatter.ofPattern("M")
        val adapter = MyCalendarAdapter(dayList,CalendarUtil.selectedDate.format(formatter2))
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
}