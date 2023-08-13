package com.example.myapplication.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.HomeFunction.viewPager2.HomeViewpager2CategoryAdapter
import com.example.myapplication.HomeFunction.viewPager2.HomeViewPagerAdapter
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeCateData
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeTodoData
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentBinding
import java.time.LocalDate
import java.util.Calendar

class FragHome : Fragment() {

    lateinit var binding: HomeFragmentBinding
    private var myAdapter : HomeViewPagerAdapter? = null
    private val viewModel : HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.classifyTodo()
        Log.d("classify", viewModel.cateTodoList.value.toString())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val homeViewPager = binding.homeViewpager2
        val homeIndicator = binding.homeIndicator

        //date clicklistener -> 추가 예정
        binding.tvHomeCalendar.setOnClickListener {

        }

        //viewpager 연결, indicator 연결
        myAdapter = HomeViewPagerAdapter(this@FragHome)
        homeViewPager.adapter = myAdapter

        homeIndicator.setViewPager(homeViewPager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarHome.inflateMenu(R.menu.home_menu)
        binding.toolbarHome.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.home_menu_timetable -> {
                    Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeTimetableFragment)
                    true
                }
                R.id.home_menu_category -> {
                    Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeCategoryFragment)
                    true
                }
                R.id.home_menu_repeatTodo -> {
                    Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeRepeatTodoFragment)
                    true
                }
                else -> false
            }
        }

        val calendarHome = binding.calendarviewHome
        val calendarLayout = binding.layoutCalendarviewHome

        var date = LocalDate.now()
        var dateCalendar = Calendar.getInstance()
        dateCalendar.set(date.year, (date.monthValue-1), date.dayOfMonth)
        val currentDay = findDayOfWeek(date.year, (date.monthValue -1), date.dayOfMonth, dateCalendar)

        binding.tvHomeCalendar.text = "${date.monthValue}월 ${date.dayOfMonth}일 ${currentDay}"

        //날짜 텍스트 클릭 시
        binding.tvHomeCalendar.setOnClickListener {
            if(calendarLayout.isVisible){
                calendarLayout.isGone = true
                Log.d("gone", "gone 작동")
            }
            else {
                calendarLayout.isVisible = true
                Log.d("visible", "visible 작동")
            }
        }

        //달력은 현재 날짜로 세팅
        // 클릭 시 텍스트 변환
        var selected : Calendar = Calendar.getInstance()

        binding.calendarviewHome.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selected.set(year, month, dayOfMonth)
            var calendarDay = findDayOfWeek(year, month, dayOfMonth, selected)
            binding.tvHomeCalendar.text = "${month + 1}월 ${dayOfMonth}일 ${calendarDay}"
            calendarLayout.isGone = true
        }

    }

    private fun findDayOfWeek(y : Int, m : Int, d : Int, selected : Calendar) : String {
        var dayOfWeek =
            when(selected.get(Calendar.DAY_OF_WEEK)){
                1 -> "일요일"
                2 -> "월요일"
                3 -> "화요일"
                4 -> "수요일"
                5 -> "목요일"
                6 -> "금요일"
                else -> "토요일"
            }
        return dayOfWeek
    }



}