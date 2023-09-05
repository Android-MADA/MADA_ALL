package com.example.myapplication.HomeFunction.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.time.LocalDate
data class ScheduleResponse2(
    @SerializedName("Timetable") val Timetable : Schedule
)

data class ScheduleResponse(
    @SerializedName("data") val data: ScheduleResponse2
)
