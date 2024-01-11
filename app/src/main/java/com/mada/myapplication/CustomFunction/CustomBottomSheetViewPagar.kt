package com.mada.myapplication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mada.myapplication.Fragment.FragCustom

class  CustomBottomSheetViewPager (customCloth : custom_cloth,customColor : custom_color,
                                   customItem : custom_item,customBackground : custom_background,fragment : FragCustom
)
    : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 4
    val custom_color  = customColor
    val custom_cloth  = customCloth
    val custom_item  = customItem
    val custom_background  = customBackground
    private val registeredFragments = mutableMapOf<Int, Fragment>(
        0 to custom_color,
        1 to custom_cloth,
        2 to custom_item,
        3 to custom_background
    )



    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> custom_color
            1 -> custom_cloth
            2 -> custom_item
            else -> custom_background
        }
    }

    fun getFragmentAtPosition(position: Int): Fragment? {
        return registeredFragments[position]
    }
}
