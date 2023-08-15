package com.example.myapplication.HomeFunction.Model

data class CategoryList(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<Category>
)
