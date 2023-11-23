package com.example.myapplication.HomeFunction.bottomsheetdialog

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.example.myapplication.R
import com.example.myapplication.TimeFunction.calendar.TimeMonthSliderAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TodoMenuBottomSheetDialog() : BottomSheetDialogFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
        }

    }
}