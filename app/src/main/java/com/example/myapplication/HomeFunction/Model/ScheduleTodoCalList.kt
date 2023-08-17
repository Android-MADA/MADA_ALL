package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName
data class todoList(
    @SerializedName("iconId") val iconId : Int,
    @SerializedName("todoName") val todoName : String
)
data class calendarList(
    @SerializedName("CalendarName") val CalendarName : String,
    @SerializedName("color") val color : String,
    @SerializedName("startDate") val startDate : String,
    @SerializedName("endDate") val endDate : String,
    @SerializedName("startTime") val startTime : String,
    @SerializedName("endTime") val endTime : String
)
data class data(
    @SerializedName("calendarList") val calendarList : ArrayList<calendarList>,
    @SerializedName("todoList") val todoList : ArrayList<todoList>
)
data class ScheduleTodoCalList(
    @SerializedName("data") val datas : data
)