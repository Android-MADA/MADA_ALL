package com.example.myapplication.MyFuction.Model

import com.google.gson.annotations.SerializedName

data class MyNoticesList(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("date") val date: String,
)

