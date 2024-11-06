package com.mada.reapp.CalenderFuntion.Calendar


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mada.reapp.CalenderFuntion.Calendar.CalendarUtils.Companion.getMonthList
import com.mada.reapp.CalenderFuntion.Model.CalendarViewModel
import com.mada.reapp.databinding.CalendarSliderViewBinding
import org.joda.time.DateTime


class CalendarFragment() : Fragment() {

    private var millis: Long = 0L
    lateinit var binding: CalendarSliderViewBinding
    private val CalendarViewModel : CalendarViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            millis = it.getLong(MILLIS)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalendarSliderViewBinding.inflate(inflater, container, false)


        binding.calendarView.initCalendar(DateTime(millis), getMonthList(DateTime(millis)))

        CalendarViewModel.getMonthDataArray(DateTime(millis).monthOfYear,DateTime(millis).year) { result ->
            when (result) {
                1 -> {/*
                    CalendarViewModel.getRepeat {
                        binding.calendarView2.getCalendarData(getMonthList(DateTime(millis)), DateTime(millis).year.toString(),DateTime(millis).monthOfYear.toString(),CalendarViewModel)
                    }*/
                    binding.calendarView2.getCalendarData(getMonthList(DateTime(millis)), DateTime(millis).year.toString(),DateTime(millis).monthOfYear.toString(),CalendarViewModel)
                }
                2 -> {
                    //binding.calendarView2.getCalendarData(getMonthList(DateTime(millis)), DateTime(millis).year.toString(),DateTime(millis).monthOfYear.toString(),CalendarViewModel)
                    if(context!=null)
                        Toast.makeText(context, "서버와의 통신 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //CalendarViewModel.tmpFun()
        //이 달 데이터 가져오기



        return binding.root
    }

    companion object {

        private const val MILLIS = "MILLIS"

        fun newInstance(millis: Long) = CalendarFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
        }
    }
}