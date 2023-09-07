package com.example.myapplication.MyFuction.Data

import com.google.gson.annotations.SerializedName

data class MyRecordData(
    val data: MyRecordData2
)

data class MyRecordData2(
    val nickName: String,
    val todosPercent: String,
    val completeTodoPercent: String,
    val categoryStatistics: ArrayList<MyRecordData3>
)

data class MyRecordData3(
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("color") val color: String,
    @SerializedName("rate") val rate: Double
)

data class MyRecordOptionData(
    @SerializedName("option") val option: String,
    @SerializedName("date") val date: String
)
