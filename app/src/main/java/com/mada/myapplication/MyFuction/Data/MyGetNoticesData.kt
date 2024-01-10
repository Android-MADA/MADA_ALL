package com.mada.myapplication.MyFuction.Data

data class MyGetNoticesData(
    val data: ArrayList<MyNoticesData>
)
data class MyNoticesData(
    val id:Int,
    val title:String,
    val content:String,
    val date: String,
)
