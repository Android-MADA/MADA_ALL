package com.example.myapplication.CustomFunction

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
    val name: String,
    val itemType: String,
    val itemUnlockCondition: String,
    val filePath: String,
    val have: Boolean
)