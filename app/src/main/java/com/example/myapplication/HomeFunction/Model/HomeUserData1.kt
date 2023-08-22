package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class HomeUserData1(
    @SerializedName("data") var data : HomeUserData2
)
