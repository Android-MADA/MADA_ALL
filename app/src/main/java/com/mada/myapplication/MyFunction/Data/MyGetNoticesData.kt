package com.mada.myapplication.MyFunction.Data

data class MyGetNoticesData(
    val data: ArrayList<MyNoticesData>
)
data class MyNoticesData(
    val id:Int,
    val title:String,
    val content:String,
    val date: String,
)
