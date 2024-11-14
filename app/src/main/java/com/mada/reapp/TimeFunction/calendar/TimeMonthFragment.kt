package com.mada.reapp.TimeFunction.calendar


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.mada.reapp.CalenderFuntion.Calendar.CalendarUtils.Companion.getMonthList
import com.mada.reapp.databinding.TimeSliderMonthViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.joda.time.DateTime


class TimeMonthFragment() : Fragment() {

    private var millis: Long = 0L
    lateinit var binding: TimeSliderMonthViewBinding
    lateinit var fm: Fragment
    lateinit var dialog: BottomSheetDialogFragment
    lateinit var vmTime : ViewModel
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
            dl: BottomSheetDialogFragment,
            viewModelTime: ViewModel
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