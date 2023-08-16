package com.example.myapplication.HomeFunction.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PatchRequestCategory(
    @SerializedName("categoryName") val categoryName : String,
    @SerializedName("color") val color : String,
    @SerializedName("iconId") val iconId : Int
)