package com.example.myapplication.CalenderFuntion

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarAddBinding
import com.example.myapplication.databinding.CalendarAddSBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date


class CalendarAddSFragment : Fragment() {
    lateinit var binding: CalendarAddSBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalendarAddSBinding.inflate(layoutInflater)

        val title = arguments?.getString("title")
        val preSchedule = arguments?.getString("preSchedule")
        val nextSchedule = arguments?.getString("nextSchedule")
        val preClock = arguments?.getString("preClock")
        val nextClock = arguments?.getString("nextClock")
        val cycle = arguments?.getString("cycle")
        val memo = arguments?.getString("memo")

        //데이터 받아서 로드하기
        binding.title.text = "제목"
        binding.calendarColor.setColorFilter(resources.getColor(R.color.sub5), PorterDuff.Mode.SRC_IN)
        binding.preScheldule.text = "  6월 9일 (금)  "
        binding.nextScheldule.text ="  6월 12일 (월)  "

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            //뭐가 있다면
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_add_popup, null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
                .create()
            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            mBuilder.show()
            val display = requireActivity().windowManager.defaultDisplay
            mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
                mBuilder.dismiss()
            })
            mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
                mBuilder.dismiss()
            })
            //onBackPressed()
        }
        binding.button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_calendarAdd_to_fragCalendar)
        }

    }
}