package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class todoData2(
    @SerializedName("nickname") val nickname : String,
    @SerializedName("TodoList") val TodoList : ArrayList<Todo>
)
