package com.mada.myapplication.ChartFunction.Calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.getMonthList
import com.mada.myapplication.databinding.MyRecordSliderMonthViewBinding
import org.joda.time.DateTime

class MyMonthFragment : Fragment() {

    private var millis: Long = 0L
    lateinit var binding: MyRecordSliderMonthViewBinding

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

        fun newInstance(millis: Long) = MyMonthFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
        }
    }
}
