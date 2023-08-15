package com.example.myapplication.CalenderFuntion.Model

import com.google.gson.Gson

class CalendarData2(
    val calendarName : String,
    val startDate: String,
    val endDate: String,
    val color: String,
    val repeat: String,
    val d_day: String,
    val memo: String
) {
    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}

class CalendarDatas (
    var status : Int ?,
    var success : Boolean ?,
    var message: String?,
    var datas: List<CalendarData2>? // 생성자, getter, setter 등의 메서드를 정의해주세요.

) {

}

class Item (
    val id: Int?,
    val itemType: String?,
    val filePath: String?
) {

}

class CharacterResponse (
    val datas: List<Item>?
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