package com.example.myapplication.CustomFunction

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.ButtonInfoTypeConverter

@Entity(tableName = "table_serverbuttoninfo")
@TypeConverters(ButtonInfoTypeConverter::class)
data class ButtonInfoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var colorButtonInfo: ButtonInfo?, // 이 필드에 Type Converter 적용
    var clothButtonInfo: ButtonInfo?, // 이 필드에 Type Converter 적용
    var itemButtonInfo: ButtonInfo?, // 이 필드에 Type Converter 적용
    var backgroundButtonInfo: ButtonInfo? // 이 필드에 Type Converter 적용
)