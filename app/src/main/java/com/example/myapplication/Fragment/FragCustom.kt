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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.CustomBottomSheetViewPager
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.CustomFunction.CustomViewModel
import com.example.myapplication.CustomFunction.RetrofitServiceCustom
import com.example.myapplication.R
import com.example.myapplication.custom_background
import com.example.myapplication.custom_cloth
import com.example.myapplication.custom_color
import com.example.myapplication.custom_item
import com.example.myapplication.databinding.FragCustomBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.app.AlertDialog
import android.graphics.Point
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myapplication.CustomFunction.NewViewModel
import com.example.myapplication.CustomFunction.customPrintDATA
import com.example.myapplication.StartFuction.Splash2Activity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

data class IdAndItemType(
    val id: Int,
    val itemType: String
)



class FragCustom : Fragment(), OnColorImageChangeListener, OnClothImageChangeListener,
    OnItemImageChangeListener, OnBackgroundImageChangeListener, OnResetButtonClickListener {
    lateinit var binding: FragCustomBinding
    private lateinit var viewPager: ViewPager2
    private var selectedColorButtonInfo: ButtonInfo? = null
    private var selectedClothButtonInfo: ButtonInfo? = null
    private var selectedItemButtonInfo: ButtonInfo? = null
    private var selectedBackgroundButtonInfo: ButtonInfo? = null
    private var custom_save = false
    private var button_temdata: selectedButtonInfo? = null
    private val viewModel: CustomViewModel by viewModels()
    private val newviewModel: NewViewModel by viewModels()


    private var colorFragment: custom_color? = null
    private var clothFragment: custom_cloth? = null
    private var itemFragment: custom_item? = null
    private var backgroundFragment: custom_background? = null

    private var adapter: CustomBottomSheetViewPager? = null

    private var printId: Int = 0
    private val printIds = mutableListOf<IdAndItemType>()
    private var itemType: String = "z"
    private var printfilePath: String = "z"
    private var curMenuItem : Int = 0

    private var unsavedChanges = false

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var customBottomSheet: ViewGroup

    private lateinit var alertDialog: AlertDialog



    val baseUrl = "http://15.165.210.13:8080/"
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)

    val token = Splash2Activity.prefs.getString("token", "")






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
        val colorFragment = custom_color()
        val clothFragment = custom_cloth()
        val itemFragment = custom_item()
        val backgroundFragment = custom_background()
        binding.CustomBottomSheetViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.CustomBottomSheetViewPager.adapter = CustomBottomSheetViewPager(clothFragment,colorFragment,itemFragment,backgroundFragment,this)
        viewPager = binding.CustomBottomSheetViewPager





        customBottomSheet = binding.CustomBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(customBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        getCustomPrint()
        //postcustomItemBuy(14)


        val savedData = viewModel.getSavedButtonInfo()
        val savedData2 = newviewModel.getSavedButtonInfo()

        if (savedData != null) {
            if(savedData2 ==null){
                selectedColorButtonInfo = savedData.selectedColorButtonInfo
                selectedClothButtonInfo = savedData.selectedClothButtonInfo
                selectedItemButtonInfo = savedData.selectedItemButtonInfo
                selectedBackgroundButtonInfo = savedData.selectedBackgroundButtonInfo

            }
            else{
                selectedColorButtonInfo = savedData2.selectedColorButtonInfo
                selectedClothButtonInfo = savedData2.selectedClothButtonInfo
                selectedItemButtonInfo = savedData2.selectedItemButtonInfo
                selectedBackgroundButtonInfo = savedData2.selectedBackgroundButtonInfo
            }
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
        adapter = CustomBottomSheetViewPager(clothFragment,colorFragment,itemFragment,backgroundFragment,this)
        binding.CustomBottomSheetViewPager.adapter = adapter

        fun Int.dpToPx(context: Context): Int {
            return (this * context.resources.displayMetrics.density).toInt()
        }

        val tabTitles = listOf("색깔", "의상", "소품", "배경")

        TabLayoutMediator(customtabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        customtabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val bottomSheetBehavior = BottomSheetBehavior.from(binding.CustomBottomSheet)

                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    val layoutParams = binding.CustomBottomSheet.layoutParams
                    layoutParams.height = 400.dpToPx(requireContext())  // Set the desired height in pixels
                    binding.CustomBottomSheet.layoutParams = layoutParams

                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }

                // Here you can add code specific to handling the selected tab if needed
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                val bottomSheetBehavior = BottomSheetBehavior.from(binding.CustomBottomSheet)

                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    val layoutParams = binding.CustomBottomSheet.layoutParams
                    layoutParams.height = 400.dpToPx(requireContext())  // Set the desired height in pixels
                    binding.CustomBottomSheet.layoutParams = layoutParams

                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }

                // Here you can add code specific to handling the reselected tab if needed
            }
        })


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
        imgCustomBackground_layoutParams.width = 1500 // 원하는 너비(dp 단위)
        imgCustomBackground_layoutParams.height = 1500 // 원하는 높이(dp 단위)
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

                imgCustomBackground_layoutParams.width = (width * (1 - slideOffset) + 1500).toInt()
                imgCustomBackground_layoutParams.height =
                    (height * (1 - slideOffset) + 1500).toInt()
                imgCustomBackground.layoutParams = imgCustomBackground_layoutParams


            }
        })

        binding.btnCustomReset.setOnClickListener {
            val colorbtninfo = ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi)
            selectedColorButtonInfo = colorbtninfo
            val clothbtninfo = ButtonInfo(R.id.btn_cloth_basic, 900, R.drawable.custom_empty)
            selectedClothButtonInfo = clothbtninfo
            val itembtninfo = ButtonInfo(R.id.btn_item_basic, 800, R.drawable.custom_empty)
            selectedItemButtonInfo = itembtninfo
            val backgroundbtninfo = ButtonInfo(R.id.btn_back_basic, 700, R.drawable.custom_empty)
            selectedBackgroundButtonInfo = backgroundbtninfo
            binding.customRamdi.setImageResource(R.drawable.c_ramdi)
            binding.imgCustomCloth.setImageResource(R.drawable.custom_empty)
            binding.imgCustomItem.setImageResource(R.drawable.custom_empty)
            binding.imgCustomBackground.setImageResource(R.drawable.custom_empty)
            val color_resetinfo = true
            val cloth_resetinfo = true
            val item_resetinfo = true
            val background_resetinfo = true
            onResetButtonClicked()
            getcustomReset()
            getCustomPrint()

        }





        binding.btnCustomSave.setOnClickListener {
            custom_save = true
            viewModel.saveButtonInfo(getSelectedButtonInfo())

            var temdata = getSelectedButtonInfo()

            Log.d(
                "Savedata",
                "${temdata.selectedColorButtonInfo?.serverID} ${temdata.selectedClothButtonInfo?.serverID} ${temdata.selectedItemButtonInfo?.serverID} ${temdata.selectedBackgroundButtonInfo?.serverID}"
            )
            printIds.forEachIndexed { index, itemId ->
                Log.d("getCustomPrint", "printIds[$index]: $itemId")
            }
            val itemIds = arrayOf("10", "900", "800", "700")

            val uniqueItemIds = mutableListOf<String>()

            printIds.forEach { idAndItemType ->
                if(idAndItemType.itemType=="color") {
                    itemIds[0]=(idAndItemType.id.toString())
                } else if(idAndItemType.itemType=="set") {
                    itemIds[1]=(idAndItemType.id.toString())
                } else if(idAndItemType.itemType=="item") {
                    itemIds[2]=(idAndItemType.id.toString())
                } else if(idAndItemType.itemType=="background") {
                    itemIds[3]=(idAndItemType.id.toString())
                }
            }

            if(temdata.selectedColorButtonInfo?.serverID == null) {
                uniqueItemIds.add(itemIds[0])
            } else {
                uniqueItemIds.add(temdata.selectedColorButtonInfo?.serverID.toString())
                newviewModel.saveButtonInfo(temdata)
            }
            if(temdata.selectedClothButtonInfo?.serverID == null) {
                if(itemIds[1]!="900")
                    uniqueItemIds.add(itemIds[1])
            } else {
                if(temdata.selectedClothButtonInfo?.serverID.toString()!="900"){
                    uniqueItemIds.add(temdata.selectedClothButtonInfo?.serverID.toString())
                    newviewModel.saveButtonInfo(temdata)
                    newviewModel.saveButtonInfo(temdata)
                }
            }
            if(temdata.selectedItemButtonInfo?.serverID == null) {
                if(itemIds[2]!="800")
                    uniqueItemIds.add(itemIds[2])
            } else {
                if(temdata.selectedItemButtonInfo?.serverID.toString()!="800")
                    uniqueItemIds.add(temdata.selectedItemButtonInfo?.serverID.toString())
                    newviewModel.saveButtonInfo(temdata)
            }
            if(temdata.selectedBackgroundButtonInfo?.serverID == null) {
                if(itemIds[3]!="700")
                    uniqueItemIds.add(itemIds[3])
            } else {
                if(temdata.selectedBackgroundButtonInfo?.serverID.toString()!="700")
                    uniqueItemIds.add(temdata.selectedBackgroundButtonInfo?.serverID.toString())
                    newviewModel.saveButtonInfo(temdata)
            }


// Convert uniqueItemIds set back to a list
            val combinedIds = uniqueItemIds.toList()
            Log.d("combined",combinedIds.toString())

// Now you can use combinedIds as needed
            patchCustomItemChange(combinedIds)

            unsavedChanges = false
            Toast.makeText(this.requireActivity(), "저장되었습니다.", Toast.LENGTH_SHORT).show()
        }

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.notice_home_back, null)
        alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)



        val btnNo = dialogView.findViewById<AppCompatButton>(R.id.btn_home_dialog_back_no)
        val btnYes = dialogView.findViewById<AppCompatButton>(R.id.btn_home_dialog_back_yes)

        btnNo.setOnClickListener {
            alertDialog.dismiss()
            // Handle "No" button click if needed
        }

        btnYes.setOnClickListener {
            alertDialog.dismiss()
            unsavedChanges = false
            findNavController().navigate(curMenuItem)

        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (unsavedChanges) {
                        showBackConfirmationDialog()
                    } else {
                        isEnabled = false // Disable this callback and let the default back button behavior work
                        requireActivity().supportFragmentManager.beginTransaction()
                    }
                }
            })



        return view
    }


    override fun onColorButtonSelected(colorbuttonInfo: ButtonInfo) {
        // 선택한 버튼에 대한 리소스를 이미지뷰에 적용
        custom_save = false
        Log.d("customcolorbtncheck", "${colorbuttonInfo.selectedImageResource}")
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


        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        //이윤소의 생각: 여기서 바텀네비게이션이 제대로 안 만들어진 것 아니냐.

        bottomNavigationView?.setOnItemSelectedListener{ menuItem ->
            curMenuItem = menuItem.itemId
            if (unsavedChanges) {
                showBackConfirmationDialog()
                false
            } else {
                findNavController().navigate(menuItem.itemId)
                true
            }
        }
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                Navigation.findNavController(view).navigate(R.id.action_fragCustom_to_fragHome)
                return@OnKeyListener true
            }
            false
        })

    }

    override fun onResetButtonClicked() {
        Log.d("FragCustom", "onResetButtonClicked()")
        colorFragment?.resetButtonColor()
        clothFragment?.resetButtonCloth()
        itemFragment?.resetButtonItem()
        backgroundFragment?.resetButtonBackground()
    }


    private fun getCustomPrint() {
        val call: Call<customPrintDATA> = service.customPrint(token)
        call.enqueue(object : Callback<customPrintDATA> {
            override fun onResponse(
                call: Call<customPrintDATA>,
                response: Response<customPrintDATA>
            ) {
                val printInfo = response.body()
                val responseCode = response.code()
                val datas = printInfo?.data?.wearingItems

                datas?.forEachIndexed { index, item ->
                    printId = item.id
                    itemType = item.itemType
                    printfilePath = item.filePath
                    Log.d(
                        "getCustomPrint",
                        "Item $index - id: ${item.id} itemType: ${item.itemType} filePath: ${item.filePath}"
                    )
                    Log.d("getCustomPrint", "Response Code: $responseCode")
                }

                datas?.forEachIndexed { index, item ->
                    val idAndItemType = IdAndItemType(item.id, item.itemType)
                    printIds.add(idAndItemType)

                }
                if (datas != null) {
                    for (item in datas) {
                        if (item.itemType == "color") {
                            Picasso.get()
                                .load(item.filePath)
                                .into(binding.customRamdi)
                        } else if (item.itemType == "set") {
                            Picasso.get()
                                .load(item.filePath)
                                .into(binding.imgCustomCloth)
                        } else if (item.itemType == "item") {
                            Picasso.get()
                                .load(item.filePath)
                                .into(binding.imgCustomItem)
                        } else if (item.itemType == "background") {
                            Picasso.get()
                                .load(item.filePath)
                                .into(binding.imgCustomBackground)
                        }
                    }
                }


            }

            override fun onFailure(call: Call<customPrintDATA>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }


    fun patchCustomItemChange(itemIds: List<String>) {
        val call: Call<Void> = service.customItemChange(token, itemIds)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val responseCode = response.code()
                if (response.isSuccessful) {
                    Log.d("patchCustomItemChange", "Response Code: $responseCode")
                }
                else{
                    Log.d("patchCustomItemChangeFail", "Response Code: $responseCode")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("patchCustomItemChange", t.message.toString())
            }
        })
    }

    fun getcustomReset() {
        val call: Call<Void> = service.customReset(token)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val responseCode = response.code()
                Log.d("putcustomReset", "Response Code: $responseCode")
                findNavController().navigate(R.id.action_fragCustom_to_fragCustom)
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

    private fun navigateToSelectedFragment(itemId: Int) {
        if (unsavedChanges) {
            showBackConfirmationDialog()
        } else {
            val navController = findNavController()
            when (itemId) {
                R.id.fragHome -> {
                    /*FragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_con, fragHome)
                        .commit()*/
                    navController.navigate(R.id.fragHome)
                }
                R.id.fragCalendar -> navController.navigate(R.id.fragCalendar)
                R.id.fragMy -> navController.navigate(R.id.fragMy)
            }
        }
    }

    private fun showBackConfirmationDialog() {
        alertDialog.show()

        //사이즈 조절
        val displayMetrics = DisplayMetrics()
        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getSize(size)
        val screenWidth = size.x
        val popupWidth = (screenWidth * 0.8).toInt()
        alertDialog?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)
    }




}