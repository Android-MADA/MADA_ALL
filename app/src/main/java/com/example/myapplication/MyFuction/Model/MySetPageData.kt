package com.example.myapplication.MyFuction.Model

import android.telephony.data.ApnSetting
import com.google.gson.annotations.SerializedName

data class MySetPageData(
    @SerializedName ("date") val data: MySetPageData2
)
data class MySetPageData2(
    @SerializedName("endTodobackString") val endTodobackSetting: Boolean,
    @SerializedName("newTodoStartSetting") val newTodoStartSetting: Boolean,
    @SerializedName("startTodoAtMonday")  val startTodoAtMonday: Boolean,
)
