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
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.ViewCompat
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
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarAddBinding
import com.example.myapplication.databinding.CalendarAddSBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalendarAddSFragment : Fragment() {
    lateinit var binding: CalendarAddSBinding


    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCalendar::class.java)
    lateinit var token : String
    var id2 = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = CalendarAddSBinding.inflate(layoutInflater)
        hideBootomNavigation(true)
        val title = arguments?.getString("title")
        val preSchedule = arguments?.getString("preSchedule") ?:"2023-6-1"
        val nextSchedule = arguments?.getString("nextSchedule") ?:"2023-6-1"
        val preClock = arguments?.getString("preClock")
        val nextClock = arguments?.getString("nextClock")
        val cycle = processInput(arguments?.getString("cycle")?:"반복 안함")
        val memo = arguments?.getString("memo")
        val color = arguments?.getString("color")
        id2 = arguments?.getInt("id")?: -1

        token = arguments?.getString("Token")?: ""

        binding.title.text = title
        binding.calendarColor.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)
        binding.preScheldule.text = convertToDateKoreanFormat(preSchedule)
        binding.nextScheldule.text = convertToDateKoreanFormat(nextSchedule)
        if(preClock != null) {
            binding.preScheldule2.text = preClock
            binding.nextScheldule2.text = nextClock
        } else {
            binding.clcckLayoutBar.visibility = View.GONE
            binding.clockLayout.visibility = View.GONE
        }

        binding.cycle.text = cycle
        if(memo != null)
            binding.memo.text = memo
        else
            binding.memoLayout.visibility = View.GONE

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_calendarAddS_to_fragCalendar)
        }
        binding.toolbarCalendar.inflateMenu(R.menu.calendar_edit_menu)
       // binding.toolbarCalendar.setBackgroundResource(R.drawable.calendar_menu_image)
        binding.toolbarCalendar.setOnMenuItemClickListener {

            when(it.itemId){

                R.id.calendar_eidt -> {
                    val bundle = Bundle()
                    bundle.putString("title", arguments?.getString("title"))
                    bundle.putString("preSchedule", arguments?.getString("preSchedule"))
                    bundle.putString("nextSchedule", arguments?.getString("nextSchedule"))
                    bundle.putString("preClock", arguments?.getString("preClock"))
                    bundle.putString("nextClock", arguments?.getString("nextClock"))
                    bundle.putString("cycle", arguments?.getString("cycle"))
                    bundle.putString("memo", arguments?.getString("memo"))
                    bundle.putString("color", arguments?.getString("color"))
                    bundle.putBoolean("edit", true)
                    bundle.putInt("id",id2)
                    bundle.putString("Token",token)
                    Navigation.findNavController(view).navigate(R.id.action_calendarAddS_to_calendarAdd,bundle)
                    true
                }
                R.id.calendar_delete -> {
                    val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_add_popup, null)
                    val mBuilder = AlertDialog.Builder(requireContext())
                        .setView(mDialogView)
                        .create()
                    mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                    mBuilder.show()

                    val displayMetrics = DisplayMetrics()
                    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

                    val deviceWidth = displayMetrics.widthPixels

                    val desiredRatio = 0.8f
                    val desiredWidth = (deviceWidth * desiredRatio).toInt()
                    mBuilder?.window?.setLayout(desiredWidth, WindowManager.LayoutParams.WRAP_CONTENT)

                    val display = requireActivity().windowManager.defaultDisplay
                    mDialogView.findViewById<TextView>(R.id.textTitle).setText("일정을 삭제하시겠습니까?")
                    mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
                        mBuilder.dismiss()
                    })
                    mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                        deleteCalendar(id2)     //아이디
                        mBuilder.dismiss()
                    })
                    true
                }
                else -> false

            }

        }
        val contentInsetEnd = resources.getDimensionPixelSize(R.dimen.toolbar_overflow_offset) // Set your desired offset value
        val contentInsetStart = binding.toolbarCalendar.contentInsetStart
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarCalendar) { _, insets ->
            binding.toolbarCalendar.setContentInsetsRelative(
                contentInsetStart,
                contentInsetEnd + insets.systemGestureInsets.bottom
            )
            insets
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
    fun processInput(input: String): String {
        return when (input) {
            "Day" -> "매일"
            "Week" -> "매일"
            "Month" -> "매일"
            "Year" -> "매일"
            else -> "반복 안함"
        }
    }
    fun convertToDateKoreanFormat(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale("en","US"))
        val outputFormat = SimpleDateFormat("M월 d일 (E)", Locale("ko", "KR"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    private fun deleteCalendar(id : Int) {
        val call1 = service.deleteCal(token,id)
        call1.enqueue(object : Callback<AddCalendarData> {
            override fun onResponse(call: Call<AddCalendarData>, response: Response<AddCalendarData>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) {
                        //Log.d("del1",responseBody.datas.name.toString())
                    }else
                        Log.d("del2","${response.code()}")

                } else {
                    //Log.d("del3","itemType: ${response.code()} ${id} ")
                }
                findNavController().navigate(R.id.action_calendarAddS_to_fragCalendar)
            }

            override fun onFailure(call: Call<AddCalendarData>, t: Throwable) {
                //Log.d("del4","itemType: ${t.message}")
            }
        })
    }




}