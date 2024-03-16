package com.mada.myapplication.MyFunction.Data

import com.google.gson.annotations.SerializedName

data class MySetPageData(
    @SerializedName ("data") val data: MySetPageData2
)
data class MySetPageData2(
    @SerializedName("endTodoBackSetting") val endTodoBackSetting: Boolean,
    @SerializedName("newTodoStartSetting") val newTodoStartSetting: Boolean,
    @SerializedName("startTodoAtMonday")  val startTodoAtMonday: Boolean,
)
