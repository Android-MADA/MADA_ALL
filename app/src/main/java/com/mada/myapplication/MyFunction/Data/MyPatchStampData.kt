package com.mada.myapplication.MyFunction.Data

import com.google.gson.annotations.SerializedName

data class MyPatchStampData(
    @SerializedName("data") val data : MyStampCntData
)
data class MyStampCntData(
    val data: Int,
)
