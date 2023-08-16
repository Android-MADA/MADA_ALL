package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.myapplication.Adpater.CustompageAdapter
import com.example.myapplication.Fragment.FragCustom
import java.lang.reflect.Field

class  CustomBottomSheetViewPager (fragment : FragCustom) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 4

    val custom_color  = custom_color()
    val custom_cloth  = custom_cloth()
    val custom_item  = custom_item()
    val custom_background  = custom_background()

    private val registeredFragments = mutableMapOf<Int, Fragment>(
        0 to custom_color,
        1 to custom_cloth,
        2 to custom_item,
        3 to custom_background
    )

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> custom_color()
            1 -> custom_cloth()
            2 -> custom_item()
            else -> custom_background()
        }
    }

    fun getFragmentAtPosition(position: Int): Fragment? {
        return registeredFragments[position]
    }
}
