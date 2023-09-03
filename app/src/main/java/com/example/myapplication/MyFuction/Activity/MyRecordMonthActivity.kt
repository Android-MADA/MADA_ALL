package com.example.myapplication.MyFuction.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.MyFuction.Adapter.MyCalendarMonthAdapter
import com.example.myapplication.databinding.MyRecordMonthBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
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

        // 투두 부분
        setTodoView()

        // 시간표 부분
        setTimetableView()

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
    // 투두 뷰 설정
    private fun setTodoView() {
        val month = CalendarUtil.selectedDate.monthValue // 월을 가져옴
        val todoCnt = 7.4 // 임시데이터
        val todoPercent = 72.6 // 임시데이터


        val formattedText1 = "${month}월 투두"
        val formattedText2 = "${month}월의 평균 투두는 ${todoCnt}개이고\n그 중 ${todoPercent}%를 클리어하셨어요"

        binding.recordTitleTodo.text = formattedText1
        binding.recordContextTodo.text = formattedText2

    }
    // 시간표 뷰 설정
    private fun setTimetableView() {
        val month = CalendarUtil.selectedDate.monthValue // 월을 가져옴
        val nickname = "김마다" // 임시데이터
        val category1 = "카테고리1" // 임시데이터
        val category2 = "카테고리2"// 임시데이터
        val category3 = "카테고리3" // 임시데이터


        val formattedText1 = "${month}월 시간표"
        val formattedText2 = "${nickname}님이 가장 많은 시간을 투자한 카테고리는\n${category1}, ${category2}, ${category3} 입니다."

        binding.recordTitleTimetable.text = formattedText1
        binding.recordContextTimetable.text = formattedText2

    }
    // 달력 뷰 설정
    private fun setMonthView() {
        val month = CalendarUtil.selectedDate.monthValue // 월을 가져옴
        var formatter = DateTimeFormatter.ofPattern("yyyy년 M월")

        val formattedText1 = CalendarUtil.selectedDate.format(formatter)
        val formattedText2 = "${month}월 투두"
        val formattedText3 = "${month}월 시간표"

        binding.textCalendar.text = formattedText1
        binding.recordTitleTodo.text = formattedText2
        binding.recordTitleTimetable.text = formattedText3

        val dayList = dayInMonthArray()

        val formatter2 = DateTimeFormatter.ofPattern("M")
        val adapter = MyCalendarMonthAdapter(dayList,CalendarUtil.selectedDate.format(formatter2))
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