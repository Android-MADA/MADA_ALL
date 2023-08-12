package com.example.myapplication.HomeFunction.Model

import java.time.LocalDate

data class Todo(
    val date : LocalDate,
    val categoryId : ArrayList<Category>,
    val todoName : String,
    val complete : Boolean,
    val repeat : String,
    val repeatWeek : String?,
    val startRepeatDate : String?,
    val endRepeatDate : String?
)
