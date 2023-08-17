package com.example.myapplication.HomeFunction.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRepeatTodoAddBinding
import com.example.myapplication.hideBottomNavigation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoField

class RepeatTodoAddFragment : Fragment() {

    lateinit var binding: FragmentRepeatTodoAddBinding
    private var bottomFlag = true
    private val viewModel: HomeViewModel by activityViewModels()

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

        val argsArrayAdd = requireArguments().getStringArrayList("keyAdd")
        val argsArrayEdit = requireArguments().getStringArrayList("keyEdit")

        if(argsArrayAdd != null){
            binding.btnHomeRepeatAddSave.text = "등록"
        }
        else{
            binding.btnHomeRepeatAddSave.text = "삭제"
            binding.edtHomeCategoryName.setText(argsArrayEdit!![0])
            if(argsArrayEdit!![1] == "N"){
                binding.tvRepeatRepeat.text = "반복 안함"
                binding.tvRepeatNo.setBackgroundResource(R.drawable.home_repeat_selected_background)
                binding.tvRepeatEveryday.setBackgroundResource(R.drawable.home_reapeat_unselected_background)
            }
            else{
                binding.tvRepeatRepeat.text = "매일"
                binding.tvRepeatEveryday.setBackgroundResource(R.drawable.home_repeat_selected_background)
                binding.tvRepeatNo.setBackgroundResource(R.drawable.home_reapeat_unselected_background)
            }
            //시작일, 종료일 null 확인하고 뿌리기
        }

        //bundle이 넘어오면 등록 -> 삭제, 데이터 뿌리기
        //start, end date 현재날짜로 달력에 뿌리기 -> 일단 text만 변경하기
        binding.ivHomeRepeatAddBack.setOnClickListener {
            if(binding.btnHomeRepeatAddSave.text == "삭제"){
                //수정 사항 서버 전송
                viewModel.editTodo(argsArrayEdit!![2].toInt(), argsArrayEdit!![3].toInt(), binding.edtHomeCategoryName.text.toString(), null, null, "N")

            }
            Navigation.findNavController(view).navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)
        }

        binding.tvHomeRepeatStartday.setOnClickListener {
            if(binding.layoutCalendarStart.isVisible){
                binding.layoutCalendarStart.isGone = true
            }
            else {
                binding.layoutCalendarEnd.isGone = true
                binding.layoutCalendarStart.isVisible = true
            }
        }

        binding.tvHomeRepeatEndday.setOnClickListener {
            if(binding.layoutCalendarEnd.isVisible){
                binding.layoutCalendarEnd.isGone = true
            }
            else {
                binding.layoutCalendarStart.isGone = true
                binding.layoutCalendarEnd.isVisible = true
            }
        }

        binding.tvRepeatRepeat.setOnClickListener {
            if(binding.layoutRepeat.isVisible){
                binding.layoutRepeat.isGone = true
            }
            else {
                binding.layoutRepeat.isVisible = true
            }
        }

        binding.tvRepeatNo.setOnClickListener {
            binding.tvRepeatNo.setBackgroundResource(R.drawable.home_repeat_selected_background)
            binding.tvRepeatEveryday.setBackgroundResource(R.drawable.home_reapeat_unselected_background)
            binding.tvRepeatRepeat.text = binding.tvRepeatNo.text
        }

        binding.tvRepeatEveryday.setOnClickListener {
            binding.tvRepeatEveryday.setBackgroundResource(R.drawable.home_repeat_selected_background)
            binding.tvRepeatNo.setBackgroundResource(R.drawable.home_reapeat_unselected_background)
            binding.tvRepeatRepeat.text = binding.tvRepeatEveryday.text
        }

        var todayDate = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()

        binding.calendarviewRepeatStart.setDate(todayDate)
        binding.calendarviewRepeatEnd.setDate(todayDate)

        binding.calendarviewRepeatStart.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var selectedStart = changeDate(year, (month +1), dayOfMonth)
            binding.tvHomeRepeatStartday.text = selectedStart.toString()
            binding.layoutCalendarStart.isGone = true
        }

        binding.calendarviewRepeatEnd.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var selectedEnd = changeDate(year, (month +1), dayOfMonth)
            binding.tvHomeRepeatEndday.text = selectedEnd.toString()
            binding.layoutCalendarEnd.isGone = true
        }



        binding.btnHomeRepeatAddSave.setOnClickListener {
            if(binding.edtHomeCategoryName.text.isBlank()){
                Toast.makeText(this.requireActivity(), "TODO 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
//            if(binding.tvRepeatRepeat.text != "반복 안함"){
//                if(binding.tvHomeRepeatStartday.text == "없음" || binding.tvHomeRepeatStartday.text == "없음"){
//                    Toast.makeText(this.requireActivity(), "시작일과 종료일을 모두 입력해주세요", Toast.LENGTH_SHORT).show()
//                }
//            }
            else {
                if(binding.btnHomeRepeatAddSave.text == "삭제"){
                    //삭제 서버 전송
                    //id = argsEdit으로 조합
                    viewModel.deleteTodo(argsArrayEdit!![2].toInt(), argsArrayEdit!![3].toInt())
                }
                else {
                    //add 서버 전송
                    //category = argsAdd로 조합
                    var todo = Todo(viewModel.categoryList!!.value!![argsArrayEdit!![2].toInt()], binding.edtHomeCategoryName.text.toString(), false, "N")
                    //var todo = Todo( LocalDate.now(), viewModel.categoryList!!.value!![position], edt.text.toString(), false, "N", null, null, null)
                    viewModel.addTodo(argsArrayEdit!![2].toInt(), todo, viewModel.todoTopFlag.value!!)
                }
                Navigation.findNavController(view).navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)
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

}