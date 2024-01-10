package com.mada.myapplication.Adpater

import androidx.fragment.app.FragmentManager
import com.mada.myapplication.custom_cloth
import com.mada.myapplication.custom_color
import com.mada.myapplication.custom_background
import com.mada.myapplication.custom_item

class CustompageAdapter(fm: FragmentManager?, private val numberOfFragment: Int) :

    CustomAdapter() {
    fun getItem(position: Int): Any {
        return when (position) {
            0 -> custom_color()
            1 -> custom_cloth()
            2 -> custom_item()
            else -> custom_background()
        }
    }

}

