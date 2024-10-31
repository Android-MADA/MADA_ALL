package com.mada.reapp.HomeFunction

import android.app.Dialog

interface HomeCustomDialogListener {

    fun onYesButtonClicked(dialog : Dialog, flag : String)

    fun onNoButtonClicked(dialog : Dialog)
}