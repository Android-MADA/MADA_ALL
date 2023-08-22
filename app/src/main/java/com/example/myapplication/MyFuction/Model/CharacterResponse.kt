package com.example.myapplication.MyFuction.Model

import com.google.gson.annotations.SerializedName

data class CharacterResponse (
    @SerializedName("data") val datas: List<Item>
)

data class Item (
    @SerializedName("id") val id: Int,
    @SerializedName("itemType") val itemType: String,
    @SerializedName("filePath") val filePath: String
)