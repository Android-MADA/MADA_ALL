package com.example.myapplication.CustomFunction

import androidx.room.TypeConverter
import com.google.gson.Gson

class ButtonInfoTypeConverter {
    @TypeConverter
    fun fromButtonInfo(buttonInfo: ButtonInfo?): String? {
        return Gson().toJson(buttonInfo)
    }

    @TypeConverter
    fun toButtonInfo(json: String?): ButtonInfo? {
        return Gson().fromJson(json, ButtonInfo::class.java)
    }
}