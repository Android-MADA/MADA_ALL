package com.example.myapplication.HomeFunction.Model

data class TodoList(
    val status : Int,
    val success : Boolean,
    val message : String,
    val data : ArrayList<Todo>
)