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
import com.mada.reapp.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.mada.reapp.CalenderFuntion.Model.AndroidCalendarData
import com.mada.reapp.CalenderFuntion.Model.CalendarData
import com.mada.reapp.CalenderFuntion.Model.CalendarDataEdit
import com.mada.reapp.CalenderFuntion.Model.CalendarViewModel
import com.mada.reapp.CalenderFuntion.Small.CalendarSliderSmallAdapter
import com.mada.reapp.R
import com.mada.reapp.databinding.CalendarAddBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.mada.reapp.MainActivity
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar


class CalendarAddFragment : Fragment() {
    private lateinit var calendar: Calendar
    lateinit var binding: CalendarAddBinding
    private val CalendarViewModel : CalendarViewModel by activityViewModels()


    lateinit var initPreSchedule : String
    lateinit var initnextSchedule : String
    lateinit var initCycle : String
    lateinit var calData : AndroidCalendarData
    //"2023-08-01"의 형태
    lateinit var preScheduleNum : TextView
    lateinit var nextScheduleNum : TextView

    var curCycle = "N"
    var curColor ="#89A9D9"
    var curId : Int = -1
    var curDday ="N"
    var curEdit = false
    var curRepeatText =0
    val todayMonth = LocalDate.now()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preScheduleNum = TextView(requireContext())
        nextScheduleNum = TextView(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalendarAddBinding.inflate(layoutInflater)
        hideBootomNavigation(true)

        val mainActivity = requireActivity() as MainActivity
        if(mainActivity.getPremium()) {
        } else {
            MobileAds.initialize(this.requireContext()) {}
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }

        val schedules = arrayOf<TextView>(
            binding.preScheldule2,
            binding.nextScheldule2
        )

        if(arguments?.getSerializable("calData")!=null) {
            calData = arguments?.getSerializable("calData") as AndroidCalendarData
            binding.textTitle.setText(calData.title)
            preScheduleNum.text = calData.startDate
            nextScheduleNum.text = calData.endDate
            binding.ddayCb.isChecked = calData.dDay == "Y"

            binding.preScheldule2.text = CalendarViewModel.TimeChangeKor(calData.startTime)
            binding.nextScheldule2.text = CalendarViewModel.TimeChangeKor(calData.endTime)
            curCycle = calData.repeat
            binding.textMemo.setText(calData.memo)
            curColor = calData.color
            curId = calData.id
            curDday = calData.dDay
            initCycle =calData.repeat
            curRepeatText = calData.repeatDate.toInt()
        } else {
            val today = arguments?.getString("today")?: "2023-06-01"
            preScheduleNum.text = today
            nextScheduleNum.text = today
            curCycle = "N"
        }
        //받아온 데이터의 달력, 색깔 적용
        binding.preScheldule.text = CalendarViewModel.convertToDateKoreanFormat(preScheduleNum.text.toString())
        binding.nextScheldule.text = CalendarViewModel.convertToDateKoreanFormat(nextScheduleNum.text.toString())
        binding.calendarColor.setColorFilter(Color.parseColor(curColor), PorterDuff.Mode.SRC_IN)
        //수정한지 체크하기 위한 초기 데이터의 달력
        initPreSchedule = preScheduleNum.text.toString()
        initnextSchedule = nextScheduleNum.text.toString()
        //수정 이라면
        curEdit = arguments?.getBoolean("edit")?: false
        if(curEdit)
            binding.submitBtn.text = "수정"
        //색깔 클릭 리스너
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
                binding.textDday.setTextColor(Color.parseColor(colorResource))
                curColor = colorResource
                toggleLayout(false, binding.layoutColorSelectors)
            }
        }

        binding.calendarColor.setOnClickListener {
            if(binding.layoutColorSelectors.visibility == View.GONE)
                toggleLayout(true,binding.layoutColorSelectors)
        }
        //dday 체크박스 리스너
        //달력 클릭 리스너
        binding.ddayCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                curDday = "Y"
                toggleLayout(true,binding.ddayLy)
                var remainDday = CalendarViewModel.daysRemainingToDate(nextScheduleNum.text.toString()).toString()
                if(remainDday.toInt() <=0) remainDday = "DAY"
                binding.textDday.text = "D-${remainDday}"
            } else {
                curDday ="N"
                toggleLayout(false,binding.ddayLy)
            }
        }

        binding.preScheldule.setOnClickListener {
            binding.calendar2.adapter = null
            schedules[0].setBackgroundColor(Color.TRANSPARENT)
            schedules[1].setBackgroundColor(Color.TRANSPARENT)
            toggleLayout(false,binding.timePicker)
            binding.preScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            if(binding.cal.visibility== View.GONE)  toggleLayout(true,binding.cal)
            else toggleLayout(false,binding.cal)
            binding.nextScheldule.setBackgroundColor(Color.TRANSPARENT)
            val calendarAdapter = CalendarSliderSmallAdapter(this,binding.textYearMonth,binding.calendar2,binding.preScheldule,preScheduleNum ,binding.cal,binding.textDdayBlank)
            binding.calendar2.adapter = calendarAdapter
            val comparisonResult =  ChronoUnit.MONTHS.between(todayMonth,LocalDate.parse(preScheduleNum.text.toString()))
            binding.calendar2.setCurrentItem(CalendarSliderAdapter.START_POSITION+comparisonResult.toInt(), false)
        }
        binding.nextScheldule.setOnClickListener {
            binding.calendar2.adapter = null
            schedules[0].setBackgroundColor(Color.TRANSPARENT)
            schedules[1].setBackgroundColor(Color.TRANSPARENT)
            toggleLayout(false,binding.timePicker)
            binding.nextScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            if(binding.cal.visibility== View.GONE)  toggleLayout(true,binding.cal)
            else toggleLayout(false,binding.cal)
            binding.preScheldule.setBackgroundColor(Color.TRANSPARENT)
            val calendarAdapter = CalendarSliderSmallAdapter(this,binding.textYearMonth,binding.calendar2,binding.nextScheldule,nextScheduleNum ,binding.cal,binding.textDday)
            binding.calendar2.adapter = calendarAdapter
            val comparisonResult =  ChronoUnit.MONTHS.between(todayMonth,LocalDate.parse(nextScheduleNum.text.toString()))
            binding.calendar2.setCurrentItem(CalendarSliderAdapter.START_POSITION+comparisonResult.toInt() , false)
        }
        binding.preBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(binding.calendar2.currentItem-1, true)
        }
        binding.nextBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(binding.calendar2.currentItem+1, true)
        }
        binding.switch2.setOnCheckedChangeListener { p0, isChecked ->
            schedules[0].setBackgroundColor(Color.TRANSPARENT)
            schedules[1].setBackgroundColor(Color.TRANSPARENT)
            toggleLayout(false,binding.timePicker)
            if(isChecked) toggleLayout(false,binding.colck)
            else toggleLayout(true,binding.colck)
        }
        //시간 클릭 리스너

        var scheduleSelect = 0
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
            if(newVal==0) schedules[scheduleSelect].text = schedules[scheduleSelect].text.toString().replace("오후","오전")
            else schedules[scheduleSelect].text = schedules[scheduleSelect].text.toString().replace("오전","오후")
        }
        var tmpCheck = true
        ticker2.setOnValueChangedListener { picker, oldVal, newVal ->
            val matchResult = regex.find(schedules[scheduleSelect].text.toString())
            if (matchResult != null) {
                var (ampm, hour, minute) = matchResult.destructured
                if((oldVal==11&&newVal==12) ||(oldVal==12&&newVal==11)) {
                    if(tmpCheck) {
                        if (ticker1.value == 0)  {
                            ampm = "오후"
                            ticker1.value = 1
                        }
                        else {
                            ampm = "오전"
                            ticker1.value = 0
                        }
                        tmpCheck = false
                    }
                } else {
                    tmpCheck = true
                }
                schedules[scheduleSelect].text = "  "+ampm+" "+newVal+":"+minute+"  "
            }
        }
        ticker3.setOnValueChangedListener { picker, oldVal, newVal ->
            val matchResult = regex.find(schedules[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                schedules[scheduleSelect].text = "  "+ampm+" "+hour+":"+data2[newVal]+"  "
            }
        }
        schedules[0].setOnClickListener {
            binding.nextScheldule.setBackgroundColor(Color.TRANSPARENT)
            binding.preScheldule.setBackgroundColor(Color.TRANSPARENT)
            toggleLayout(false,binding.cal)
            schedules[0].setBackgroundResource(R.drawable.calendar_prebackground)
            schedules[1].setBackgroundColor(Color.TRANSPARENT)
            if(binding.timePicker.visibility== View.GONE)  toggleLayout(true,binding.timePicker)
            else toggleLayout(false,binding.timePicker)
            scheduleSelect=0
            val matchResult = regex.find(schedules[0].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                if(ampm=="오전") ticker1.value=0
                else ticker1.value=1
                ticker2.value=hour.toInt()
                ticker3.value=minute.toInt()/5
            } else {
                ticker1.value=0
                ticker2.value=10
                ticker3.value=0
            }
        }
        schedules[1].setOnClickListener {
            binding.nextScheldule.setBackgroundColor(Color.TRANSPARENT)
            binding.preScheldule.setBackgroundColor(Color.TRANSPARENT)
            toggleLayout(false,binding.cal)
            schedules[1].setBackgroundResource(R.drawable.calendar_prebackground)
            schedules[0].setBackgroundColor(Color.TRANSPARENT)
            if(binding.timePicker.visibility== View.GONE)  toggleLayout(true,binding.timePicker)
            else toggleLayout(false,binding.timePicker)
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
        val textWeek = arrayOf(
            binding.textWeek1, binding.textWeek2, binding.textWeek3, binding.textWeek4, binding.textWeek5,binding.textWeek6,binding.textWeek7
        )
        val textMon = arrayOf(
            binding.textCal1, binding.textCal2, binding.textCal3, binding.textCal4, binding.textCal5,
            binding.textCal6, binding.textCal7, binding.textCal8, binding.textCal9, binding.textCal10,
            binding.textCal11, binding.textCal12, binding.textCal13, binding.textCal14, binding.textCal15,
            binding.textCal16, binding.textCal17, binding.textCal18, binding.textCal19, binding.textCal20,
            binding.textCal21, binding.textCal22, binding.textCal23, binding.textCal24, binding.textCal25,
            binding.textCal26, binding.textCal27, binding.textCal28, binding.textCal29, binding.textCal30,
            binding.textCal31, binding.textCalFinal
        )
        var preTextView = binding.textWeek1

        if(curCycle =="D") {
            binding.cyclebtn.text = "매일"
            updateUIForWeekRepeat(chip2,chip1,chip3,chip4,chip5)
            binding.calAll.visibility = View.GONE
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
            binding.repeatYear.visibility = View.GONE

        } else if(curCycle =="W") {
            binding.cyclebtn.text = "매주"
            binding.calAll.visibility = View.GONE
            binding.repeatWeek.visibility = View.VISIBLE
            binding.repeatMonth.visibility = View.GONE
            binding.repeatYear.visibility = View.GONE
            if(calData.repeatDate==0) {
                textWeek[6].setBackgroundResource(R.drawable.calendar_add_repeat_back1)
                textWeek[6].setTextColor(Color.WHITE)
                preTextView = textWeek[6]
            } else {
                textWeek[calData.repeatDate.toInt()-1].setBackgroundResource(R.drawable.calendar_add_repeat_back1)
                textWeek[calData.repeatDate.toInt()-1].setTextColor(Color.WHITE)
                preTextView = textWeek[calData.repeatDate.toInt()-1]
            }
            updateUIForWeekRepeat(chip3,chip1,chip2,chip4,chip5)
        } else if(curCycle =="M") {
            binding.cyclebtn.text = "매월"
            binding.calAll.visibility = View.GONE
            preTextView = textWeek[calData.repeatDate.toInt()-1]
            textMon[calData.repeatDate.toInt()-1].setBackgroundResource(R.drawable.calendar_add_repeat_back1)
            textMon[calData.repeatDate.toInt()-1].setTextColor(Color.WHITE)
            updateUIForWeekRepeat(chip4,chip1,chip3,chip2,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.VISIBLE
            binding.repeatYear.visibility = View.GONE
        } else if(curCycle =="Y") {
            binding.cyclebtn.text = "매년"
            binding.calAll.visibility = View.GONE
            updateUIForWeekRepeat(chip5,chip1,chip3,chip4,chip2)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
            binding.repeatYear.visibility = View.VISIBLE
        } else {
            binding.cyclebtn.text = "반복 안함"
            updateUIForWeekRepeat(chip1,chip2,chip3,chip4,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
            binding.repeatYear.visibility = View.GONE
        }
        binding.cyclebtn.setOnClickListener {
            if(binding.chipGroup.visibility ==View.VISIBLE) {
                toggleLayout(false, binding.chipGroup)
                toggleLayout(false, binding.repeatWeek)
                toggleLayout(false, binding.repeatMonth)
            }
            else
                toggleLayout(true,binding.chipGroup)
        }
        chip1.setOnClickListener {
            binding.cyclebtn.text = "반복 안함"
            toggleLayout(true,binding.calAll)
            curCycle = "N"
            updateUIForWeekRepeat(chip1,chip2,chip3,chip4,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
            binding.repeatYear.visibility = View.GONE
        }
        chip2.setOnClickListener {
            binding.cyclebtn.text = "매일"
            curCycle = "D"
            updateUIForWeekRepeat(chip2,chip1,chip3,chip4,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
            binding.repeatYear.visibility = View.GONE
        }
        chip3.setOnClickListener {
            binding.cyclebtn.text = "매주"
            curCycle = "W"/*
            binding.nextScheldule.setBackgroundColor(Color.TRANSPARENT)
            binding.preScheldule.setBackgroundColor(Color.TRANSPARENT)
            toggleLayout(false,binding.cal)
            toggleLayout(false,binding.calAll)*/
            binding.repeatWeek.visibility = View.VISIBLE
            binding.repeatMonth.visibility = View.GONE
            binding.repeatYear.visibility = View.GONE
            updateUIForWeekRepeat(chip3,chip1,chip2,chip4,chip5)
        }
        chip4.setOnClickListener {
            binding.cyclebtn.text = "매월"
            curCycle = "M"
            updateUIForWeekRepeat(chip4,chip1,chip3,chip2,chip5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.VISIBLE
            binding.repeatYear.visibility = View.GONE
        }
        chip5.setOnClickListener {
            binding.cyclebtn.text = "매년"
            curCycle = "Y"
            updateUIForWeekRepeat(chip5,chip1,chip3,chip4,chip2)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
            binding.repeatYear.visibility = View.VISIBLE
        }
        var selectedTextWeek = binding.textWeek1



        for ((index, textView) in textWeek.withIndex()) {
            textView.setOnClickListener {
                preTextView.setTextColor(Color.BLACK)
                preTextView.setBackgroundResource(android.R.color.transparent)
                // 클릭 시 기존에 선택된 TextView의 배경을 투명하게 만듭니다.
                selectedTextWeek?.setBackgroundResource(android.R.color.transparent)
                if(index == 6) curRepeatText = 0
                else curRepeatText = index+1

                // 클릭한 TextView의 배경을 원하는 배경으로 설정합니다.
                textView.setBackgroundResource(R.drawable.calendar_add_repeat_back1) // 여기서 R.drawable.selected_background는 적절한 배경 리소스로 변경해야 합니다.
                preTextView = textView
                preTextView.setTextColor(Color.WHITE)
                // 선택된 TextView를 업데이트합니다.
                selectedTextWeek = textView
            }
        }
        var selectedTextMon = binding.textCal1

        for ((index, textView) in textMon.withIndex()) {
            textView.setOnClickListener {
                preTextView.setTextColor(Color.BLACK)
                preTextView.setBackgroundResource(android.R.color.transparent)
                // 클릭 시 기존에 선택된 TextView의 배경을 투명하게 만듭니다.
                selectedTextMon?.setBackgroundResource(android.R.color.transparent)
                curRepeatText = index+1
                // 클릭한 TextView의 배경을 원하는 배경으로 설정합니다.
                textView.setBackgroundResource(R.drawable.calendar_add_repeat_back1) // 선택한 배경 리소스로 변경
                preTextView = textView
                preTextView.setTextColor(Color.WHITE)
                // 선택된 TextView를 업데이트합니다.
                selectedTextMon = textView
            }
        }
        binding.numberPickerMonth.value = todayMonth .monthValue
        binding.numberPickerDay.value = todayMonth .dayOfMonth

        binding.numberPickerMonth.setOnValueChangedListener { picker, oldVal, newVal ->
            if(newVal ==1 ||newVal ==3 ||newVal ==5 ||newVal ==7 ||newVal ==8 ||newVal ==10 ||newVal ==12)
                binding.numberPickerDay.maxValue=31
            else if(newVal == 2) binding.numberPickerDay.maxValue=29
            else if(newVal ==3 ||newVal ==4 ||newVal ==6 ||newVal ==9 ||newVal ==11) binding.numberPickerDay.maxValue=30
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            //뭐가 있다면
            if(binding.textTitle.text.toString() != "" || preScheduleNum.text.toString() != (initPreSchedule) ||
                nextScheduleNum.text.toString() != (initnextSchedule) || binding.preScheldule2.text.toString() != "  오전 10:00  " ||
                binding.nextScheldule2.text.toString() != "  오전 11:00  " || binding.cyclebtn.text.toString() != "반복 안함" ||
                binding.textMemo.text.toString()!="") {
                //팝업 띄우기
                CalendarViewModel.setPopupTwo(requireContext(),"지금 나가시면 수정된 내용이 저장되지 않아요.",view,R.id.action_calendarAdd_to_fragCalendar)
            } else {
                Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
            }
        }
        binding.submitBtn.setOnClickListener {
            if(curCycle=="Y")  curRepeatText = CalendarViewModel.calculateDaysPassedFromStartOfYear("${binding.numberPickerMonth.value}-${binding.numberPickerDay.value}")
            if(binding.textTitle.text.toString()=="") {
                CalendarViewModel.setPopupOne(requireContext(),"일정 이름을 입력해 주십시오",view)
            }
            else if(!CalendarViewModel.compareDates(preScheduleNum.text.toString(),nextScheduleNum.text.toString())) {
                CalendarViewModel.setPopupOne(requireContext(),"올바른 날짜를 입력해 주십시오",view)
            } else if(binding.nextScheldule2.text.toString()!="  오전 12:00  "
                        &&!CalendarViewModel.compareTimes(binding.preScheldule2.text.toString(),binding.nextScheldule2.text.toString())) {
                CalendarViewModel.setPopupOne(requireContext(),"올바른 시간을 입력해 주십시오",view)
            }
            else {
                val buffering = CalendarViewModel.setPopupBuffering(requireContext())
                if (binding.switch2.isChecked) {
                    binding.preScheldule2.text = "오전 12:00"
                    binding.nextScheldule2.text = "오전 12:00"
                }
                if(binding.submitBtn.text =="등록") {
                    if(binding.ddayCb.isChecked) {
                        CalendarViewModel.addCalendar( CalendarData( binding.textTitle.text.toString(),nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),
                            curColor,"N",curRepeatText,"10:00:00", "11:00:00","Y",binding.textMemo.text.toString() ) ) { result ->
                            when (result) {
                                1 -> {
                                    val tmpId = CalendarViewModel.addId        //서버로 부터 얻은 아이디
                                    Toast.makeText(context, "추가 성공", Toast.LENGTH_SHORT).show()
                                    CalendarViewModel.ddayArrayList.add(AndroidCalendarData(nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),
                                        "10:00:00","11:00:00",curColor,"N","Y",binding.textTitle.text.toString(), -1,false,
                                        binding.textMemo.text.toString(),"CAL",tmpId,curRepeatText))
                                    CalendarViewModel.ddayArrayList.sortBy { CalendarViewModel.daysRemainingToDate(it.endDate) }

                                    addDday(nextScheduleNum.text.substring(0,4).toInt(),nextScheduleNum.text.substring(5,7).toInt(),tmpId)
                                    buffering.dismiss()
                                    Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
                                }
                                2 -> {
                                    buffering.dismiss()
                                    Toast.makeText(context, "추가 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        CalendarViewModel.addCalendar( CalendarData( binding.textTitle.text.toString(),preScheduleNum.text.toString(),nextScheduleNum.text.toString(),
                            curColor,curCycle,curRepeatText,CalendarViewModel.timeChangeNum(binding.preScheldule2.text.toString()),
                            CalendarViewModel.timeChangeNum(binding.nextScheldule2.text.toString()),"N",binding.textMemo.text.toString() ) ) { result ->
                            when (result) {
                                1 -> {
                                    val tmpId = CalendarViewModel.addId        //서버로 부터 얻은 아이디
                                    Toast.makeText(context, "추가 성공", Toast.LENGTH_SHORT).show()
                                    if(curCycle=="N") addCal(tmpId)
                                    else CalendarViewModel.hashMapArrayCal.clear()
                                        /*CalendarViewModel.repeatArrayList.add(AndroidCalendarData(preScheduleNum.text.toString(),preScheduleNum.text.toString(),nextScheduleNum.text.toString(),
                                        CalendarViewModel.timeChangeNum(binding.preScheldule2.text.toString()),CalendarViewModel.timeChangeNum(binding.nextScheldule2.text.toString()),curColor,curCycle,curDday,binding.textTitle.text.toString(),
                                        -1,false,binding.textMemo.text.toString(),"CAL",tmpId,curRepeatText))*/

                                    buffering.dismiss()
                                    Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
                                }
                                2 -> {
                                    buffering.dismiss()
                                    Toast.makeText(context, "추가 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                } else if(binding.submitBtn.text =="수정"){
                    if(binding.ddayCb.isChecked) {
                        CalendarViewModel.editCalendar(
                            CalendarDataEdit( curId,binding.textTitle.text.toString(),nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),
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
                                    CalendarViewModel.ddayArrayList.add(AndroidCalendarData(nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),
                                        "10:00:00","11:00:00",curColor,"N","Y",binding.textTitle.text.toString(),
                                        -1,false,binding.textMemo.text.toString(),"CAL",curId,0))
                                    CalendarViewModel.ddayArrayList.sortBy { CalendarViewModel.daysRemainingToDate(it.endDate) }

                                    delDday(initnextSchedule.substring(0,4).toInt(),initnextSchedule.substring(5,7).toInt(),curId)
                                    addDday(nextScheduleNum.text.substring(0,4).toInt(),nextScheduleNum.text.substring(5,7).toInt(),curId
                                    )
                                    buffering.dismiss()
                                    Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
                                }
                                2 -> {
                                    buffering.dismiss()
                                    Toast.makeText(context, "추가 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        if(curCycle!="N") {

                        }
                        CalendarViewModel.editCalendar(
                            CalendarDataEdit(curId,binding.textTitle.text.toString(),preScheduleNum.text.toString(),nextScheduleNum.text.toString(),
                            curColor,curCycle,curRepeatText,CalendarViewModel.timeChangeNum(binding.preScheldule2.text.toString()),CalendarViewModel.timeChangeNum(binding.nextScheldule2.text.toString()) , "N",binding.textMemo.text.toString()),curId) { result ->
                            when (result) {
                                1 -> {
                                    Toast.makeText(context, "수정 성공", Toast.LENGTH_SHORT).show()
                                    for(data in CalendarViewModel.ddayArrayList) {
                                        if(data.id==curId) {
                                            CalendarViewModel.ddayArrayList.remove(data)
                                            break
                                        }
                                    }
                                    if(curCycle=="N"&&initCycle=="N") {
                                        delCal(curId)
                                        addCal(curId)
                                    }
                                    else  CalendarViewModel.hashMapArrayCal.clear()
                                    buffering.dismiss()
                                    Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
                                }
                                2 -> {
                                    buffering.dismiss()
                                    Toast.makeText(context, "수정 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
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

    private fun toggleLayout(isExpanded: Boolean, layoutExpand: LinearLayout): Boolean {
        if (isExpanded &&layoutExpand.visibility == View.GONE) {
            ToggleAnimation.expand(layoutExpand)
        } else if(!isExpanded &&layoutExpand.visibility == View.VISIBLE){
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

    private fun addCal(tmpId : Int) {
        val startDate = LocalDate.parse(preScheduleNum.text.toString(), DateTimeFormatter.ISO_DATE)
        val endDate = LocalDate.parse(nextScheduleNum.text.toString(), DateTimeFormatter.ISO_DATE)

        var durationTmp = true
        if(startDate==endDate) durationTmp=false

        var currentDate = startDate.plusMonths(-1)
        val endDatePlusOne = endDate.plusDays(2)  // Including the end date

        while (currentDate.isBefore(endDatePlusOne)) {
            val yearMonth = YearMonth.from(currentDate)
            val formattedYearMonth = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-M"))
            if(CalendarViewModel.hashMapArrayCal.get(formattedYearMonth)==null){
                CalendarViewModel.hashMapArrayCal.put(formattedYearMonth,ArrayList<AndroidCalendarData>())
            }
            CalendarViewModel.hashMapArrayCal.get(formattedYearMonth)?.add(AndroidCalendarData(preScheduleNum.text.toString(),preScheduleNum.text.toString(),nextScheduleNum.text.toString(),
                CalendarViewModel.timeChangeNum(binding.preScheldule2.text.toString()),CalendarViewModel.timeChangeNum(binding.nextScheldule2.text.toString()),curColor,"N",curDday,binding.textTitle.text.toString(),
                -1,durationTmp,binding.textMemo.text.toString(),"CAL",tmpId,curRepeatText))

            currentDate = currentDate.plusMonths(1)
        }
    }
    private fun delCal(curId: Int) {
        val startDate = LocalDate.parse(calData.startDate, DateTimeFormatter.ISO_DATE)
        val endDate = LocalDate.parse(calData.endDate, DateTimeFormatter.ISO_DATE)

        var currentDate = startDate.plusMonths(-1)
        val endDatePlusOne = endDate.plusDays(2)  // Including the end date

        while (currentDate.isBefore(endDatePlusOne)) {
            val yearMonth = YearMonth.from(currentDate)
            val formattedYearMonth = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-M"))
            val tmpArrayList = CalendarViewModel.hashMapArrayCal.get(formattedYearMonth)
            if(tmpArrayList!=null) {
                for(data in tmpArrayList!!) {
                    if(data.id==curId) {
                        tmpArrayList.remove(data)
                        break
                    }
                }
            }
            currentDate = currentDate.plusMonths(1)
        }
    }
    private fun addDday(Year : Int, Month : Int, tmpId :Int) {

        if(CalendarViewModel.hashMapArrayCal.get("${Year}-${Month}")==null){
            CalendarViewModel.hashMapArrayCal.put("${Year}-${Month}",ArrayList<AndroidCalendarData>())
        }

        CalendarViewModel.hashMapArrayCal.get("${Year}-${Month}")?.add(AndroidCalendarData(nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),nextScheduleNum.text.toString(),
            "10:00:00","11:00:00",curColor,"N","Y",binding.textTitle.text.toString(),
            -1,false,binding.textMemo.text.toString(),"CAL",tmpId,curRepeatText))

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