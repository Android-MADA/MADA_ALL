package com.mada.myapplication.ChartFunction.Calendar

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.mada.myapplication.ChartFunction.Fragment.FragChartMonth
import org.joda.time.DateTime

class MyMonthSliderlAdapter(
    fm: Fragment,
    private val monthWeekText: TextView,
    private val viewPager: ViewPager2
) : FragmentStateAdapter(fm) {
    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val millis = getItemId(position)

                monthWeekText.text = DateTime(millis).year.toString() + "년 "+ DateTime(millis).monthOfYear.toString()+ "월 "

                (fm as? FragChartMonth)?.monthChange(DateTime(millis).toString("yyyy-MM-dd"))
            }
        })
    }
    /* 달의 첫 번째 Day timeInMillis*/
    private var start: Long = DateTime().withDayOfMonth(1).withTimeAtStartOfDay().millis

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): MyMonthFragment {
        val millis = getItemId(position)
        return MyMonthFragment.newInstance(millis)
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
