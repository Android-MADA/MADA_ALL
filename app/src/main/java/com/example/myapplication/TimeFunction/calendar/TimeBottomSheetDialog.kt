package com.example.myapplication.TimeFunction.calendar
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.example.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TimeBottomSheetDialog(context: Context) : BottomSheetDialogFragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.time_calendar_view, container, false)
        return view
    }
    override fun getTheme(): Int = R.style.newDialog
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        val vp = view?.findViewById<ViewPager2>(R.id.time_vp)
        val monthText = view?.findViewById<TextView>(R.id.textCalendar)
        val preBtn = view?.findViewById<ImageButton>(R.id.preBtn)
        val nextBtn = view?.findViewById<ImageButton>(R.id.nextBtn)
        if (vp != null && preBtn != null && nextBtn != null&&monthText!=null) {
            val calendarAdapter = TimeMonthSliderAdapter(this,monthText ,vp)
            vp.adapter = calendarAdapter
            vp.setCurrentItem(CalendarSliderAdapter.START_POSITION, false)
           preBtn.setOnClickListener {
                vp.setCurrentItem(vp.currentItem-1, true)
            }

            nextBtn.setOnClickListener {
                vp.setCurrentItem(vp.currentItem+1, true)
            }
        }

        super.onActivityCreated(savedInstanceState)
    }
}