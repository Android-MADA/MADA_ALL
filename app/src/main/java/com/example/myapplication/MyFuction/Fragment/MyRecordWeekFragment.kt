package com.example.myapplication.MyFuction.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.example.myapplication.MyFuction.Adapter.MyRecordCategoryAdapter
import com.example.myapplication.MyFuction.Calendar.MyWeekSliderlAdapter
import com.example.myapplication.MyFuction.Data.MyRecordCategoryData
import com.example.myapplication.R
import com.example.myapplication.databinding.MyRecordWeekBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class MyRecordWeekFragment : Fragment() {
    private lateinit var binding: MyRecordWeekBinding
    private lateinit var calendar: Calendar
    lateinit var navController: NavController
    val datas = mutableListOf<MyRecordCategoryData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyRecordWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isGone = true
        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_myRecordWeekFragment_to_fragMy)
        }

        // 통계 부분
        initCategoryRecycler()
        initCategoryPieChart()

        val calendarAdapter = MyWeekSliderlAdapter(this,binding.textCalendar,binding.calendar2)
        binding.calendar2.adapter = calendarAdapter
        binding.calendar2.setCurrentItem(CalendarSliderAdapter.START_POSITION, false)
        binding.preBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(1, true)
        }

        binding.nextBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(1, true)
        }

        binding.dayWeekMonthBtn.setOnClickListener {
            navController.navigate(R.id.action_myRecordWeekFragment_to_myRecordMonthFragment)
        }

    }
    fun weekChange(month : Int, week : Int,date : String) {
        setTodoView(month, week)
        setTimetableView(month, week)
        Log.d("dasdas",date)
    }

    // 투두 뷰 설정
    private fun setTodoView(month : Int, weekOfMonth : Int) {
        val todoCnt = 7.4 // 임시데이터
        val todoPercent = 72.6 // 임시데이터

        val formattedText1 = "${month}월 ${weekOfMonth}주 투두"
        val formattedText2 =
            "${month}월 ${weekOfMonth}주차의 평균 투두는 ${todoCnt}개이고\n그 중 ${todoPercent}%를 클리어하셨어요"

        binding.recordTitleTodo.text = formattedText1
        binding.recordContextTodo.text = formattedText2
    }

    // 시간표 뷰 설정
    private fun setTimetableView(month : Int, weekOfMonth : Int) {
        val nickname = "김마다" // 임시데이터
        val category1 = "카테고리1" // 임시데이터
        val category2 = "카테고리2"// 임시데이터
        val category3 = "카테고리3" // 임시데이터

        val formattedText1 = "${month}월 ${weekOfMonth}주 시간표"
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

            // 임시데이터
            val categoryName = "카테고리명"
            val percentNum = 20
            val colorCode = "F0768C"
            val categoryNum = 6
            var size = if (categoryNum != 0) { categoryNum.minus(1) } else { -1 }

            // 서버데이터 받으면 반복문으로 수정하기
            add(MyRecordCategoryData(percent = "${percentNum}%", colorCode = colorCode, category = categoryName))
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

    // 통계 좌측 파이차트 뷰 설정
    private fun initCategoryPieChart() {
        binding.myChart.setUsePercentValues(true)

        // 임시데이터
        val categoryName = "카테고리명"
        val percentNum = 20f
        val colorCode = "F0768C"
        val categoryNum = 6

        var size = if (categoryNum != 0) { categoryNum.minus(1) }
        else { -1 }

        // 서버데이터 받으면 반복문으로 수정하기
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(percentNum, categoryName))
        entries.add(PieEntry(percentNum, categoryName))
        entries.add(PieEntry(percentNum, categoryName))
        entries.add(PieEntry(percentNum, categoryName))
        entries.add(PieEntry(percentNum, categoryName))

        // 색상
        val colorsItems = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
        for (c in COLORFUL_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
        colorsItems.add(ColorTemplate.getHoloBlue())


        // 데이터셋 초기화
        val pieDataSet = PieDataSet(entries, "")
            pieDataSet.apply {
                colors = colorsItems
                valueTextColor = Color.BLACK
                valueTextSize = 12f
                setDrawValues(false) // 차트 내 수치 값 표시 비활성화
            }

        // 데이터셋 세팅
        val pieData = PieData(pieDataSet)
        binding.myChart.apply {
            data = pieData
            isRotationEnabled = false
            description.isEnabled = false // 차트 내 항목 값 표시 비활성화
            legend.isEnabled = false // 범례 비활성화
            setDrawEntryLabels(false) // 라벨 비활성화
            setDrawMarkers(false) // 차트 위에 마우스포인트 올릴 시 마커(필요 시 뷰 구현하겠음!)
            animateY(1400, Easing.EaseInOutQuad) // 시계방향 애니메이션
            animate()
        }
        // 범례 커스텀
//        binding.myChart.legend.apply {
//            isEnabled = true // 범례 활성화
//            verticalAlignment = Legend.LegendVerticalAlignment.CENTER // 세로 위치 중앙
//            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT // 우측 배치
//            orientation = Legend.LegendOrientation.VERTICAL // 세로방향으로 배치
//            setDrawInside(false) // 차트 내부에 그리지 않도록 설정
//            // 그 외 범례에 대한 스타일 등을 설정할 수 있습니다.
//        }
    }


}
