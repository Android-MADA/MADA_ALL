package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class PostRequestTodo(
    @SerializedName("date") val date: String?,
    @SerializedName("category") var category: PostRequestTodoCateId,
    @SerializedName("todoName") var todoName: String,
    @SerializedName("complete") var complete: Boolean,
    @SerializedName("repeat") var repeat: String,
    @SerializedName("repeatWeek") var repeatWeek: String?,
    @SerializedName("repeatMonth") var repeatMonth: String?,
    @SerializedName("startRepeatDate") var startRepeatDate: String?,
    @SerializedName("endRepeatDate") var endRepeatDate: String?,
    @SerializedName("isAlarm") var isAlarm: Boolean,
    @SerializedName("startTodoAtMonday") var startTodoAtMonday: Boolean,
    @SerializedName("endTodoBackSetting") var endTodoBackSetting: Boolean,
    @SerializedName("newTodoStartSetting") var newTodoStartSetting: Boolean

)

