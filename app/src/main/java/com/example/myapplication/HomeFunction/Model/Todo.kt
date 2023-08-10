package com.example.myapplication.HomeFunction.Model

import java.time.LocalDate

data class Todo(
    val userId: ArrayList<User>,
    val data: LocalDate,
    val categoryId: ArrayList<Category>,
    val todoName: String,
    val complete: Boolean,
    val repeat: String,
    val repeatWeek: Int?,
    val startRepeatDate: LocalDate?,
    val endRepeatDate: LocalDate?
)