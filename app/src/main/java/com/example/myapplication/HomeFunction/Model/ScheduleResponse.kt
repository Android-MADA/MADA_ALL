package com.example.myapplication.HomeFunction.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.time.LocalDate

data class ScheduleResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("succcess") val succcess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Schedule
)
