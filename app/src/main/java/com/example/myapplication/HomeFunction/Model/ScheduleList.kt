package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class ScheduleList(
    //@SerializedName("status")val status: Int,
    //@SerializedName("success") val success: Boolean,
    //@SerializedName("message")val message: String,
    @SerializedName("TimetableList") val datas: List<Schedule>
)

data class ScheduleListData(
    @SerializedName("data") val datas2: ScheduleList
)