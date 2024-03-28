package com.mada.myapplication.MyFunction.Data

import com.google.gson.annotations.SerializedName

data class MyGetSetPageData(
    @SerializedName("data") val data : MyGetSetPageData2,
)

data class MyGetSetPageData2(
    @SerializedName("endTodoBackSetting") val endTodoBackSetting: Boolean,
    @SerializedName("newTodoStartSetting") val newTodoStartSetting: Boolean,
    @SerializedName("startTodoAtMonday")  val startTodoAtMonday: Boolean,
)
