package com.example.myapplication.CustomFunction

import androidx.annotation.Keep

@Keep
data class customPrintDATA(
    val id: Long,
    val itemType: String,
    val filePath: String
)