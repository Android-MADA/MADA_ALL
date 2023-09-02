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
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myapplication.CalenderFuntion.Model.AddCalendarData
import com.example.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarAddBinding
import com.example.myapplication.databinding.CalendarAddSBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale


class CalendarAddSFragment : Fragment() {
    lateinit var binding: CalendarAddSBinding
    lateinit var calData : AndroidCalendarData
    private val CalendarViewModel : CalendarViewModel by activityViewModels()
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
            binding.ddayLayout.visibility = View.GONE
        } else {
            binding.clcckLayoutBar.visibility = View.GONE
            binding.clockLayout.visibility = View.GONE
            binding.ddayLayout.visibility = View.VISIBLE
            binding.preScheldule.visibility = View.GONE
            binding.textDday.text = "D - ${CalendarViewModel.daysRemainingToDate(calData.endDate)}"
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
                            delCal(calData.id)
                            findNavController().navigate(R.id.action_calendarAddS_to_fragCalendar)
                        }
                        2 -> {
                            Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }


                mBuilder.dismiss()
            })
        }
        binding.calendarS.setOnClickListener {
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
            "Day" -> "매일"
            "Week" -> "매일"
            "Month" -> "매일"
            "Year" -> "매일"
            else -> "반복 안함"
        }
    }
    private fun delCal(curId: Int) {
        val startDate = LocalDate.parse(calData.startDate, DateTimeFormatter.ISO_DATE)
        val endDate = LocalDate.parse(calData.endDate, DateTimeFormatter.ISO_DATE)

        var currentDate = startDate.plusMonths(-1)
        val endDatePlusOne = endDate.plusDays(1)  // Including the end date

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