package com.example.myapplication.CustomFunction

import androidx.lifecycle.ViewModel
import com.example.myapplication.Fragment.FragCustom

class NewViewModel : ViewModel() {
    private var savedButtonInfo: FragCustom.selectedButtonInfo? = null

    fun saveButtonInfo(buttonInfo: FragCustom.selectedButtonInfo) {
        savedButtonInfo = buttonInfo
    }

    fun getSavedButtonInfo(): FragCustom.selectedButtonInfo? {
        return savedButtonInfo
    }

}