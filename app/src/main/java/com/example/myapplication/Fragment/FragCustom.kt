package com.example.myapplication.Fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.CustomBottomSheetViewPager
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.CustomFunction.CustomViewModel
import com.example.myapplication.CustomFunction.RetrofitServiceCustom
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
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Math.log
import java.math.BigInteger
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.myapplication.CustomFunction.CustomItemChangeDATA
import com.example.myapplication.CustomFunction.customItemCheckDATA
import com.example.myapplication.CustomFunction.customPrintDATA
import com.example.myapplication.MyFuction.MyWebviewActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

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
    private lateinit var viewPager: ViewPager2
    private var selectedColorButtonInfo: ButtonInfo? = null
    private var selectedClothButtonInfo: ButtonInfo? = null
    private var selectedItemButtonInfo: ButtonInfo? = null
    private var selectedBackgroundButtonInfo: ButtonInfo? = null
    private var custom_save = false
    private var button_temdata: selectedButtonInfo? = null
    private val viewModel: CustomViewModel by viewModels()

    private var colorFragment: custom_color? = null
    private var clothFragment: custom_cloth? = null
    private var backgroundFragment: custom_background? = null
    private var adapter: CustomBottomSheetViewPager? = null

    private var printId: Int = 0
    private var printfilePath: String = "z"

    private var unsavedChanges = false



    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)

    val token = MyWebviewActivity.prefs.getString("token","")






    data class selectedButtonInfo(
        var selectedColorButtonInfo: ButtonInfo?,
        var selectedClothButtonInfo: ButtonInfo?,
        var selectedItemButtonInfo: ButtonInfo?,
        var selectedBackgroundButtonInfo: ButtonInfo?
    )

    fun setImageViewWithFilePath(imageView: ImageView, filePath: String) {
        try {
            Picasso.get()
                .load(filePath)
                .into(imageView)
        } catch (e: Exception) {
            Log.e("Picasso Error", "Error loading image: ${e.message}")
        }
    }


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

        getCustomPrint()
        postcustomItemBuy(11)
        postcustomItemBuy(41)
        postcustomItemBuy(22)
        postcustomItemBuy(1)



        if (printId in 1..9) {
            setImageViewWithFilePath(binding.imgCustomBackground, printfilePath)
            Log.d("background", "${printId} ${printfilePath}")
        }
        if (printId in 10..18) {
            setImageViewWithFilePath(binding.customRamdi, printfilePath)
            Log.d("customRamdi", "${printId} ${printfilePath}")
        }
        if (printId in 19..38) {
            setImageViewWithFilePath(binding.imgCustomItem, printfilePath)
            Log.d("imgCustomItem", "${printId} ${printfilePath}")
        }
        if (printId in 39..47) {
            setImageViewWithFilePath(binding.imgCustomCloth, printfilePath)
            Log.d("imgCustomCloth", "${printId} ${printfilePath}")
        }




        /*val savedData = viewModel.getSavedButtonInfo()
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
        }*/


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
        customRamdi_layoutParams.height = 1200  // 원하는 높이(dp 단위)
        customRamdi.layoutParams = customRamdi_layoutParams

        val imgCustomCloth = binding.imgCustomCloth
        val imgCustomCloth_layoutParams = imgCustomCloth.layoutParams
        imgCustomCloth_layoutParams.width = 1200  // 원하는 너비(dp 단위)
        imgCustomCloth_layoutParams.height = 1200 // 원하는 높이(dp 단위)
        imgCustomCloth.layoutParams = imgCustomCloth_layoutParams

        val imgCustomItem = binding.imgCustomItem
        val imgCustomItem_layoutParams = imgCustomItem.layoutParams
        imgCustomItem_layoutParams.width = 1200 // 원하는 너비(dp 단위)
        imgCustomItem_layoutParams.height = 1200 // 원하는 높이(dp 단위)
        imgCustomItem.layoutParams = imgCustomItem_layoutParams

        val imgCustomBackground = binding.imgCustomBackground
        val imgCustomBackground_layoutParams = imgCustomBackground.layoutParams
        imgCustomBackground_layoutParams.width = 2000 // 원하는 너비(dp 단위)
        imgCustomBackground_layoutParams.height = 2000 // 원하는 높이(dp 단위)
        imgCustomBackground.layoutParams = imgCustomBackground_layoutParams

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.CustomBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 바텀시트 상태 변화를 감지하는 메서드
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                customRamdi_layoutParams.width = (width * (1 - slideOffset) + 800).toInt()
                customRamdi_layoutParams.height = (height * (1 - slideOffset) + 800).toInt()
                customRamdi.layoutParams = imgCustomCloth_layoutParams

                imgCustomCloth_layoutParams.width = (width * (1 - slideOffset) + 800).toInt()
                imgCustomCloth_layoutParams.height = (height * (1 - slideOffset) + 800).toInt()
                imgCustomCloth.layoutParams = imgCustomCloth_layoutParams

                imgCustomItem_layoutParams.width = (width * (1 - slideOffset) + 800).toInt()
                imgCustomItem_layoutParams.height = (height * (1 - slideOffset) + 800).toInt()
                imgCustomItem.layoutParams = imgCustomItem_layoutParams

                imgCustomBackground_layoutParams.width = (width * (1 - slideOffset) + 2000).toInt()
                imgCustomBackground_layoutParams.height =
                    (height * (1 - slideOffset) + 2000).toInt()
                imgCustomBackground.layoutParams = imgCustomBackground_layoutParams


            }
        })

        binding.btnCustomReset.setOnClickListener {
            val colorbtninfo = ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi)
            selectedColorButtonInfo = colorbtninfo
            val clothbtninfo = ButtonInfo(R.id.btn_cloth_basic, 900, R.drawable.custom_empty)
            selectedClothButtonInfo = clothbtninfo
            val itembtninfo = ButtonInfo(R.id.btn_item_basic, 800,R.drawable.custom_empty)
            selectedItemButtonInfo = itembtninfo
            val backgroundbtninfo = ButtonInfo(R.id.btn_back_basic, 700,R.drawable.custom_empty)
            selectedBackgroundButtonInfo = backgroundbtninfo
            binding.customRamdi.setImageResource(R.drawable.c_ramdi)
            binding.imgCustomCloth.setImageResource(R.drawable.custom_empty)
            binding.imgCustomItem.setImageResource(R.drawable.custom_empty)
            binding.imgCustomBackground.setImageResource(R.drawable.custom_empty)
            onResetButtonClicked()
            getcustomReset()


        }



        binding.btnCustomSave.setOnClickListener {
            custom_save = true
            viewModel.saveButtonInfo(getSelectedButtonInfo())
            val temdata = getSelectedButtonInfo()
            Log.d("Savedata", "${temdata.selectedColorButtonInfo?.serverID} ${temdata.selectedClothButtonInfo?.serverID} ${temdata.selectedItemButtonInfo?.serverID} ${temdata.selectedBackgroundButtonInfo?.serverID}")
            if (temdata.selectedColorButtonInfo?.serverID != 0){
                patchCustomItemChange(temdata.selectedColorButtonInfo!!.serverID)
            }
            if (temdata.selectedClothButtonInfo?.serverID != 900){
                patchCustomItemChange(temdata.selectedClothButtonInfo!!.serverID)
            }
            if (temdata.selectedItemButtonInfo?.serverID != 800){
                patchCustomItemChange(temdata.selectedItemButtonInfo!!.serverID)
            }
            if (temdata.selectedBackgroundButtonInfo?.serverID != 700){
                patchCustomItemChange(temdata.selectedBackgroundButtonInfo!!.serverID)
            }
            unsavedChanges = false
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackConfirmationDialog()
            }
        })



        return view
    }




    override fun onColorButtonSelected(colorbuttonInfo: ButtonInfo) {
        // 선택한 버튼에 대한 리소스를 이미지뷰에 적용
        custom_save = false
        binding.customRamdi.setImageResource(colorbuttonInfo.selectedImageResource)
        selectedColorButtonInfo = colorbuttonInfo
        unsavedChanges = true
    }

    override fun onClothButtonSelected(clothbuttonInfo: ButtonInfo) {
        custom_save = false
        binding.imgCustomCloth.setImageResource(clothbuttonInfo.selectedImageResource)
        selectedClothButtonInfo = clothbuttonInfo
        unsavedChanges = true
    }

    override fun onItemButtonSelected(itembuttonInfo: ButtonInfo) {
        custom_save = false
        binding.imgCustomItem.setImageResource(itembuttonInfo.selectedImageResource)
        selectedItemButtonInfo = itembuttonInfo
        unsavedChanges = true
    }

    override fun onBackgroundButtonSelected(backgroundbuttonInfo: ButtonInfo) {
        custom_save = false
        binding.imgCustomBackground.setImageResource(backgroundbuttonInfo.selectedImageResource)
        selectedBackgroundButtonInfo = backgroundbuttonInfo
        unsavedChanges = true
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

        /*val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView?.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.fragHome -> {
                    if (unsavedChanges) {
                        showBackConfirmationDialog()
                    }
                    true
                }
                R.id.fragCalendar -> {
                    if (unsavedChanges) {
                        showBackConfirmationDialog()
                    }
                    true
                }
                R.id.fragMy -> {
                    if (unsavedChanges) {
                        showBackConfirmationDialog()
                    }
                    true
                }
                // Handle other menu items
                else -> false
            }
        }*/
    }

    override fun onResetButtonClicked() {
        Log.d("FragCustom", "onResetButtonClicked()")
        val colorFragment = (viewPager.adapter as? CustomBottomSheetViewPager)?.getFragmentAtPosition(0) as? custom_color
        val clothFragment = (viewPager.adapter as? CustomBottomSheetViewPager)?.getFragmentAtPosition(1) as? custom_cloth
        val itemFragment = (viewPager.adapter as? CustomBottomSheetViewPager)?.getFragmentAtPosition(2) as? custom_item
        val backgroundFragment = (viewPager.adapter as? CustomBottomSheetViewPager)?.getFragmentAtPosition(1) as? custom_background
        colorFragment?.resetButtonColor()
        clothFragment?.resetButtonCloth()
        itemFragment?.resetButtonItem()
        backgroundFragment?.resetButtonBackground()
    }


    private fun getCustomPrint() {
        val call: Call<customPrintDATA> = service.customPrint(token)

        call.enqueue(object : Callback<customPrintDATA> {
            override fun onResponse(call: Call<customPrintDATA>, response: Response<customPrintDATA>) {
                val printInfo = response.body()
                val responseCode = response.code()
                printInfo?.data?.forEachIndexed { index, item ->
                    printId = item.id
                    printfilePath = item.filePath
                    Log.d("getCustomPrint", "Item $index - id: ${item.id} itemType: ${item.itemType} filePath: ${item.filePath}")
                    Log.d("patchCustomItemChange", "Response Code: $responseCode")
                }
            }

            override fun onFailure(call: Call<customPrintDATA>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })

    }




    fun patchCustomItemChange(itemID: Int) {
        val call: Call<Void> = service.customItemChange(token, itemID)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val responseCode = response.code()
                Log.d("patchCustomItemChange", "Response Code: $responseCode")
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }

    fun getcustomReset() {
        val call: Call<Void> = service.customReset(token)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val responseCode = response.code()
                Log.d("putcustomReset", "Response Code: $responseCode")
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }

    fun postcustomItemBuy(itemID: Int) {
        val call: Call<Void> = service.customItemBuy(token, itemID)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val responseCode = response.code()
                Log.d("postcustomItemBuy", "Response Code: $responseCode")
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }


    /*private fun showBackConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Unsaved Changes")
            .setMessage("You have unsaved changes. Are you sure you want to go back?")
            .setPositiveButton("Discard") { _, _ ->
                // Discard changes and navigate back
                requireActivity().onBackPressed()
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }*/

    private fun showBackConfirmationDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.notice_home_back, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val btnNo = dialogView.findViewById<AppCompatButton>(R.id.btn_home_dialog_back_no)
        val btnYes = dialogView.findViewById<AppCompatButton>(R.id.btn_home_dialog_back_yes)

        btnNo.setOnClickListener {
            alertDialog.dismiss()
            // Handle "No" button click if needed
        }

        btnYes.setOnClickListener {
            alertDialog.dismiss()
            requireActivity().onBackPressed()
        }

        alertDialog.show()
    }




}

