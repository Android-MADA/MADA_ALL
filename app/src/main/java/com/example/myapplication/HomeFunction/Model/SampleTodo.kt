package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class SampleTodo(
    @SerializedName("todoName") var todoName : String
)
