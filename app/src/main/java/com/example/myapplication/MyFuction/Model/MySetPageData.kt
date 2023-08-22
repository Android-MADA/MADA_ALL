package com.example.myapplication.MyFuction.Model

import com.google.gson.annotations.SerializedName

data class MySetPageData(
    @SerializedName ("data") val data: MySetPageData2
)
data class MySetPageData2(
    @SerializedName("endTodobackSetting") val endTodobackSetting: Boolean,
    @SerializedName("newTodoStartSetting") val newTodoStartSetting: Boolean,
    @SerializedName("startTodoAtMonday")  val startTodoAtMonday: Boolean,
)
