package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class TodoList(
    @SerializedName("status") val status : Int,
    @SerializedName("message") val message : String,
    @SerializedName("success") val success : Boolean,
    @SerializedName("data") val data : List<Todo>
)