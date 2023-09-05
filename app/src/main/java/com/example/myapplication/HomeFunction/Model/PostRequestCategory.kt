package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class PostRequestCategory(
    @SerializedName("categoryName") var categoryName: String,
    @SerializedName("color") var color: String,
    @SerializedName("iconId") var iconId : Int,
    @SerializedName("isInActive") var isInActive : Boolean,
    @SerializedName("isDeleted") var isDeleted : Boolean
)