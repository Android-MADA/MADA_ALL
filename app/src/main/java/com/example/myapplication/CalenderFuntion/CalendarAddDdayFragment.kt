package com.example.myapplication.CalenderFuntion

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.example.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.CalenderFuntion.Small.CalendarSliderSmallAdapter
import com.example.myapplication.CalenderFuntion.Small.CalendarSmallAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarAddDdayBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarAddDdayFragment : Fragment() {
    private lateinit var calendar: Calendar
    lateinit var binding: CalendarAddDdayBinding
    private val CalendarViewModel : CalendarViewModel by activityViewModels()

    lateinit var ScheduleNum : TextView
    lateinit var initnextSchedule : String
    lateinit var calData : AndroidCalendarData

    var curColor ="#486DA3"
    var curEdit = false
    var curId : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScheduleNum = TextView(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalendarAddDdayBinding.inflate(layoutInflater)
        hideBootomNavigation(true)
        val today: LocalDate = LocalDate.now()

        if(arguments?.getSerializable("calData")!=null) {
            calData = arguments?.getSerializable("calData") as AndroidCalendarData
            binding.textTitle.setText(calData.title)
            ScheduleNum.text = calData.endDate
            binding.textMemo.setText(calData.memo)
            curColor = calData.color
            curId = calData.id
        } else {
            val today = arguments?.getString("today")?: "2023-06-01"
            ScheduleNum.text = today
        }

        binding.nextScheldule.text = CalendarViewModel.convertToDateKoreanFormat(ScheduleNum.text.toString())
        binding.calendarColor.setColorFilter(Color.parseColor(curColor), PorterDuff.Mode.SRC_IN)
        var remainDday = CalendarViewModel.daysRemainingToDate(ScheduleNum.text.toString()).toString()
        if(remainDday.toInt() <=0) remainDday = "DAY"
        binding.textDday.text = "D - ${remainDday}"
        initnextSchedule = ScheduleNum.text.toString()
        if(curEdit) {
            binding.addBtn.text ="수정"
        }
        binding.calendarColor1.setOnClickListener {
            binding.calendarColor.setColorFilter(Color.parseColor("#486DA3"), PorterDuff.Mode.SRC_IN)
            curColor = "#486DA3"
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor2.setOnClickListener {
            binding.calendarColor.setColorFilter(Color.parseColor("#2AA1B7"), PorterDuff.Mode.SRC_IN)
            curColor = "#2AA1B7"
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor3.setOnClickListener {
            binding.calendarColor.setColorFilter(Color.parseColor("#F8D141"), PorterDuff.Mode.SRC_IN)
            curColor = "#F8D141"
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor4.setOnClickListener {
            binding.calendarColor.setColorFilter(Color.parseColor("#F0768C"), PorterDuff.Mode.SRC_IN)
            curColor = "#F0768C"
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor.setOnClickListener {
            toggleLayout(true,binding.layoutColorSelector)
        }

        val ticker1 = binding.numberPicker1
        val ticker2 = binding.numberPicker2
        val ticker3 = binding .numberPicker3
        ticker1.value = ScheduleNum.text.toString().substring(0,4).toInt()
        ticker2.value = ScheduleNum.text.toString().substring(5,7).toInt()
        ticker3.value = ScheduleNum.text.toString().substring(9).toInt()

        ticker1.setOnValueChangedListener { picker, oldVal, newVal ->
            ScheduleNum.text = String.format("%04d", newVal) +ScheduleNum.text.toString().substring(4)
            binding.nextScheldule.text = CalendarViewModel.convertToDateKoreanFormat(ScheduleNum.text.toString())
            var remainDday = CalendarViewModel.daysRemainingToDate(ScheduleNum.text.toString()).toString()
            if(remainDday.toInt() <=0) remainDday = "DAY"
            binding.textDday.text = "D - ${remainDday}"
        }
        ticker2.setOnValueChangedListener { picker, oldVal, newVal ->
            ScheduleNum.text = ScheduleNum.text.toString().substring(0,5) +String.format("%02d", newVal) + ScheduleNum.text.toString().substring(7)
            var remainDday = CalendarViewModel.daysRemainingToDate(ScheduleNum.text.toString()).toString()
            if(remainDday.toInt() <=0) remainDday = "DAY"
            binding.textDday.text = "D - ${remainDday}"
        }
        ticker3.setOnValueChangedListener { picker, oldVal, newVal ->
            ScheduleNum.text = ScheduleNum.text.toString().substring(0,8) + String.format("%02d", newVal)
            var remainDday = CalendarViewModel.daysRemainingToDate(ScheduleNum.text.toString()).toString()
            if(remainDday.toInt() <=0) remainDday = "DAY"
            binding.textDday.text = "D - ${remainDday}"
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            //뭐가 있다면
            if(binding.textTitle.text.toString() != "" || ScheduleNum.text.toString() != (initnextSchedule) || binding.textMemo.text.toString()!="") {
                CalendarViewModel.setPopupTwo(requireContext(),"수정하지 않고 나가시겠습니까?",view,R.id.action_calendarAddDday_to_fragHome)
            } else {
                Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_fragHome)
            }

        }
        binding.addBtn.setOnClickListener {
            if(binding.textDday.text.toString()=="D - DAY") {
                CalendarViewModel.setPopupOne(requireContext(),"올바른 날짜를 입력해 주십시오",view)
            } else if(binding.textTitle.text.toString() == "") {
                CalendarViewModel.setPopupOne(requireContext(),"제목을ㅡ 입력해 주십시오",view)
            }   else {
                if(binding.addBtn.text.toString()=="수정") {
                    CalendarViewModel.editCalendar(CalendarData( binding.textTitle.text.toString(),ScheduleNum.text.toString(),ScheduleNum.text.toString(),
                        curColor,"No","Y",binding.textMemo.text.toString(), "10:00:00","11:00:00","" ),curId) { result ->
                        when (result) {
                            1 -> {
                                Toast.makeText(context, "수정 성공", Toast.LENGTH_SHORT).show()
                                for(data in CalendarViewModel.ddayArrayList) {
                                    if(data.id==curId) {
                                        CalendarViewModel.ddayArrayList.remove(data)
                                        break
                                    }
                                }
                                CalendarViewModel.ddayArrayList.add(AndroidCalendarData(ScheduleNum.text.toString(),ScheduleNum.text.toString(),ScheduleNum.text.toString(),
                                    "10:00:00","11:00:00",curColor,"No","Y",binding.textTitle.text.toString(),
                                    -1,false,binding.textMemo.text.toString(),"CAL",curId,""))
                                CalendarViewModel.ddayArrayList.sortBy { CalendarViewModel.daysRemainingToDate(it.endDate) }

                                val yearMonth = YearMonth.from(LocalDate.parse(initnextSchedule, DateTimeFormatter.ISO_DATE))
                                val formattedYearMonth = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-M"))

                                val tmpArrayList = CalendarViewModel.hashMapArrayCal.get(formattedYearMonth)
                                for(data in tmpArrayList!!) {
                                    if(data.id==curId) {
                                        tmpArrayList.remove(data)
                                        break
                                    }
                                }
                                tmpArrayList.add(AndroidCalendarData(ScheduleNum.text.toString(),ScheduleNum.text.toString(),ScheduleNum.text.toString(),
                                    "10:00:00","11:00:00",curColor,"No","Y",binding.textTitle.text.toString(),
                                    -1,false,binding.textMemo.text.toString(),"CAL",curId,""))

                                Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_fragHome)
                            }
                            2 -> {
                                Toast.makeText(context, "추가 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    CalendarViewModel.addCalendar( CalendarData( binding.textTitle.text.toString(),ScheduleNum.text.toString(),ScheduleNum.text.toString(),
                        curColor,"No","Y",binding.textMemo.text.toString(), "10:00:00","11:00:00","" ) ) { result ->
                        when (result) {
                            1 -> {
                                val tmpId = 1532        //서버로 부터 얻은 아이디
                                Toast.makeText(context, "추가 성공", Toast.LENGTH_SHORT).show()
                                CalendarViewModel.ddayArrayList.add(AndroidCalendarData(ScheduleNum.text.toString(),ScheduleNum.text.toString(),ScheduleNum.text.toString(),
                                    "10:00:00","11:00:00",curColor,"No","Y",binding.textTitle.text.toString(), -1,false,
                                    binding.textMemo.text.toString(),"CAL",tmpId,""))
                                CalendarViewModel.ddayArrayList.sortBy { CalendarViewModel.daysRemainingToDate(it.endDate) }

                                val yearMonth = YearMonth.from(LocalDate.parse(ScheduleNum.text.toString(), DateTimeFormatter.ISO_DATE))
                                val formattedYearMonth = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-M"))
                                if(CalendarViewModel.hashMapArrayCal.get(formattedYearMonth)==null){
                                    CalendarViewModel.hashMapArrayCal.put(formattedYearMonth,ArrayList<AndroidCalendarData>())
                                }
                                CalendarViewModel.hashMapArrayCal.get(formattedYearMonth)?.add(AndroidCalendarData(ScheduleNum.text.toString(),ScheduleNum.text.toString(),ScheduleNum.text.toString(),
                                    "10:00:00","11:00:00",curColor,"No","Y",binding.textTitle.text.toString(),
                                    -1,false,binding.textMemo.text.toString(),"CAL",tmpId,""))

                                Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_fragHome)
                            }
                            2 -> {
                                Toast.makeText(context, "추가 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        hideBootomNavigation(false)
    }
    fun hideBootomNavigation(bool : Boolean){
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if(bool){
            bottomNavigation?.isGone = true
        }
        else {
            bottomNavigation?.isVisible = true
        }
    }

    private fun toggleLayout(isExpanded: Boolean, layoutExpand: LinearLayout): Boolean {
        if (isExpanded) {
            ToggleAnimation.expand(layoutExpand)
        } else {
            ToggleAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }
}