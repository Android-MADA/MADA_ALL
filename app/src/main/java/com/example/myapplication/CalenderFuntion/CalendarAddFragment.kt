package com.example.myapplication.CalenderFuntion

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarAddBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalendarAddFragment : Fragment() {
    private lateinit var calendar: Calendar
    lateinit var binding: CalendarAddBinding
    val weekdays = arrayOf("일" ,"월", "화", "수", "목", "금", "토")
    var scheduleSelect = 0
    var dayString : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalendarAddBinding.inflate(layoutInflater)
        hideBootomNavigation(true)
        val bundle = arguments
        val title = bundle?.getString("title") ?: ""
        val preSchedule = bundle?.getString("preSchedule") ?: "2023-6-1"
        val nextSchedule = bundle?.getString("nextSchedule") ?: "2023-6-1"
        val preClock = bundle?.getString("preClock") ?: "  오전 10:00  "
        val nextClock = bundle?.getString("nextClock") ?: "  오전 11:00  "
        val cycle = bundle?.getString("cycle") ?: "반복 안함"
        val memo = bundle?.getString("memo") ?: ""
        val color = bundle?.getString("color") ?:"#89A9D9"

        binding.textTitle.setText(title)
        binding.preScheldule.text = convertToDateKoreanFormat(preSchedule)
        binding.nextScheldule.text = convertToDateKoreanFormat(nextSchedule)
        binding.preScheldule2.text = preClock
        binding.nextScheldule2.text = nextClock
        binding.cyclebtn.text = cycle
        binding.textMemo.setText(memo)
        binding.calendarColor.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)
        dayString =binding.preScheldule.text.toString()

        binding.textDday.visibility= View.GONE
        binding.cal.visibility= View.GONE
        binding.timePicker.visibility = View.GONE



        CalendarUtil.selectedDate = LocalDate.now()
        calendar = Calendar.getInstance()

        binding.calendarColor1.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub5), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelectors.visibility = View.GONE
        }
        binding.calendarColor2.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.main), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelectors.visibility = View.GONE
        }
        binding.calendarColor3.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.point_main), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelectors.visibility = View.GONE
        }
        binding.calendarColor4.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub1), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelectors.visibility = View.GONE
        }
        binding.calendarColor5.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub6), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelectors.visibility = View.GONE
        }
        binding.calendarColor6.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub3), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelectors.visibility = View.GONE
        }
        binding.calendarColor7.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub5), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelectors.visibility = View.GONE
        }
        binding.calendarColor8.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub6), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelectors.visibility = View.GONE
        }
        binding.calendarColor9.setOnClickListener {
            binding.calendarColor.setColorFilter(Color.parseColor("#F5EED1"), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelectors.visibility = View.GONE
        }
        binding.calendarColor.setOnClickListener {
            binding.layoutColorSelectors.visibility = View.VISIBLE
        }

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.textDday.visibility= View.VISIBLE
                binding.clockAndCycle.visibility = View.GONE
                binding.layoutColorSelector2.visibility = View.VISIBLE
                binding.layoutColorSelector1.visibility = View.GONE
            } else {
                binding.textDday.visibility= View.GONE
                binding.clockAndCycle.visibility = View.VISIBLE

                binding.layoutColorSelector2.visibility = View.GONE
                binding.layoutColorSelector1.visibility = View.VISIBLE
            }

        }
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
        binding.switch2.setOnCheckedChangeListener { p0, isChecked ->
            if(isChecked) {
                binding.colck.visibility =View.GONE
            } else {
                binding.colck.visibility =View.VISIBLE
            }

        }
        val schedules = arrayOf<TextView>(
            binding.preScheldule2,
            binding.nextScheldule2
        )

        val data1 = arrayOf("오전", "오후")
        val data2 = arrayOf("00","05","10","15","20","25","30","35","40","45","50","55")
        val regex = """\s*(오전|오후)\s+(\d{1,2}):(\d{2})\s*""".toRegex()
        val ticker1 = binding.numberPicker1
        val ticker2 = binding.numberPicker2
        val ticker3 = binding.numberPicker3
        ticker1.minValue = 0
        ticker1.maxValue = 1
        ticker1.displayedValues  = data1
        ticker3.minValue = 0
        ticker3.maxValue = 11
        ticker3.displayedValues  = data2
        ticker1.setOnValueChangedListener { picker, oldVal, newVal ->
            if(newVal==0) {
                schedules[scheduleSelect].text = schedules[scheduleSelect].text.toString().replace("오후","오전")
            } else {
                schedules[scheduleSelect].text = schedules[scheduleSelect].text.toString().replace("오전","오후")
            }
        }
        ticker2.setOnValueChangedListener { picker, oldVal, newVal ->
            val matchResult = regex.find(schedules[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                schedules[scheduleSelect].text = "  "+ampm+" "+newVal+":"+minute+"  "
            } else {
            }
        }
        ticker3.setOnValueChangedListener { picker, oldVal, newVal ->
            val matchResult = regex.find(schedules[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                schedules[scheduleSelect].text = "  "+ampm+" "+hour+":"+data2[newVal-1]+"  "
            } else {
            }
        }
        schedules[0].setOnClickListener {
            schedules[0].setBackgroundResource(R.drawable.calendar_prebackground)
            schedules[1].setBackgroundColor(Color.TRANSPARENT)
            binding.timePicker.isVisible = true
            scheduleSelect=0
            val matchResult = regex.find(schedules[0].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured

                if(ampm=="오전") {
                    ticker1.value=0
                }
                else {
                    ticker1.value=1
                }

                ticker2.value=hour.toInt()
                ticker3.value=minute.toInt()/5
            } else {
                ticker1.value=0
                ticker2.value=10
                ticker3.value=0
            }
        }
        schedules[1].setOnClickListener {
            schedules[1].setBackgroundResource(R.drawable.calendar_prebackground)
            schedules[0].setBackgroundColor(Color.TRANSPARENT)
            binding.timePicker.isVisible = true
            scheduleSelect=1
            val matchResult = regex.find(schedules[1].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                if(ampm=="오전")
                    ticker1.value=0
                else
                    ticker1.value=1
                ticker2.value=hour.toInt()
                ticker3.value=minute.toInt()/5
            } else {
                ticker1.value=0
                ticker2.value=11
                ticker3.value=0
            }

        }
        val chip1 = binding.chip1
        val chip2 = binding.chip2
        val chip3 = binding.chip3
        val chip4 = binding.chip4
        val chip5 = binding.chip5
        binding.cyclebtn.setOnClickListener {
            binding.chipGroup.visibility = View.VISIBLE
        }
        chip1.setOnClickListener {
            binding.cyclebtn.text = "반복 안함"
            chip2.isChecked = false
            chip3.isChecked = false
            chip4.isChecked = false
            chip5.isChecked = false
        }
        chip2.setOnClickListener {
            binding.cyclebtn.text = "매일"
            chip1.isChecked = false
            chip3.isChecked = false
            chip4.isChecked = false
            chip5.isChecked = false
        }
        chip3.setOnClickListener {
            binding.cyclebtn.text = "매주"
            chip2.isChecked = false
            chip1.isChecked = false
            chip4.isChecked = false
            chip5.isChecked = false
        }
        chip4.setOnClickListener {
            binding.cyclebtn.text = "매월"
            chip2.isChecked = false
            chip3.isChecked = false
            chip1.isChecked = false
            chip5.isChecked = false
        }
        chip5.setOnClickListener {
            binding.cyclebtn.text = "매년"
            chip2.isChecked = false
            chip3.isChecked = false
            chip4.isChecked = false
            chip1.isChecked = false
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            //뭐가 있다면
            if(binding.textTitle.text.toString() != "" || binding.checkBox.isChecked || binding.preScheldule.text.toString() != dayString ||
                binding.nextScheldule.text.toString() != dayString || binding.preScheldule2.text.toString() != "  오전 10:00  " ||
                binding.nextScheldule2.text.toString() != "  오전 11:00  " || binding.cyclebtn.text.toString() != "반복 안함" ||
                binding.textMemo.text.toString()!="") {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_add_popup, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()

                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

                val deviceWidth = displayMetrics.widthPixels

                val desiredRatio = 0.8f
                val desiredWidth = (deviceWidth * desiredRatio).toInt()
                mBuilder?.window?.setLayout(desiredWidth, WindowManager.LayoutParams.WRAP_CONTENT)

                val display = requireActivity().windowManager.defaultDisplay
                mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
                    mBuilder.dismiss()
                })
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                    Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
                    mBuilder.dismiss()
                })
            } else {
                Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
            }

        }
        binding.button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
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
}