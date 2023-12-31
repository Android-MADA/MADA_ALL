package com.example.myapplication.Fragment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.CustomFunction.DataRepo
import com.example.myapplication.HomeFunction.adapter.todo.HomeCateListAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.bottomsheetdialog.TodoDateBottomSheetDialog
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.TimeFunction.calendar.TimeBottomSheetDialog
import com.example.myapplication.databinding.TodoLayoutBinding
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.hideBottomNavigation
import java.util.Calendar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragHome : Fragment() {

    //lateinit var binding: HomeFragmentBinding
    lateinit var binding : TodoLayoutBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val CalendarViewModel : CalendarViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.userToken = Splash2Activity.prefs.getString("token", "")

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TodoLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        hideBottomNavigation(false, activity)
        //날짜 변경 시 서버에서 cateogry, todo받아오기


        val colorbuttonInfo = when (DataRepo.buttonInfoEntity?.colorButtonInfo?.serverID) {
            10 -> ButtonInfo(R.id.btn_back_basic, 10, R.drawable.c_ramdi)
            11 -> ButtonInfo(R.id.btn_color_blue, 11, R.drawable.c_ramdyb)
            17 -> ButtonInfo(R.id.btn_color_Rblue, 17, R.drawable.c_ramdyrb)
            12 -> ButtonInfo(R.id.btn_color_bluepurple, 12, R.drawable.c_ramdybp)
            13 -> ButtonInfo(R.id.btn_color_green, 13, R.drawable.c_ramdyg)
            14 -> ButtonInfo(R.id.btn_color_orange, 14, R.drawable.c_ramdyo)
            16 -> ButtonInfo(R.id.btn_color_pink, 16, R.drawable.c_ramdypn)
            15 -> ButtonInfo(R.id.btn_color_purple, 15, R.drawable.c_ramdyp)
            18 -> ButtonInfo(R.id.btn_color_yellow, 18, R.drawable.c_ramdyy)
            else -> throw IllegalArgumentException("Unknown button ID")
        }





        val clothbuttonInfo = when (DataRepo.buttonInfoEntity?.clothButtonInfo?.serverID) {
            900 -> ButtonInfo(R.id.btn_cloth_basic, 900, R.drawable.custom_empty)
            41 -> ButtonInfo(R.id.btn_cloth_dev, 41, R.drawable.set_dev)
            44 -> ButtonInfo(R.id.btn_cloth_movie, 44, R.drawable.set_movie)
            40 -> ButtonInfo(R.id.btn_cloth_caffK, 40, R.drawable.set_caffk)
            46 -> ButtonInfo(R.id.btn_cloth_v, 46, R.drawable.set_v)
            39 -> ButtonInfo(R.id.btn_cloth_astronauts, 39, R.drawable.set_astronauts,)
            47 -> ButtonInfo(R.id.btn_cloth_zzim, 47, R.drawable.set_zzim)
            42 -> ButtonInfo(R.id.btn_cloth_hanbokF, 42, R.drawable.set_hanbokf)
            43 -> ButtonInfo(R.id.btn_cloth_hanbokM, 43, R.drawable.set_hanbokm)
            45 -> ButtonInfo(R.id.btn_cloth_snowman, 45, R.drawable.set_snowman)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        val itembuttonInfo = when (DataRepo.buttonInfoEntity?.itemButtonInfo?.serverID) {
            800 -> ButtonInfo(R.id.btn_item_basic, 800, R.drawable.custom_empty)
            22 -> ButtonInfo(R.id.btn_item_glass_normal, 22,R.drawable.g_nomal)
            30 -> ButtonInfo(R.id.btn_item_hat_ber, 30, R.drawable.hat_ber)
            33 -> ButtonInfo(R.id.btn_item_hat_grad, 33, R.drawable.hat_grad)
            21 -> ButtonInfo(R.id.btn_item_glass_8bit, 21,R.drawable.g_8bit)
            25 -> ButtonInfo(R.id.btn_item_glass_woig, 25, R.drawable.g_woig)
            35 -> ButtonInfo(R.id.btn_item_hat_ipod , 35, R.drawable.hat_ipod)
            24 -> ButtonInfo(R.id.btn_item_glass_sunR , 24,R.drawable.g_sunr)
            23 -> ButtonInfo(R.id.btn_item_glass_sunB,23, R.drawable.g_sunb)
            32 -> ButtonInfo(R.id.btn_item_hat_flower, 32, R.drawable.hat_flower)
            37 -> ButtonInfo(R.id.btn_item_hat_v, 37, R.drawable.hat_v)
            31 -> ButtonInfo(R.id.btn_item_hat_dinof, 31,R.drawable.hat_dinof)
            36 -> ButtonInfo(R.id.btn_item_hat_sheep, 36, R.drawable.hat_sheep)
            19 -> ButtonInfo(R.id.btn_item_bag_e,19, R.drawable.bag_e)
            20 -> ButtonInfo(R.id.btn_item_bag_luck,20, R.drawable.bag_luck)
            34 -> ButtonInfo(R.id.btn_item_hat_heart,34, R.drawable.hat_heart)
            29 -> ButtonInfo(R.id.btn_item_hat_bee, 29, R.drawable.hat_bee)
            38 -> ButtonInfo(R.id.btn_item_hat_heads, 38, R.drawable.heads)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        //캐릭터 커스텀 -> 확인하기
        binding.todoCharacterIv.setImageResource(
            colorbuttonInfo.selectedImageResource ?: 0
        )

        binding.ivHomeCloth.setImageResource(
            clothbuttonInfo.selectedImageResource ?: 0
        )
        binding.ivHomeItem.setImageResource(
            itembuttonInfo.selectedImageResource ?: 0
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
        Log.d("token", viewModel.userToken)
        viewModel.readAllTodo()
        viewModel.todoEntityList.observe(viewLifecycleOwner, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                var completeNum = 0
                var todoNum = it.size
                for(i in it){
                    if(i.complete == true){
                        completeNum++
                    }
                }

                binding.progressLayout.weightSum = todoNum.toFloat()
                binding.progressDoneLayout.layoutParams.apply {
                    (this as LinearLayout.LayoutParams).weight = completeNum.toFloat()
                }
                binding.todoAllNumTv.text = "${todoNum.toString()}개"
                binding.todoDoneNumTv.text = "${completeNum.toString()}개"
                var percent = if(todoNum == 0 || completeNum == 0){
                    0
                }
                else{
                    (completeNum.toFloat() / todoNum.toFloat() * 100.0).toInt()
                }
                binding.progressTv.text = "${percent.toString()}%"
            }
        })

        //todo rv
        //카테고리 데이터 가져와서 adapter 넣기
        viewModel.readActiveCate(false)
        viewModel.cateEntityList.observe(viewLifecycleOwner, Observer {
            val cateList = it as List<CateEntity>
            Log.d("cateList", cateList.toString())
            val mAdapter = HomeCateListAdapter(binding.todoActiveCategoryRv, requireFragmentManager())
            mAdapter.viewModel = viewModel
            mAdapter.submitList(cateList)
            binding.todoActiveCategoryRv.adapter = mAdapter
            binding.todoActiveCategoryRv.layoutManager = LinearLayoutManager(this.requireActivity())

        })

        viewModel.readQuitCate(true)
        viewModel.quitCateEntityList.observe(viewLifecycleOwner, Observer {
            val cateList = it as List<CateEntity>
            Log.d("cateList", cateList.toString())
            val mAdapter = HomeCateListAdapter(binding.todoInactiveCategoryRv, requireFragmentManager())
            mAdapter.viewModel = viewModel
            mAdapter.submitList(cateList)
            binding.todoInactiveCategoryRv.adapter = mAdapter
            binding.todoInactiveCategoryRv.layoutManager = LinearLayoutManager(this.requireActivity())
        })


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

        //date setting
        binding.todoDateTv.text = "${viewModel.homeDate.value!!.monthValue}월 ${viewModel.homeDate.value!!.dayOfMonth}일 ${currentDay}"


        //반복투두 페이지 이동
        binding.todoRepeatIv.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeRepeatTodoFragment)
        }

        //마이페이지 페이지 이동
        binding.todoMyIv.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragHome_to_fragMy)
        }


        //날짜 텍스트 클릭 시 -> bottomsheetdialog 연결하기
        binding.todoDateLayout.setOnClickListener{
            Log.d("date", "bottomsheetdialog up")
            val todoMenuBottomSheet = TodoDateBottomSheetDialog(viewModel)
            if (todoMenuBottomSheet != null) {
                todoMenuBottomSheet.show(childFragmentManager, todoMenuBottomSheet.tag)
            }
        }

        binding.categoryIv.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeCategoryFragment)
        }


        // 클릭 시 텍스트 변환 -> bottomsheetdialog에서 처리할 부분

//        binding.calendarviewHome.setOnDateChangeListener { view, year, month, dayOfMonth ->
//
//            dateCalendar.set(year, month, dayOfMonth)
//            var calendarDay = findDayOfWeek(year, month, dayOfMonth, dateCalendar)
//            binding.tvHomeCalendar.text = "${month + 1}월 ${dayOfMonth}일 ${calendarDay}"
//            calendarLayout.isGone = true
//            binding.tvHomeSentence.text = homeMent(calendarDay)
//            viewModel.changeDate(year, (month +1), dayOfMonth, "home")
//            //db 투두 데이터 전체 삭제 후 해당 날짜의 데이터를 서버에서 받아서 db에 저장하고 어댑터에 연결하기
//            CoroutineScope(Dispatchers.IO).launch {
//                CoroutineScope(Dispatchers.IO).async {
//                    viewModel.deleteAllTodo()
//                }.await()
//
//                CoroutineScope(Dispatchers.IO).async {
//                    api.getAllMyTodo(viewModel.userToken, viewModel.homeDate.value.toString()).enqueue(object : Callback<TodoList> {
//                        override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
//                            if(response.isSuccessful){
//                                for(i in response.body()!!.data.TodoList){
//                                    val todoData = TodoEntity(id = i.id, date = i.date, category = i.category.id, todoName = i.todoName, complete = i.complete, repeat = i.repeat, repeatWeek = i.repeatWeek, repeatMonth = i.repeatMonth, endRepeatDate = i.endRepeatDate, startRepeatDate = i.startRepeatDate, isAlarm = i.isAlarm, startTodoAtMonday = i.startTodoAtMonday,  endTodoBackSetting = i.endTodoBackSetting, newTodoStartSetting = i.newTodoStartSetting )
//                                    Log.d("todo server", todoData.toString())
//                                    viewModel.createTodo(todoData, null)
//                                }
//                                //닉네임 저장하기
//                                viewModel.userHomeName = response.body()!!.data.nickname
//                            }
//                            else {
//                                Log.d("todo안드 잘못", "서버 연결 실패")
//                            }
//                        }
//
//                        override fun onFailure(call: Call<TodoList>, t: Throwable) {
//                            Log.d("todo서버 연결 오류", "서버 연결 실패")
//                        }
//
//                    })
//                }
//
//                //투두 새로 서버 에서 읽어오기
//
//            }
//        }

        viewModel.homeDate.observe(viewLifecycleOwner, Observer {
            Log.d("date변경", binding.todoDateTv.text.toString())
        })

        //시작요일 변경 -> bottomsheetdialog에서 처리할 부분
//        viewModel.startDay.observe(viewLifecycleOwner, Observer{
//            Log.d("startday", "데이터 변경 감지 ${viewModel.startDay.value}")
//            binding.calendarviewHome.firstDayOfWeek = viewModel.startDay.value!!
//        })


        // 시스템 뒤로가기
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                CalendarViewModel.setPopupTwo(requireContext(),"종료하시겠습니까?",requireView(),0)
                return@OnKeyListener true
            }
            false
        })
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
            "금요일" -> "주말을 위해 조금만 더 화이팅!"
            "토요일" -> "주말을 알차게!"
            else -> "일주일의 마지막도 파이팅!"
        }
        return homeMent
    }

}