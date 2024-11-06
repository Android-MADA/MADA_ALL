package com.mada.reapp.CustomFunction

import androidx.annotation.Keep

@Keep
data class customPrintDATA(
    val data: WearingItems1
)

data class WearingItems1(
    val wearingItems: ArrayList<print>
)

data class print(
    val id: Int,
    val itemType: String,
    val name: String,
    val filePath: String
)