package com.mada.myapplication.MyFuction.Data

data class FragMyData(
    val data: FragMyData2
)
data class FragMyData2(
    val subscribe: Boolean,
    val nickname: String,
    val saying: ArrayList<MySayingData>,
)
data class MySayingData(
    val id:Int,
    val content:String,
    val sayer:String,
)
