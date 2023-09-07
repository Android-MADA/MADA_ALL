package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class RepeatData1(
    @SerializedName("data") val data : RepeatData2
)

data class RepeatData2(
    @SerializedName("RepeatTodoList") val RepeatTodoList : ArrayList<repeatTodo>
)

data class repeatTodo(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String?,
    @SerializedName("category") var category: Category,
    @SerializedName("todoName") var todoName: String,
    @SerializedName("repeat") var repeat: String,
    @SerializedName("repeatWeek") var repeatWeek: String?,
    @SerializedName("repeatMonth") var repeatMonth: String?,
    @SerializedName("startRepeatDate") var startRepeatDate: String?,
    @SerializedName("endRepeatDate") var endRepeatDate: String?
)

