package com.example.myapplication.CustomFunction

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_serverbuttoninfo")
data class ButtonInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val colorButtonInfo : ButtonInfo,
    val clothButtonInfo : ButtonInfo,
    val itemButtonInfo : ButtonInfo,
    val backgroundButtonInfo : ButtonInfo
)