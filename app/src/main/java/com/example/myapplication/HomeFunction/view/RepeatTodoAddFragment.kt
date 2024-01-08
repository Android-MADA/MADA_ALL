package com.example.myapplication.HomeFunction.view

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.HomeFunction.HomeBackCustomDialog
import com.example.myapplication.HomeFunction.HomeCustomDialogListener
import com.example.myapplication.HomeFunction.Model.PatchRequestTodo
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.Model.TodoList
import com.example.myapplication.HomeFunction.adapter.repeatTodo.RepeatTypeAdapter
import com.example.myapplication.HomeFunction.adapter.repeatTodo.RepeatWeeklyAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRepeatTodoAddBinding
import com.example.myapplication.db.entity.RepeatEntity
import com.example.myapplication.db.entity.TodoEntity
import com.example.myapplication.hideBottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoField

class RepeatTodoAddFragment : Fragment(), HomeCustomDialogListener {

    lateinit var binding: FragmentRepeatTodoAddBinding
    private var bottomFlag = true
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var backDialog: HomeBackCustomDialog

    /*반복 종류 rv 설정*/
    val typeList = arrayListOf<String>("매일", "매주", "매월")
    val dayList = arrayListOf<String>("월", "화", "수", "목", "금", "토", "일")
    val dateList = arrayListOf<String>(" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 ", " 9 ", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")
    val typeAdapter = RepeatTypeAdapter(typeList)
    val weekAdapter = RepeatWeeklyAdapter(dayList)
    val monthAdapter = RepeatWeeklyAdapter(dateList)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback : OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                customBackDialog()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_repeat_todo_add, container, false)
        hideBottomNavigation(bottomFlag, activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
        val _argsArrayEdit = requireArguments().getStringArrayList("keyEdit")

//        0 : todoId,
//        1 : id,
//        2 : date,
//        3 : category,
//        4 : todoName,
//        5 : repeat,
//        6 : repeatWeek,
//        7 : reepatMonth,
//        8 : startDay,
//        9 : endDay


        binding.edtHomeCategoryName.setText(_argsArrayEdit!![4])

            if(_argsArrayEdit!![5] == "DAY"){
                binding.tvRepeatRepeat.text = "매일"
                //반복 종류 설정 코드 추가 예정
                binding.tvHomeRepeatStartday.text = _argsArrayEdit!![8]
                binding.tvHomeRepeatEndday.text = _argsArrayEdit!![9]
            }


        binding.ivHomeRepeatAddBack.setOnClickListener {
            customBackDialog()
            //Navigation.findNavController(view).navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)
        }

        binding.tvHomeRepeatStartday.setOnClickListener {
            //시작일 캘린더 visibility 처리 예정
        }

        binding.tvHomeRepeatEndday.setOnClickListener {
            //종료일 캘린더 visibility 처리 예정
        }




        /**
         * 반복 종류 선택 클릭 리스너
        **/
        binding.tvRepeatRepeat.setOnClickListener {
            if(binding.repeatLayout.isVisible){
                binding.repeatLayout.isGone = true
            }
            else{
                binding.repeatLayout.isVisible = true
            }
        }

        val typeRv = binding.repeatTypeRv
        typeAdapter.setItemClickListener(object : RepeatTypeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                if(typeAdapter.dataSet[position] == "매일"){
                    binding.tvRepeatRepeat.text = "매일"
                    binding.repeatMonthlyLayout.isGone = true
                    binding.repeatWeeklyRv.isGone = true
                }
                else if(typeAdapter.dataSet[position] == "매주"){
                    binding.tvRepeatRepeat.text = "매주"
                    binding.repeatWeeklyRv.isVisible = true
                    binding.repeatMonthlyLayout.isGone = true
                }
                else {
                    binding.tvRepeatRepeat.text = "매월"
                    binding.repeatWeeklyRv.isGone = true
                    binding.repeatMonthlyLayout.isVisible = true
                }

            }

        })
        typeRv.adapter = typeAdapter
        typeRv.layoutManager = GridLayoutManager(this.activity, 3)

        weekAdapter.setItemClickListener(object : RepeatWeeklyAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                var selectedDay = weekAdapter.dataSet[position]
                binding.tvRepeatRepeat.text = "매주 ${selectedDay}요일"
            }

        })
        val weekRv = binding.repeatWeeklyRv
        weekRv.adapter = weekAdapter
        weekRv.layoutManager = GridLayoutManager(this.activity, 7)


        monthAdapter.setItemClickListener(object : RepeatWeeklyAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                var selectedDay = monthAdapter.dataSet[position]
                binding.tvRepeatRepeat.text = "매월 ${selectedDay} 일"
                binding.repeatMonthlyLastDay.setBackgroundResource(R.drawable.background_10_strokex)
                binding.repeatMonthlyLastDay.background.setTint(Color.parseColor("#F5F5F5"))
                binding.repeatMonthlyLastDay.setTextColor(Color.parseColor("#000000"))
            }

        })
        val monthRv = binding.repeatMonthlyRv
        monthRv.adapter = monthAdapter
        monthRv.layoutManager = GridLayoutManager(this.activity, 7)

        binding.repeatMonthlyLastDay.setOnClickListener {
            monthAdapter.selectedDay = "마지막 날"
            binding.tvRepeatRepeat.text = "매월 마지막 ㅍ"
            binding.repeatMonthlyLastDay.setBackgroundResource(R.drawable.background_10_strokex)
            binding.repeatMonthlyLastDay.background.setTint(Color.parseColor("#486DA3"))
            binding.repeatMonthlyLastDay.setTextColor(Color.parseColor("#ffffff"))
            monthRv.adapter = monthAdapter
            monthRv.layoutManager = GridLayoutManager(this.activity, 7)
        }




        /**
         * 서버 연결
         */

        binding.btnHomeRepeatAddSave.setOnClickListener {
            if(binding.edtHomeCategoryName.text.isBlank()){
                Toast.makeText(this.requireActivity(), "TODO 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                    if(binding.tvHomeRepeatStartday.text == "없음" || binding.tvHomeRepeatEndday.text == "없음"){
                        Toast.makeText(this.requireActivity(), "시작일과 종료일을 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val repeatString = findRepeat(binding.tvRepeatRepeat.text.toString())
                        val startDay = binding.tvHomeRepeatStartday.text.toString()
                        val endDay = binding.tvHomeRepeatEndday.text.toString()
                        //val data = PatchRequestTodo(binding.edtHomeCategoryName.text.toString(), repeatString, null, null, startDay, endDay, complete = false)
                        //Log.d("repeat patch", data.toString())
                        //viewModel.patchTodo(_argsArrayEdit!![0].toInt(), data, _argsArrayEdit!![7].toInt(), _argsArrayEdit!![8].toInt(), view)
                        //db에 반복투두 수정 저장
                        CoroutineScope(Dispatchers.IO).launch {

                            val updateData = RepeatEntity(_argsArrayEdit[0].toInt(), _argsArrayEdit[1].toInt(), _argsArrayEdit[2], _argsArrayEdit[3].toInt(), binding.edtHomeCategoryName.text.toString(), repeatString, null, null, startDay, endDay)
                            val patchData = PatchRequestTodo(binding.edtHomeCategoryName.text.toString(), repeat = repeatString, repeatWeek = null, repeatMonth = null, startRepeatDate = startDay, endRepeatDate = endDay, complete = false, date = _argsArrayEdit[2])
                            viewModel.updateRepeatTodo(updateData)
                            //서버 연결 patch
                            api.editTodo(viewModel!!.userToken, _argsArrayEdit[1].toInt(), patchData).enqueue(object : Callback<Void>{
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    if(response.isSuccessful){
                                        Log.d("editRTodo", "success")
                                        viewModel!!.deleteAllTodo()
                                        //투두 새로 서버 에서 읽어오기
                                        api.getAllMyTodo(viewModel!!.userToken, viewModel!!.homeDate.value.toString()).enqueue(object : Callback<TodoList> {
                                            override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                                                if(response.isSuccessful){
                                                    for(i in response.body()!!.data.TodoList){
                                                        val todoData = TodoEntity(id = i.id, date = i.date, category = i.category.id, todoName = i.todoName, complete = i.complete, repeat = i.repeat, repeatWeek = i.repeatWeek, repeatMonth = i.repeatMonth, endRepeatDate = i.endRepeatDate, startRepeatDate = i.startRepeatDate, isAlarm = i.isAlarm, startTodoAtMonday = i.startTodoAtMonday,  endTodoBackSetting = i.endTodoBackSetting, newTodoStartSetting = i.newTodoStartSetting )
                                                        Log.d("todo server", todoData.toString())
                                                        viewModel!!.createTodo(todoData, null)
                                                    }
                                                    //닉네임 저장하기
                                                    viewModel!!.userHomeName = response.body()!!.data.nickname
                                                }
                                                else {
                                                    Log.d("todo안드 잘못", "서버 연결 실패")
                                                }
                                            }

                                            override fun onFailure(call: Call<TodoList>, t: Throwable) {
                                                Log.d("todo서버 연결 오류", "서버 연결 실패")
                                            }

                                        })
                                    } else {
                                        Log.d("editRTodo", "And fail")

                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Log.d("editRTodo", "server fail")
                                }

                            })
                            withContext(Dispatchers.Main){
                                Navigation.findNavController(view).navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)
                            }
                        }

                    }

            }
        }

    }

    private fun changeDate(year : Int, month : Int, dayOfWeek : Int) : LocalDate {
        var monthString : String = month.toString()
        var dayString = dayOfWeek.toString()

        if(month == 1 || month == 2 || month == 3 || month == 4 || month == 5 || month == 6 || month == 7 || month == 8 || month == 9){
            monthString = "0${month}"
        }
        if(dayOfWeek == 1 || dayOfWeek == 2 || dayOfWeek == 3 || dayOfWeek == 4 || dayOfWeek == 5 || dayOfWeek == 6 || dayOfWeek == 7 || dayOfWeek == 8 || dayOfWeek == 9){
            dayString = "0${dayOfWeek}"
        }

        return LocalDate.parse("${year}-${monthString}-${dayString}")
    }

    private fun findRepeat(repeat : String) : String{
        val repeatString  = when(repeat){
            "반복 안함" -> "N"
            "매일" -> "DAY"
            else -> "N"
        }
        return repeatString
    }

    fun findNull(day : String) : String?{
        val repeatDay = when(day){
            "시작일" -> null
            "종료일" -> null
            else -> day
        }
        return repeatDay
    }

    private fun customBackDialog() {
        backDialog = HomeBackCustomDialog(requireActivity(), this)
        backDialog.show()
    }

    // 커스텀 다이얼로그에서 버튼 클릭 시
    override fun onYesButtonClicked(dialog: Dialog, flag: String) {
            Navigation.findNavController(requireView()).navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)
        dialog.dismiss()
    }

    override fun onNoButtonClicked(dialog: Dialog) {
        dialog.dismiss()
    }


}