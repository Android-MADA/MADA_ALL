package com.example.myapplication.Fragment

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.example.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarDataDday
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.CalenderFuntion.Model.CharacterResponse
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.CustomFunction.CustomViewModel
import com.example.myapplication.databinding.FragCalendarBinding
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Integer.min
import java.time.LocalDate


class FragCalendar : Fragment(){

    lateinit var binding: FragCalendarBinding
    private lateinit var calendarAdapter: CalendarSliderAdapter
    private val CalendarViewModel : CalendarViewModel by activityViewModels()
    private val viewModel: CustomViewModel by viewModels()

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
        CalendarViewModel.repeatArrayList.add(AndroidCalendarData("2023-08-10","2023-08-10","2023-08-10",
            "10:00:00","11:00:00","#2AA1B7","Week","N","반복",
            -1,false,"","CAL",-1,"3"))
        CalendarViewModel.hashMapArrayCal.put("2023-8",tmp)
        calendarAdapter = CalendarSliderAdapter(this,binding.textYear, binding.textMonth,viewPager)

        viewPager.adapter = calendarAdapter
        viewPager.setCurrentItem(CalendarSliderAdapter.START_POSITION, false)
        binding.calendarTodayText.text =  LocalDate.now().dayOfMonth.toString()
        binding.calendarTodayBtn.setOnClickListener {
            viewPager.setCurrentItem(CalendarSliderAdapter.START_POSITION, true)
        }

        //getMonthDataArray(CalendarUtil.selectedDate.format(viewModelCal.formatterM),CalendarUtil.selectedDate.format(viewModelCal.formatterYYYY),datas)

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

/*
    */
    /*
    private fun getCustomChar() {
        val call2 = service.characterRequest(token)
        call2.enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(call2: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.data.datas
                        if(datas != null) {
                            for (data in datas) {
                                //arrays.add(data)
                                //Log.d("111","datas: ${data.id} ${data.itemType} ${data.filePath}")
                                if(data.itemType=="color") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.calendarRamdi)
                                } else if(data.itemType=="set") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.imgCalendarCloth)
                                } else if(data.itemType=="item") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.imgCalendarItem)
                                }
                                // ...
                            }
                        } else {
                            //Log.d("2221","${response.code()}")
                        }
                    } else {
                        //Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                    //Log.d("3331","itemType: ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                //Log.d("444","itemType: ${t.message}")
            }
        })
    }

     */

}