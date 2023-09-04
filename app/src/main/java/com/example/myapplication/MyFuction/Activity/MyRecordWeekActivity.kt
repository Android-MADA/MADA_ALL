package com.example.myapplication.MyFuction.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.MyFuction.Adapter.MyCalendarWeekAdapter
import com.example.myapplication.databinding.MyRecordWeekBinding
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MyRecordWeekActivity : AppCompatActivity() {
    private lateinit var binding: MyRecordWeekBinding
    private lateinit var calendar: Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyRecordWeekBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        //달력 부분
        val daysUntilSaturday = (Calendar.SATURDAY - CalendarUtil.selectedDate.dayOfWeek.value) % 7
        Log.d("d",daysUntilSaturday.toString())
        // 선택된 날짜를 현재 날짜에서 토요일까지의 차이를 더한 날짜로 설정
        CalendarUtil.selectedDate = LocalDate.now().plusDays(daysUntilSaturday.toLong())
        calendar = Calendar.getInstance()
        setWeekView()

        binding.preBtn.setOnClickListener {

            CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusWeeks(1)
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
            setWeekView()
        }
        binding.nextBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusWeeks(1)
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            setWeekView()
        }
        binding.dayWeekMonthBtn.setOnClickListener {
            finish()
            val intent = Intent(this, MyRecordMonthActivity::class.java)
            startActivity(intent)
        }

    }
    private fun setWeekView() {
        val month = CalendarUtil.selectedDate.monthValue // 월을 가져옴
        val weekOfMonth = CalendarUtil.selectedDate.get(WeekFields.of(Locale("en","US")).weekOfMonth()) // 주차를 가져옴

        val formattedText = "${month}월 ${weekOfMonth}주차"
        binding.textCalendar.text = formattedText

        val weekList = getDatesInWeek()

        val adapter = MyCalendarWeekAdapter(weekList)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(this, 7)
        binding.calendar2.layoutManager = manager
        binding.calendar2.adapter = adapter
    }
    fun getDatesInWeek(): ArrayList<Date> {
        var monthCalendar = calendar.clone() as Calendar
        val weekDates = ArrayList<Date>()

        // 해당 주의 첫번째 날짜로 이동
        monthCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        monthCalendar.firstDayOfWeek = Calendar.SUNDAY

        for (i in 0 until 7) {
            weekDates.add(monthCalendar.time.clone() as Date)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return weekDates
    }
}