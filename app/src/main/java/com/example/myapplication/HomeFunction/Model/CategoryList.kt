package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class CategoryList(
    @SerializedName("status") val status: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Category>
)
