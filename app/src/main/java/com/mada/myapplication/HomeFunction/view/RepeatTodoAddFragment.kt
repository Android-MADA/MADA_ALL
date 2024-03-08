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
import androidx.navigation.NavAction
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.mada.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.mada.myapplication.CalenderFuntion.Small.CalendarSliderSmallAdapter
import com.mada.myapplication.CalenderFuntion.ToggleAnimation
import com.mada.myapplication.HomeFunction.HomeBackCustomDialog
import com.mada.myapplication.HomeFunction.HomeCustomDialogListener
import com.mada.myapplication.HomeFunction.Model.PatchRequestTodo
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
import com.mada.myapplication.getRepeatTodo
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
    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
    private lateinit var mAdView : AdView
    private var modeFlag = "add"
    private var todoId = 0


    /*반복 종류 rv 설정*/
    val typeList = arrayListOf<String>("매일", "매주", "매월")
    val dayList = arrayListOf<String>("월", "화", "수", "목", "금", "토", "일")
    val dateList = arrayListOf<String>("1", "2", "3", "4", "5", "6", "7", "8", "9","10", "11", "12", "13", "14", "15", "16", "17", "18", "19","20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")
    val typeAdapter = RepeatTypeAdapter(typeList)
    val weekAdapter = RepeatWeeklyAdapter(dayList)
    val monthAdapter = RepeatWeeklyAdapter(dateList)
    private var selectedType = "DAY"
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

                if(modeFlag == "add"){
                    customBackDialog()
                }
                else {
                    if(binding.edtHomeCategoryName.text.isBlank()){
                        calendarViewModel.setPopupOne(requireContext(), "제목을 입력해주세요.", view!!)
                    }
                    else if(viewModel.repeatEndDate!!.isBefore(viewModel.repeatStartDate) || viewModel.repeatEndDate!!.isEqual(viewModel.repeatStartDate)){
                        calendarViewModel.setPopupOne(requireContext(), "가능한 날짜를 선택해주세요.", view!!)
                    }
                    else{
                        val patchDate = PatchRequestTodo(todoName = binding.edtHomeCategoryName.text.toString(), date = null, complete = false, repeat = selectedType, repeatInfo = inputRepeatInfo, startRepeatDate = viewModel.repeatStartDate.toString(), endRepeatDate = viewModel.repeatEndDate.toString())
                        Log.d("repeat Edit send", patchDate.toString())
                        api.editTodo(viewModel.userToken, todoId, patchDate).enqueue(object :Callback<Void>{
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if(response.isSuccessful){
                                    Log.d("repeat Edit", "sucess")

                                }
                                else{
                                    Log.d("repeat Edit", "Android fail")
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.d("repeat Edit", "server fail")
                            }

                        })
                    }
                    Navigation.findNavController(view!!).navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)
                }

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

        val _argsArrayEdit = requireArguments().getStringArrayList("keyEdit")
        val _argsArrayAdd = requireArguments().getStringArrayList("keyAdd")
        viewModel.resetRepeatDate()

        /**
         * 1. repeatTodo Add
         */

        if(!_argsArrayAdd.isNullOrEmpty()){
            Log.d("add check", "running")

            modeFlag = "add"
            selectedType = "DAY"
            startDay.text = viewModel.repeatStartDate.toString()
            endDay.text = viewModel.repeatEndDate.toString()

            //set add btn
            binding.btnHomeRepeatAddSave.isVisible = true

            //set startDay
            binding.tvHomeRepeatStartday.text = "${viewModel.repeatStartDate!!.year}년 ${viewModel.repeatStartDate!!.monthValue}월 ${viewModel.repeatStartDate!!.dayOfMonth}일"

            //set endDay
            binding.tvHomeRepeatEndday.text = "${viewModel.repeatEndDate!!.year}년 ${viewModel.repeatEndDate!!.monthValue}월 ${viewModel.repeatEndDate!!.dayOfMonth}일"

            //set repeatType
            binding.tvRepeatRepeat.text = "매일"
            typeAdapter.selectedType = "매일"
            weekAdapter.selectedDay = selectedDay
            monthAdapter.selectedDay = selectedDate

        }

        /**
         * 2. repeatTodo Edit
         */

        /**
         * [0] : id
         * [1] : date
         * [2] : category
         * [3] : todoName
         * [4] : repeat
         * [5] : repeatInfo
         * [6] : startDate
         * [7] : endDate
         */

        if(!_argsArrayEdit.isNullOrEmpty()){

            todoId = _argsArrayEdit[0].toInt()
            modeFlag = "edit"
            selectedType = _argsArrayEdit[4]
            inputRepeatInfo = _argsArrayEdit[5].toInt()
            viewModel.repeatStartDate = LocalDate.parse(_argsArrayEdit[6])
            viewModel.repeatEndDate = LocalDate.parse(_argsArrayEdit[7])
            startDay.text = viewModel.repeatStartDate.toString()
            endDay.text = viewModel.repeatEndDate.toString()

            //set add btn
            binding.btnHomeRepeatAddSave.isGone = true

            //set repeatTodo title
            binding.edtHomeCategoryName.setText(_argsArrayEdit!![3])

            //set startday
            binding.tvHomeRepeatStartday.text = "${viewModel.repeatStartDate!!.year}년 ${viewModel.repeatStartDate!!.monthValue}월 ${viewModel.repeatStartDate!!.dayOfMonth}일"

            //set endday
            binding.tvHomeRepeatEndday.text = "${viewModel.repeatEndDate!!.year}년 ${viewModel.repeatEndDate!!.monthValue}월 ${viewModel.repeatEndDate!!.dayOfMonth}일"

            //set repeat type
            if(_argsArrayEdit!![4] == "DAY"){
                binding.tvRepeatRepeat.text = "매일"
                typeAdapter.selectedType = "매일"
                weekAdapter.selectedDay = selectedDay
                monthAdapter.selectedDay = selectedDate
            }
            else if(_argsArrayEdit!![4] == "WEEK"){
                binding.tvRepeatRepeat.text = "매주 ${findDay(_argsArrayEdit[5])}요일"
                typeAdapter.selectedType = "매주"
                selectedDay = findDay(_argsArrayEdit[5])
                weekAdapter.selectedDay = selectedDay
                monthAdapter.selectedDay = selectedDate
                binding.repeatWeeklyRv.visibility = View.VISIBLE

            }
            else{
                binding.tvRepeatRepeat.text = "매월 ${findDate(_argsArrayEdit[5].toInt())}"
                typeAdapter.selectedType = "매월"
                selectedDate = _argsArrayEdit[5].toString()
                weekAdapter.selectedDay = selectedDay
                monthAdapter.selectedDay = selectedDate
                binding.repeatMonthlyLayout.visibility = View.VISIBLE
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
                if(binding.edtHomeCategoryName.text.isBlank()){
                    calendarViewModel.setPopupOne(requireContext(), "제목을 입력해주세요.", view)
                }
                else if(viewModel.repeatEndDate!!.isBefore(viewModel.repeatStartDate) || viewModel.repeatEndDate!!.isEqual(viewModel.repeatStartDate)){
                    calendarViewModel.setPopupOne(requireContext(), "가능한 날짜를 선택해주세요.", view)
                }
                else{
                    val patchDate = PatchRequestTodo(todoName = binding.edtHomeCategoryName.text.toString(), date = null, complete = false, repeat = selectedType, repeatInfo = inputRepeatInfo, startRepeatDate = viewModel.repeatStartDate.toString(), endRepeatDate = viewModel.repeatEndDate.toString())
                    Log.d("repeat Edit send", patchDate.toString())
                    api.editTodo(viewModel.userToken, todoId, patchDate).enqueue(object :Callback<Void>{
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if(response.isSuccessful){
                                Log.d("repeat Edit send", "sucess")
                                Navigation.findNavController(view).navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)

                            }
                            else{
                                Log.d("repeat Edit", "Android fail")
                                Navigation.findNavController(view).navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)

                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.d("repeat Edit", "server fail")
                            Navigation.findNavController(view).navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)

                        }

                    })
                }
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
                selectedDay = findDay(selectedRvDay + 1)
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
                    selectedDay = "월"
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
         * 4. 서버 연결
         */

        /**
         * 1. 공백 검사
         * 2. 달력 검사(종료일이 시작일보다 앞선 경우 확인
         * 3. 수정 시 이전까지 작성했던 투두들은 모두 삭제됩니다. 문구가 있어야 될 것 같은데?
         */
        binding.btnHomeRepeatAddSave.setOnClickListener {
            if(binding.edtHomeCategoryName.text.isBlank()){
                calendarViewModel.setPopupOne(requireContext(), "제목을 입력해주세요.", view)
            }
            else if(viewModel.repeatEndDate!!.isBefore(viewModel.repeatStartDate) || viewModel.repeatEndDate!!.isEqual(viewModel.repeatStartDate)){
                calendarViewModel.setPopupOne(requireContext(), "가능한 날짜를 선택해주세요.", view)
            }
            else{
                val data = PostRequestTodo(category = PostRequestTodoCateId(_argsArrayAdd!![0].toInt()), complete = false, repeat = selectedType, repeatInfo = inputRepeatInfo, date = viewModel.homeDate.value.toString(), endRepeatDate = viewModel.repeatEndDate.toString(), startRepeatDate = viewModel.repeatStartDate.toString(), todoName = binding.edtHomeCategoryName.text.toString())
                api.addTodo(viewModel.userToken, data).enqueue(object : Callback<PostResponseTodo>{
                    override fun onResponse(
                        call: Call<PostResponseTodo>,
                        response: Response<PostResponseTodo>
                    ) {
                        if(response.isSuccessful){
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

        /**
         * 5. google ad
         */
        //구글 플레이스토어 광고
        MobileAds.initialize(this.requireContext()) {}
        mAdView = binding.adViewRepeat
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

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
            "1" -> "월"
            "2" -> "화"
            "3" -> "수"
            "4" -> "목"
            "5" -> "금"
            "6" -> "토"
            else -> "일"
        }
        return korString
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