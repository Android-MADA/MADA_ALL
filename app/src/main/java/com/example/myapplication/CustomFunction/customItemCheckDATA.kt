package com.example.myapplication.CustomFunction

import androidx.annotation.Keep

@Keep
data class customItemCheckDATA(
    val id: Long,
    val itemType: String,
    val itemUnlockCondition: String,
    val filePath: String,
    val have: Boolean
)