package com.mada.reapp.CalenderFuntion.Calendar

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import org.joda.time.DateTime

class CalendarSliderAdapter(fm: Fragment,private val yearText : TextView, private val monthText : TextView, private val viewPager: ViewPager2) : FragmentStateAdapter(fm) {
    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val millis = getItemId(position)
                yearText.text = DateTime(millis).year.toString() + "년"
                monthText.text = DateTime(millis).monthOfYear.toString() + "월"

            }
        })
    }


    /* 달의 첫 번째 Day timeInMillis*/
    private var start: Long = DateTime().withDayOfMonth(1).withTimeAtStartOfDay().millis

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): CalendarFragment {
        val millis = getItemId(position)
        return CalendarFragment.newInstance(millis)
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