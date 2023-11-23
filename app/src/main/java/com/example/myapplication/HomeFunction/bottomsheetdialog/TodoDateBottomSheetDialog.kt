package com.example.myapplication.HomeFunction.bottomsheetdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.TimeFunction.calendar.TimeMonthSliderAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TodoDateBottomSheetDialog(viewModel: HomeViewModel) : BottomSheetDialogFragment() {
    val viewModel = viewModel
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
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
/*
        view?.findViewById<LinearLayout>(R.id.todo_menu_delete_layout)?.setOnClickListener {
            //다이얼로그 띄우기
            Log.d("todo menu", "delete click")
            dismiss()
        }

        view?.findViewById<LinearLayout>(R.id.todo_menu_edit_layout)?.setOnClickListener {
            Log.d("todo menu", "edit click")
            dismiss()
        }

        view?.findViewById<LinearLayout>(R.id.todo_menu_date_layout)?.setOnClickListener {
            view?.findViewById<TextView>(R.id.calendar_tv)?.isVisible = true
        }*/

        //달력 view
        val vp = view?.findViewById<ViewPager2>(R.id.time_vp)
        val monthText = view?.findViewById<TextView>(R.id.textCalendar)
        val preBtn = view?.findViewById<ImageButton>(R.id.preBtn)
        val nextBtn = view?.findViewById<ImageButton>(R.id.nextBtn)
        if (vp != null && preBtn != null && nextBtn != null&&monthText!=null) {
            val calendarAdapter = TimeMonthSliderAdapter(this,monthText ,vp,this,viewModel)
            vp.adapter = calendarAdapter
            vp.setCurrentItem(CalendarSliderAdapter.START_POSITION, false)
            preBtn.setOnClickListener {
                vp.setCurrentItem(vp.currentItem-1, true)
            }
            nextBtn.setOnClickListener {
                vp.setCurrentItem(vp.currentItem+1, true)
            }
        }

    }
}