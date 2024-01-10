package com.mada.myapplication.MyFuction.Data

import com.google.gson.annotations.SerializedName

data class MyPremiumData(
    @SerializedName("is_subscribe") val is_subscribe: Boolean,
)
