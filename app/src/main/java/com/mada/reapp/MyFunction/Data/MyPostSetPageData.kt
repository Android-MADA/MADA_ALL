package com.mada.reapp.MyFunction.Data

import com.google.gson.annotations.SerializedName

data class MyPostSetPageData(
    @SerializedName("endTodoBackSetting") val endTodoBackSetting: Boolean,
    @SerializedName("newTodoStartSetting") val newTodoStartSetting: Boolean,
    @SerializedName("startTodoAtMonday")  val startTodoAtMonday: Boolean,
)
