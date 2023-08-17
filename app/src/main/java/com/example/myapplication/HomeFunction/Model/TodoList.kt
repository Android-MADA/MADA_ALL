package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class TodoList(
    //@SerializedName("status") var status : Int,.
    //@SerializedName("message") var message : String,
    //@SerializedName("success") var success : Boolean,
    @SerializedName("data") var data: List<SampleTodo>
)