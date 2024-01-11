package com.mada.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class ScheduleResponse2(
    @SerializedName("DailyTimetable") val DailyTimetable : Schedule
)

data class ScheduleResponse(
    @SerializedName("data") val data: ScheduleResponse2
)
