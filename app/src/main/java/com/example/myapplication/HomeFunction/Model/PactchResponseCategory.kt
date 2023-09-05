package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class PactchResponseCategory(
    @SerializedName("data") val data: PatchResponseCate2
)

data class PatchResponseCate2(
    @SerializedName("Category") val Category : Category2
)
