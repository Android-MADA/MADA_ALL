package com.mada.reapp.Adpater

import androidx.fragment.app.FragmentManager
import com.mada.reapp.custom_cloth
import com.mada.reapp.custom_color
import com.mada.reapp.custom_background
import com.mada.reapp.custom_item
import com.mada.reapp.databinding.CustomBackgroundBinding
import com.mada.reapp.databinding.CustomClothBinding
import com.mada.reapp.databinding.CustomColorBinding
import com.mada.reapp.databinding.CustomItemBinding

class CustompageAdapter(fm: FragmentManager?, private val numberOfFragment: Int, private val colorBinding: CustomColorBinding, private val clothBinding: CustomClothBinding, private val itemBinding: CustomItemBinding, private val backgroundBinding: CustomBackgroundBinding) :

    CustomAdapter() {
    fun getItem(position: Int): Any {
        return when (position) {
            0 -> custom_color(colorBinding)
            1 -> custom_cloth(clothBinding)
            2 -> custom_item(itemBinding)
            else -> custom_background(backgroundBinding)
        }
    }

}

