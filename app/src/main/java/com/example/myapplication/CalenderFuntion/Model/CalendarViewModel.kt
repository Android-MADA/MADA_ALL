package com.example.myapplication.CalenderFuntion.Model

import androidx.lifecycle.ViewModel
import java.time.format.DateTimeFormatter

class CalendarViewModel : ViewModel(){

    var formatterM = DateTimeFormatter.ofPattern("M")
    var formatterYYYY = DateTimeFormatter.ofPattern("YYYY")


    val hashMap = HashMap<String, ArrayList<AndroidCalendarData>>()

}