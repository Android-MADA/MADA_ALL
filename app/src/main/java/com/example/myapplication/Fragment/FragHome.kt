package com.example.myapplication.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.myapplication.HomeFunction.Model.HomeCharacData
import com.example.myapplication.HomeFunction.Model.TodoList
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.HomeFunction.adapter.todo.HomeViewPagerAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.HomeFragmentBinding
import com.example.myapplication.db.entity.TodoEntity
import com.example.myapplication.hideBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class FragHome : Fragment() {

    lateinit var binding: HomeFragmentBinding
    private var myAdapter: HomeViewPagerAdapter? = null
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.userToken = Splash2Activity.prefs.getString("token", "")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        hideBottomNavigation(false, activity)
        //날짜 변경 시 서버에서 cateogry, todo받아오기


        val homeViewPager = binding.homeViewpager2
        val homeIndicator = binding.homeIndicator

        //viewpager 연결, indicator 연결
        myAdapter = HomeViewPagerAdapter(this@FragHome)
        homeViewPager.adapter = myAdapter
        homeIndicator.setViewPager(homeViewPager)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
        Log.d("token", viewModel.userToken)

        //유저 이름
        viewModel.dUserName.observe(viewLifecycleOwner, Observer {
            binding.tvHomeUsername.text = "${it}님,"
        })

        //배경설정

        val calendarLayout = binding.layoutCalendarviewHome
//        binding.tvHomeProgressMax.text = viewModel.todoNum.toString()
//        binding.progressBar.max = viewModel.todoNum.value!!
//        binding.tvHomeProgressComplete.text = viewModel.completeTodoNum.value.toString()
//        binding.progressBar.progress = viewModel.completeTodoNum.value!!
//        binding.calendarviewHome.firstDayOfWeek = viewModel.startDay.value!!
//
        //달력은 현재 날짜로 세팅
        var dateCalendar = Calendar.getInstance()
        dateCalendar.set(
            viewModel.homeDate.value!!.year,
            (viewModel.homeDate.value!!.monthValue - 1),
            viewModel.homeDate.value!!.dayOfMonth
        )
        var currentDay = findDayOfWeek(
            viewModel.homeDate.value!!.year,
            (viewModel.homeDate.value!!.monthValue - 1),
            viewModel.homeDate.value!!.dayOfMonth,
            dateCalendar
        )
        binding.tvHomeCalendar.text =
            "${viewModel.homeDate.value!!.monthValue}월 ${viewModel.homeDate.value!!.dayOfMonth}일 ${currentDay}"

        binding.tvHomeSentence.text = homeMent(currentDay)

        //actinobar 설정
        binding.ivHomeMenu.setOnClickListener {
            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.home_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.home_menu_timetable -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_fragHome_to_homeTimetableFragment)
                        true
                    }

                    R.id.home_menu_category -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_fragHome_to_homeCategoryFragment)
                        true
                    }

                    R.id.home_menu_repeatTodo -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_fragHome_to_homeRepeatTodoFragment)
                        true
                    }

                    else -> false
                }
            }
            popup.show()
        }


        //날짜 텍스트 클릭 시
        binding.tvHomeCalendar.setOnClickListener {
            if (calendarLayout.isVisible) {
                calendarLayout.isGone = true
            } else {
                calendarLayout.isVisible = true
            }
        }

        // 클릭 시 텍스트 변환
        binding.calendarviewHome.setOnDateChangeListener { view, year, month, dayOfMonth ->

            dateCalendar.set(year, month, dayOfMonth)
            var calendarDay = findDayOfWeek(year, month, dayOfMonth, dateCalendar)
            binding.tvHomeCalendar.text = "${month + 1}월 ${dayOfMonth}일 ${calendarDay}"
            calendarLayout.isGone = true
            binding.tvHomeSentence.text = homeMent(calendarDay)
            viewModel.changeDate(year, (month +1), dayOfMonth, "home")
            //db 투두 데이터 전체 삭제 후 해당 날짜의 데이터를 서버에서 받아서 db에 저장하고 어댑터에 연결하기
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteAllTodo()
                //투두 새로 서버 에서 읽어오기
                api.getAllMyTodo(viewModel.userToken, viewModel.homeDate.value.toString()).enqueue(object : Callback<TodoList> {
                    override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                        if(response.isSuccessful){
                            for(i in response.body()!!.data.TodoList){
                                val todoData = TodoEntity(id = i.id, date = i.date, category = i.category.id, todoName = i.todoName, complete = i.complete, repeat = i.repeat, repeatWeek = i.repeatWeek, repeatMonth = i.repeatMonth, endRepeatDate = i.endRepeatDate, startRepeatDate = i.startRepeatDate, isAlarm = i.isAlarm, startTodoAtMonday = i.startTodoAtMonday,  endTodoBackSetting = i.endTodoBackSetting, newTodoStartSetting = i.newTodoStartSetting )
                                Log.d("todo server", todoData.toString())
                                viewModel.createTodo(todoData, null)
                            }
                            //닉네임 저장하기
                            viewModel.userHomeName = response.body()!!.data.nickname
                        }
                        else {
                            Log.d("todo안드 잘못", "서버 연결 실패")
                        }
                    }

                    override fun onFailure(call: Call<TodoList>, t: Throwable) {
                        Log.d("todo서버 연결 오류", "서버 연결 실패")
                    }

                })
            }
        }
//
//
        viewModel.homeDate.observe(viewLifecycleOwner, Observer {
            Log.d("date변경", binding.tvHomeCalendar.text.toString())
            //getCustomChar()
        })
//
        viewModel.startDay.observe(viewLifecycleOwner, Observer{
            Log.d("startday", "데이터 변경 감지 ${viewModel.startDay.value}")
            binding.calendarviewHome.firstDayOfWeek = viewModel.startDay.value!!
        })
//
//        //date를 통해서 todo가 변경되었을 때 실행
//        viewModel.todoNum.observe(viewLifecycleOwner, Observer {
//            binding.tvHomeProgressMax.text = viewModel.todoNum.value.toString()
//            binding.progressBar.max = viewModel.todoNum.value!!
//        })
//
//        viewModel.completeTodoNum.observe(viewLifecycleOwner, Observer {
//            binding.tvHomeProgressComplete.text = viewModel.completeTodoNum.value.toString()
//            binding.progressBar.progress = viewModel.completeTodoNum.value!!
//        })


    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation(false, activity)
    }

    private fun findDayOfWeek(y: Int, m: Int, d: Int, selected: Calendar): String {
        var dayOfWeek =
            when (selected.get(Calendar.DAY_OF_WEEK)) {
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

    private fun homeMent(day: String): String {
        var homeMent = when (day) {
            "월요일" -> "월요병 날려버리고 화이팅!"
            "화요일" -> "화끈한 에너지로 화요일을 불태워보세요! 화이팅!"
            "수요일" -> "수투레스받을 땐 심호흡 한 번 해보세요!!"
            "목요일" -> "오늘도 열심히 달려 봐요"
            "금요일" -> "오늘도 열심히 달려 봐요"
            "토요일" -> "주말을 알차게!"
            else -> "일주일의 마지막도 파이팅!"
        }
        return homeMent
    }

    private fun getCustomChar() {

        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

        api.getHomeRamdi(viewModel.userToken).enqueue(object : Callback<HomeCharacData> {
            override fun onResponse(
                call: Call<HomeCharacData>,
                response: Response<HomeCharacData>
            ) {
                if (response.isSuccessful) {
                    Log.d("home캐릭터 성공", "성공 ${response.body()!!.data.wearingItems}")
                    val apiResponse = response.body()!!.data.wearingItems
                    if (apiResponse.isEmpty() != true) {
                        for (i in apiResponse) {
                            if (i != null) {
                                if (i.itemType == "color") {
                                    Picasso.get()
                                        .load(i.filePath)
                                        .into(binding.ivHomeRamdi)
                                } else if (i.itemType == "set") {
                                    Picasso.get()
                                        .load(i.filePath)
                                        .into(binding.ivHomeCloth)
                                } else if (i.itemType == "item") {
                                    Picasso.get()
                                        .load(i.filePath)
                                        .into(binding.ivHomeItem)
                                }
                            }
                        }
                    }
                } else {
                    Log.d("home캐릭터 안드 잘못", "실패")
                }
            }

            override fun onFailure(call: Call<HomeCharacData>, t: Throwable) {
                Log.d("home캐릭터 연결 실패", "실패")
            }

        })

//
    }

}