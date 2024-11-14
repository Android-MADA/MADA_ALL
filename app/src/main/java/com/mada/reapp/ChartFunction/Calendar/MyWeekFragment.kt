package com.mada.reapp.ChartFunction.Calendar


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mada.reapp.CalenderFuntion.Calendar.CalendarUtils.Companion.getWeekList
import com.mada.reapp.databinding.MyRecordSliderWeekViewBinding
import org.joda.time.DateTime


class MyWeekFragment() : Fragment() {

    private var millis: Long = 0L
    var selectDay:TextView?=null
    lateinit var fm:Fragment
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
        binding.calendarView.initCalendar2(getWeekList(DateTime(millis)),fm,selectDay)
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