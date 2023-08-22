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

data class MySettingData2(
    @SerializedName("endTodoBackSetting") val endTodoBackSetting : Boolean,
    @SerializedName("newTodoStartSetting") val newTodoStartSetting : Boolean,
    @SerializedName("startTodoAtMonday") val startTodoAtMonday : Boolean
)

data class MySettingData3(
    @SerializedName("data") val data : MySettingData2
)