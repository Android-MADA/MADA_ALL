package com.mada.myapplication.ChartFunction.Data

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
    val categoryName: String,
    val color: String,
    val rate: Float
)

data class MyRecordOptionData(
    @SerializedName("option") val option: String,
    @SerializedName("date") val date: String
)