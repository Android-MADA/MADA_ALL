package com.example.myapplication.MyFuction.Model

import com.google.gson.annotations.SerializedName

data class MyGetNoticesData(
    @SerializedName("data") val data: List<MyNoticesData>
)
