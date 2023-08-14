package com.example.myapplication.CalenderFuntion

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Fragment.FragCalendar
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarAddDdayBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarAddDdayFragment : Fragment() {
    private lateinit var calendar: Calendar
    lateinit var binding: CalendarAddDdayBinding
    val weekdays = arrayOf("일" ,"월", "화", "수", "목", "금", "토")
    var dayString : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalendarAddDdayBinding.inflate(layoutInflater)
        hideBootomNavigation(true)
        val ddayValue = arguments?.getBoolean("dday", false)
        val today: LocalDate = LocalDate.now()
        val dayOfWeekKorean = today.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
        dayString = "${today.year}-${today.monthValue}-${today.dayOfMonth}"

        val num = arguments?.getInt("num")
        if(num!=null) {
            binding.textTitle.setText(arguments?.getString("title"))
            binding.preScheldule.text  = convertToDateKoreanFormat(arguments?.getString("startDate") ?: dayString)
            binding.nextScheldule.text = convertToDateKoreanFormat(arguments?.getString("endDate") ?: dayString)
            binding.textDday.text = "D - "+(arguments?.getInt("dday") ?:"0").toString()
            binding.calendarColor.setColorFilter(Color.parseColor(arguments?.getString("color") ?: "#E1E9F5"), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelector.visibility = View.GONE
            binding.textMemo.setText(arguments?.getString("memo"))
        }
        binding.cal.visibility= View.GONE

        CalendarUtil.selectedDate = LocalDate.now()
        calendar = Calendar.getInstance()


        binding.calendarColor1.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub5), PorterDuff.Mode.SRC_IN)
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor2.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub6), PorterDuff.Mode.SRC_IN)
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor3.setOnClickListener {
            binding.calendarColor.setColorFilter(Color.parseColor("#F5EED1"), PorterDuff.Mode.SRC_IN)
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor.setOnClickListener {
            toggleLayout(true,binding.layoutColorSelector)
        }
        /*
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.textDday.visibility= View.VISIBLE
            } else {
                binding.textDday.visibility= View.GONE
            }

        }*/
        binding.preBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1)
            calendar.add(Calendar.MONTH, -1)
            setMonthView(-1)
        }
        binding.nextBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1)
            calendar.add(Calendar.MONTH, 1)
            setMonthView(1)
        }

        binding.preScheldule.setOnClickListener {
            binding.preScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.nextScheldule.setBackgroundColor(Color.TRANSPARENT)
            binding.cal.visibility= View.VISIBLE
            setMonthView(-1)
        }
        binding.nextScheldule.setOnClickListener {
            binding.nextScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.preScheldule.setBackgroundColor(Color.TRANSPARENT)
            binding.cal.visibility= View.VISIBLE
            setMonthView(1)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_fragCalendar)
        }
        binding.addBtn.setOnClickListener {
            //데이터 추가 코드
            Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_fragCalendar)
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

    private fun setMonthView(preNext : Int) {
        var formatter = DateTimeFormatter.ofPattern("yyyy년 M월")
        binding.textCalendar.text = CalendarUtil.selectedDate.format(formatter)
        val dayList = dayInMonthArray()
        val adapter: CalendarSmallAdapter = if (preNext == -1) {
            CalendarSmallAdapter(dayList, binding.cal, binding.preScheldule,binding.textBlank)
        } else {
            CalendarSmallAdapter(dayList, binding.cal, binding.nextScheldule,binding.textDday)
        }
        var manager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(),7)
        binding.calendar2.layoutManager = manager
        binding.calendar2.adapter = adapter
    }
    private fun dayInMonthArray() : ArrayList<Date> {
        var dayList = ArrayList<Date>()
        var monthCalendar = calendar.clone() as Calendar
        monthCalendar[Calendar.DAY_OF_MONTH] = 1        //달의 첫 번째 날짜
        var firstDayofMonth = monthCalendar[Calendar.DAY_OF_WEEK]-1

        monthCalendar.add(Calendar.DAY_OF_MONTH,-firstDayofMonth)
        while(dayList.size<42) {
            dayList.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)
        }
        return dayList
    }
    private fun yearMonthFromDate(date : LocalDate) :String{
        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        return date.format(formatter)
    }
    fun convertToDateKoreanFormat(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val outputFormat = SimpleDateFormat("  M월 d일 (E)  ", Locale("ko", "KR"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
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