package com.example.myapplication.HomeFunction.Model

import java.time.LocalDate

data class Todo(
    val id : Int,
    val date : LocalDate,
    var categoryId : Category,
    var todoName : String,
    var complete : Boolean,
    var repeat : String,
    var repeatWeek: String?,
    var startRepeatDate: String?,
    var endRepeatDate: String?
)

