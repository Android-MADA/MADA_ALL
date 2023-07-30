package com.example.myapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.google.android.material.tabs.TabLayout

class FragCustom : Fragment() {
    lateinit var binding: FragCustomBinding
    lateinit var colorbinding: CustomColorBinding
    lateinit var clothbinding: CustomClothBinding
    lateinit var itembinding: CustomItemBinding
    lateinit var backgroundBinding: CustomBackgroundBinding
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




        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val inflater1: LayoutInflater = layoutInflater
                var pos = tab.position
                var colorTable = colorbinding.customColortable
                var clothTable = clothbinding.customClothtable
                var itemTable = itembinding.customItemtable
                //네비게이션 바처럼 레이아웃을 전환하는 방식
                parentFragmentManager.beginTransaction()
                    .replace(R.id.CustomBottomSheetTable, custom_color())
                    .commitAllowingStateLoss()

                when (pos) {
                    0 -> {//인플레이터 사용해서 레이아웃을 연결해주는 방식
                        inflater1.inflate(R.id.custom_colortable, binding.CustomBottomSheetTable, true)
                    }

                    1 -> {
                        inflater1.inflate(R.id.custom_clothtable, binding.CustomBottomSheetTable, true)
                    }
                    2 -> {
                        inflater1.inflate(R.id.custom_itemtable, binding.CustomBottomSheetTable, true)
                    }
                    3 -> {
                        inflater1.inflate(R.id.custom_backgroundtable, binding.CustomBottomSheetTable, true)
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



        return binding.root
    }

    private fun changeView(index: Int) {





    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}