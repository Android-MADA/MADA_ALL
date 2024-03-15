package com.mada.myapplication.CalenderFuntion

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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mada.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.mada.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.mada.myapplication.R
import com.mada.myapplication.databinding.CalendarAddSBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class CalendarAddSFragment : Fragment() {
    lateinit var binding: CalendarAddSBinding
    lateinit var calData : AndroidCalendarData
    private val CalendarViewModel : CalendarViewModel by activityViewModels()

    private val weekdays = arrayOf("일", "월","화", "수", "목", "금", "토")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = CalendarAddSBinding.inflate(layoutInflater)
        hideBootomNavigation(true)

        calData = arguments?.getSerializable("calData") as AndroidCalendarData

        binding.title.text = calData.title
        binding.calendarColor.setColorFilter(Color.parseColor(calData.color), PorterDuff.Mode.SRC_IN)
        binding.preScheldule.text = CalendarViewModel.convertToDateKoreanFormat(calData.startDate)
        binding.nextScheldule.text = CalendarViewModel.convertToDateKoreanFormat(calData.endDate)
        if(calData.dDay == "N") {
            binding.preScheldule2.text = CalendarViewModel.TimeChangeKor(calData.startTime)
            binding.nextScheldule2.text = CalendarViewModel.TimeChangeKor(calData.endTime)
        } else {
            binding.textDday.visibility = View.VISIBLE
            binding.ddayCb.isChecked = true
            binding.textDday.text = "D-${CalendarViewModel.daysRemainingToDate(calData.endDate)}"
            binding.textDday.setTextColor(Color.parseColor(calData.color))
        }
        if(calData.repeat!="N") {
            //binding.calAndClock.visibility = View.GONE
        }
        binding.cycle.text = processInput(calData.repeat)
        if(calData.memo =="") binding.memoLayout.visibility = View.GONE
        else binding.memo.text = calData.memo
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_calendarAddS_to_fragCalendar)
        }
        binding.calendarDeleteBtn.setOnClickListener {
            if(true) { // calData.repeat=="N"
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
                mDialogView.findViewById<TextView>(R.id.textTitle).setText("일정을 삭제하시겠습니까?")
                mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
                    mBuilder.dismiss()
                })
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                    CalendarViewModel.deleteCalendar(calData.id) { result ->
                        when (result) {
                            1 -> {
                                Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show()

                                if(calData.repeat!=="N") {
                                    delCal(calData.id)
                                } else {
                                    CalendarViewModel.hashMapArrayCal.clear()
                                }
                                findNavController().navigate(R.id.action_calendarAddS_to_fragCalendar)
                            }
                            2 -> {
                                Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }


                    mBuilder.dismiss()
                })
            } else {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_repeat_popup, null)
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

                var btn1Click = false
                var btn2Click = false
                var btn3Click = false
                var whatBtn = 0
                mDialogView.findViewById<ImageView>(R.id.btn_del_this).setOnClickListener( {
                    if(btn1Click) {
                        btn1Click = false
                        mDialogView.findViewById<ImageView>(R.id.btn_del_this).setColorFilter(Color.parseColor("#F5F5F5"))
                    } else {
                        btn1Click = true
                        btn2Click = false
                        btn3Click = false
                        whatBtn = 2
                        mDialogView.findViewById<ImageView>(R.id.btn_del_this).setColorFilter(Color.parseColor("#486DA3"))
                        mDialogView.findViewById<ImageView>(R.id.btn_del_fromto).setColorFilter(Color.parseColor("#F5F5F5"))
                        mDialogView.findViewById<ImageView>(R.id.btn_del_all).setColorFilter(Color.parseColor("#F5F5F5"))
                    }
                })
                mDialogView.findViewById<ImageView>(R.id.btn_del_fromto).setOnClickListener( {
                    if(btn2Click) {
                        btn2Click = false
                        mDialogView.findViewById<ImageView>(R.id.btn_del_fromto).setColorFilter(Color.parseColor("#F5F5F5"))
                    } else {
                        btn1Click = false
                        btn2Click = true
                        btn3Click = false
                        whatBtn = 1
                        mDialogView.findViewById<ImageView>(R.id.btn_del_this).setColorFilter(Color.parseColor("#F5F5F5"))
                        mDialogView.findViewById<ImageView>(R.id.btn_del_fromto).setColorFilter(Color.parseColor("#486DA3"))
                        mDialogView.findViewById<ImageView>(R.id.btn_del_all).setColorFilter(Color.parseColor("#F5F5F5"))
                    }
                })
                mDialogView.findViewById<ImageView>(R.id.btn_del_all).setOnClickListener( {
                    if(btn3Click) {
                        btn3Click = false
                        mDialogView.findViewById<ImageView>(R.id.btn_del_all).setColorFilter(Color.parseColor("#F5F5F5"))
                    } else {
                        btn1Click = false
                        btn2Click = false
                        btn3Click = true
                        whatBtn = 0
                        mDialogView.findViewById<ImageView>(R.id.btn_del_this).setColorFilter(Color.parseColor("#F5F5F5"))
                        mDialogView.findViewById<ImageView>(R.id.btn_del_fromto).setColorFilter(Color.parseColor("#F5F5F5"))
                        mDialogView.findViewById<ImageView>(R.id.btn_del_all).setColorFilter(Color.parseColor("#486DA3"))
                    }
                })

                mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
                    mBuilder.dismiss()
                })
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                    if(btn1Click||btn2Click||btn3Click) {
                        CalendarViewModel.delRepeat(calData.id,whatBtn,calData.startDate) { result ->
                            when (result) {
                                1 -> {
                                    Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show()
                                    CalendarViewModel.hashMapArrayCal.clear()
                                    findNavController().navigate(R.id.action_calendarAddS_to_fragCalendar)
                                }
                                2 -> {
                                    Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        mBuilder.dismiss()
                    }

                })
            }

        }
        binding.calendarS.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("edit",true)
            bundle.putSerializable("calData",calData)
            Navigation.findNavController(view).navigate(R.id.action_calendarAddS_to_calendarAdd,bundle)
        }
        binding.title.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("edit",true)
            bundle.putSerializable("calData",calData)
            Navigation.findNavController(view).navigate(R.id.action_calendarAddS_to_calendarAdd,bundle)
        }
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
    fun processInput(input: String): String {
        return when (input) {
            "D" -> "매일"
            "W" -> "매주 " + weekdays[calData.repeatDate.toInt()]+"요일"
            "M" -> {
                if(calData.repeatDate==32) "매월 " + "마지막 날"
                else "매월 " + calData.repeatDate + "일"
            }
            "Y" -> { //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 수정
                //val  tmp = calData.repeatDate.split("-")
                "매년 "//${tmp[0].toInt()}월 ${tmp[1].toInt()}일"
            }
            else -> "반복 안함"
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
}