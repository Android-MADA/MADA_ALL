package com.mada.myapplication.MyFuction.Data

import com.google.gson.annotations.SerializedName

data class CharacterResponse (
    @SerializedName("data") val datas: CharacterResponse2
)

data class Item (
    @SerializedName("id") val id: Int,
    @SerializedName("itemType") val itemType: String,
    @SerializedName("filePath") val filePath: String
)

data class CharacterResponse2 (
    @SerializedName("wearingItems") val datas: List<Item>
)