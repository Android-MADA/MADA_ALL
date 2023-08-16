package com.example.myapplication.HomeFunction.Model

import java.time.LocalDate

data class Todo(
    //val id : Int,
    val date : LocalDate,
    var categoryId : Category,
    var todoName : String,
    var complete : Boolean,
    val repeat : String,

)

//val repeatWeek : String?,
//val startRepeatDate : String?,
//val endRepeatDate : String?