package com.example.myapplication.CalenderFuntion.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class CalendarData2(
    @SerializedName("calendarName") val name : String,
    @SerializedName("startDate") val start_date: String,
    @SerializedName("endDate") val end_date: String,
    @SerializedName("color") val color: String,
    @SerializedName("repeat") val repeat: String,
    @SerializedName("dday") val d_day: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("startTime") val start_time: String,
    @SerializedName("endTime") val end_time: String
){
    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}
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
    @SerializedName("calendarId") val id: Int
)
data class AddCalendarData (
    @SerializedName("data") val datas: CalendarData2

) {
    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}
data class CalendarDatas (
    @SerializedName("startTodoAtMonday") val startMon : Boolean ,
    @SerializedName("data") val datas: List<CalendarDataId> // 생성자, getter, setter 등의 메서드를 정의해주세요.

) {

}

data class Item (
    @SerializedName("id") val id: Int,
    @SerializedName("itemType") val itemType: String,
    @SerializedName("filePath") val filePath: String
) {

}

data class CharacterResponse (
    @SerializedName("data") val datas: List<Item>
) {

}
class ResponseSample (
    val status : String?,
    val success : String?,
    var message: String?,
    var name: String?,
    var start_date: String?,
    var end_date: String?,
    var color: String?,
    var repeat: String?,
    var d_day: String?,
    var memo: String?
) {
    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}
data class nickName (
    @SerializedName("nickName") val name: String
)