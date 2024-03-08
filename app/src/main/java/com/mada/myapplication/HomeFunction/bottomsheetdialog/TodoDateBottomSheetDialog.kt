package com.mada.myapplication.HomeFunction.bottomsheetdialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.R
import com.mada.myapplication.TimeFunction.calendar.TimeMonthSliderAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TodoDateBottomSheetDialog(viewModel: HomeViewModel) : BottomSheetDialogFragment() {
    val viewModel = viewModel
    var menuFlag = "calendar"
    private var listener: OnSendFromBottomSheetDialog? = null

    override fun onStart() {
        super.onStart()

        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)

        var view = if(menuFlag == "todoMenu"){
            inflater.inflate(R.layout.todo_menu_bottomsheet_dialog, container, false)
        }
        else{
            inflater.inflate(R.layout.time_calendar_view, container, false)
        }
        return view
    }
    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(menuFlag == "todoMenu"){
            viewModel.isTodoMenu = true
            val todoMenuDelete = view?.findViewById<LinearLayout>(R.id.todo_menu_delete_layout)
            val todoMenuEdit = view?.findViewById<LinearLayout>(R.id.todo_menu_edit_layout)
            val todoMenuChangeDate = view?.findViewById<LinearLayout>(R.id.todo_menu_date_layout)
            val calView = view?.findViewById<LinearLayout>(R.id.cal)
            val dateSaveBtn = view?.findViewById<TextView>(R.id.todo_menu_date_save_btn)

            todoMenuDelete?.setOnClickListener {
                Log.d("todo menu", "delete click")
                if(listener == null) return@setOnClickListener
                listener?.sendValue("delete")
                dismiss()
            }

            todoMenuEdit?.setOnClickListener {
                Log.d("todo menu", "edit click")
                if(listener == null) return@setOnClickListener
                listener?.sendValue("edit")
                dismiss()
            }

            todoMenuChangeDate?.setOnClickListener {

                if(calView!!.isVisible){
                    calView?.isGone = true
                }
                else{
                    calView?.isVisible = true
                }
            }

            dateSaveBtn?.setOnClickListener {
                if(listener == null) return@setOnClickListener
                listener?.sendValue("date")
                dismiss()
            }


        }

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

    interface OnSendFromBottomSheetDialog {
        fun sendValue(value: String)
    }

    fun setCallback(listener: OnSendFromBottomSheetDialog) {
        this.listener = listener
    }
}