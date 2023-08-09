package com.example.myapplication.HomeFunction.Model

import java.time.LocalDate
import java.time.LocalDateTime

data class Category(
    val id: Int,
    val categoryName: String,
    val color: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)