package com.mada.reapp.CalenderFuntion

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
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
import com.mada.reapp.CalenderFuntion.Model.AndroidCalendarData
import com.mada.reapp.CalenderFuntion.Model.CalendarData
import com.mada.reapp.CalenderFuntion.Model.CalendarDataEdit
import com.mada.reapp.CalenderFuntion.Model.CalendarViewModel
import com.mada.reapp.R
import com.mada.reapp.databinding.CalendarAddDdayBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.util.Calendar

class CalendarAddDdayFragment : Fragment() {
    private lateinit var calendar: Calendar
    lateinit var binding: CalendarAddDdayBinding
    private val CalendarViewModel : CalendarViewModel by activityViewModels()

    lateinit var ScheduleNum : TextView
    lateinit var initSchedule : String
    lateinit var calData : AndroidCalendarData

    var curColor ="#F46D85"
    var curId : Int = -1
    var toggleDay = false
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
            //binding.textMemo.setText(calData.memo)
            curColor = calData.color
            curId = calData.id
            binding.layoutColorSelector.visibility = View.GONE
            binding.addBtn.text ="삭제"
            binding.timePicker.visibility = View.GONE
            toggleDay = true
        } else {
            val today = arguments?.getString("today")?: "2023-06-01"
            ScheduleNum.text = today
        }

        binding.nextScheldule.text = CalendarViewModel.convertToDateKoreanFormatDday(ScheduleNum.text.toString())
        binding.textDday.setTextColor(Color.parseColor(curColor))
        binding.titleLayout.setBackgroundColor(Color.parseColor(curColor))
        binding.calendarColor.setColorFilter(Color.parseColor(curColor), PorterDuff.Mode.SRC_IN)
        var remainDday = CalendarViewModel.daysRemainingToDate(ScheduleNum.text.toString()).toString()
        if(remainDday.toInt() <=0) remainDday = "DAY"
        binding.textDday.text = "D-${remainDday}"
        initSchedule = ScheduleNum.text.toString()
        val colorSelectors = listOf(
            binding.calendarColor1 to "#ED3024",
            binding.calendarColor2 to "#F65F55",
            binding.calendarColor3 to "#FD8415",
            binding.calendarColor4 to "#FEBD16",
            binding.calendarColor5 to "#FBA1B1",
            binding.calendarColor6 to "#F46D85",
            binding.calendarColor7 to "#D087F2",
            binding.calendarColor8 to "#A516BC",
            binding.calendarColor9 to "#89A9D9",
            binding.calendarColor10 to "#269CB1",
            binding.calendarColor11 to "#3C67A7",
            binding.calendarColor12 to "#405059",
            binding.calendarColor13 to "#C0D979",
            binding.calendarColor14 to "#8FBC10",
            binding.calendarColor15 to "#107E3D",
            binding.calendarColor16 to "#0E4122",
        )
        for ((colorView, colorResource) in colorSelectors) {
            colorView.setOnClickListener {
                binding.calendarColor.setColorFilter(Color.parseColor(colorResource), PorterDuff.Mode.SRC_IN)
                binding.titleLayout.setBackgroundColor(Color.parseColor(colorResource))
                binding.textDday.setTextColor(Color.parseColor(colorResource))
                curColor = colorResource
                toggleLayout(false, binding.layoutColorSelector)
            }
        }
        binding.calendarColor.setOnClickListener {
            if(curId==-1) binding.addBtn.text ="등록"
            else binding.addBtn.text = "수정"

            if(binding.layoutColorSelector.visibility == View.GONE)
                toggleLayout(true,binding.layoutColorSelector)
        }
        binding.textTitle.setOnClickListener {
            if(curId==-1) binding.addBtn.text ="등록"
            else binding.addBtn.text = "수정"
        }
        val ticker1 = binding.numberPicker1
        val ticker2 = binding.numberPicker2
        val ticker3 = binding .numberPicker3
        ticker1.value = ScheduleNum.text.toString().substring(0,4).toInt()
        ticker2.value = ScheduleNum.text.toString().substring(5,7).toInt()
        ticker3.value = ScheduleNum.text.toString().substring(8,10).toInt()

        binding.nextScheldule.setOnClickListener {
            if(curId==-1) binding.addBtn.text ="등록"
            else binding.addBtn.text = "수정"
            toggleLayout(toggleDay,binding.timePicker)
            if(toggleDay) toggleDay = false
            else toggleDay = true
        }

        ticker1.setOnValueChangedListener { picker, oldVal, newVal ->
            ScheduleNum.text = String.format("%04d", newVal) +ScheduleNum.text.toString().substring(4)
            binding.nextScheldule.text = CalendarViewModel.convertToDateKoreanFormatDday(ScheduleNum.text.toString())
            var remainDday = CalendarViewModel.daysRemainingToDate(ScheduleNum.text.toString()).toString()
            if(remainDday.toInt() <=0) remainDday = "DAY"
            binding.textDday.text = "D-${remainDday}"
        }
        ticker2.setOnValueChangedListener { picker, oldVal, newVal ->
            if(newVal ==1 ||newVal ==3 ||newVal ==5 ||newVal ==7 ||newVal ==8 ||newVal ==10 ||newVal ==12)
                ticker3.maxValue=31
            else if(newVal == 2) ticker3.maxValue=29
            else if(newVal ==3 ||newVal ==4 ||newVal ==6 ||newVal ==9 ||newVal ==11) ticker3.maxValue=30

            ScheduleNum.text = ScheduleNum.text.toString().substring(0,5) +String.format("%02d", newVal) + ScheduleNum.text.toString().substring(7)
            binding.nextScheldule.text = CalendarViewModel.convertToDateKoreanFormatDday(ScheduleNum.text.toString())
            var remainDday = CalendarViewModel.daysRemainingToDate(ScheduleNum.text.toString()).toString()
            if(remainDday.toInt() <=0) remainDday = "DAY"
            binding.textDday.text = "D-${remainDday}"
        }
        ticker3.setOnValueChangedListener { picker, oldVal, newVal ->
            ScheduleNum.text = ScheduleNum.text.toString().substring(0,8) + String.format("%02d", newVal)
            binding.nextScheldule.text = CalendarViewModel.convertToDateKoreanFormatDday(ScheduleNum.text.toString())
            var remainDday = CalendarViewModel.daysRemainingToDate(ScheduleNum.text.toString()).toString()
            if(remainDday.toInt() <=0) remainDday = "DAY"
            binding.textDday.text = "D-${remainDday}"
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            //뭐가 있다면
            if(binding.textTitle.text.toString() != "" || ScheduleNum.text.toString() != (initSchedule) || binding.textMemo.text.toString()!="") {
                CalendarViewModel.setPopupTwo(requireContext(),"수정하지 않고 나가시겠습니까?",view,R.id.action_calendarAddDday_to_calendarDday)
            } else {
                Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_calendarDday)
            }
        }
        binding.addBtn.setOnClickListener {
            if(binding.textDday.text.toString()=="D-DAY") {
                CalendarViewModel.setPopupOne(requireContext(),"올바른 날짜를 입력해 주십시오",view)
            } else if(binding.textTitle.text.toString() == "") {
                CalendarViewModel.setPopupOne(requireContext(),"제목을 입력해 주십시오",view)
            }   else {
                val buffering = CalendarViewModel.setPopupBuffering(requireContext())
                if(binding.addBtn.text.toString()=="수정") {
                    CalendarViewModel.editCalendar(
                        CalendarDataEdit( curId,binding.textTitle.text.toString(),ScheduleNum.text.toString(),ScheduleNum.text.toString(),
                        curColor,"N",0,"10:00:00", "11:00:00","Y",binding.textMemo.text.toString() ),curId) { result ->
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
                                    "10:00:00","11:00:00",curColor,"N","Y",binding.textTitle.text.toString(),
                                    -1,false,binding.textMemo.text.toString(),"CAL",curId,0))
                                CalendarViewModel.ddayArrayList.sortBy { CalendarViewModel.daysRemainingToDate(it.endDate) }

                                delDday(initSchedule.substring(0,4).toInt(),initSchedule.substring(5,7).toInt(),curId)
                                addDday(ScheduleNum.text.substring(0,4).toInt(),ScheduleNum.text.substring(5,7).toInt(),curId
                                )
                                buffering.dismiss()
                                Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_calendarDday)
                            }
                            2 -> {
                                buffering.dismiss()
                                Toast.makeText(context, "추가 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else  if(binding.addBtn.text.toString()=="등록") {
                    CalendarViewModel.addCalendar( CalendarData( binding.textTitle.text.toString(),ScheduleNum.text.toString(),ScheduleNum.text.toString(),
                        curColor,"N",0,"10:00:00", "11:00:00","Y",binding.textMemo.text.toString() ) ) { result ->
                        when (result) {
                            1 -> {
                                val tmpId = CalendarViewModel.addId        //서버로 부터 얻은 아이디
                                Toast.makeText(context, "추가 성공", Toast.LENGTH_SHORT).show()
                                CalendarViewModel.ddayArrayList.add(AndroidCalendarData(ScheduleNum.text.toString(),ScheduleNum.text.toString(),ScheduleNum.text.toString(),
                                    "10:00:00","11:00:00",curColor,"N","Y",binding.textTitle.text.toString(), -1,false,
                                    binding.textMemo.text.toString(),"CAL",tmpId,0))
                                CalendarViewModel.ddayArrayList.sortBy { CalendarViewModel.daysRemainingToDate(it.endDate) }

                                addDday(ScheduleNum.text.substring(0,4).toInt(),ScheduleNum.text.substring(5,7).toInt(),tmpId)

                                buffering.dismiss()
                                Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_calendarDday)
                            }
                            2 -> {
                                buffering.dismiss()
                                Toast.makeText(context, "추가 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    CalendarViewModel.deleteCalendar(calData.id) { result ->
                        when (result) {
                            1 -> {
                                Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show()
                                for(data in CalendarViewModel.ddayArrayList) {
                                    if(data.id==curId) {
                                        CalendarViewModel.ddayArrayList.remove(data)
                                        break
                                    }
                                }
                                delDday(initSchedule.substring(0,4).toInt(),initSchedule.substring(5,7).toInt(),curId)
                                buffering.dismiss()
                                Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_calendarDday)
                            }
                            2 -> {
                                buffering.dismiss()
                                Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()
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
    private fun addDday(Year : Int, Month : Int, tmpId :Int) {

        if(CalendarViewModel.hashMapArrayCal.get("${Year}-${Month}")==null){
            CalendarViewModel.hashMapArrayCal.put("${Year}-${Month}",ArrayList<AndroidCalendarData>())
        }

        CalendarViewModel.hashMapArrayCal.get("${Year}-${Month}")?.add(AndroidCalendarData(ScheduleNum.text.toString(),ScheduleNum.text.toString(),ScheduleNum.text.toString(),
            "10:00:00","11:00:00",curColor,"N","Y",binding.textTitle.text.toString(),
            -1,false,binding.textMemo.text.toString(),"CAL",tmpId,0))

    }
    private fun delDday(Year : Int, Month : Int, tmpId :Int) {

        if(CalendarViewModel.hashMapArrayCal.get("${Year}-${Month}")!=null){
            for(data in CalendarViewModel.hashMapArrayCal.get("${Year}-${Month}")!!) {
                if(data.id == tmpId) {
                    CalendarViewModel.hashMapArrayCal.get("${Year}-${Month}")!!.remove(data)
                    break
                }
            }
        }

    }
}