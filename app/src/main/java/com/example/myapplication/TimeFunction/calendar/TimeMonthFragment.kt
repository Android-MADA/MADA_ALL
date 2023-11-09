package com.example.myapplication.TimeFunction.calendar


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.getMonthList
import com.example.myapplication.databinding.MyRecordSliderMonthViewBinding
import org.joda.time.DateTime


class TimeMonthFragment() : Fragment() {

    private var millis: Long = 0L
    lateinit var binding: MyRecordSliderMonthViewBinding
    lateinit var fm: Fragment
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
        binding = MyRecordSliderMonthViewBinding.inflate(inflater, container, false)
        binding.calendarView.initCalendar(getMonthList(DateTime(millis)))
        return binding.root
    }
    companion object {

        private const val MILLIS = "MILLIS"

        fun newInstance(millis: Long, theFm: Fragment) = TimeMonthFragment().apply {
            arguments = Bundle().apply {
                fm = theFm
                putLong(MILLIS, millis)
            }
        }
    }
}