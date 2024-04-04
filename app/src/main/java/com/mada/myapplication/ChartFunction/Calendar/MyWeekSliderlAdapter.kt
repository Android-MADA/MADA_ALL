package com.mada.myapplication.ChartFunction.Calendar

import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.mada.myapplication.ChartFunction.Fragment.FragChartWeek
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants

class MyWeekSliderlAdapter(
    private val fm: Fragment,
    private val monthWeekText: TextView,
    private val viewPager: ViewPager2,
    private val selectDay: TextView?
) : FragmentStateAdapter(fm) {
    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val millis = getItemId(position)

                val weekOfMonthText = when (getWeekOfMonth(DateTime(millis))) {
                    1 -> "첫째"
                    2 -> "둘째"
                    3 -> "셋째"
                    4 -> "넷째"
                    5 -> "다섯째"
                    else -> "Invalid week"
                }
                monthWeekText.text = DateTime(millis).year.toString() + "년 "+ DateTime(millis).monthOfYear.toString() + "월 "

                Log.d("123",monthWeekText.text.toString())
                (fm as? FragChartWeek)?.weekChange(DateTime(millis).monthOfYear,getWeekOfMonth(DateTime(millis)),DateTime(millis).toString("yyyy-MM-dd"))
            }
        })
    }
    /* 달의 첫 번째 Day timeInMillis*/
    private var start: Long = DateTime().withDayOfWeek(6).withTimeAtStartOfDay().millis

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): MyWeekFragment {
        val millis = getItemId(position)
        return MyWeekFragment.newInstance(millis,selectDay,fm)
    }

    override fun getItemId(position: Int): Long
            = DateTime(start).plusWeeks(position - START_POSITION).millis

    override fun containsItem(itemId: Long): Boolean {
        val date = DateTime(itemId)
        return date.dayOfWeek  == DateTimeConstants.MONDAY && date.millisOfDay == 0
    }

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
    private fun getWeekOfMonth(date: DateTime): Int {
        val firstDayOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstDayOfMonth.dayOfWeek
        val daysToAdd = (DateTimeConstants.DAYS_PER_WEEK - dayOfWeek + 1) % DateTimeConstants.DAYS_PER_WEEK
        val startOfWeek = firstDayOfMonth.plusDays(daysToAdd)
        val weekNumber = (date.dayOfMonth - 1) / DateTimeConstants.DAYS_PER_WEEK + 1
        return if (startOfWeek.monthOfYear == date.monthOfYear) {
            weekNumber
        } else {
            // Adjust for the last week of the previous month
            5
        }
    }
}