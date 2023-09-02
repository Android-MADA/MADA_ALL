package com.example.myapplication.CalenderFuntion.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class CalendarData(
    @SerializedName("calendarName") val name : String,
    @SerializedName("startDate") val start_date: String,
    @SerializedName("endDate") val end_date: String,
    @SerializedName("color") val color: String,
    @SerializedName("repeat") val repeat: String,
    @SerializedName("dday") val d_day: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("startTime") val start_time: String,
    @SerializedName("endTime") val end_time: String,
    @SerializedName("repeatDate") val repeatDate: String
)
{
    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}
data class CalendarDatas (
    @SerializedName("startTodoAtMonday") val startMon : Boolean ,
    @SerializedName("calendar") val datas: List<CalendarDataId>
)


data class CalendarDatasData (
    @SerializedName("data") val data : CalendarDatas

)
data class CalendarDataId(
    @SerializedName("calendarName") val name : String,
    @SerializedName("startDate") val start_date: String,
    @SerializedName("endDate") val end_date: String,
    @SerializedName("startTime") val start_time: String,
    @SerializedName("endTime") val end_time: String,
    @SerializedName("color") val color: String,
    @SerializedName("repeat") val repeat: String,
    @SerializedName("dday") val d_day: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("calendarId") val id: Int,
    @SerializedName("repeatDate") val repeatDate: String
)

data class AddCalendarData (
    @SerializedName("data") val datas: CalendarData
)

//Dday 모든 정보 요청
data class CalendarDataDday (
    @SerializedName("startTodoAtMonday") val startMon : Boolean ,
    @SerializedName("data") val datas: DdayCalendar
)
data class DdayCalendar (
    @SerializedName("calendar") val datas: List<CalendarDataId>
)


data class Item (
    @SerializedName("id") val id: Int,
    @SerializedName("itemType") val itemType: String,
    @SerializedName("filePath") val filePath: String
)
data class CharacterResponse2 (
    @SerializedName("wearingItems") val datas: List<Item>
)



data class CharacterResponse (
    @SerializedName("data") val data: CharacterResponse2
) {

}
data class nickName (
    @SerializedName("nickName") val name: String
)