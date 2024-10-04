package com.mada.myapplication.ChartFunction.Calendar


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.getWeekList
import com.mada.myapplication.databinding.MyRecordSliderWeekViewBinding
import org.joda.time.DateTime


class MyWeekFragment() : Fragment() {

    private var millis: Long = 0L
    var selectDay:TextView?=null
    var fm: Fragment? = null
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
        fm?.let { binding.calendarView.initCalendar2(getWeekList(DateTime(millis)), it,selectDay) }
        return binding.root
    }
    companion object {

        private const val MILLIS = "MILLIS"

        fun newInstance(millis: Long, selectDay: TextView?, fm: Fragment) = MyWeekFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
            this.selectDay = selectDay
            this.fm = fm
        }
    }
}