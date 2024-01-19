package com.mada.myapplication.HomeFunction.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ScheduleAdd(
    @SerializedName("date") val date: String,
    @SerializedName("scheduleName") val scheduleName: String,
    @SerializedName("color") val color: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("comment") val comment : String,
    @SerializedName("isDeleted") val isDeleted : Int,
    @SerializedName("dayOfWeek") val dayOfWeek : String
) {
    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}