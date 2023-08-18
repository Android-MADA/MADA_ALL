package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class TodoList(
    @SerializedName("data") var data: List<SampleTodo>
)