package com.mada.reapp.CustomFunction

import androidx.annotation.Keep

@Keep
data class customItemChangeDATA(
    val success: Boolean,
    val data : Data
)

data class Data (
    val wearingItems: ArrayList<wearingItemsList>
)

data class wearingItemsList (
    val id: Int,
    val category: String
)





