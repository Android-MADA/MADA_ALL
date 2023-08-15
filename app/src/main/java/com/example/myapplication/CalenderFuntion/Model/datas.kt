package com.example.myapplication.CalenderFuntion.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class CalendarData2(
    @SerializedName("calenderName") val calender_name : String,
    @SerializedName("startDate") val start_date: String,
    @SerializedName("endDate") val end_date: String,
    @SerializedName("color") val color: String,
    @SerializedName("repeat") val repeat: String,
    @SerializedName("dday") val d_day: String,
    @SerializedName("memo") val memo: String

) {
    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}

data class CalendarDatas (
    @SerializedName("status") val status : Int ?,
    @SerializedName("success") val success : Boolean ?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val datas: List<CalendarData2>? // 생성자, getter, setter 등의 메서드를 정의해주세요.

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

}