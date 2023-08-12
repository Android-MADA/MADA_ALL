package com.example.myapplication.HomeFunction.Model

import java.time.LocalDate
import java.time.LocalDateTime

data class Category(
    val id: Int,
    var categoryName: String,
    var color: String,
    val icon_id : Icon
)

//val createdAt: LocalDateTime,
//val updatedAt: LocalDateTime