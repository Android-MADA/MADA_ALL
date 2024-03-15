package com.mada.myapplication.CustomFunction

import com.mada.myapplication.R

object DataRepo {
    var buttonInfoEntity: ButtonInfoEntity =  ButtonInfoEntity(
        id = 0,
        colorButtonInfo = ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi),
        clothButtonInfo = ButtonInfo(0, 49, R.drawable.custom_empty),
        itemButtonInfo = ButtonInfo(0, 50, R.drawable.custom_empty),
        backgroundButtonInfo = ButtonInfo(0, 48, R.drawable.custom_empty)
    )

}