package com.example.myapplication.MyFuction.Model

import com.google.gson.annotations.SerializedName

data class MyGetNoticesData(
    val data: ArrayList<MyNoticesData>
)
data class MyNoticesData(
    val id:Int,
    val title:String,
    val content:String,
    val date: String,
)
