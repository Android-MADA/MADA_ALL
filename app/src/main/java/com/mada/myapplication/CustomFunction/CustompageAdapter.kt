package com.mada.myapplication.Adpater

import androidx.fragment.app.FragmentManager
import com.mada.myapplication.custom_cloth
import com.mada.myapplication.custom_color
import com.mada.myapplication.custom_background
import com.mada.myapplication.custom_item
import com.mada.myapplication.databinding.CustomBackgroundBinding
import com.mada.myapplication.databinding.CustomClothBinding
import com.mada.myapplication.databinding.CustomColorBinding
import com.mada.myapplication.databinding.CustomItemBinding

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

