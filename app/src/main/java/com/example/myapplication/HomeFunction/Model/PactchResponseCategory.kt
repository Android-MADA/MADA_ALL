package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class PactchResponseCategory(
    @SerializedName("success") val success : Boolean,
    @SerializedName("message") val message : String
)
