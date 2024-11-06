package com.mada.reapp

import androidx.room.TypeConverter
import com.mada.reapp.CustomFunction.ButtonInfo
import com.google.gson.Gson

class ButtonInfoTypeConverter {

    @TypeConverter
    fun fromButtonInfo(buttonInfo: ButtonInfo): String {
        return Gson().toJson(buttonInfo)
    }

    @TypeConverter
    fun toButtonInfo(json: String): ButtonInfo {
        return Gson().fromJson(json, ButtonInfo::class.java)
    }
}