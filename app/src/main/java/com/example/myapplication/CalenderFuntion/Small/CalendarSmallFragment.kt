package com.example.myapplication.CalenderFuntion.Small


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.getMonthList
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.databinding.CalendarSliderSmallViewBinding
import org.joda.time.DateTime


class CalendarSmallFragment() : Fragment() {

    private var millis: Long = 0L
    lateinit var binding: CalendarSliderSmallViewBinding
    private val CalendarViewModel : CalendarViewModel by activityViewModels()
    private lateinit var Scheldule: TextView // Change the type if necessary
    private lateinit var ScheduleNum: TextView
    private lateinit var cal: LinearLayout //

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
        binding = CalendarSliderSmallViewBinding.inflate(inflater, container, false)
        binding.calendarView.initCalendar(DateTime(millis), getMonthList(DateTime(millis)),Scheldule,ScheduleNum,cal)
        return binding.root
    }
    fun setValues(Scheldule: TextView, ScheduleNum: TextView, cal: LinearLayout) {
        this.Scheldule = Scheldule
        this.ScheduleNum = ScheduleNum
        this.cal = cal
    }
    companion object {

        private const val MILLIS = "MILLIS"

        fun newInstance(millis: Long, Scheldule: TextView, SchelduleNum: TextView, cal: LinearLayout) = CalendarSmallFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
            setValues(Scheldule, SchelduleNum, cal)
        }
    }
}