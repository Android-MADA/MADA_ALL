package com.example.myapplication.CustomFunction

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_serverbuttoninfo")
data class ButtonInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val colorButtonInfo: ButtonInfo?, // 이 필드에 Type Converter 적용
    val clothButtonInfo: ButtonInfo?, // 이 필드에 Type Converter 적용
    val itemButtonInfo: ButtonInfo?, // 이 필드에 Type Converter 적용
    val backgroundButtonInfo: ButtonInfo? // 이 필드에 Type Converter 적용
)