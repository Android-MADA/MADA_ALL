package com.example.myapplication.CalenderFuntion.Model

data class CalendarDATA(
    val startDate: String,
    var startDate2: String,
    val endDate: String,
    val startTime : String,
    val endTime : String,
    val color: String,
    val repeat: String,
    val dDay: Char,
    val title: String,
    //val createAt: String,
    //val updateAt: String
    var floor : Int,
    val duration : Boolean,
    val memo : String,
    val what : String       //종류

)