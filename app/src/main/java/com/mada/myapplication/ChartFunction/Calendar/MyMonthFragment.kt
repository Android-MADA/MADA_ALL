package com.mada.myapplication.ChartFunction.Calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.getMonthList
import com.mada.myapplication.ChartFunction.Fragment.FragChartMonth
import com.mada.myapplication.databinding.MyRecordSliderMonthViewBinding
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

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

        // MyMonthView에서 하위 뷰에 대한 클릭 리스너 추가
        binding.calendarView.children.forEach { dayItemView ->
            dayItemView.setOnClickListener {
                val date = (dayItemView as MyItemView).date
                Log.d("달력 선택", "${date}")
                val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
                (parentFragment as? FragChartMonth)?.monthChange(formatter.print(date))
            }
        }

        return binding.root
    }


    companion object {

        private const val MILLIS = "MILLIS"

        fun newInstance(millis: Long) = MyMonthFragment().apply {
            arguments = Bundle().apply {
                //dayOrMonth = theDayOrMonth
                //fm = theFm
                putLong(MILLIS, millis)
            }
        }
    }
}
