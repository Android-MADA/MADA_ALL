package com.example.myapplication.MyFuction.Model

data class FragMyData(
    val data: FragMyData2
)
data class FragMyData2(
    val subscribe: Boolean,
    val nickname: String,
    val saying: MySayingData,
)
data class MySayingData(
    val id:Int,
    val content:String,
    val sayer:String,
)
