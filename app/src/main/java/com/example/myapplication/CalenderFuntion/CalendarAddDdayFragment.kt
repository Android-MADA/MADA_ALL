package com.example.myapplication.CalenderFuntion

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.AddCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarData2
import com.example.myapplication.CalenderFuntion.Model.ResponseSample
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.Fragment.FragCalendar
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarAddDdayBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarAddDdayFragment : Fragment() {
    private lateinit var calendar: Calendar
    lateinit var binding: CalendarAddDdayBinding
    val weekdays = arrayOf("일" ,"월", "화", "수", "목", "금", "토")
    var dayString : String = ""
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCalendar::class.java)
    var token = ""
    var edit = false
    lateinit var preSchedule : TextView
    lateinit var nextSchedule : TextView
    var curColor ="#E1E9F5"
    var id2 : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preSchedule = TextView(requireContext())
        nextSchedule = TextView(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalendarAddDdayBinding.inflate(layoutInflater)
        hideBootomNavigation(true)
        val today: LocalDate = LocalDate.now()
        dayString = "${today.year}-${today.monthValue}-${today.dayOfMonth}"
        token = arguments?.getString("Token")?: ""
        binding.textTitle.setText(arguments?.getString("title"))
        preSchedule.text = StringBuilder(arguments?.getString("startDate") ?: dayString)
        nextSchedule.text = StringBuilder(arguments?.getString("endDate") ?: dayString)


        binding.preScheldule.text  = convertToDateKoreanFormat(preSchedule.text.toString())
        binding.nextScheldule.text = convertToDateKoreanFormat(nextSchedule.text.toString())
        dayString = binding.preScheldule.text.toString()
        binding.textDday.text = "D - "+(arguments?.getInt("dday") ?:"0").toString()
        binding.calendarColor.setColorFilter(Color.parseColor(arguments?.getString("color") ?: "#E1E9F5"), PorterDuff.Mode.SRC_IN)
        curColor = arguments?.getString("color") ?: "#89A9D9"
        binding.layoutColorSelector.visibility = View.GONE
        binding.textMemo.setText(arguments?.getString("memo"))
        binding.cal.visibility= View.GONE
        edit = arguments?.getBoolean("edit")?: false
        id2 = arguments?.getInt("id")?: -1
        if(edit) {
            binding.addBtn.text ="수정"
        }
        CalendarUtil.selectedDate = LocalDate.now()
        calendar = Calendar.getInstance()


        binding.calendarColor1.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.main), PorterDuff.Mode.SRC_IN)
            curColor = "#89A9D9"
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor2.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub3), PorterDuff.Mode.SRC_IN)
            curColor = "#F0768C"
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor3.setOnClickListener {
            binding.calendarColor.setColorFilter(resources.getColor(R.color.sub1), PorterDuff.Mode.SRC_IN)
            curColor = "#F8D141"
            toggleLayout(false,binding.layoutColorSelector)
        }
        binding.calendarColor.setOnClickListener {
            toggleLayout(true,binding.layoutColorSelector)
        }
        /*
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.textDday.visibility= View.VISIBLE
            } else {
                binding.textDday.visibility= View.GONE
            }

        }*/
        binding.preBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1)
            calendar.add(Calendar.MONTH, -1)
            setMonthView(-1)
        }
        binding.nextBtn.setOnClickListener {
            CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1)
            calendar.add(Calendar.MONTH, 1)
            setMonthView(1)
        }

        binding.preScheldule.setOnClickListener {
            binding.preScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.nextScheldule.setBackgroundColor(Color.TRANSPARENT)
            binding.cal.visibility= View.VISIBLE
            setMonthView(-1)
        }
        binding.nextScheldule.setOnClickListener {
            binding.nextScheldule.setBackgroundResource(R.drawable.calendar_prebackground)
            binding.preScheldule.setBackgroundColor(Color.TRANSPARENT)
            binding.cal.visibility= View.VISIBLE
            setMonthView(1)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            //뭐가 있다면
            if(binding.textTitle.text.toString() != "" || binding.preScheldule.text.toString() != dayString ||
                binding.nextScheldule.text.toString() != dayString  || binding.textMemo.text.toString()!="") {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_add_popup, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()

                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()
                if(edit) {
                    mDialogView.findViewById<TextView>(R.id.textTitle).text = "수정하지 않고 나가시겠습니가?"
                }
                val display = requireActivity().windowManager.defaultDisplay
                mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
                    mBuilder.dismiss()
                })
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                    Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_fragCalendar)
                    mBuilder.dismiss()
                })
            } else {
                Navigation.findNavController(view).navigate(R.id.action_calendarAddDday_to_fragCalendar)
            }

        }
        binding.addBtn.setOnClickListener {
            if(compareDates(nextSchedule.text.toString(),preSchedule.text.toString())||
                binding.textDday.text.toString()=="D - 0") {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_add_popup_one, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()

                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                mDialogView.findViewById<TextView>(R.id.textTitle).text = "올바른 날짜를 입력해 주십시오"
                val display = requireActivity().windowManager.defaultDisplay
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                    mBuilder.dismiss()
                })
            }  else {
                if(binding.addBtn.text.toString()=="수정") {
                    eidtCalendar( CalendarData2( binding.textTitle.text.toString(),convertToDateKoreanFormat2(nextSchedule.text.toString()),convertToDateKoreanFormat2(nextSchedule.text.toString()),
                        curColor,"No","Y",binding.textMemo.text.toString(),
                        "10:00:00","11:00:00"),id2)
                } else {            //등록
                    addCalendar( CalendarData2( binding.textTitle.text.toString(),convertToDateKoreanFormat2(nextSchedule.text.toString()),convertToDateKoreanFormat2(nextSchedule.text.toString()),
                        curColor,"No","Y",binding.textMemo.text.toString(),
                        "10:00:00","11:00:00"))
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        hideBootomNavigation(false)
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

    private fun setMonthView(preNext : Int) {
        var formatter = DateTimeFormatter.ofPattern("yyyy년 M월")
        binding.textCalendar.text = CalendarUtil.selectedDate.format(formatter)
        val dayList = dayInMonthArray()
        val adapter: CalendarSmallAdapter = if (preNext == -1) {
            CalendarSmallAdapter(dayList, binding.cal, binding.preScheldule,binding.textBlank,preSchedule)
        } else {
            CalendarSmallAdapter(dayList, binding.cal, binding.nextScheldule,binding.textDday,nextSchedule)
        }
        var manager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(),7)
        binding.calendar2.layoutManager = manager
        binding.calendar2.adapter = adapter
    }
    private fun dayInMonthArray() : ArrayList<Date> {
        var dayList = ArrayList<Date>()
        var monthCalendar = calendar.clone() as Calendar
        monthCalendar[Calendar.DAY_OF_MONTH] = 1        //달의 첫 번째 날짜
        var firstDayofMonth = monthCalendar[Calendar.DAY_OF_WEEK]-1

        monthCalendar.add(Calendar.DAY_OF_MONTH,-firstDayofMonth)
        while(dayList.size<42) {
            dayList.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)
        }
        return dayList
    }
    private fun yearMonthFromDate(date : LocalDate) :String{
        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        return date.format(formatter)
    }
    fun compareDates(date1: String, date2: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        val localDate1 = LocalDate.parse(date1, formatter)
        val localDate2 = LocalDate.parse(date2, formatter)
        Log.d("string",date1)
        Log.d("string",date2)
        return if (localDate1.isBefore(localDate2)) {
            true
        } else {
            false
        }
    }
    fun convertToDateKoreanFormat(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale("en","US"))
        val outputFormat = SimpleDateFormat("  M월 d일 (E)  ", Locale("ko", "KR"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    private fun toggleLayout(isExpanded: Boolean, layoutExpand: LinearLayout): Boolean {
        if (isExpanded) {
            ToggleAnimation.expand(layoutExpand)
        } else {
            ToggleAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }
    fun convertToDateKoreanFormat2(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale("en","US"))
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en","US"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    private fun addCalendar(data : CalendarData2) {
        val call1 = service.addCal(token,data)
        call1.enqueue(object : Callback<ResponseSample> {
            override fun onResponse(call: Call<ResponseSample>, response: Response<ResponseSample>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) {
                        Log.d("status",responseBody.status.toString())
                        Log.d("success",responseBody.success.toString())
                        Log.d("message",responseBody.message.toString())
                    }
                }
                findNavController().navigate(R.id.action_calendarAddDday_to_fragCalendar)
            }
            override fun onFailure(call: Call<ResponseSample>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }
    private fun eidtCalendar(data : CalendarData2,id : Int) {
        val call1 = service.editCal(token,id,data)

        call1.enqueue(object : Callback<CalendarData2> {
            override fun onResponse(call: Call<CalendarData2>, response: Response<CalendarData2>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) {
                        //Log.d("status",responseBody.data.name.toString())
                    }

                }
                findNavController().navigate(R.id.action_calendarAddDday_to_fragCalendar)
            }

            override fun onFailure(call: Call<CalendarData2>, t: Throwable) {
                Log.d("eidt","itemType: ${t.message}")
            }
        })
    }
}