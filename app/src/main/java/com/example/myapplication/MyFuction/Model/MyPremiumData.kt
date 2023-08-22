package com.example.myapplication.MyFuction.Model

import com.google.gson.annotations.SerializedName

data class MyPremiumData(
    @SerializedName("is_subscribe") val is_subscribe: Boolean,
)
