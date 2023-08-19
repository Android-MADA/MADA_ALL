package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class PatchRequestTodo(
    @SerializedName("todoName") var todoName: String,
    @SerializedName("repeat") var repeat: String,
    @SerializedName("repeatWeek") var repeatWeek: String?,
    @SerializedName("repeatMonth") var repeatMonth: String?,
    @SerializedName("startRepeatDate") var startRepeatDate: String?,
    @SerializedName("endRepeatDate") var endRepeatDate: String?,
)