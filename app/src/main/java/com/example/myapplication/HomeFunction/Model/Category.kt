package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") val id: Int,
    @SerializedName("categoryName") var categoryName: String,
    @SerializedName("color") var color: String,
    //@SerializedName("iconId") var iconId : Int
)