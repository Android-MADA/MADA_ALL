package com.mada.myapplication.MyFuction.Data


import com.google.gson.annotations.SerializedName

data class MyGetProfileData(
    @SerializedName("data") val data : MyProfileData
)
data class MyProfileData(
    val nickname: String,
    val email: String,
)

