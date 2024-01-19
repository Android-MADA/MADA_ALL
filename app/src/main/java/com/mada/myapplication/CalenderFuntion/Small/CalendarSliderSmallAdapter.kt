package com.mada.myapplication.CalenderFuntion.Small

import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import org.joda.time.DateTime

class CalendarSliderSmallAdapter(
    fm: Fragment,
    private val yearmonthText: TextView,
    private val viewPager: ViewPager2,
    private val Scheldule: TextView,
    private val ScheduleNum: TextView,
    private val cal: LinearLayout,
    private val textDday :TextView,
    private var repeatFlag : Boolean = false
) : FragmentStateAdapter(fm) {
    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val millis = getItemId(position)
                yearmonthText.text = DateTime(millis).year.toString() + "년 " + DateTime(millis).monthOfYear.toString() + "월"

            }
        })
    }
    /* 달의 첫 번째 Day timeInMillis*/
    private var start: Long = DateTime().withDayOfMonth(1).withTimeAtStartOfDay().millis

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): CalendarSmallFragment {
        val millis = getItemId(position)
        return CalendarSmallFragment.newInstance(millis,Scheldule,ScheduleNum,cal,textDday, repeatFlag)
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