package com.example.myapplication.MyFuction.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarUtil
import com.example.myapplication.MyFuction.Adapter.MyCalendarMonthAdapter
import com.example.myapplication.MyFuction.Adapter.MyRecordCategoryAdapter
import com.example.myapplication.MyFuction.Data.MyRecordCategoryData
import com.example.myapplication.R
import com.example.myapplication.databinding.MyRecordMonthBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MyRecordMonthFragment : Fragment() {
    private lateinit var binding: MyRecordMonthBinding
    private lateinit var calendar: Calendar
    lateinit var navController: NavController
    val datas = mutableListOf<MyRecordCategoryData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyRecordMonthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_myRecordMonthFragment_to_fragMy)
        }

        initCategoryRecycler()


        //달력 부분
        CalendarUtil.selectedDate = LocalDate.now()
        calendar = Calendar.getInstance()
        setMonthView()
        setTodoView()
        setTimetableView()

        binding.preBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1)
            calendar.add(Calendar.MONTH, -1)
            setMonthView()
            setTodoView()
            setTimetableView()
        }
        binding.nextBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1)
            calendar.add(Calendar.MONTH, 1)
            setMonthView()
            setTodoView()
            setTimetableView()
        }
        binding.dayWeekMonthBtn.setOnClickListener {
            navController.navigate(R.id.action_myRecordMonthFragment_to_myRecordDayFragment)
        }
    }

    // 투두 뷰 설정
    private fun setTodoView() {
        val month = CalendarUtil.selectedDate.monthValue // 월을 가져옴
        val todoCnt = 7.4 // 임시데이터
        val todoPercent = 72.6 // 임시데이터

        val formattedText1 = "${month}월 투두"
        val formattedText2 =
            "${month}월의 평균 투두는 ${todoCnt}개이고\n그 중 ${todoPercent}%를 클리어하셨어요"

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
        val formattedText2 =
            "${nickname}님이 가장 많은 시간을 투자한 카테고리는\n${category1}, ${category2}, ${category3} 입니다."

        binding.recordTitleTimetable.text = formattedText1
        binding.recordContextTimetable.text = formattedText2
    }

    // 통계 우측 카테고리 리사이클러뷰 설정
    private fun initCategoryRecycler() {
        val adapter = MyRecordCategoryAdapter(requireContext())
        var manager = LinearLayoutManager(requireContext())

        datas.apply {
            val categoryName = "카테고리명" // 임시데이터
            val percentNum = 25 // 임시데이터
            val colorCode = "F0768C" // 임시데이터
            val categoryNum = 6 // 임시데이터

            var size = if (categoryNum != 0) { categoryNum.minus(1) }
            else { -1 }

            // 서버 데이터 받아서 반복문으로 수정하기
            add(MyRecordCategoryData(percent = "${percentNum}%", colorCode = colorCode, category = categoryName))
            add(MyRecordCategoryData(percent = "${percentNum}%", colorCode = colorCode, category = categoryName))
            add(MyRecordCategoryData(percent = "${percentNum}%", colorCode = colorCode, category = categoryName))
            add(MyRecordCategoryData(percent = "${percentNum}%", colorCode = colorCode, category = categoryName))

            adapter.datas = datas
            adapter.notifyDataSetChanged()

        }
        binding.myCategoryRecycler.adapter = adapter
        binding.myCategoryRecycler.layoutManager = manager
    }

    // 달력 뷰 설정
    private fun setMonthView() {
        val month = CalendarUtil.selectedDate.monthValue // 월을 가져옴
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월")

        val formattedText1 = CalendarUtil.selectedDate.format(formatter)
        val formattedText2 = "${month}월 투두"
        val formattedText3 = "${month}월 시간표"

        binding.textCalendar.text = formattedText1
        binding.recordTitleTodo.text = formattedText2
        binding.recordTitleTimetable.text = formattedText3

        val dayList = dayInMonthArray()

        val formatter2 = DateTimeFormatter.ofPattern("M")
        val adapter = MyCalendarMonthAdapter(dayList, CalendarUtil.selectedDate.format(formatter2))
        val manager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 7)
        binding.calendar2.layoutManager = manager
        binding.calendar2.adapter = adapter
    }

    private fun dayInMonthArray(): ArrayList<Date> {
        val dayList = ArrayList<Date>()
        val monthCalendar = calendar.clone() as Calendar

        monthCalendar[Calendar.DAY_OF_MONTH] = 1
        val firstDayofMonth = monthCalendar[Calendar.DAY_OF_WEEK] - 1
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayofMonth)

        var i = 0
        val curMon = calendar.get(Calendar.MONTH) + 1
        while (i < 38) {
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val date = dateFormat.parse(monthCalendar.time.toString())
            if (curMon < SimpleDateFormat("M", Locale.ENGLISH).format(date).toInt()) {
                break
            }
            dayList.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dayList
    }
}
