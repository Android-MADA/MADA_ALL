package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class CateResponseData(
    //@SerializedName("id") val id : Int,
    @SerializedName("categoryName") val categoryName : String,
    @SerializedName("color") val color : String,
    //@SerializedName("iconId") val iconId : Int
)
