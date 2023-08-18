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
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.CalendarDATA
import com.example.myapplication.CalenderFuntion.Model.CalendarData2
import com.example.myapplication.CalenderFuntion.Model.ResponseSample
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarAddBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
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
            curColor ="#E1E9F5"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor2.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.main), PorterDuff.Mode.SRC_IN)
            curColor ="#89A9D9"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor3.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.point_main), PorterDuff.Mode.SRC_IN)
            curColor ="#2AA1B7"
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
            curColor ="#E1E9F5"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor8.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub6), PorterDuff.Mode.SRC_IN)
            curColor ="#FFE7EB"
            toggleLayout(false,binding.layoutColorSelectors)
        }
        binding.calendarColor9.setOnClickListener {
            binding.calendarColor.setColorFilter(Color.parseColor("#F5EED1"), PorterDuff.Mode.SRC_IN)
            curColor ="#F5EED1"
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
                binding.calendarColor.setColorFilter(resources.getColor(R.color.sub5), PorterDuff.Mode.SRC_IN)
                curDday = "Y"
            } else {
                binding.textDday.visibility= View.GONE
                binding.clockAndCycle.visibility = View.VISIBLE
                binding.layoutColorSelector2.visibility = View.GONE
                binding.layoutColorSelector1.visibility = View.VISIBLE
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
        binding.cyclebtn.setOnClickListener {
            binding.chipGroup.visibility = View.VISIBLE
        }
        chip1.setOnClickListener {
            binding.cyclebtn.text = "반복 안함"
            curRepeat = "No"
            binding.calAll.visibility = View.VISIBLE
            chip1.setChipBackgroundColorResource(R.color.sub5)
            chip2.setChipBackgroundColorResource(R.color.white)
            chip3.setChipBackgroundColorResource(R.color.white)
            chip4.setChipBackgroundColorResource(R.color.white)
            chip5.setChipBackgroundColorResource(R.color.white)
            chip2.isChecked = false
            chip3.isChecked = false
            chip4.isChecked = false
            chip5.isChecked = false
            binding.repeatWeek.visibility = View.GONE
        }
        chip2.setOnClickListener {
            binding.cyclebtn.text = "매일"
            curRepeat = "Day"
            binding.calAll.visibility = View.GONE
            chip1.setChipBackgroundColorResource(R.color.white)
            chip2.setChipBackgroundColorResource(R.color.sub5)
            chip3.setChipBackgroundColorResource(R.color.white)
            chip4.setChipBackgroundColorResource(R.color.white)
            chip5.setChipBackgroundColorResource(R.color.white)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
            chip1.isChecked = false
            chip3.isChecked = false
            chip4.isChecked = false
            chip5.isChecked = false
        }
        chip3.setOnClickListener {
            binding.cyclebtn.text = "매주"
            curRepeat = "Week"
            binding.calAll.visibility = View.GONE
            binding.repeatMonth.visibility = View.VISIBLE
            binding.repeatMonth.visibility = View.GONE
            chip1.setChipBackgroundColorResource(R.color.white)
            chip2.setChipBackgroundColorResource(R.color.white)
            chip3.setChipBackgroundColorResource(R.color.sub5)
            chip4.setChipBackgroundColorResource(R.color.white)
            chip5.setChipBackgroundColorResource(R.color.white)
            chip2.isChecked = false
            chip1.isChecked = false
            chip4.isChecked = false
            chip5.isChecked = false
        }
        chip4.setOnClickListener {
            binding.cyclebtn.text = "매월"
            curRepeat = "Month"
            binding.calAll.visibility = View.GONE
            chip1.setChipBackgroundColorResource(R.color.white)
            chip2.setChipBackgroundColorResource(R.color.white)
            chip3.setChipBackgroundColorResource(R.color.white)
            chip4.setChipBackgroundColorResource(R.color.sub5)
            chip5.setChipBackgroundColorResource(R.color.white)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.VISIBLE
            chip2.isChecked = false
            chip3.isChecked = false
            chip1.isChecked = false
            chip5.isChecked = false
        }
        chip5.setOnClickListener {
            binding.cyclebtn.text = "매년"
            curRepeat = "Year"
            binding.calAll.visibility = View.GONE
            chip1.setChipBackgroundColorResource(R.color.white)
            chip2.setChipBackgroundColorResource(R.color.white)
            chip3.setChipBackgroundColorResource(R.color.white)
            chip4.setChipBackgroundColorResource(R.color.white)
            chip5.setChipBackgroundColorResource(R.color.sub5)
            binding.repeatWeek.visibility = View.GONE
            binding.repeatMonth.visibility = View.GONE
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
                if(edit) {
                    mDialogView.findViewById<TextView>(R.id.textTitle).text = "수정하지 않고 나가시겠습니가?"
                }
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
        val datasDday = ArrayList<CalendarDATA>()
        /*
        val datasDday = arrayOf(        //임시 데이터, 끝나는 날짜 순서대로 정렬해야함
            CalendarDATA(
                "2023-7-21", "2023-7-21", "2023-8-31", "", "",
                "#FFE7EB", "", "Y", "방학", -1, true, "방학이 끝나간다...","CAL"
            ),
            CalendarDATA(
                "2023-7-2", "2023-7-2", "2023-9-2", "", "",
                "#E1E9F5", "", "Y", "UMC 데모데이", -1, true, "메모는 여기에 뜨게 하면 될것 같습니다!","CAL"
            ),
            CalendarDATA(
                    "2023-7-1", "2023-7-1", "2023-11-27", "", "",
            "#F5EED1", "", "Y", "생일 ", -1, true, "이날을 기다리고 있어","CAL"
            )
        )
         */

        binding.submitBtn.setOnClickListener {
            if(compareDates(nextSchedule.text.toString(),preSchedule.text.toString())) {
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
            } else if(compareTimes(binding.preScheldule2.text.toString(),binding.nextScheldule2.text.toString())) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_add_popup_one, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()

                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                mDialogView.findViewById<TextView>(R.id.textTitle).text = "올바른 시간을 입력해 주십시오"
                val display = requireActivity().windowManager.defaultDisplay
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                    mBuilder.dismiss()
                })
            }
            else {
                if(binding.checkBox.isChecked&&datasDday.size==3) {
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
                        val color = datasDday[i].color
                        val imageResource = when (color) {
                            "#E1E9F5" -> R.drawable.calendar_ddayblue_smallbackground
                            "#FFE7EB" -> R.drawable.calendar_ddaypink_smallbackground
                            "#F5EED1" -> R.drawable.calendar_ddayyellow_smallbackground
                            else -> R.drawable.calendar_dday_plus
                        }
                        when (i) {
                            0 -> {
                                mDialogView.findViewById<ImageView>(R.id.image1).setImageResource(imageResource)
                                mDialogView.findViewById<TextView>(R.id.textDday1).text = "D-${daysRemainingToDate(datasDday[i].endDate)}"
                                mDialogView.findViewById<TextView>(R.id.textTitle1).text = datasDday[i].title
                            }
                            1 -> {
                                mDialogView.findViewById<ImageView>(R.id.image2).setImageResource(imageResource)
                                mDialogView.findViewById<TextView>(R.id.textDday2).text = "D-${daysRemainingToDate(datasDday[i].endDate)}"
                                mDialogView.findViewById<TextView>(R.id.textTitle2).text = datasDday[i].title
                            }
                            2 -> {
                                mDialogView.findViewById<ImageView>(R.id.image3).setImageResource(imageResource)
                                mDialogView.findViewById<TextView>(R.id.textDday3).text = "D-${daysRemainingToDate(datasDday[i].endDate)}"
                                mDialogView.findViewById<TextView>(R.id.textTitle3).text = datasDday[i].title
                            }
                        }
                    }
                    checkBox1.setOnClickListener {
                        checkBox2.isChecked = false
                        checkBox3.isChecked = false
                    }
                    checkBox2.setOnClickListener {
                        checkBox1.isChecked = false
                        checkBox3.isChecked = false
                    }
                    checkBox3.setOnClickListener {
                        checkBox2.isChecked = false
                        checkBox1.isChecked = false
                    }
                    mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
                        mBuilder.dismiss()
                    })
                    mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                        if(!checkBox1.isChecked&&!checkBox2.isChecked&&!checkBox3.isChecked) {
                            mDialogView.findViewById<TextView>(R.id.textInfo).text = "대체할 디데이를 선택해야 합니다"
                        } else {
                            //addCalendar(CalendarData2())                                                      @@@@@@@@@@@@@@@@@@@@@@정보 추가해야함
                            Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
                            mBuilder.dismiss()
                        }

                    })
                } else if(binding.checkBox.isChecked) {
                    if(binding.textDday.text=="D - 0") {
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
                    } else {
                        eidtCalendar( CalendarData2( binding.textTitle.text.toString(),convertToDateKoreanFormat2(preSchedule.text.toString()),convertToDateKoreanFormat2(nextSchedule.text.toString()),
                            curColor,curRepeat,curDday,binding.textMemo.text.toString(),
                            timeChange(binding.preScheldule2.text.toString()),timeChange(binding.nextScheldule2.text.toString()) ),id2 )
                    }
                }
                else {
                    if(binding.submitBtn.text =="등록") {
                        addCalendar( CalendarData2( binding.textTitle.text.toString(),convertToDateKoreanFormat2(preSchedule.text.toString()),convertToDateKoreanFormat2(nextSchedule.text.toString()),
                            curColor,curRepeat,curDday,binding.textMemo.text.toString(),
                            timeChange(binding.preScheldule2.text.toString()),timeChange(binding.nextScheldule2.text.toString()) ) )

                    } else if(binding.submitBtn.text =="수정"){
                        //dasdasd
                        eidtCalendar( CalendarData2( binding.textTitle.text.toString(),convertToDateKoreanFormat2(preSchedule.text.toString()),convertToDateKoreanFormat2(nextSchedule.text.toString()),
                            curColor,curRepeat,curDday,binding.textMemo.text.toString(),
                            timeChange(binding.preScheldule2.text.toString()),timeChange(binding.nextScheldule2.text.toString()) ),id2 )


                    } else {

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
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
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
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

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
        val inputFormat = SimpleDateFormat("  a h:mm  ", Locale.getDefault()) // 수정된 형식
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
        val inputFormat = SimpleDateFormat("  a h:mm  ", Locale.getDefault())
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

}