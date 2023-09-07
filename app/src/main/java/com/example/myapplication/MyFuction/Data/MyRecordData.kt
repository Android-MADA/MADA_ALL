package com.example.myapplication.MyFuction.Data

import com.google.gson.annotations.SerializedName

data class MyRecordData(
    val data: MyRecordData2
)

data class MyRecordData2(
    val nickName: String,
    val todosPercent: Double,
    val completeTodoPercent: Double,
    val categoryStatistics: ArrayList<MyRecordData3>
)

data class MyRecordData3(
    val categoryName: String,
    val color: String,
    val rate: Double
)

data class MyRecordOptionData(
    @SerializedName("option") val option: String,
    @SerializedName("date") val date: String
)
