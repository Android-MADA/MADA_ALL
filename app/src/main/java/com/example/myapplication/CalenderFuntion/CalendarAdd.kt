package com.example.myapplication.CalenderFuntion

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarAddBinding
import com.google.android.material.chip.Chip
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalendarAdd : AppCompatActivity(), OnItemListener {
    private lateinit var calendar: Calendar
    lateinit var binding: CalendarAddBinding
    val preList: ArrayList<Int> = ArrayList()
    val nextList: ArrayList<Int> = ArrayList()
    val weekdays = arrayOf("일" ,"월", "화", "수", "목", "금", "토")
    var scheduleSelect = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalendarAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preList.add(intent.getIntExtra("Month",0))
        nextList.add(intent.getIntExtra("Month",0))
        preList.add(intent.getIntExtra("Day",0))
        nextList.add(intent.getIntExtra("Day",0))
        preList.add(intent.getIntExtra("Yoil",1))
        nextList.add(intent.getIntExtra("Yoil",1))
        preList.add(0)
        nextList.add(0)

        binding.preScheldule.text = "  "+preList[0].toString()+"월 " + preList[1].toString() +"일 ("+weekdays[preList[2]]+")  "
        binding.nextScheldule.text = "  "+nextList[0].toString()+"월 " + nextList[1].toString() +"일 ("+weekdays[nextList[2]]+")  "

        binding.textDday.visibility= View.GONE
        binding.cal.visibility= View.GONE
        binding.timePicker.visibility = View.GONE

        CalendarUtil.selectedDate = LocalDate.now()
        calendar = Calendar.getInstance()
        setMonthView()
        binding.backButton.setOnClickListener {
            //뭐가 있다면
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.calendar_add_popup, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .create()
            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            mBuilder.show()
            val display = (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
                mBuilder.dismiss()
            })
            mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                onBackPressed()
            })
            //onBackPressed()
        }
        binding.calendarColor1.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub5), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelector.visibility = View.GONE
        }
        binding.calendarColor2.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.main), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelector.visibility = View.GONE
        }
        binding.calendarColor3.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.point_main), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelector.visibility = View.GONE
        }
        binding.calendarColor4.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub1), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelector.visibility = View.GONE
        }
        binding.calendarColor5.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub6), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelector.visibility = View.GONE
        }
        binding.calendarColor6.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub3), PorterDuff.Mode.SRC_IN)
            binding.layoutColorSelector.visibility = View.GONE
        }
        binding.calendarColor.setOnClickListener {
            binding.layoutColorSelector.visibility = View.VISIBLE
        }
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.textDday.visibility= View.VISIBLE
            } else {
                binding.textDday.visibility= View.GONE
            }

        }
        binding.preBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1)
            calendar.add(Calendar.MONTH, -1)
            setMonthView()
        }
        binding.nextBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1)
            calendar.add(Calendar.MONTH, 1)
            setMonthView()
        }

        binding.preScheldule.setOnClickListener {
            binding.preScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.nextScheldule.setBackgroundColor(Color.TRANSPARENT)
            preList[3]=1
            nextList[3]=0
            binding.cal.visibility= View.VISIBLE
        }
        binding.nextScheldule.setOnClickListener {
            binding.nextScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.preScheldule.setBackgroundColor(Color.TRANSPARENT)
            preList[3]=0
            nextList[3]=1
            binding.cal.visibility= View.VISIBLE
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
        schedules[0].setOnClickListener {
            schedules[0].setBackgroundResource(R.drawable.calendar_prebackground)
            schedules[1].setBackgroundColor(Color.TRANSPARENT)
            binding.timePicker.visibility = View.VISIBLE
            scheduleSelect=0
        }
        schedules[1].setOnClickListener {
            schedules[1].setBackgroundResource(R.drawable.calendar_prebackground)
            schedules[0].setBackgroundColor(Color.TRANSPARENT)
            binding.timePicker.visibility = View.VISIBLE
            scheduleSelect=1
        }
        val data1 = arrayOf("오전", "오후")
        val data2 = arrayOf("00","05","10","15","20","25","30","35","40","45","50","55")
        val regex = """\s*(오전|오후)\s+(\d{1,2}):(\d{2})\s*""".toRegex()
        val ticker1 = binding.numberPicker1
        val ticker2 = binding.numberPicker2
        val ticker3 = binding.numberPicker3
        ticker1.minValue = 1
        ticker1.maxValue = 2
        ticker1.displayedValues  = data1
        ticker3.minValue = 1
        ticker3.maxValue = 12
        ticker3.displayedValues  = data2
        ticker1.setOnValueChangedListener { picker, oldVal, newVal ->
            if(newVal==1) {
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
        val chip1 = binding.chip1
        val chip2 = binding.chip2
        val chip3 = binding.chip3
        val chip4 = binding.chip4
        val chip5 = binding.chip5
        chip1.setOnClickListener {
            chip2.isChecked = false
            chip3.isChecked = false
            chip4.isChecked = false
            chip5.isChecked = false
        }
        chip2.setOnClickListener {
            chip1.isChecked = false
            chip3.isChecked = false
            chip4.isChecked = false
            chip5.isChecked = false
        }
        chip3.setOnClickListener {
            chip2.isChecked = false
            chip1.isChecked = false
            chip4.isChecked = false
            chip5.isChecked = false
        }
        chip4.setOnClickListener {
            chip2.isChecked = false
            chip3.isChecked = false
            chip1.isChecked = false
            chip5.isChecked = false
        }
        chip5.setOnClickListener {
            chip2.isChecked = false
            chip3.isChecked = false
            chip4.isChecked = false
            chip1.isChecked = false
        }
    }
    private fun setMonthView() {
        var formatter = DateTimeFormatter.ofPattern("yyyy년 M월")
        binding.textCalendar.text = CalendarUtil.selectedDate.format(formatter)
        val dayList = dayInMonthArray()
        val adapter = CalendarSmallAdapter(dayList,preList,nextList,binding.cal,binding.preScheldule,binding.nextScheldule)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(this,7)
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
    override fun onItemClick(dayText: String) {
        var yearMonthDay = yearMonthFromDate(CalendarUtil.selectedDate) + " " + dayText + "일"
        Toast.makeText(this, yearMonthDay, Toast.LENGTH_SHORT).show()
    }
}