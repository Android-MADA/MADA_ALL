package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class PostResponseTodo(
    @SerializedName("data") val data: Todo
)
