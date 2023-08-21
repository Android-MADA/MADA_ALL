package com.example.myapplication.CalenderFuntion

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
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
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.AddCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarDATA
import com.example.myapplication.CalenderFuntion.Model.CalendarData2
import com.example.myapplication.CalenderFuntion.Model.CalendarDatas
import com.example.myapplication.CalenderFuntion.Model.ResponseSample
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarAddBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    var title = ""
    lateinit var preSchedule : TextView
    lateinit var nextSchedule : TextView
    var preClock = "  오전 10:00  "
    var nextClock = "  오전 11:00  "
    var cycle = "반복 안함"
    var memo = ""
    var color = "#89A9D9"
    var edit = false

    var curColor ="#89A9D9"
    var curDday ="N"
    var curRepeat = "No"
    var id2 : Int = 0
    var ddayId : Int = -1




    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCalendar::class.java)
    lateinit var token : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preSchedule = TextView(requireContext())
        nextSchedule = TextView(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CalendarAddBinding.inflate(layoutInflater)
        hideBootomNavigation(true)
        val bundle = arguments
        title = bundle?.getString("title") ?: ""
        preSchedule.text = (bundle?.getString("preSchedule") ?: "2023-6-1")
        nextSchedule.text = (bundle?.getString("nextSchedule") ?: "2023-6-1")
        preClock = bundle?.getString("preClock") ?: "  오전 10:00  "
        nextClock = bundle?.getString("nextClock") ?: "  오전 11:00  "
        cycle = processInput(bundle?.getString("cycle") ?: "No")
        memo = bundle?.getString("memo") ?: ""
        color = bundle?.getString("color") ?:"#89A9D9"
        curColor = color
        edit = bundle?.getBoolean("edit")?: false
        token = bundle?.getString("Token")?: ""
        id2 = bundle?.getInt("id")?: -1
        val today = bundle?.getString("Today")?: null
        if(edit)
            binding.submitBtn.text = "수정"
        binding.textTitle.setText(title)
        binding.preScheldule.text = convertToDateKoreanFormat(preSchedule.text.toString())
        binding.nextScheldule.text = convertToDateKoreanFormat(nextSchedule.text.toString())
        if(today!=null) {
            preSchedule.text = today
            nextSchedule.text = today
            binding.preScheldule.text = convertToDateKoreanFormat(today)
            binding.nextScheldule.text = convertToDateKoreanFormat(today)
        }
        binding.preScheldule2.text = preClock
        binding.nextScheldule2.text = nextClock

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
            curColor ="#89A9D9"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor2.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.main), PorterDuff.Mode.SRC_IN)
            curColor ="#2AA1B7"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor3.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.point_main), PorterDuff.Mode.SRC_IN)
            curColor ="#F5EED1"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor4.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub1), PorterDuff.Mode.SRC_IN)
            curColor ="#F8D141"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor5.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub6), PorterDuff.Mode.SRC_IN)
            curColor ="#FFE7EB"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor6.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub3), PorterDuff.Mode.SRC_IN)
            curColor ="#F0768C"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor7.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub5), PorterDuff.Mode.SRC_IN)
            curColor ="#89A9D9"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor8.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub6), PorterDuff.Mode.SRC_IN)
            curColor ="#F0768C"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor9.setOnClickListener {
            binding.calendarColor.setColorFilter(Color.parseColor("#F5EED1"), PorterDuff.Mode.SRC_IN)
            curColor ="#F8D141"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor.setOnClickListener {
            toggleLayout(true,binding.layoutColorSelectors)
        }

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.textDday.visibility= View.VISIBLE
                binding.clockAndCycle.visibility = View.GONE
                binding.layoutColorSelector2.visibility = View.VISIBLE
                binding.layoutColorSelector1.visibility = View.GONE
                binding.preScheldule.visibility = View.GONE
                binding.calendarColor.setColorFilter(resources.getColor(R.color.sub5), PorterDuff.Mode.SRC_IN)
                curDday = "Y"
            } else {
                binding.textDday.visibility= View.GONE
                binding.clockAndCycle.visibility = View.VISIBLE
                binding.layoutColorSelector2.visibility = View.GONE
                binding.layoutColorSelector1.visibility = View.VISIBLE
                binding.preScheldule.visibility = View.VISIBLE
                binding.calendarColor.setColorFilter(resources.getColor(R.color.sub5), PorterDuff.Mode.SRC_IN)
                curDday = "N"
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
                schedules[scheduleSelect].text = "  "+ampm+" "+hour+":"+data2[newVal]+"  "
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
        /*
        if(cycle =="매일") {
            binding.cyclebtn.text = "매일"
            curRepeat = "Day"
            binding.calAll.visibility = View.GONE
            updateUIForWeekRepeat(chip2,chip1,chip3,chip4,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
        } else if(cycle =="매주") {

            binding.cyclebtn.text = "매주"
            curRepeat = "Week"
            binding.calAll.visibility = View.GONE
            binding.repeatWeek.visibility = View.VISIBLE
            binding.repeatMonth.visibility = View.GONE
            updateUIForWeekRepeat(chip3,chip1,chip2,chip4,chip5)
        } else if(cycle =="매월") {
            binding.cyclebtn.text = "매월"
            curRepeat = "Month"
            binding.calAll.visibility = View.GONE
            updateUIForWeekRepeat(chip4,chip1,chip3,chip2,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.VISIBLE
        } else if(cycle =="매년") {

            binding.cyclebtn.text = "매년"
            curRepeat = "Year"
            binding.calAll.visibility = View.GONE
            updateUIForWeekRepeat(chip5,chip1,chip3,chip4,chip2)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
        } else {
            binding.cyclebtn.text = "반복 안함"
            curRepeat = "No"
            updateUIForWeekRepeat(chip1,chip2,chip3,chip4,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
        }
        binding.cyclebtn.setOnClickListener {
            binding.chipGroup.visibility = View.VISIBLE
        }
        chip1.setOnClickListener {
            binding.cyclebtn.text = "반복 안함"
            toggleLayout(true,binding.calAll)
            curRepeat = "No"
            updateUIForWeekRepeat(chip1,chip2,chip3,chip4,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
        }
        chip2.setOnClickListener {
            binding.cyclebtn.text = "매일"
            curRepeat = "Day"
            toggleLayout(false,binding.calAll)
            updateUIForWeekRepeat(chip2,chip1,chip3,chip4,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
        }
        chip3.setOnClickListener {
            binding.cyclebtn.text = "매주"
            curRepeat = "Week"
            toggleLayout(false,binding.calAll)
            binding.repeatWeek.visibility = View.VISIBLE
            binding.repeatMonth.visibility = View.GONE
            updateUIForWeekRepeat(chip3,chip1,chip2,chip4,chip5)
        }
        chip4.setOnClickListener {
            binding.cyclebtn.text = "매월"
            curRepeat = "Month"
            toggleLayout(false,binding.calAll)
            updateUIForWeekRepeat(chip4,chip1,chip3,chip2,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.VISIBLE
        }
        chip5.setOnClickListener {
            binding.cyclebtn.text = "매년"
            curRepeat = "Year"
            toggleLayout(false,binding.calAll)
            updateUIForWeekRepeat(chip5,chip1,chip3,chip4,chip2)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
        }
        */

        /*
        var selectedTextWeek = binding.textWeek1

        val textWeek = arrayOf(
            binding.textWeek1, binding.textWeek2, binding.textWeek3, binding.textWeek4, binding.textWeek5,binding.textWeek6,binding.textWeek7
        )
        for (textView in textWeek) {
            textView.setOnClickListener {
                // 클릭 시 기존에 선택된 TextView의 배경을 투명하게 만듭니다.
                selectedTextWeek?.setBackgroundResource(android.R.color.transparent)

                // 클릭한 TextView의 배경을 원하는 배경으로 설정합니다.
                textView.setBackgroundResource(R.drawable.calendar_add_repeat_back1) // 여기서 R.drawable.selected_background는 적절한 배경 리소스로 변경해야 합니다.

                // 선택된 TextView를 업데이트합니다.
                selectedTextWeek = textView
            }
        }
        var selectedTextMon = binding.textCal1
        val textMon = arrayOf(
            binding.textCal1, binding.textCal2, binding.textCal3, binding.textCal4, binding.textCal5,
            binding.textCal6, binding.textCal7, binding.textCal8, binding.textCal9, binding.textCal10,
            binding.textCal11, binding.textCal12, binding.textCal13, binding.textCal14, binding.textCal15,
            binding.textCal16, binding.textCal17, binding.textCal18, binding.textCal19, binding.textCal20,
            binding.textCal21, binding.textCal22, binding.textCal23, binding.textCal24, binding.textCal25,
            binding.textCal26, binding.textCal27, binding.textCal28, binding.textCal29, binding.textCal30,
            binding.textCal31, binding.textCalFinal
        )
        for (textView in textMon) {
            textView.setOnClickListener {
                // 클릭 시 기존에 선택된 TextView의 배경을 투명하게 만듭니다.
                selectedTextMon?.setBackgroundResource(android.R.color.transparent)

                // 클릭한 TextView의 배경을 원하는 배경으로 설정합니다.
                textView.setBackgroundResource(R.drawable.calendar_add_repeat_back1) // 선택한 배경 리소스로 변경

                // 선택된 TextView를 업데이트합니다.
                selectedTextMon = textView
            }
        }*/

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
                val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val size = Point()
                val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                display.getSize(size)
                val screenWidth = size.x
                val popupWidth = (screenWidth * 0.8).toInt()
                mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)


                if(edit) {
                    mDialogView.findViewById<TextView>(R.id.textTitle).text = "수정하지 않고 나가시겠습니까?"
                }
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

        binding.submitBtn.setOnClickListener {
            if(compareDates(nextSchedule.text.toString(),preSchedule.text.toString())) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_add_popup_one, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()

                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                val displayMetrics = DisplayMetrics()
                val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val size = Point()
                val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                display.getSize(size)
                val screenWidth = size.x
                val popupWidth = (screenWidth * 0.8).toInt()
                mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)

                mDialogView.findViewById<TextView>(R.id.textTitle).text = "올바른 날짜를 입력해 주십시오"
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                    mBuilder.dismiss()
                })
            } else if(binding.nextScheldule2.text.toString()!="  오전 12:00  "&&compareTimes(binding.preScheldule2.text.toString(),binding.nextScheldule2.text.toString())) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_add_popup_one, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()

                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()


                val displayMetrics = DisplayMetrics()
                val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val size = Point()
                val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                display.getSize(size)
                val screenWidth = size.x
                val popupWidth = (screenWidth * 0.8).toInt()
                mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)


                mDialogView.findViewById<TextView>(R.id.textTitle).text = "올바른 시간을 입력해 주십시오"
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                    mBuilder.dismiss()
                })
            }
            else {
                if(binding.checkBox.isChecked) {
                    getDdayDataArray()
                }
                else {
                    if (binding.switch2.isChecked) {
                        binding.preScheldule2.text = "  오전 12:00  "
                        binding.nextScheldule2.text = "  오전 12:00  "
                    } else {
                        binding.preScheldule2.text = "  오전 12:00  "
                        binding.nextScheldule2.text = "  오전 12:00  "
                    }
                    if(binding.submitBtn.text =="등록") {
                        addCalendar( CalendarData2( binding.textTitle.text.toString(),convertToDateKoreanFormat2(preSchedule.text.toString()),convertToDateKoreanFormat2(nextSchedule.text.toString()),
                            curColor,curRepeat,curDday,binding.textMemo.text.toString(),
                            timeChange(binding.preScheldule2.text.toString()),timeChange(binding.nextScheldule2.text.toString()) ) )

                    } else if(binding.submitBtn.text =="수정"){
                        //dasdasd
                        eidtCalendar( CalendarData2( binding.textTitle.text.toString(),convertToDateKoreanFormat2(preSchedule.text.toString()),convertToDateKoreanFormat2(nextSchedule.text.toString()),
                            curColor,curRepeat,curDday,binding.textMemo.text.toString(),
                            timeChange(binding.preScheldule2.text.toString()),timeChange(binding.nextScheldule2.text.toString()) ),id2 )


                    }

                }
            }


        }

    }
    fun updateUIForWeekRepeat(chip1: Chip, chip2: Chip, chip3: Chip, chip4: Chip, chip5: Chip) {
        chip1.setChipBackgroundColorResource(R.color.sub5)
        chip2.setChipBackgroundColorResource(R.color.white)
        chip3.setChipBackgroundColorResource(R.color.white)
        chip4.setChipBackgroundColorResource(R.color.white)
        chip5.setChipBackgroundColorResource(R.color.white)
        chip2.isChecked = false
        chip3.isChecked = false
        chip1.isChecked = true
        chip4.isChecked = false
        chip5.isChecked = false
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
            CalendarSmallAdapter(dayList, binding.cal, binding.preScheldule,binding.textBlank,preSchedule)
        } else {
            CalendarSmallAdapter(dayList, binding.cal, binding.nextScheldule,binding.textDday,nextSchedule)
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
    fun convertToDateKoreanFormat(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale("en","US"))
        val outputFormat = SimpleDateFormat("  M월 d일 (E)  ", Locale("ko", "KR"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    fun daysRemainingToDate(targetDate: String): Int {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        val today = LocalDate.now()
        val target = LocalDate.parse(targetDate, dateFormatter)
        val daysRemaining = target.toEpochDay() - today.toEpochDay()
        return daysRemaining.toInt()
    }
    fun convertToDateKoreanFormat2(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale("en","US"))
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en","US"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    fun compareDates(date1: String, date2: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        val localDate1 = LocalDate.parse(date1, formatter)
        val localDate2 = LocalDate.parse(date2, formatter)
        return if (localDate1.isBefore(localDate2)) {
            true
        } else {
            false
        }
    }
    fun compareTimes(time1: String, time2: String): Boolean {
        val inputFormat = SimpleDateFormat("  a h:mm  ", Locale("en","US")) // 수정된 형식
        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()

        val time1Modified = time1.replace("오전", "AM").replace("오후", "PM")
        val time2Modified = time2.replace("오전", "AM").replace("오후", "PM")

        calendar1.time = inputFormat.parse(time1Modified)
        calendar2.time = inputFormat.parse(time2Modified)

        val hour1 = calendar1.get(Calendar.HOUR_OF_DAY)
        val minute1 = calendar1.get(Calendar.MINUTE)

        val hour2 = calendar2.get(Calendar.HOUR_OF_DAY)
        val minute2 = calendar2.get(Calendar.MINUTE)

        if (hour1 > hour2 || (hour1 == hour2 && minute1 > minute2)) {
            return true
        } else {
            return false
        }
    }
    fun timeChange(time: String): String {
        val inputFormat = SimpleDateFormat("  a h:mm  ", Locale("en","US"))
        val calendar = Calendar.getInstance()

        val timeModified = time.replace("오전", "AM").replace("오후", "PM")

        calendar.time = inputFormat.parse(timeModified)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return String.format("%02d:%02d:00", hour, minute)
    }
    private fun toggleLayout(isExpanded: Boolean, layoutExpand: LinearLayout): Boolean {
        if (isExpanded) {
            ToggleAnimation.expand(layoutExpand)
        } else {
            ToggleAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }
    fun processInput(input: String): String {
        return when (input) {
            "Day" -> "매일"
            "Week" -> "매일"
            "Month" -> "매일"
            "Year" -> "매일"
            else -> "반복 안함"
        }
    }
    fun convertToDate2(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    private fun addCalendar(data : CalendarData2) {
        val call1 = service.addCal(token,data)
        call1.enqueue(object : Callback<ResponseSample> {
            override fun onResponse(call: Call<ResponseSample>, response: Response<ResponseSample>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) {
                        Log.d("status",responseBody.status.toString())
                    }else
                        Log.d("777","${response.code()}")

                } else {
                    Log.d("666","itemType: ${response.code()} ")
                }
                findNavController().navigate(R.id.action_calendarAdd_to_fragCalendar)
            }

            override fun onFailure(call: Call<ResponseSample>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }
    private fun eidtCalendar(data : CalendarData2,id : Int) {
        val call1 = service.editCal(token,id,data)

        call1.enqueue(object : Callback<CalendarData2> {
            override fun onResponse(call: Call<CalendarData2>, response: Response<CalendarData2>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) {
                        //Log.d("status",responseBody.data.name.toString())
                    }else
                        Log.d("eidt","${response.code()}")

                } else {
                    Log.d("eidt","itemType: ${response.code()} ")
                    Log.d("eidt","itemType: ${token} ")
                }
                findNavController().navigate(R.id.action_calendarAdd_to_fragCalendar)
            }

            override fun onFailure(call: Call<CalendarData2>, t: Throwable) {
                Log.d("eidt","itemType: ${t.message}")
            }
        })
    }
    private fun getDdayDataArray() {
        val ddayDatas = ArrayList<CalendarDATA>()
        val call2 = service.getAllDday(token)
        call2.enqueue(object : Callback<CalendarDatas> {
            override fun onResponse(call2: Call<CalendarDatas>, response: Response<CalendarDatas>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas
                        if(datas != null) {
                            for (data in datas) {
                                val tmp = CalendarDATA("${convertToDate2(data.start_date)}","${convertToDate2(data.start_date)}","${convertToDate2(data.end_date)}",
                                    "${data.start_time}","${data.end_time}","${data.color}","${data.repeat}","${data.d_day}","${data.name}",
                                    -1,true,"${data.memo}","CAL",data.id)
                                ddayDatas.add(tmp)
                                Log.d("111","datas: ${data.name} ${data.color}")
                                // ...
                            }
                        }

                        ddayDatas.sortBy { daysRemainingToDate(it.endDate) }
                        if(binding.textDday.text=="D - 0"){
                            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_add_popup_one, null)
                            val mBuilder = AlertDialog.Builder(requireContext())
                                .setView(mDialogView)
                                .create()

                            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                            mBuilder.show()

                            mDialogView.findViewById<TextView>(R.id.textTitle).text = "올바른 날짜를 입력해 주십시오"
                            val display = requireActivity().windowManager.defaultDisplay
                            mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                                mBuilder.dismiss()
                            })
                        }else if(ddayDatas.size==3) {
                            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_change, null)
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
                            val checkBox1 = mDialogView.findViewById<CheckBox>(R.id.checkBox1)
                            val checkBox2 =mDialogView.findViewById<CheckBox>(R.id.checkBox2)
                            val checkBox3 =mDialogView.findViewById<CheckBox>(R.id.checkBox3)

                            for (i in 0 until 3) {
                                val color = ddayDatas[i].color
                                val imageResource = when (color) {
                                    "#E1E9F5" -> R.drawable.calendar_ddayblue_smallbackground
                                    "#FFE7EB" -> R.drawable.calendar_ddaypink_smallbackground
                                    "#F5EED1" -> R.drawable.calendar_ddayyellow_smallbackground
                                    else -> R.drawable.calendar_dday_plus
                                }
                                when (i) {
                                    0 -> {
                                        mDialogView.findViewById<ImageView>(R.id.image1).setImageResource(imageResource)
                                        mDialogView.findViewById<TextView>(R.id.textDday1).text = "D-${daysRemainingToDate(ddayDatas[i].endDate)}"
                                        mDialogView.findViewById<TextView>(R.id.textTitle1).text = ddayDatas[i].title
                                    }
                                    1 -> {
                                        mDialogView.findViewById<ImageView>(R.id.image2).setImageResource(imageResource)
                                        mDialogView.findViewById<TextView>(R.id.textDday2).text = "D-${daysRemainingToDate(ddayDatas[i].endDate)}"
                                        mDialogView.findViewById<TextView>(R.id.textTitle2).text = ddayDatas[i].title
                                    }
                                    2 -> {
                                        mDialogView.findViewById<ImageView>(R.id.image3).setImageResource(imageResource)
                                        mDialogView.findViewById<TextView>(R.id.textDday3).text = "D-${daysRemainingToDate(ddayDatas[i].endDate)}"
                                        mDialogView.findViewById<TextView>(R.id.textTitle3).text = ddayDatas[i].title
                                    }
                                }
                            }
                            checkBox1.setOnClickListener {
                                checkBox2.isChecked = false
                                checkBox3.isChecked = false
                                ddayId = ddayDatas[0].id
                            }
                            checkBox2.setOnClickListener {
                                checkBox1.isChecked = false
                                checkBox3.isChecked = false
                                ddayId = ddayDatas[1].id
                            }
                            checkBox3.setOnClickListener {
                                checkBox2.isChecked = false
                                checkBox1.isChecked = false
                                ddayId = ddayDatas[2].id
                            }
                            mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
                                mBuilder.dismiss()
                            })
                            mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                                if(!checkBox1.isChecked&&!checkBox2.isChecked&&!checkBox3.isChecked) {
                                    mDialogView.findViewById<TextView>(R.id.textInfo).text = "대체할 디데이를 선택해야 합니다"
                                } else {
                                    deleteCalendar(ddayId)
                                    addCalendar( CalendarData2( binding.textTitle.text.toString(),convertToDateKoreanFormat2(nextSchedule.text.toString()),convertToDateKoreanFormat2(nextSchedule.text.toString()),
                                        curColor,curRepeat,curDday,binding.textMemo.text.toString(),
                                        timeChange(binding.preScheldule2.text.toString()),timeChange(binding.nextScheldule2.text.toString()) ) )

                                    mBuilder.dismiss()
                                }
                            })
                        } else {
                            addCalendar( CalendarData2( binding.textTitle.text.toString(),convertToDateKoreanFormat2(nextSchedule.text.toString()),convertToDateKoreanFormat2(nextSchedule.text.toString()),
                                curColor,curRepeat,curDday,binding.textMemo.text.toString(),
                                timeChange(binding.preScheldule2.text.toString()),timeChange(binding.nextScheldule2.text.toString()) ) )
                        }

                    }
                }
            }
            override fun onFailure(call: Call<CalendarDatas>, t: Throwable) {
                //Log.d("444","itemType: ${t.message}")
            }
        })
    }
    private fun deleteCalendar(id : Int) {
        val call1 = service.deleteCal(token,id)
        call1.enqueue(object : Callback<AddCalendarData> {
            override fun onResponse(call: Call<AddCalendarData>, response: Response<AddCalendarData>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) {
                        //Log.d("del1",responseBody.datas.name.toString())
                    }else
                        Log.d("del2","${response.code()}")

                } else {
                    //Log.d("del3","itemType: ${response.code()} ${id} ")
                }
            }

            override fun onFailure(call: Call<AddCalendarData>, t: Throwable) {
                //Log.d("del4","itemType: ${t.message}")
            }
        })
    }

}