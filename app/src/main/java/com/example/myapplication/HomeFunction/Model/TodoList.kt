package com.example.myapplication.HomeFunction.Model

data class TodoList(
    val status : Int,
    val message : String,
    val success : Boolean,
    val data : ArrayList<Todo>
)