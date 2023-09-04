package com.example.myapplication.MyFuction.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.HomeFunction.Model.categorylist2
import com.example.myapplication.MyFuction.Adapter.MyCalendarWeekAdapter
import com.example.myapplication.MyFuction.Adapter.MyRecordCategoryAdapter
import com.example.myapplication.MyFuction.Data.MyRecordCategoryData
import com.example.myapplication.R
import com.example.myapplication.databinding.MyRecordWeekBinding
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MyRecordWeekActivity : AppCompatActivity() {
    private lateinit var binding: MyRecordWeekBinding
    private lateinit var calendar: Calendar
    lateinit var navController: NavController
    lateinit var myRecordCategoryAdapter: MyRecordCategoryAdapter
    val datas = mutableListOf<MyRecordCategoryData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //navController = binding.navHostFragmentContainer.findNavController()

        binding = MyRecordWeekBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        // 투두 멘트 부분
        setTodoView()

        // 시간표 멘트 부분
        setTimetableView()

        // 통계 카테고리 부분
        initCategoryRecycler()

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
    // 투두 뷰 설정
    private fun setTodoView() {
        val month = CalendarUtil.selectedDate.monthValue // 월을 가져옴
        val weekOfMonth = CalendarUtil.selectedDate.get(WeekFields.of(Locale("en","US")).weekOfMonth()) // 주차를 가져옴
        val todoCnt = 7.4 // 임시데이터
        val todoPercent = 72.6 // 임시데이터


        val formattedText1 = "${month}월 ${weekOfMonth}주 투두"
        val formattedText2 = "${month}월 ${weekOfMonth}주차의 평균 투두는 ${todoCnt}개이고\n그 중 ${todoPercent}%를 클리어하셨어요"

        binding.recordTitleTodo.text = formattedText1
        binding.recordContextTodo.text = formattedText2

    }
    // 시간표 뷰 설정
    private fun setTimetableView() {
        val month = CalendarUtil.selectedDate.monthValue // 월을 가져옴
        val weekOfMonth = CalendarUtil.selectedDate.get(WeekFields.of(Locale("en","US")).weekOfMonth()) // 주차를 가져옴
        val nickname = "김마다" // 임시데이터
        val category1 = "카테고리1" // 임시데이터
        val category2 = "카테고리2"// 임시데이터
        val category3 = "카테고리3" // 임시데이터


        val formattedText1 = "${month}월 ${weekOfMonth}주 시간표"
        val formattedText2 = "${nickname}님이 가장 많은 시간을 투자한 카테고리는\n${category1}, ${category2}, ${category3} 입니다."

        binding.recordTitleTimetable.text = formattedText1
        binding.recordContextTimetable.text = formattedText2

    }
    // 통계 우측 카테고리 리사이클러뷰 설정
    private fun initCategoryRecycler() {
        myRecordCategoryAdapter = MyRecordCategoryAdapter(this)
        binding.myCategoryRecycler.adapter = myRecordCategoryAdapter


        datas.apply {
            val categoryName = "카테고리명" // 임시데이터
            val percentNum = 25 // 임시데이터

            // 서버 데이터 받아서 반복문으로 수정하기
            add(MyRecordCategoryData(percent = "${percentNum}%", colorImage = R.drawable.ic_record_color, category = categoryName))
            add(MyRecordCategoryData(percent = "${percentNum}%", colorImage = R.drawable.ic_record_color, category = categoryName))
            add(MyRecordCategoryData(percent = "${percentNum}%", colorImage = R.drawable.ic_record_color, category = categoryName))
            add(MyRecordCategoryData(percent = "${percentNum}%", colorImage = R.drawable.ic_record_color, category = categoryName))

            myRecordCategoryAdapter.datas = datas
        }
        myRecordCategoryAdapter.notifyDataSetChanged()
    }

    // 캘린더 뷰 설정
    private fun setWeekView() {
        val month = CalendarUtil.selectedDate.monthValue // 월을 가져옴
        val weekOfMonth = CalendarUtil.selectedDate.get(WeekFields.of(Locale("en","US")).weekOfMonth()) // 주차를 가져옴

        val formattedText = "${month}월 ${weekOfMonth}주차"
        val formattedText3 = "${month}월 ${weekOfMonth}주 시간표"

        binding.textCalendar.text = formattedText
        binding.recordTitleTimetable.text = formattedText3

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