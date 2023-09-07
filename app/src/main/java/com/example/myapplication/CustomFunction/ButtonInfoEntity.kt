package com.example.myapplication.CustomFunction

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.ButtonInfoTypeConverter

@Entity(tableName = "table_serverbuttoninfo")
@TypeConverters(ButtonInfoTypeConverter::class)
data class ButtonInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val colorButtonInfo: ButtonInfo, // 이 필드에 Type Converter 적용
    val clothButtonInfo: ButtonInfo, // 이 필드에 Type Converter 적용
    val itemButtonInfo: ButtonInfo, // 이 필드에 Type Converter 적용
    val backgroundButtonInfo: ButtonInfo // 이 필드에 Type Converter 적용
)