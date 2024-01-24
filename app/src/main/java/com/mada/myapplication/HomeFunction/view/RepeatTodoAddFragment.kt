package com.mada.myapplication.HomeFunction.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.mada.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.mada.myapplication.CalenderFuntion.Small.CalendarSliderSmallAdapter
import com.mada.myapplication.CalenderFuntion.ToggleAnimation
import com.mada.myapplication.HomeFunction.HomeBackCustomDialog
import com.mada.myapplication.HomeFunction.HomeCustomDialogListener
import com.mada.myapplication.HomeFunction.Model.PostRequestTodo
import com.mada.myapplication.HomeFunction.Model.PostRequestTodoCateId
import com.mada.myapplication.HomeFunction.Model.PostResponseTodo
import com.mada.myapplication.HomeFunction.adapter.repeatTodo.RepeatTypeAdapter
import com.mada.myapplication.HomeFunction.adapter.repeatTodo.RepeatWeeklyAdapter
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.R
import com.mada.myapplication.databinding.FragmentRepeatTodoAddBinding
import com.mada.myapplication.db.entity.RepeatEntity
import com.mada.myapplication.getHomeTodo
import com.mada.myapplication.hideBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class RepeatTodoAddFragment : Fragment(), HomeCustomDialogListener {

    lateinit var binding: FragmentRepeatTodoAddBinding
    private var bottomFlag = true
    private val viewModel: HomeViewModel by activityViewModels()
    private val calendarViewModel : CalendarViewModel by activityViewModels()
    private lateinit var backDialog: HomeBackCustomDialog
    private var modeFlag = "add"

    /*반복 종류 rv 설정*/
    val typeList = arrayListOf<String>("매일", "매주", "매월")
    val dayList = arrayListOf<String>("월", "화", "수", "목", "금", "토", "일")
    val dateList = arrayListOf<String>("1", "2", "3", "4", "5", "6", "7", "8", "9","10", "11", "12", "13", "14", "15", "16", "17", "18", "19","20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")
    val typeAdapter = RepeatTypeAdapter(typeList)
    val weekAdapter = RepeatWeeklyAdapter(dayList)
    val monthAdapter = RepeatWeeklyAdapter(dateList)
    private var selectedType = "DAY"
    private var selectedStartDay : LocalDate? = null
    private var selectedEndDay : LocalDate? = null
    private var selectedDay = "월"
    private var selectedDate = "1"
    private var inputRepeatInfo : Int? = null

    //시작 날짜
    lateinit var startDay : TextView
    lateinit var endDay : TextView
    //오늘 달
    val todayMonth = LocalDate.now()

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
        startDay = TextView(requireContext())
        endDay = TextView(requireContext())
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
        val _argsArrayAdd = requireArguments().getStringArrayList("keyAdd")



        /**
         * 1. repeatTodo Add
         */

        if(!_argsArrayAdd.isNullOrEmpty()){

            modeFlag = "add"
            selectedType = "DAY"
            //selectedStartDay = changeDate(viewModel.homeDate.value!!.year, viewModel.homeDate.value!!.monthValue, viewModel.homeDate.value!!.dayOfMonth)
            //selectedEndDay = changeDate((viewModel.homeDate.value!!.year+1), viewModel.homeDate.value!!.monthValue, viewModel.homeDate.value!!.dayOfMonth)
            selectedStartDay = viewModel.repeatStartDate
            selectedEndDay = viewModel.repeatEndDate
            startDay.text = selectedStartDay.toString()
            endDay.text = selectedEndDay.toString()

            //set add btn
            binding.btnHomeRepeatAddSave.isVisible = true

            //set startDay
            binding.tvHomeRepeatStartday.text = "${selectedStartDay!!.year}년 ${selectedStartDay!!.monthValue}월 ${selectedStartDay!!.dayOfMonth}일"

            //set endDay
            binding.tvHomeRepeatEndday.text = "${selectedEndDay!!.year}년 ${selectedEndDay!!.monthValue}월 ${selectedEndDay!!.dayOfMonth}일"

            //set repeatType
            binding.tvRepeatRepeat.text = "매일"
            typeAdapter.selectedType = "매일"
            weekAdapter.selectedDay = selectedDay
            monthAdapter.selectedDay = selectedDate

        }

        /**
         * 2. repeatTodo Edit
         */

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

        if(!_argsArrayEdit.isNullOrEmpty()){

            modeFlag = "edit"
            selectedType = _argsArrayEdit[5]
            Log.d("date 확인", _argsArrayEdit[2])
            selectedStartDay = LocalDate.parse(_argsArrayEdit[8])
            selectedEndDay = LocalDate.parse(_argsArrayEdit[9])

            //set add btn
            binding.btnHomeRepeatAddSave.isGone = true

            //set repeatTodo title
            binding.edtHomeCategoryName.setText(_argsArrayEdit!![4])

            //set startday
            binding.tvHomeRepeatStartday.text = "${selectedStartDay!!.year}년 ${selectedStartDay!!.monthValue}월 ${selectedStartDay!!.dayOfMonth}일"

            //set endday
            binding.tvHomeRepeatEndday.text = "${selectedEndDay!!.year}년 ${selectedEndDay!!.monthValue}월 ${selectedEndDay!!.dayOfMonth}일"

            //set repeat type
            if(_argsArrayEdit!![5] == "DAY"){
                binding.tvRepeatRepeat.text = "매일"
                typeAdapter.selectedType = "매일"
            }
            else if(_argsArrayEdit!![5] == "WEEK"){
                binding.tvRepeatRepeat.text = "매주 ${findDay(_argsArrayEdit[6])}"
                typeAdapter.selectedType = "매주"
                selectedDay = findDay(_argsArrayEdit[6])
            }
            else{
                binding.tvRepeatRepeat.text = "매월 ${findDate(_argsArrayEdit[7].toInt())}"
                typeAdapter.selectedType = "매월"
                selectedDate = findRvDate(_argsArrayEdit[7].toInt())
            }


        }

        /**
         * 3. 공통
         */


        /**
         * 3-1 페이지 이동
         */
        binding.ivHomeRepeatAddBack.setOnClickListener {
            if(modeFlag == "add"){
                customBackDialog()
            }
            else {
                //edit 상황
                // 빈칸 확인 -> 수정 내용 저장
            }

        }

        /**
         * 3-2 캘린더 클릭 리스너
         */


        binding.tvHomeRepeatStartday.setOnClickListener {
            //시작일 캘린더 visibility 처리 예정
            binding.calendar2Start.adapter = null
            if(binding.calHomeRepeatStart.visibility== View.GONE)  toggleLayout(true,binding.calHomeRepeatStart)
            else toggleLayout(false,binding.calHomeRepeatStart)
            val calendarAdapter = CalendarSliderSmallAdapter(this,binding.textYearMonthStart,binding.calendar2Start,binding.tvHomeRepeatStartday,startDay ,binding.calHomeRepeatStart,binding.textBlank, repeatFlag = "start", viewModel)
            binding.calendar2Start.adapter = calendarAdapter
            val comparisonResult =  ChronoUnit.MONTHS.between(todayMonth,LocalDate.parse(startDay.text.toString()))
            binding.calendar2Start.setCurrentItem(CalendarSliderAdapter.START_POSITION+comparisonResult.toInt(), false)

        }

        binding.tvHomeRepeatEndday.setOnClickListener {
            //종료일 캘린더 visibility 처리 예정
            binding.calendar2End.adapter = null
            if(binding.calHomeRepeatEnd.visibility== View.GONE)  toggleLayout(true,binding.calHomeRepeatEnd)
            else toggleLayout(false,binding.calHomeRepeatEnd)
            val calendarAdapter = CalendarSliderSmallAdapter(this,binding.textYearMonthEnd,binding.calendar2End,binding.tvHomeRepeatEndday,endDay ,binding.calHomeRepeatEnd,binding.textBlank, repeatFlag = "end", viewModel)
            binding.calendar2End.adapter = calendarAdapter
            val comparisonResult =  ChronoUnit.MONTHS.between(todayMonth,LocalDate.parse(endDay.text.toString()))

            binding.calendar2End.setCurrentItem(CalendarSliderAdapter.START_POSITION+comparisonResult.toInt(), false)
        }


        /**
         * 3-3 반복 종류 선택 클릭 리스너
        **/

        weekAdapter.setItemClickListener(object : RepeatWeeklyAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                var selectedRvDay = weekAdapter.dataSet[position]
                selectedType = "WEEK"
                selectedDay = findDay2(selectedRvDay)
                binding.tvRepeatRepeat.text = "매주 ${selectedRvDay}요일"
                inputRepeatInfo = position + 1
            }

        })
        val weekRv = binding.repeatWeeklyRv
        weekRv.adapter = weekAdapter
        weekRv.layoutManager = GridLayoutManager(this.activity, 7)


        monthAdapter.setItemClickListener(object : RepeatWeeklyAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                var selectedRvDay = monthAdapter.dataSet[position]
                binding.tvRepeatRepeat.text = "매월 ${selectedRvDay} 일"
                binding.repeatMonthlyLastDay.setBackgroundResource(R.drawable.background_10_strokex)
                binding.repeatMonthlyLastDay.background.setTint(Color.parseColor("#F5F5F5"))
                binding.repeatMonthlyLastDay.setTextColor(Color.parseColor("#000000"))
                selectedType = "MONTH"
                selectedDate = selectedRvDay
                inputRepeatInfo = position + 1
            }

        })
        val monthRv = binding.repeatMonthlyRv
        monthRv.adapter = monthAdapter
        monthRv.layoutManager = GridLayoutManager(this.activity, 7)

        binding.repeatMonthlyLastDay.setOnClickListener {
            monthAdapter.selectedDay = "마지막 날"
            binding.tvRepeatRepeat.text = "매월 마지막 날"
            binding.repeatMonthlyLastDay.setBackgroundResource(R.drawable.background_10_strokex)
            binding.repeatMonthlyLastDay.background.setTint(Color.parseColor("#486DA3"))
            binding.repeatMonthlyLastDay.setTextColor(Color.parseColor("#ffffff"))
            monthRv.adapter = monthAdapter
            monthRv.layoutManager = GridLayoutManager(this.activity, 7)
            selectedType = "MONTH"
            selectedDate = "0"
            inputRepeatInfo = 0
        }

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
                    selectedType = "DAY"
                    inputRepeatInfo = null
                }
                else if(typeAdapter.dataSet[position] == "매주"){
                    binding.tvRepeatRepeat.text = "매주 월요일"
                    weekAdapter.selectedDay = "월"
                    weekAdapter.notifyDataSetChanged()
                    selectedType = "WEEK"
                    selectedDay = findDay2("월")
                    inputRepeatInfo = 1
                    binding.repeatWeeklyRv.isVisible = true
                    binding.repeatMonthlyLayout.isGone = true
                }
                else {
                    binding.tvRepeatRepeat.text = "매월 1일"
                    monthAdapter.selectedDay = "1"
                    monthAdapter.notifyDataSetChanged()
                    selectedType = "MONTH"
                    selectedDate = "1"
                    inputRepeatInfo = 1
                    binding.repeatMonthlyLastDay.setBackgroundResource(R.drawable.background_10_strokex)
                    binding.repeatMonthlyLastDay.background.setTint(Color.parseColor("#F5F5F5"))
                    binding.repeatMonthlyLastDay.setTextColor(Color.parseColor("#000000"))
                    binding.repeatWeeklyRv.isGone = true
                    binding.repeatMonthlyLayout.isVisible = true
                }

            }

        })
        typeRv.adapter = typeAdapter
        typeRv.layoutManager = GridLayoutManager(this.activity, 3)






        /**
         * 서버 연결
         */

        /**
         * 1. 공백 검사
         * 2. 달력 검사(종료일이 시작일보다 앞선 경우 확인
         * 3. 수정 시 이전까지 작성했던 투두들은 모두 삭제됩니다. 문구가 있어야 될 것 같은데?
         */
        binding.btnHomeRepeatAddSave.setOnClickListener {
            /**
             * 1. 공백검사
             */
            Log.d("startDate", viewModel.repeatStartDate.toString())
            Log.d("endDate", viewModel.repeatStartDate.toString())
            Log.d("endDate", selectedType)
            Log.d("endDate", inputRepeatInfo.toString())
            if(binding.edtHomeCategoryName.text.isBlank()){
                calendarViewModel.setPopupOne(requireContext(), "제목을 입력해주세요.", view)
            }
            else if(viewModel.repeatEndDate!!.isBefore(viewModel.repeatStartDate) || viewModel.repeatEndDate!!.isEqual(viewModel.repeatStartDate)){
                calendarViewModel.setPopupOne(requireContext(), "가능한 날짜를 선택해주세요.", view)
            }
            else{
                val data = PostRequestTodo(category = PostRequestTodoCateId(_argsArrayAdd!![0].toInt()), complete = false, repeat = selectedType, repeatInfo = inputRepeatInfo, date = viewModel.homeDate.value.toString(), endRepeatDate = selectedEndDay.toString(), startRepeatDate = selectedStartDay.toString(), todoName = binding.edtHomeCategoryName.text.toString())
                Log.d("repeat check", data.toString())
                api.addTodo(viewModel.userToken, data).enqueue(object : Callback<PostResponseTodo>{
                    override fun onResponse(
                        call: Call<PostResponseTodo>,
                        response: Response<PostResponseTodo>
                    ) {
                        if(response.isSuccessful){
                            val repeatData = response.body()!!.data.Todo
                            val inputData = RepeatEntity(id = repeatData.id, date = repeatData.date, repeat = repeatData.repeat, repeatInfo = repeatData.repeatInfo, startRepeatDate = repeatData.startRepeatDate, endRepeatDate = repeatData.endRepeatDate, category = repeatData.category.id, todoName = repeatData.todoName)
                            //repeatTodo db 생성
                            viewModel.createRepeatTodo(inputData)
                            //투두 db 모두 삭제
                            viewModel!!.deleteAllTodo()
                            //todo 새로 받아오기
                            getHomeTodo(api, viewModel, context!!)
                            Navigation.findNavController(view).navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)
                        }
                        else{
                            Log.d("repeat Add", "Android fail")
                        }
                    }

                    override fun onFailure(call: Call<PostResponseTodo>, t: Throwable) {
                        Log.d("repeat Add", "server fail")
                    }

                })
            }

        }

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

    fun findDay(enString : String) : String{
        var korString : String = when(enString){
            "MON" -> "월요일"
            "TUE" -> "화요일"
            "WED" -> "수요일"
            "THU" -> "목요일"
            "FRI" -> "금요일"
            "SAT" -> "토요일"
            else -> "일요일"
        }
        return korString
    }

    fun findDay2(korString: String) : String{
        var enString : String = when(korString){
            "월" -> "MON"
            "화" -> "TUE"
            "수" -> "WED"
            "목" -> "THU"
            "금" -> "FRI"
            "토"-> "SAT"
            else -> "SUN"
        }
        return enString
    }

    fun findDay3(engString : String) : Int{
        var selectedInt : Int = when(engString){
            "MON" -> 1
            "TUE" -> 2
            "WED" -> 3
            "THU" -> 4
            "FRI" -> 5
            "SAT" -> 6
            else -> 7
        }
        return selectedInt
    }

    fun findDate(dateInput : Int) : String {
        var dateOutput =
            if(dateInput in 1..31){
                "${dateInput.toString()}일"
            }
        else{
            "마지막 날"
            }
        return dateOutput
    }

    fun findDate2(inputString: String) : String{
        var resultString =
            if(inputString == " 1 " || inputString == " 2 " || inputString == " 3 " || inputString == " 4 " || inputString == " 5 " || inputString == " 6 " || inputString == " 7 " || inputString == " 8 " || inputString == " 9 "){
                inputString.toCharArray()[1]
            }
        else {
            inputString
            }
        return resultString.toString()
    }

    fun findRvDate(inputInt: Int) : String {
        val resultString = if(inputInt in 1..9){
            " ${inputInt}"
        }
        else{
            inputInt.toString()
        }
        return resultString
    }

    // 레이아웃 자연스럽게 열기 위한 애니메이션 함수
    private fun toggleLayout(isExpanded: Boolean, layoutExpand: LinearLayout): Boolean {
        if (isExpanded &&layoutExpand.visibility == View.GONE) {
            ToggleAnimation.expand(layoutExpand)
        } else if(!isExpanded &&layoutExpand.visibility == View.VISIBLE){
            ToggleAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }


}