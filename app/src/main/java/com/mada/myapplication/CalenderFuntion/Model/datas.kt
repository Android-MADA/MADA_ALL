package com.mada.myapplication.CalenderFuntion.Model

import com.google.gson.annotations.SerializedName

data class CalendarData(
    @SerializedName("calendarName") val name : String,
    @SerializedName("startDate") val start_date: String,
    @SerializedName("endDate") val end_date: String,
    @SerializedName("color") val color: String,
    @SerializedName("repeat") val repeat: String,
    @SerializedName("repeatInfo") val repeatInfo: Int,
    @SerializedName("startTime") val start_time: String,
    @SerializedName("endTime") val end_time: String,
    @SerializedName("dday") val d_day: String,
    @SerializedName("memo") val memo: String
)
data class CalendarDataEdit(
    @SerializedName("calendarId") val calendarId : Int,
    @SerializedName("calendarName") val name : String,
    @SerializedName("startDate") val start_date: String,
    @SerializedName("endDate") val end_date: String,
    @SerializedName("color") val color: String,
    @SerializedName("repeat") val repeat: String,
    @SerializedName("repeatInfo") val repeatInfo: Int,
    @SerializedName("startTime") val start_time: String,
    @SerializedName("endTime") val end_time: String,
    @SerializedName("dday") val d_day: String,
    @SerializedName("memo") val memo: String
)
data class RepeatData(
    @SerializedName("calendarId") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("isExpired") val isExpired : Boolean
)
data class CalendarDatas (
    @SerializedName("startTodoAtMonday") val startMon : Boolean ,
    @SerializedName("calendars") val datas: List<CalendarDataId>,
    @SerializedName("repeats") val repeats: List<RepeatData>
)


data class CalendarDatasData (
    @SerializedName("data") val data : CalendarDatas
)

data class CalendarDataId(
    @SerializedName("calendarId") val id: Int,
    @SerializedName("calendarName") val name : String,
    @SerializedName("startDate") val start_date: String,
    @SerializedName("endDate") val end_date: String,
    @SerializedName("dday") val d_day: String,
    @SerializedName("startTime") val start_time: String,
    @SerializedName("endTime") val end_time: String,
    @SerializedName("repeat") val repeat: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("color") val color: String,
    @SerializedName("repeatInfo") val repeatInfo: Int,
    @SerializedName("expired") val expired : Boolean
)

data class AddCalendarData2 (
    @SerializedName("calendar") val calendars: CalendarDataId
)
data class AddCalendarData1 (
    @SerializedName("data") val data: AddCalendarData2
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