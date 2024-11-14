package com.mada.reapp.CustomFunction

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mada.reapp.ButtonInfoTypeConverter

@Entity(tableName = "table_serverbuttoninfo")
@TypeConverters(ButtonInfoTypeConverter::class)
data class ButtonInfoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var colorButtonInfo: ButtonInfo?, // 이 필드에 Type Converter 적용
    var clothButtonInfo: ButtonInfo?, // 이 필드에 Type Converter 적용
    var itemButtonInfo: ButtonInfo?, // 이 필드에 Type Converter 적용
    var backgroundButtonInfo: ButtonInfo? // 이 필드에 Type Converter 적용
) {
    fun updateButtonInfo(index: Int, buttonInfo: ButtonInfo) {
        when (index) {
            0 -> colorButtonInfo = buttonInfo
            1 -> clothButtonInfo = buttonInfo
            2 -> itemButtonInfo = buttonInfo
            3 -> backgroundButtonInfo = buttonInfo
        }
    }
}