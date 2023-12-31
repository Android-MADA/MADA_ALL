package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.myapplication.Adpater.CustompageAdapter
import com.example.myapplication.CustomFunction.customItemCheckDATA
import com.example.myapplication.Fragment.FragCustom
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Field

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
