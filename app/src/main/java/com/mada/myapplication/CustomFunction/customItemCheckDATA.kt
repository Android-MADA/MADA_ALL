package com.mada.myapplication.CustomFunction

import androidx.annotation.Keep

@Keep
data class customItemCheckDATA(
    val data : itemList
)

data class itemList(
    val itemList: ArrayList<itemcheck>
)
data class itemcheck(
    val id: Int,
    val itemType: String,
    val have: Boolean,
    val itemCategory: List<String>
)