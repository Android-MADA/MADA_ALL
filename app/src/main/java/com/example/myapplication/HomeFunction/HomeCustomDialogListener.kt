package com.example.myapplication.HomeFunction

import android.app.Dialog

interface HomeCustomDialogListener {

    fun onYesButtonClicked(dialog : Dialog)

    fun onNoButtonClicked(dialog : Dialog)
}