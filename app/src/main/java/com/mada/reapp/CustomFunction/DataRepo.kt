package com.mada.reapp.CustomFunction

import com.mada.reapp.HomeFunction.viewModel.HomeViewModel
import com.mada.reapp.R

object DataRepo {
    var buttonInfoEntity: ButtonInfoEntity =  ButtonInfoEntity(
        id = 0,
        colorButtonInfo = ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi),
        clothButtonInfo = ButtonInfo(0, 49, R.drawable.custom_empty),
        itemButtonInfo = ButtonInfo(0, 50, R.drawable.custom_empty),
        backgroundButtonInfo = ButtonInfo(0, 48, R.drawable.custom_empty)
    )


    fun updateButtonInfoEntity(viewModel: HomeViewModel, index: Int, id: Int) {
        viewModel.updateButtonInfoEntity(index, id)
    }

}
