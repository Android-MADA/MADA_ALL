package com.example.myapplication.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.CustomBottomSheetViewPager
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.R
import com.example.myapplication.custom_background
import com.example.myapplication.custom_cloth
import com.example.myapplication.custom_color
import com.example.myapplication.custom_item
import com.example.myapplication.databinding.CustomBackgroundBinding
import com.example.myapplication.databinding.CustomClothBinding
import com.example.myapplication.databinding.CustomColorBinding
import com.example.myapplication.databinding.CustomItemBinding
import com.example.myapplication.databinding.FragCustomBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.tabs.TabLayout
import java.lang.Math.log

interface OnImageChangeListener {
    fun onButtonSelected(buttonInfo: ButtonInfo)
}


class FragCustom : Fragment(), OnImageChangeListener {
    lateinit var binding: FragCustomBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private var tabCurrentIdx = 0
    private var customRamdi: ImageView? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragCustomBinding.inflate(inflater, container, false)
        var view = binding.root
        var customtabLayout = binding.CustomPagetabLayout
        var viewPager = binding.CustomBottomSheetViewPager

        val colorFragment = custom_color()
        val clothFragment = custom_cloth()
        val itemFragment = custom_item()
        val backgroundFragment = custom_background()

        val fragmentManager: FragmentManager = childFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.commit()

        customtabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val inflater1: LayoutInflater = layoutInflater
                var pos = tab.position
                when (pos) {
                    0 -> {
                        childFragmentManager.beginTransaction()
                            .replace(R.id.CustomBottomSheetTable, colorFragment)
                            .commit()
                    }
                    1 -> {
                        childFragmentManager.beginTransaction()
                            .replace(R.id.CustomBottomSheetTable, clothFragment)
                            .commit()
                    }
                    2 -> {
                        childFragmentManager.beginTransaction()
                            .replace(R.id.CustomBottomSheetTable, itemFragment)
                            .commit()
                    }
                    3 -> {
                        childFragmentManager.beginTransaction()
                            .replace(R.id.CustomBottomSheetTable, backgroundFragment)
                            .commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // do nothing
            }
        })



        var width = 580
        var height = 580
        val customRamdi = binding.customRamdi
        val customRamdi_layoutParams = customRamdi.layoutParams
        customRamdi_layoutParams.width = 1080 // 원하는 너비(dp 단위)
        customRamdi_layoutParams.height = 1080 // 원하는 높이(dp 단위)
        customRamdi.layoutParams = customRamdi_layoutParams

        val imgCustomCloth = binding.imgCustomCloth
        val imgCustomCloth_layoutParams = imgCustomCloth.layoutParams
        imgCustomCloth_layoutParams.width = 1080 // 원하는 너비(dp 단위)
        imgCustomCloth_layoutParams.height = 1080 // 원하는 높이(dp 단위)
        imgCustomCloth.layoutParams = imgCustomCloth_layoutParams

        val imgCustomItem = binding.imgCustomItem
        val imgCustomItem_layoutParams = imgCustomItem.layoutParams
        imgCustomItem_layoutParams.width = 1080 // 원하는 너비(dp 단위)
        imgCustomItem_layoutParams.height = 1080 // 원하는 높이(dp 단위)
        imgCustomItem.layoutParams = imgCustomItem_layoutParams

        val imgCustomBackground = binding.imgCustomBackground
        val imgCustomBackground_layoutParams = imgCustomBackground.layoutParams
        imgCustomBackground_layoutParams.width = 1080 // 원하는 너비(dp 단위)
        imgCustomBackground_layoutParams.height = 1080 // 원하는 높이(dp 단위)
        imgCustomBackground.layoutParams = imgCustomBackground_layoutParams

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.CustomBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 바텀시트 상태 변화를 감지하는 메서드
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                customRamdi_layoutParams.width = (width*(1-slideOffset)+600).toInt()
                customRamdi_layoutParams.height = (height*(1-slideOffset)+600).toInt()
                customRamdi.layoutParams = imgCustomCloth_layoutParams

                imgCustomCloth_layoutParams.width = (width*(1-slideOffset)+600).toInt()
                imgCustomCloth_layoutParams.height = (height*(1-slideOffset)+600).toInt()
                imgCustomCloth.layoutParams = imgCustomCloth_layoutParams

                imgCustomItem_layoutParams.width = (width*(1-slideOffset)+600).toInt()
                imgCustomItem_layoutParams.height = (height*(1-slideOffset)+600).toInt()
                imgCustomItem.layoutParams = imgCustomItem_layoutParams

                imgCustomBackground_layoutParams.width = (width*(1-slideOffset)+600).toInt()
                imgCustomBackground_layoutParams.height = (height*(1-slideOffset)+600).toInt()
                imgCustomBackground.layoutParams = imgCustomBackground_layoutParams


            }
        })






        return view
    }

    override fun onButtonSelected(buttonInfo: ButtonInfo) {
        // 선택한 버튼에 대한 리소스를 이미지뷰에 적용
        Log.d("dsa",buttonInfo.selectedImageResource.toString())
        binding.customRamdi.setImageResource(buttonInfo.selectedImageResource)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}