package com.mada.reapp.HomeFunction.Model

import com.google.gson.annotations.SerializedName
data class todoList2(
    @SerializedName("iconId") val iconId : Int,
    @SerializedName("todoName") val todoName : String
)
data class calendarList2(
    @SerializedName("CalendarName") val CalendarName : String,
    @SerializedName("color") val color : String,
    @SerializedName("startTime") val startTime : String,
    @SerializedName("endTime") val endTime : String,
    @SerializedName("d-day") val dday : String
)
data class data(
    @SerializedName("calendarList") val calendarList : ArrayList<calendarList2>,
    @SerializedName("todoList") val todoList : ArrayList<todoList2>
)
data class ScheduleTodoCalList(
    @SerializedName("data") val datas : data
)