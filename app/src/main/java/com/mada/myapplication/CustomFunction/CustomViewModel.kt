package com.mada.myapplication.CustomFunction

import androidx.lifecycle.ViewModel
import com.mada.myapplication.Fragment.FragCustom

class CustomViewModel : ViewModel() {

    private var savedButtonInfo: FragCustom.selectedButtonInfo? = null

    fun saveButtonInfo(buttonInfo: FragCustom.selectedButtonInfo) {
        savedButtonInfo = buttonInfo
    }

    fun getSavedButtonInfo(): FragCustom.selectedButtonInfo? {
        return savedButtonInfo
    }
}