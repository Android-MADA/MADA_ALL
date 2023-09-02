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
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarAddDdayFragment : Fragment() {
    private lateinit var calendar: Calendar
    lateinit var binding: CalendarAddDdayBinding
    private val CalendarViewModel : CalendarViewModel by activityViewModels()

    lateinit var nextScheduleNum : TextView
    lateinit var initnextSchedule : String
    lateinit var calData : AndroidCalendarData

    var curColor ="#89A9D9"
    var curEdit = false
    var curId : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nextScheduleNum = TextView(requireContext())
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
            nextScheduleNum.text = calData.endDate
            binding.textMemo.setText(calData.memo)
            curColor = calData.color
            curId = calData.id
        } else {
            val today = arguments?.getString("today")?: "2023-06-01"
            nextScheduleNum.text = today
        }

        binding.nextScheldule.text = CalendarViewModel.convertToDateKoreanFormat(nextScheduleNum.text.toString())
        binding.calendarColor.setColorFilter(Color.parseColor(curColor), PorterDuff.Mode.SRC_IN)
        binding.textDday.text = "D - ${CalendarViewModel.daysRemainingToDate(nextScheduleNum.text.toString())}"
        initnextSchedule = nextScheduleNum.text.toString()
        if(curEdit) {
            binding.addBtn.text ="수정"
        }
        binding.calendarColor1.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.main), PorterDuff.Mode.SRC_IN)
            curColor = "#89A9D9"
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor2.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub3), PorterDuff.Mode.SRC_IN)
            curColor = "#F0768C"
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor3.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub1), PorterDuff.Mode.SRC_IN)
            curColor = "#F8D141"
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor.setOnClickListener {
            toggleLayout(true,binding.layoutColorSelector)
        }

        binding.nextScheldule.setOnClickListener {
            binding.nextScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.preScheldule.setBackgroundColor(Color.TRANSPARENT)
            binding.cal.visibility= View.VISIBLE
            val calendarAdapter = CalendarSliderSmallAdapter(this,binding.textYearMonth,binding.calendar2,binding.nextScheldule,nextScheduleNum ,binding.cal)
            binding.calendar2.adapter = calendarAdapter
            binding.calendar2.setCurrentItem(CalendarSliderAdapter.START_POSITION, false)
        }

        binding.preBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(binding.calendar2.currentItem-1, true)
        }
        binding.nextBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(binding.calendar2.currentItem+1, true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            //뭐가 있다면
            if(binding.textTitle.text.toString() != "" || nextScheduleNum.text.toString() != (initnextSchedule) || binding.textMemo.text.toString()!="") {
                CalendarViewModel.setPopupTwo(requireContext(),"수정하지 않고 나가시겠습니까?",view,R.id.action_calendarAddDday_to_fragHome)
            } else {
                Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_fragHome)
            }

        }
        binding.addBtn.setOnClickListener {
            if(binding.textDday.text.toString()=="D - 0") {
                CalendarViewModel.setPopupOne(requireContext(),"올바른 날짜를 입력해 주십시오",view)
            }  else {
                if(binding.addBtn.text.toString()=="수정") {
                    CalendarViewModel.editCalendar(CalendarData( binding.textTitle.text.toString(),nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),
                        curColor,"No","Y",binding.textMemo.text.toString(), "10:00:00","11:00:00","" ),curId) { result ->
                        when (result) {
                            1 -> {
                                Toast.makeText(context, "추가 성공", Toast.LENGTH_SHORT).show()
                                for(data in CalendarViewModel.ddayArrayList) {
                                    if(data.id==curId) {
                                        CalendarViewModel.ddayArrayList.remove(data)
                                        break
                                    }
                                }
                                CalendarViewModel.ddayArrayList.add(AndroidCalendarData(nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),
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
                    CalendarViewModel.addCalendar( CalendarData( binding.textTitle.text.toString(),nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),
                        curColor,"No","Y",binding.textMemo.text.toString(), "10:00:00","11:00:00","" ) ) { result ->
                        when (result) {
                            1 -> {
                                val tmpId = 1532        //서버로 부터 얻은 아이디
                                Toast.makeText(context, "추가 성공", Toast.LENGTH_SHORT).show()
                                CalendarViewModel.ddayArrayList.add(AndroidCalendarData(nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),
                                    "10:00:00","11:00:00",curColor,"No","Y",binding.textTitle.toString(), -1,false,
                                    binding.textMemo.text.toString(),"CAL",tmpId,""))
                                CalendarViewModel.ddayArrayList.sortBy { CalendarViewModel.daysRemainingToDate(it.endDate) }
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