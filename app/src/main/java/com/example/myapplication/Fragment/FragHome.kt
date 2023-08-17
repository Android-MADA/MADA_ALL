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
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.CategoryList
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.Model.TodoList
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.HomeFunction.adapter.todo.HomeViewPagerAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.Calendar

class FragHome : Fragment() {

    lateinit var binding: HomeFragmentBinding
    private var myAdapter : HomeViewPagerAdapter? = null
    private val viewModel : HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        //서버에서 cateogry, todo받아오기



        val homeViewPager = binding.homeViewpager2
        val homeIndicator = binding.homeIndicator

        //viewpager 연결, indicator 연결
        myAdapter = HomeViewPagerAdapter(this@FragHome)
        homeViewPager.adapter = myAdapter
        homeIndicator.setViewPager(homeViewPager)

        //서버연결 시작
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyUldNdDc0LVN2aUljMnh6SE5pQXJQNzZwRnB5clNaXzgybWJNMTJPR000IiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjE5NDg0NywiZXhwIjoxNjkyMjMwODQ3fQ.b1YBDxmWi45MkkL6DknWZYEFfefXC3h4Gi0meLtv4WWs-1WAMFxFKloW7x8EjRFX_iDFOdmGdJCvJgJbsWfGgA"
        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

        viewModel.getCategory(viewModel.userToken)
        //viewModel.getTodo(viewModel.userToken, "2023-08-16")
        api.getAllTodo(viewModel.userToken, "2023-08-15").enqueue(object :Callback<TodoList>{
            override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                Log.d("getTodo", response.isSuccessful.toString())
            }

            override fun onFailure(call: Call<TodoList>, t: Throwable) {
                Log.d("getTodo", "todoGet 실패")
            }

        })
        viewModel.todoList.observe(viewLifecycleOwner, Observer {
            viewModel.classifyTodo()
            viewModel.cateTodoList.observe(viewLifecycleOwner, Observer {
                Log.d("FragHome 서버", viewModel.cateTodoList.toString())
            })
        })

        viewModel.updateTodoNum()
        viewModel.updateCompleteTodo()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarLayout = binding.layoutCalendarviewHome
        binding.tvHomeProgressMax.text = viewModel.todoNum.toString()
        binding.progressBar.max = viewModel.todoNum.value!!
        binding.tvHomeProgressComplete.text = viewModel.completeTodoNum.value.toString()
        binding.progressBar.progress = viewModel.completeTodoNum.value!!
        binding.calendarviewHome.firstDayOfWeek = viewModel.startDay.value!!

        //달력은 현재 날짜로 세팅
        var dateCalendar = Calendar.getInstance()
        dateCalendar.set(viewModel.homeDate.value!!.year, (viewModel.homeDate.value!!.monthValue-1), viewModel.homeDate.value!!.dayOfMonth)
        var currentDay = findDayOfWeek(viewModel.homeDate.value!!.year, (viewModel.homeDate.value!!.monthValue -1), viewModel.homeDate.value!!.dayOfMonth, dateCalendar)
        binding.tvHomeCalendar.text = "${viewModel.homeDate.value!!.monthValue}월 ${viewModel.homeDate.value!!.dayOfMonth}일 ${currentDay}"

        binding.tvHomeSentence.text = homeMent(currentDay)

        //actinobar 설정
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


        //날짜 텍스트 클릭 시
        binding.tvHomeCalendar.setOnClickListener {
            if(calendarLayout.isVisible) {calendarLayout.isGone = true}
            else {calendarLayout.isVisible = true}
        }

        // 클릭 시 텍스트 변환
        binding.calendarviewHome.setOnDateChangeListener { view, year, month, dayOfMonth ->
            dateCalendar.set(year, month, dayOfMonth)
            var calendarDay = findDayOfWeek(year, month, dayOfMonth, dateCalendar)
            binding.tvHomeCalendar.text = "${month + 1}월 ${dayOfMonth}일 ${calendarDay}"
            calendarLayout.isGone = true
            viewModel.changeDate(year, (month +1), dayOfMonth)
            binding.tvHomeSentence.text = homeMent(calendarDay)
        }

        viewModel.homeDate.observe(viewLifecycleOwner, Observer {
            //서버에서 category 새로 받아오기(date)
            //서버에서 Tood 새로 받아오기(date)
            //서버에서 ment 새로 받아오기(date)

            Log.d("date변경", binding.tvHomeCalendar.text.toString())
            viewModel.updateCateTodoList()
        })


        viewModel.cateTodoList.observe(viewLifecycleOwner, Observer {
            //todoNum 업데이트
            //completeNum 업데이트
            viewModel.updateTodoNum()
            viewModel.updateCompleteTodo()
        })

        //date를 통해서 todo가 변경되었을 때 실행
        viewModel.todoNum.observe(viewLifecycleOwner, Observer {
            binding.tvHomeProgressMax.text = viewModel.todoNum.value.toString()
            binding.progressBar.max = viewModel.todoNum.value!!
        })

        viewModel.completeTodoNum.observe(viewLifecycleOwner, Observer {
            binding.tvHomeProgressComplete.text = viewModel.completeTodoNum.value.toString()
            binding.progressBar.progress = viewModel.completeTodoNum.value!!
        })

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

    private fun homeMent(day : String) : String {
        var homeMent = when(day){
            "월요일" -> "월요일 입니다."
            "화요일" -> "화요일 입니다."
            "수요일" -> "수요일 입니다."
            "목요일" -> "목요일 입니다."
            "금요일" -> "금요일 입니다."
            "토요일" -> "토요일 입니다."
            else -> "일요일 입니다."
        }
        return homeMent
    }

}