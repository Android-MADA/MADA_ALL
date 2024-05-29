package com.mada.myapplication.CustomFunction

import android.util.Log
import com.mada.myapplication.BuildConfig
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataRepo {
    var buttonInfoEntity: ButtonInfoEntity =  ButtonInfoEntity(
        id = 0,
        colorButtonInfo = ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi),
        clothButtonInfo = ButtonInfo(0, 49, R.drawable.custom_empty),
        itemButtonInfo = ButtonInfo(0, 50, R.drawable.custom_empty),
        backgroundButtonInfo = ButtonInfo(0, 48, R.drawable.custom_empty)
    )

    fun updateButtonInfoEntity(index: Int, id: Int) {
        val newButtonInfo = ButtonInfo(0, id, 0)
        buttonInfoEntity.updateButtonInfo(index, newButtonInfo)
    }

}
