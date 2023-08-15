package com.example.myapplication.HomeFunction.Model

data class ScheduleList(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<Schedule>
)