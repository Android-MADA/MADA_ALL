package com.example.myapplication.MyFuction.Data

data class MyRecordData(
    val data: MyRecordData2
)

data class MyRecordData2(
    val nickname: String,
    val todosPercent: Float,
    val completeTodoPercent: Float,
    val categoryStatistics: ArrayList<MyRecordData3>
)

data class MyRecordData3(
    val categoryName: String,
    val color: String,
    val rate: Float
)

data class MyRecordOptionData(
    val option: String,
    val date: String
)
