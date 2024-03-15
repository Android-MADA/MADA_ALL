package com.mada.myapplication.MyFunction.Data

import com.google.gson.annotations.SerializedName

data class MyPostSetPageData(
    @SerializedName("endTodobackSetting") val endTodobackSetting: Boolean,
    @SerializedName("newTodoStartSetting") val newTodoStartSetting: Boolean,
    @SerializedName("startTodoAtMonday")  val startTodoAtMonday: Boolean,
)
