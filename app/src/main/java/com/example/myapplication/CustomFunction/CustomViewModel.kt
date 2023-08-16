package com.example.myapplication.CustomFunction

import androidx.lifecycle.ViewModel
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.Fragment.FragCustom

class CustomViewModel : ViewModel() {

    private var savedButtonInfo: FragCustom.selectedButtonInfo? = null

    fun saveButtonInfo(buttonInfo: FragCustom.selectedButtonInfo) {
        savedButtonInfo = buttonInfo
    }

    fun getSavedButtonInfo(): FragCustom.selectedButtonInfo? {
        return savedButtonInfo
    }
}