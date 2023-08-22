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
import com.example.myapplication.CalenderFuntion.Model.CharacterResponse
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.HomeFunction.adapter.todo.HomeViewPagerAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.MyWebviewActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentBinding
import com.example.myapplication.hideBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.Calendar
import com.squareup.picasso.Picasso

class FragHome : Fragment() {

    lateinit var binding: HomeFragmentBinding
    private var myAdapter : HomeViewPagerAdapter? = null
    private val viewModel : HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.userToken = MyWebviewActivity.prefs.getString("token","")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        hideBottomNavigation(false, activity)
        //서버에서 cateogry, todo받아오기


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

        viewModel.cateTodoList.observe(viewLifecycleOwner, Observer {
            binding.tvHomeUsername.text = "${viewModel.userName}님,"
            getCustomChar()
        })

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
            //달력에 지정된 날짜 다음 프래그먼트로 전달
            val year = viewModel.homeDate.value!!.year
            val month = String.format("%02d", viewModel.homeDate.value!!.monthValue)
            val day = String.format("%02d", viewModel.homeDate.value!!.dayOfMonth)
            val bundle = Bundle()
            bundle.putString("today","${year}-${month}-${day}")

            when(it.itemId){

                R.id.home_menu_timetable -> {
                    Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeTimetableFragment,bundle)
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
            viewModel.changeDate(year, (month +1), dayOfMonth, "home")
            Log.d("date 확인", viewModel.homeDate.toString())
            binding.tvHomeSentence.text = homeMent(calendarDay)
        }


        viewModel.homeDate.observe(viewLifecycleOwner, Observer {
            //viewModel.getTodo(viewModel.userToken, viewModel.homeDate.value.toString())
            Log.d("date변경", binding.tvHomeCalendar.text.toString())
            //viewModel.updateCateTodoList()
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

    override fun onResume() {
        super.onResume()
        hideBottomNavigation(false, activity)
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

        val service = RetrofitInstance.getInstance().create(RetrofitServiceCalendar::class.java)
        val call2 = service.characterRequest(viewModel.userToken)
        call2.enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(call2: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.data.datas
                        if(datas != null) {
                            for (data in datas) {
                                //arrays.add(data)
                                Log.d("111","datas: ${data.id} ${data.itemType} ${data.filePath}")
                                if(data.itemType=="color") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.ivHomeRamdi)
                                } else if(data.itemType=="set") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.ivHomeCloth)
                                } else if(data.itemType=="item") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.ivHomeItem)
                                }
                                // ...
                            }
                        } else {
                            //Log.d("2221","${response.code()}")
                        }
                    } else {
                        //Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                    //Log.d("3331","itemType: ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                //Log.d("444","itemType: ${t.message}")
            }
        })
    }

}