package com.example.myapplication.Fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.example.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.CustomFunction.CustomViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragCalendarBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shawnlin.numberpicker.NumberPicker
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class FragCalendar : Fragment(){

    lateinit var binding: FragCalendarBinding
    private lateinit var calendarAdapter: CalendarSliderAdapter
    private val CalendarViewModel : CalendarViewModel by activityViewModels()
    private val viewModel: CustomViewModel by viewModels()
    val today = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBootomNavigation(false)
        binding = FragCalendarBinding.inflate(inflater, container, false)
        val viewPager =binding.calendarViewPager
        val tmp = ArrayList<AndroidCalendarData>()
        tmp.add(AndroidCalendarData("2023-08-01","2023-08-01","2023-08-31",
            "10:00:00","11:00:00","#2AA1B7","No","N","안녕하세요 저의 이름은",
            -1,true,"","CAL",-1,""))
        tmp.add(AndroidCalendarData("2023-08-01","2023-08-01","2023-08-06",
            "10:00:00","11:00:00","#2AA1B7","No","N","6",
            -1,true,"","CAL",-1,""))
        tmp.add(AndroidCalendarData("2023-08-01","2023-08-01","2023-08-07",
            "10:00:00","11:00:00","#2AA1B7","No","N","7",
            -1,true,"","CAL",-1,""))
        tmp.add(AndroidCalendarData("2023-08-01","2023-08-01","2023-08-04",
            "10:00:00","11:00:00","#2AA1B7","No","N","4",
            -1,true,"","CAL",-1,""))
        tmp.add(AndroidCalendarData("2023-08-01","2023-08-01","2023-08-05",
            "10:00:00","11:00:00","#2AA1B7","No","N","5",
            -1,true,"","CAL",-1,""))
        tmp.add(AndroidCalendarData("2023-08-07","2023-08-07","2023-08-09",
            "10:00:00","11:00:00","#2AA1B7","No","N","6",
            -1,true,"","CAL",-1,""))
        tmp.add(AndroidCalendarData("2023-08-08","2023-08-08","2023-08-09",
            "10:00:00","11:00:00","#2AA1B7","No","N","7",
            -1,true,"","CAL",-1,""))
        tmp.add(AndroidCalendarData("2023-08-08","2023-08-08","2023-08-10",
            "10:00:00","11:00:00","#2AA1B7","No","N","9",
            -1,true,"","CAL",-1,""))
        tmp.add(AndroidCalendarData("2023-08-10","2023-08-10","2023-08-10",
            "10:00:00","11:00:00","#2AA1B7","No","N","9fhdfhdfhfdhd",
            -1,false,"","CAL",-1,""))
        //CalendarViewModel.repeatArrayList.add(AndroidCalendarData("2023-09-4","2023-09-04","2023-09-04",
        //    "10:00:00","11:00:00","#2AA1B7","Week","N","반복",
         //   -1,false,"","CAL",-1,"3"))
        //CalendarViewModel.hashMapArrayCal.put("2023-8",tmp)
        val currentDate = LocalDate.now()
        calendarAdapter = CalendarSliderAdapter(this,binding.textYear, binding.textMonth,viewPager)
        binding.textYear.text = currentDate.year.toString() + "년"
        binding.textMonth.text = currentDate.monthValue.toString() + "월"
        viewPager.adapter = calendarAdapter
        viewPager.setCurrentItem(CalendarSliderAdapter.START_POSITION, false)
        binding.calendarTodayText.text =  LocalDate.now().dayOfMonth.toString()
        binding.calendarTodayBtn.setOnClickListener {
            viewPager.setCurrentItem(CalendarSliderAdapter.START_POSITION, true)
        }
        binding.changeCal.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.calendar_change_cal, null)
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .create()

            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            mBuilder.show()
            mDialogView.findViewById<NumberPicker>(R.id.number_picker1).value = today.year
            mDialogView.findViewById<NumberPicker>(R.id.number_picker2).value = today.monthValue
            //팝업 사이즈 조절
            DisplayMetrics()
            context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val size = Point()
            val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            display.getSize(size)
            val screenWidth = size.x
            val popupWidth = (screenWidth * 0.9).toInt()
            mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)

            //팝업 타이틀 설정, 버튼 작용 시스템
            mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {

                mBuilder.dismiss()
            })
            mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                val yearTmp = mDialogView.findViewById<NumberPicker>(R.id.number_picker1).value
                val monthTmp = mDialogView.findViewById<NumberPicker>(R.id.number_picker2).value
                var comparisonResult =  ChronoUnit.MONTHS.between(today,LocalDate.parse(String.format("%02d-%02d",yearTmp, monthTmp)+"-"+String.format("%02d",today.dayOfMonth)))
                viewPager.setCurrentItem(CalendarSliderAdapter.START_POSITION+comparisonResult.toInt() , true)

                mBuilder.dismiss()
            })
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getCustomChar()

        binding.calendarDdayPlusBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("today",CalendarViewModel.todayDate())
            Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_calendarAdd,bundle)
        }
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                Navigation.findNavController(view).navigate(R.id.action_fragCalendar_to_fragHome)
                return@OnKeyListener true
            }
            false
        })
    }
    fun hideBootomNavigation(bool : Boolean){
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if(bool){
            bottomNavigation?.isGone = true
        }
        else {
            bottomNavigation?.isVisible = true
        }
    }


}