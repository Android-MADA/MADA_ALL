package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class CategoryList(
    @SerializedName("data") val data: ArrayList<Category>
)
