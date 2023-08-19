package com.example.myapplication.HomeFunction.view

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
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.myapplication.HomeFunction.Model.PatchRequestTodo
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

        var argsArrayEdit = requireArguments().getStringArrayList("keyEdit")
        //0 id
        //1 todoName
        //2 repeat
        //3 repeatWeek
        //4 repeatMonth
        //5 repeatStartDay
        //6 repeatEndDay
        //7 category position
        //8 todoPosition

            binding.edtHomeCategoryName.setText(argsArrayEdit!![1])
            if(argsArrayEdit!![2] == "N"){
                binding.tvRepeatRepeat.text = "반복 안함"
                binding.tvRepeatNo.setBackgroundResource(R.drawable.home_repeat_selected_background)
                binding.tvRepeatEveryday.setBackgroundResource(R.drawable.home_reapeat_unselected_background)
            }
            else if(argsArrayEdit!![2] == "day"){
                binding.tvRepeatRepeat.text = "매일"
                binding.tvRepeatEveryday.setBackgroundResource(R.drawable.home_repeat_selected_background)
                binding.tvRepeatNo.setBackgroundResource(R.drawable.home_reapeat_unselected_background)
                binding.tvHomeRepeatStart.text = argsArrayEdit!![5]
                binding.tvHomeRepeatEnd.text = argsArrayEdit!![6]
            }


        binding.ivHomeRepeatAddBack.setOnClickListener {
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
            //시작일, 종료일 gone?
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
            else {
                if(binding.tvRepeatRepeat.text != "반복 안함"){
                    if(binding.tvHomeRepeatStartday.text == "없음" || binding.tvHomeRepeatEndday.text == "없음"){
                        Toast.makeText(this.requireActivity(), "시작일과 종료일을 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val repeatString = findRepeat(binding.tvRepeatRepeat.text.toString())
                        val startDay = findNull(binding.tvHomeRepeatStart.text.toString())
                        val endDay = findNull(binding.tvHomeRepeatEnd.text.toString())
                        val data = PatchRequestTodo(binding.edtHomeCategoryName.text.toString(), repeatString, null, null, startDay, endDay)

                        viewModel.patchTodo(argsArrayEdit!![0].toInt(), data, argsArrayEdit!![7].toInt(), argsArrayEdit!![8].toInt(), view)
                    }
                }
                else if (binding.tvRepeatRepeat.text == "반복 안함") {
                    val repeatString = "N"
                    val startDay = null
                    val endDay = null
                    val data = PatchRequestTodo(binding.edtHomeCategoryName.text.toString(), repeatString, null, null, null, null)

                    viewModel.patchTodo(argsArrayEdit!![0].toInt(), data, argsArrayEdit!![7].toInt(), argsArrayEdit!![8].toInt(), view)
                }
                else {
                    val repeatString = findRepeat(binding.tvRepeatRepeat.text.toString())
                    val startDay = findNull(binding.tvHomeRepeatStart.text.toString())
                    val endDay = findNull(binding.tvHomeRepeatEnd.text.toString())
                    val data = PatchRequestTodo(binding.edtHomeCategoryName.text.toString(), repeatString, null, null, startDay, endDay)

                    viewModel.patchTodo(argsArrayEdit!![0].toInt(), data, argsArrayEdit!![7].toInt(), argsArrayEdit!![8].toInt(), view)
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
            "매일" -> "day"
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


}