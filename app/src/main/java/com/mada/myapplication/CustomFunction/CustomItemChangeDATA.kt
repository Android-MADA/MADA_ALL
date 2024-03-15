package com.mada.myapplication.CustomFunction

import androidx.annotation.Keep

@Keep
data class customItemChangeDATA(
    val success: Boolean,
    val data : wearingItems
)

data class wearingItems (
    val wearingItems: ArrayList<wearingItemsList>
)





