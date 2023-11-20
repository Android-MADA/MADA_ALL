package com.example.myapplication.TimeFunction.calendar

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.MyFuction.Fragment.MyRecordMonthFragment
import com.example.myapplication.TimeFunction.TimeViewModel
import org.joda.time.DateTime

class TimeMonthSliderAdapter(
    private val fm: Fragment,
    private val monthText: TextView,
    private val viewPager: ViewPager2,
    private val dialog: TimeBottomSheetDialog,
    private val viewModelTime: TimeViewModel
) : FragmentStateAdapter(fm) {
    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val millis = getItemId(position)
                monthText.text =DateTime(millis).year.toString() + "년 " + DateTime(millis).monthOfYear.toString() + "월"
                (fm as? MyRecordMonthFragment)?.monthChange(DateTime(millis).monthOfYear,DateTime(millis).toString("yyyy-MM-dd"))
            }
        })
    }
    /* 달의 첫 번째 Day timeInMillis*/
    private var start: Long = DateTime().withDayOfMonth(1).withTimeAtStartOfDay().millis

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): TimeMonthFragment {
        val millis = getItemId(position)
        return TimeMonthFragment.newInstance(millis,fm,dialog,viewModelTime)
    }

    override fun getItemId(position: Int): Long
            = DateTime(start).plusMonths(position - START_POSITION).millis

    override fun containsItem(itemId: Long): Boolean {
        val date = DateTime(itemId)
        return date.dayOfMonth == 1 && date.millisOfDay == 0
    }

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
}