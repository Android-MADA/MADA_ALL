package com.example.myapplication.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.CustomBottomSheetViewPager
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.CustomFunction.CustomViewModel
import com.example.myapplication.R
import com.example.myapplication.custom_cloth
import com.example.myapplication.custom_color
import com.example.myapplication.databinding.CustomBackgroundBinding
import com.example.myapplication.databinding.CustomClothBinding
import com.example.myapplication.databinding.CustomColorBinding
import com.example.myapplication.databinding.CustomItemBinding
import com.example.myapplication.databinding.FragCustomBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Math.log
import java.math.BigInteger

interface OnColorImageChangeListener {
    fun onColorButtonSelected(buttonInfo: ButtonInfo)
}

interface OnClothImageChangeListener {
    fun onClothButtonSelected(clothbuttonInfo: ButtonInfo)
}

interface OnItemImageChangeListener {
    fun onItemButtonSelected(buttonInfo: ButtonInfo)
}

interface OnBackgroundImageChangeListener {
    fun onBackgroundButtonSelected(buttonInfo: ButtonInfo)
}

interface OnResetButtonClickListener {
    fun onResetButtonClicked()
}


class FragCustom : Fragment(), OnColorImageChangeListener, OnClothImageChangeListener, OnItemImageChangeListener, OnBackgroundImageChangeListener, OnResetButtonClickListener {
    lateinit var binding: FragCustomBinding
    private lateinit var customtabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private var tabCurrentIdx = 0
    private var selectedColorButtonInfo: ButtonInfo? = null
    private var selectedClothButtonInfo: ButtonInfo? = null
    private var selectedItemButtonInfo: ButtonInfo? = null
    private var selectedBackgroundButtonInfo: ButtonInfo? = null
    private var custom_save = false
    private var button_temdata: selectedButtonInfo? = null
    private val viewModel: CustomViewModel by viewModels()

    private var colorFragment: custom_color? = null
    private var clothFragment: custom_cloth? = null
    private var adapter: CustomBottomSheetViewPager? = null

    private lateinit var preColor : ImageButton
    private lateinit var preCloth : ImageButton
    private lateinit var preItem : ImageButton
    private lateinit var preBackground : ImageButton
    data class selectedButtonInfo(
        var selectedColorButtonInfo: ButtonInfo?,
        var selectedClothButtonInfo: ButtonInfo?,
        var selectedItemButtonInfo: ButtonInfo?,
        var selectedBackgroundButtonInfo: ButtonInfo?
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragCustomBinding.inflate(inflater, container, false)
        var view = binding.root
        var customtabLayout = binding.CustomPagetabLayout
        binding.CustomBottomSheetViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.CustomBottomSheetViewPager.adapter = CustomBottomSheetViewPager(this)
        viewPager = binding.CustomBottomSheetViewPager



        val savedData = viewModel.getSavedButtonInfo()
        if (savedData != null) {

            selectedColorButtonInfo = savedData.selectedColorButtonInfo
            selectedClothButtonInfo = savedData.selectedClothButtonInfo
            selectedItemButtonInfo = savedData.selectedItemButtonInfo
            selectedBackgroundButtonInfo = savedData.selectedBackgroundButtonInfo

            binding.customRamdi.setImageResource(
                selectedColorButtonInfo?.selectedImageResource ?: 0
            )
            binding.imgCustomCloth.setImageResource(
                selectedClothButtonInfo?.selectedImageResource ?: 0
            )
            binding.imgCustomItem.setImageResource(
                selectedItemButtonInfo?.selectedImageResource ?: 0
            )
            binding.imgCustomBackground.setImageResource(
                selectedBackgroundButtonInfo?.selectedImageResource ?: 0
            )
        }


        /*val fragmentManager: FragmentManager = childFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.commit()*/
        adapter = CustomBottomSheetViewPager(this)
        binding.CustomBottomSheetViewPager.adapter = adapter

        val tabTitles = listOf("색깔", "의상", "소품", "배경")

        TabLayoutMediator(customtabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()



        var width = 500
        var height = 500
        val customRamdi = binding.customRamdi
        val customRamdi_layoutParams = customRamdi.layoutParams
        customRamdi_layoutParams.width = 1200 // 원하는 너비(dp 단위)
        customRamdi_layoutParams.height = 1200 // 원하는 높이(dp 단위)
        customRamdi.layoutParams = customRamdi_layoutParams

        val imgCustomCloth = binding.imgCustomCloth
        val imgCustomCloth_layoutParams = imgCustomCloth.layoutParams
        imgCustomCloth_layoutParams.width = 1200 // 원하는 너비(dp 단위)
        imgCustomCloth_layoutParams.height = 1200 // 원하는 높이(dp 단위)
        imgCustomCloth.layoutParams = imgCustomCloth_layoutParams

        val imgCustomItem = binding.imgCustomItem
        val imgCustomItem_layoutParams = imgCustomItem.layoutParams
        imgCustomItem_layoutParams.width = 1200 // 원하는 너비(dp 단위)
        imgCustomItem_layoutParams.height = 1200 // 원하는 높이(dp 단위)
        imgCustomItem.layoutParams = imgCustomItem_layoutParams

        val imgCustomBackground = binding.imgCustomBackground
        val imgCustomBackground_layoutParams = imgCustomBackground.layoutParams
        imgCustomBackground_layoutParams.width = 1900 // 원하는 너비(dp 단위)
        imgCustomBackground_layoutParams.height = 1900 // 원하는 높이(dp 단위)
        imgCustomBackground.layoutParams = imgCustomBackground_layoutParams

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.CustomBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 바텀시트 상태 변화를 감지하는 메서드
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                customRamdi_layoutParams.width = (width * (1 - slideOffset) + 700).toInt()
                customRamdi_layoutParams.height = (height * (1 - slideOffset) + 700).toInt()
                customRamdi.layoutParams = imgCustomCloth_layoutParams

                imgCustomCloth_layoutParams.width = (width * (1 - slideOffset) + 700).toInt()
                imgCustomCloth_layoutParams.height = (height * (1 - slideOffset) + 700).toInt()
                imgCustomCloth.layoutParams = imgCustomCloth_layoutParams

                imgCustomItem_layoutParams.width = (width * (1 - slideOffset) + 700).toInt()
                imgCustomItem_layoutParams.height = (height * (1 - slideOffset) + 700).toInt()
                imgCustomItem.layoutParams = imgCustomItem_layoutParams

                imgCustomBackground_layoutParams.width = (width * (1 - slideOffset) + 1500).toInt()
                imgCustomBackground_layoutParams.height =
                    (height * (1 - slideOffset) + 1500).toInt()
                imgCustomBackground.layoutParams = imgCustomBackground_layoutParams


            }
        })

        binding.btnCustomReset.setOnClickListener {
            val colorbtninfo = ButtonInfo(R.id.btn_color_basic, R.drawable.c_ramdi)
            selectedColorButtonInfo = colorbtninfo
            val clothbtninfo = ButtonInfo(
                R.id.btn_cloth_basic,
                R.drawable.custom_empty
            )
            selectedClothButtonInfo = clothbtninfo
            val itembtninfo = ButtonInfo(R.id.btn_item_basic, R.drawable.custom_empty)
            selectedItemButtonInfo = itembtninfo
            val backgroundbtninfo = ButtonInfo(
                R.id.btn_back_basic,
                R.drawable.custom_empty
            )
            selectedBackgroundButtonInfo = backgroundbtninfo
            binding.customRamdi.setImageResource(R.drawable.c_ramdi)
            binding.imgCustomCloth.setImageResource(R.drawable.custom_empty)
            binding.imgCustomItem.setImageResource(R.drawable.custom_empty)
            binding.imgCustomBackground.setImageResource(R.drawable.custom_empty)
            onResetButtonClicked()


        }



        binding.btnCustomSave.setOnClickListener {
            custom_save = true
            viewModel.saveButtonInfo(getSelectedButtonInfo())
        }





        return view
    }


    override fun onColorButtonSelected(colorbuttonInfo: ButtonInfo) {
        // 선택한 버튼에 대한 리소스를 이미지뷰에 적용
        custom_save = false
        binding.customRamdi.setImageResource(colorbuttonInfo.selectedImageResource)
        //preColor = colorbuttonInfo.selectedButton
        selectedColorButtonInfo = colorbuttonInfo
    }

    override fun onClothButtonSelected(clothbuttonInfo: ButtonInfo) {
        custom_save = false
        binding.imgCustomCloth.setImageResource(clothbuttonInfo.selectedImageResource)
        //preCloth = clothbuttonInfo.selectedButton
        selectedClothButtonInfo = clothbuttonInfo
    }

    override fun onItemButtonSelected(itembuttonInfo: ButtonInfo) {
        custom_save = false
        binding.imgCustomItem.setImageResource(itembuttonInfo.selectedImageResource)
        //preItem = itembuttonInfo.selectedButton
        selectedItemButtonInfo = itembuttonInfo
    }

    override fun onBackgroundButtonSelected(backgroundbuttonInfo: ButtonInfo) {
        custom_save = false
        binding.imgCustomBackground.setImageResource(backgroundbuttonInfo.selectedImageResource)
        //preBackground = backgroundbuttonInfo.selectedButton
        selectedBackgroundButtonInfo = backgroundbuttonInfo
    }

    fun getSelectedButtonInfo(): selectedButtonInfo {

        fun getSelectedColorButtonInfo(): ButtonInfo? {
            return selectedColorButtonInfo
        }

        fun getSelectedClothButtonInfo(): ButtonInfo? {
            return selectedClothButtonInfo
        }

        fun getSelectedItemButtonInfo(): ButtonInfo? {
            return selectedItemButtonInfo
        }

        fun getSelectedBackgroundButtonInfo(): ButtonInfo? {
            return selectedBackgroundButtonInfo
        }

        val temdata = selectedButtonInfo(
            getSelectedColorButtonInfo(),
            getSelectedClothButtonInfo(),
            getSelectedItemButtonInfo(),
            getSelectedBackgroundButtonInfo()
        )

        return temdata

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = binding.CustomBottomSheetViewPager
    }

    override fun onResetButtonClicked() {
        Log.d("FragCustom", "onResetButtonClicked()")
        val colorFragment = (viewPager.adapter as? CustomBottomSheetViewPager)?.getFragmentAtPosition(0) as? custom_color
        colorFragment?.resetButtonColor()
        //clothFragment?.resetButtonImage()
        //itemFragment?.resetButtonImage()
        //backgroundFragment?.resetButtonImage()
    }
}

    /* override fun onBackPressed() {
        // if(var custom_save = false) {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_add_popup, null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
                .create()

            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            mBuilder.show()

            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

            val deviceWidth = displayMetrics.widthPixels

            val desiredRatio = 0.8f
            val desiredWidth = (deviceWidth * desiredRatio).toInt()
            mBuilder?.window?.setLayout(desiredWidth, WindowManager.LayoutParams.WRAP_CONTENT)

            val display = requireActivity().windowManager.defaultDisplay
            mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
                mBuilder.dismiss()
            })
            mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
                저장
            }) */