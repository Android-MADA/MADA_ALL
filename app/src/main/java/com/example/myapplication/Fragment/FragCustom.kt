package com.example.myapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.CustomBottomSheetViewPager
import com.example.myapplication.HomeFuction.view.HomeTimeEditFragment
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

class FragCustom : Fragment() {
    lateinit var binding: FragCustomBinding
    private lateinit var frag_color: custom_color
    private lateinit var frag_cloth: custom_cloth
    private lateinit var frag_item: custom_item
    private lateinit var frag_background: custom_background
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private var tabCurrentIdx = 0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragCustomBinding.inflate(inflater, container, false)
        var view = binding.root
        var customtabLayout = binding.CustomPagetabLayout
        var viewPager = binding.CustomBottomSheetViewPager

        frag_color = custom_color()
        frag_cloth = custom_cloth()
        frag_item = custom_item()
        frag_background = custom_background()

        val fragmentManager: FragmentManager = childFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.commit()

        customtabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val inflater1: LayoutInflater = layoutInflater
                var pos = tab.position
                when (pos) {
                    0 -> {//인플레이터 사용해서 레이아웃을 연결해주는 방식
                        inflater1.inflate(R.layout.custom_color, binding.CustomBottomSheetTable, true)
                    }
                    1 -> {
                        inflater1.inflate(R.layout.custom_cloth, binding.CustomBottomSheetTable, true)
                    }
                    2 -> {
                        inflater1.inflate(R.layout.custom_item, binding.CustomBottomSheetTable, true)
                    }
                    3 -> {
                        inflater1.inflate(R.layout.custom_background, binding.CustomBottomSheetTable, true)
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



        return view
    }

    fun onClickbtncolor() {
        binding.customRamdi.setImageResource(R.drawable.c_ramdi)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initBottomSheet() {
        val sheetBehavior = BottomSheetBehavior.from(binding.CustomBottomSheet)
        sheetBehavior.isGestureInsetBottomIgnored = true
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_DRAGGING -> {
                        val inflater1: LayoutInflater = layoutInflater
                        inflater1.inflate(R.layout.custom_color, binding.CustomBottomSheetTable, true)
                    }
                }
            }
        })
    }

}