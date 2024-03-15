package com.mada.myapplication.MyFunction.Data

import com.google.gson.annotations.SerializedName

data class MyChangeNicknameData(
    @SerializedName("data") val data: MyChangeNicknameData2
)
data class MyChangeNicknameData2(
    @SerializedName("nickname") val nickname: String
)

