package com.example.myapplication.TimeFunction.calendar


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.getMonthList
import com.example.myapplication.TimeFunction.TimeViewModel
import com.example.myapplication.databinding.TimeSliderMonthViewBinding
import org.joda.time.DateTime
import java.sql.Time


class TimeMonthFragment() : Fragment() {

    private var millis: Long = 0L
    lateinit var binding: TimeSliderMonthViewBinding
    lateinit var fm: Fragment
    lateinit var dialog: TimeBottomSheetDialog
    lateinit var vmTime : TimeViewModel
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
        binding = TimeSliderMonthViewBinding.inflate(inflater, container, false)
        binding.calendarView.initCalendar(getMonthList(DateTime(millis)),vmTime,dialog,DateTime(millis))
        return binding.root
    }
    companion object {

        private const val MILLIS = "MILLIS"

        fun newInstance(
            millis: Long,
            theFm: Fragment,
            dl: TimeBottomSheetDialog,
            viewModelTime: TimeViewModel
        ) = TimeMonthFragment().apply {
            arguments = Bundle().apply {
                fm = theFm
                putLong(MILLIS, millis)
                dialog = dl
                vmTime =viewModelTime
            }
        }
    }
}