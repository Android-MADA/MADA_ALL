package com.mada.myapplication.HomeFunction.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Schedule(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("scheduleName") val scheduleName: String,
    @SerializedName("color") val color: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("memo") var memo: String,
    @SerializedName("isDeleted") val isDeleted : Boolean,
    @SerializedName("dayOfWeek") val dayOfWeek : String
) {
    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}

data class ScheduleAdd(
    @SerializedName("date") val date: String,
    @SerializedName("scheduleName") val scheduleName: String,
    @SerializedName("color") val color: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("isDeleted") val isDeleted : Boolean,
    @SerializedName("dayOfWeek") val dayOfWeek : String
) {
    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}
data class ScheduleList(
    @SerializedName("DailyTimetableList") val datas: List<Schedule>
)
data class ScheduleWeekList(
    @SerializedName("WeeklyTimetableList") val datas: List<Schedule>
)

data class ScheduleListData(
    @SerializedName("data") val datas2: ScheduleList
)
data class ScheduleWeekListData(
    @SerializedName("data") val datas2: ScheduleWeekList
)
data class ScheduleResponse2(
    @SerializedName("DailyTimetable") val DailyTimetable : Schedule
)

data class ScheduleResponse(
    @SerializedName("data") val data: ScheduleResponse2
)
data class ScheduleWeekResponse2(
    @SerializedName("WeeklyTimetable") val DailyTimetable : Schedule
)
data class ScheduleWeekResponse(
    @SerializedName("data") val data: ScheduleWeekResponse2
)
data class Comment2(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("content") val content: String,
)
data class CommentAdd(
    @SerializedName("date") val date: String,
    @SerializedName("content") val content: String,
)
data class Comment(
    @SerializedName("Comment") val comment: Comment2
)

data class CommentData(
    @SerializedName("data") val data: Comment
)
