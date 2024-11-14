package com.mada.reapp.MyFunction.Data

import com.google.gson.annotations.SerializedName

data class MyGetNoticesData(
    @SerializedName("data") val data: ArrayList<MyNoticesData>
)
data class MyNoticesData(
    @SerializedName("id") val id:Int,
    @SerializedName("title") val title:String,
    @SerializedName("content") val content:String,
    @SerializedName("date") val date: String,
)
