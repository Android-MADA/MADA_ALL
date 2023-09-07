package com.example.myapplication.MyFuction.Calendar


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.getWeekList
import com.example.myapplication.databinding.MyRecordSliderWeekViewBinding
import org.joda.time.DateTime


class MyWeekFragment() : Fragment() {

    private var millis: Long = 0L
    lateinit var binding: MyRecordSliderWeekViewBinding

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
        binding = MyRecordSliderWeekViewBinding.inflate(inflater, container, false)
        binding.calendarView.initCalendar(getWeekList(DateTime(millis)))
        return binding.root
    }
    companion object {

        private const val MILLIS = "MILLIS"

        fun newInstance(millis: Long) = MyWeekFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
        }
    }
}