package com.example.myapplication.MyFuction.Model

import com.google.gson.annotations.SerializedName

data class MyAlarmData(
    @SerializedName ("data") val data : MyAlarmData2
)

data class MyAlarmData2(
    @SerializedName ("calendarAlarmSetting") val calendarAlarmSetting: Boolean,
    @SerializedName ("dDayAlarmSetting") val dDayAlarmSetting: Boolean,
    @SerializedName ("timetableAlarmSetting") val timetableAlarmSetting: Boolean
)


