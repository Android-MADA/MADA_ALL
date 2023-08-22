package com.example.myapplication.CustomFunction

import androidx.annotation.Keep

@Keep
data class customPrintDATA(
    val data: WearingItems
)

data class WearingItems(
    val wearingItems: ArrayList<print>
)

data class print(
    val id: Int,
    val itemType: String,
    val name: String,
    val filePath: String
)