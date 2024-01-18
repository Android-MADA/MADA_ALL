package com.mada.myapplication.Fragment

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
import com.mada.myapplication.CustomFunction.ButtonInfo
import com.mada.myapplication.CustomFunction.CustomViewModel
import com.mada.myapplication.CustomFunction.RetrofitServiceCustom
import com.mada.myapplication.R
import com.mada.myapplication.custom_background
import com.mada.myapplication.custom_cloth
import com.mada.myapplication.custom_color
import com.mada.myapplication.custom_item
import com.mada.myapplication.databinding.FragCustomBinding
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mada.myapplication.CustomFunction.DataRepo
import com.mada.myapplication.StartFuction.Splash2Activity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mada.myapplication.BuildConfig
import com.mada.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
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
    private var selectedColorButtonInfo: ButtonInfo? = null
    private var selectedClothButtonInfo: ButtonInfo? = null
    private var selectedItemButtonInfo: ButtonInfo? = null
    private var selectedBackgroundButtonInfo: ButtonInfo? = null
    private var custom_save = false
    private val viewModel: CustomViewModel by viewModels()


    private var colorFragment: custom_color? = null
    private var clothFragment: custom_cloth? = null
    private var itemFragment: custom_item? = null
    private var backgroundFragment: custom_background? = null

    private val printIds = mutableListOf<IdAndItemType>()
    private var curMenuItem : Int = 0

    private var unsavedChanges = false

    private lateinit var alertDialog: AlertDialog


    val retrofit = Retrofit.Builder().baseUrl(BuildConfig.MADA_BASE)
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




        //getCustomPrint()
        //postcustomItemBuy(14)
        val savedData = viewModel.getSavedButtonInfo()

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


        val colorbuttonInfo = when (DataRepo.buttonInfoEntity?.colorButtonInfo?.serverID) {
            10 -> ButtonInfo(R.id.btn_back_basic, 10, R.drawable.c_ramdi)
            11 -> ButtonInfo(R.id.btn_color_blue, 11, R.drawable.c_ramdyb)
            17 -> ButtonInfo(R.id.btn_color_Rblue, 17, R.drawable.c_ramdyrb)
            12 -> ButtonInfo(R.id.btn_color_bluepurple, 12, R.drawable.c_ramdybp)
            13 -> ButtonInfo(R.id.btn_color_green, 13, R.drawable.c_ramdyg)
            14 -> ButtonInfo(R.id.btn_color_orange, 14, R.drawable.c_ramdyo)
            16 -> ButtonInfo(R.id.btn_color_pink, 16, R.drawable.c_ramdypn)
            15 -> ButtonInfo(R.id.btn_color_purple, 15, R.drawable.c_ramdyp)
            18 -> ButtonInfo(R.id.btn_color_yellow, 18, R.drawable.c_ramdyy)
            else -> savedData?.selectedColorButtonInfo
        }

        val clothbuttonInfo = when (DataRepo.buttonInfoEntity?.clothButtonInfo?.serverID) {
            900 -> ButtonInfo(R.id.btn_cloth_basic, 900, R.drawable.custom_empty)
            41 -> ButtonInfo(R.id.btn_cloth_dev, 41, R.drawable.set_dev)
            44 -> ButtonInfo(R.id.btn_cloth_movie, 44, R.drawable.set_movie)
            40 -> ButtonInfo(R.id.btn_cloth_caffK, 40, R.drawable.set_caffk)
            46 -> ButtonInfo(R.id.btn_cloth_v, 46, R.drawable.set_v)
            39 -> ButtonInfo(R.id.btn_cloth_astronauts, 39, R.drawable.set_astronauts,)
            47 -> ButtonInfo(R.id.btn_cloth_zzim, 47, R.drawable.set_zzim)
            42 -> ButtonInfo(R.id.btn_cloth_hanbokF, 42, R.drawable.set_hanbokf)
            43 -> ButtonInfo(R.id.btn_cloth_hanbokM, 43, R.drawable.set_hanbokm)
            45 -> ButtonInfo(R.id.btn_cloth_snowman, 45, R.drawable.set_snowman)
            else -> savedData?.selectedClothButtonInfo
        }

        val itembuttonInfo = when (DataRepo.buttonInfoEntity?.itemButtonInfo?.serverID) {
            800 -> ButtonInfo(R.id.btn_item_basic, 800, R.drawable.custom_empty)
            22 -> ButtonInfo(R.id.btn_item_glass_normal, 22,R.drawable.g_nomal)
            30 -> ButtonInfo(R.id.btn_item_hat_ber, 30, R.drawable.hat_ber)
            33 -> ButtonInfo(R.id.btn_item_hat_grad, 33, R.drawable.hat_grad)
            21 -> ButtonInfo(R.id.btn_item_glass_8bit, 21,R.drawable.g_8bit)
            25 -> ButtonInfo(R.id.btn_item_glass_woig, 25, R.drawable.g_woig)
            35 -> ButtonInfo(R.id.btn_item_hat_ipod , 35, R.drawable.hat_ipod)
            24 -> ButtonInfo(R.id.btn_item_glass_sunR , 24,R.drawable.g_sunr)
            23 -> ButtonInfo(R.id.btn_item_glass_sunB,23, R.drawable.g_sunb)
            32 -> ButtonInfo(R.id.btn_item_hat_flower, 32, R.drawable.hat_flower)
            37 -> ButtonInfo(R.id.btn_item_hat_v, 37, R.drawable.hat_v)
            31 -> ButtonInfo(R.id.btn_item_hat_dinof, 31,R.drawable.hat_dinof)
            36 -> ButtonInfo(R.id.btn_item_hat_sheep, 36, R.drawable.hat_sheep)
            19 -> ButtonInfo(R.id.btn_item_bag_e,19, R.drawable.bag_e)
            20 -> ButtonInfo(R.id.btn_item_bag_luck,20, R.drawable.bag_luck)
            34 -> ButtonInfo(R.id.btn_item_hat_heart,34, R.drawable.hat_heart)
            29 -> ButtonInfo(R.id.btn_item_hat_bee, 29, R.drawable.hat_bee)
            38 -> ButtonInfo(R.id.btn_item_hat_heads, 38, R.drawable.heads)
            else -> savedData?.selectedItemButtonInfo
        }

        val backgroundbuttonInfo = when (DataRepo.buttonInfoEntity?.backgroundButtonInfo?.serverID) {
            700 -> ButtonInfo(R.id.btn_back_basic, 700, R.drawable.custom_empty)
            1 -> ButtonInfo(R.id.btn_back_brid_s, 1, R.drawable.back_brid)
            3 -> ButtonInfo(R.id.btn_back_n_s, 3, R.drawable.back_n)
            8 -> ButtonInfo(R.id.btn_back_win_s, 8, R.drawable.back_win)
            4 -> ButtonInfo(R.id.btn_back_normal_s, 4, R.drawable.back_nomal)
            5 -> ButtonInfo(R.id.btn_back_store_s, 5, R.drawable.back_store)
            9 -> ButtonInfo(R.id.btn_back_zzim_s, 9, R.drawable.back_zzim)
            7 -> ButtonInfo(R.id.btn_back_uni_s, 7, R.drawable.back_uni)
            2 -> ButtonInfo(R.id.btn_back_cin_s, 2, R.drawable.back_cin)
            6 -> ButtonInfo(R.id.btn_back_sum_s, 6, R.drawable.back_sum)
            else -> savedData?.selectedBackgroundButtonInfo
        }

        binding.customRamdi.setImageResource(
            colorbuttonInfo?.selectedImageResource ?: 0
        )

        binding.imgCustomCloth.setImageResource(
            clothbuttonInfo?.selectedImageResource ?: 0
        )
        binding.imgCustomItem.setImageResource(
            itembuttonInfo?.selectedImageResource ?: 0
        )
        binding.imgCustomBackground.setImageResource(
            backgroundbuttonInfo?.selectedImageResource ?: 0
        )





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

        fun Int.dpToPx(context: Context): Int {
            return (this * context.resources.displayMetrics.density).toInt()
        }




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
            onResetButtonClicked()
            getcustomReset()

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
            var serverpatchIds = mutableListOf("10", "900", "800", "700")

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
                serverpatchIds[0] = DataRepo.buttonInfoEntity?.colorButtonInfo?.serverID.toString()
            } else {
                uniqueItemIds.add(temdata.selectedColorButtonInfo?.serverID.toString())
                serverpatchIds[0] = temdata.selectedColorButtonInfo?.serverID.toString()
            }
            if(temdata.selectedClothButtonInfo?.serverID == null) {
                if(itemIds[1]!="900") {
                    uniqueItemIds.add(itemIds[1])
                    serverpatchIds[1] = DataRepo.buttonInfoEntity?.colorButtonInfo?.serverID.toString()
                }
            } else {
                if(temdata.selectedClothButtonInfo?.serverID.toString()!="900"){
                    uniqueItemIds.add(temdata.selectedClothButtonInfo?.serverID.toString())
                    serverpatchIds[1] = temdata.selectedClothButtonInfo?.serverID.toString()
                }
            }
            if(temdata.selectedItemButtonInfo?.serverID == null) {
                if(itemIds[2]!="800") {
                    uniqueItemIds.add(itemIds[2])
                    serverpatchIds[2] = DataRepo.buttonInfoEntity?.colorButtonInfo?.serverID.toString()
                }
            } else {
                if(temdata.selectedItemButtonInfo?.serverID.toString()!="800"){
                    uniqueItemIds.add(temdata.selectedItemButtonInfo?.serverID.toString())
                    serverpatchIds[2] = temdata.selectedItemButtonInfo?.serverID.toString()
                }
            }
            if(temdata.selectedBackgroundButtonInfo?.serverID == null) {
                if(itemIds[3]!="700") {
                    uniqueItemIds.add(itemIds[3])
                    serverpatchIds[3] = DataRepo.buttonInfoEntity?.colorButtonInfo?.serverID.toString()
                }
            } else {
                if(temdata.selectedBackgroundButtonInfo?.serverID.toString()!="700") {
                    uniqueItemIds.add(temdata.selectedBackgroundButtonInfo?.serverID.toString())
                    serverpatchIds[3] = temdata.selectedBackgroundButtonInfo?.serverID.toString()
                }
            }


// Convert uniqueItemIds set back to a list
            val combinedIds = uniqueItemIds.toList()
            Log.d("combined",combinedIds.toString())
            val serverpatchList = serverpatchIds.toList()

// Now you can use combinedIds as needed
            patchCustomItemChange(combinedIds)




            var colorbuttonInfo = when (serverpatchList[0]) {
                "10" -> ButtonInfo(R.id.btn_back_basic, 10, R.drawable.c_ramdi)
                "11" -> ButtonInfo(R.id.btn_color_blue, 11, R.drawable.c_ramdyb)
                "17" -> ButtonInfo(R.id.btn_color_Rblue, 17, R.drawable.c_ramdyrb)
                "12" -> ButtonInfo(R.id.btn_color_bluepurple, 12, R.drawable.c_ramdybp)
                "13" -> ButtonInfo(R.id.btn_color_green, 13, R.drawable.c_ramdyg)
                "14" -> ButtonInfo(R.id.btn_color_orange, 14, R.drawable.c_ramdyo)
                "16" -> ButtonInfo(R.id.btn_color_pink, 16, R.drawable.c_ramdypn)
                "15" -> ButtonInfo(R.id.btn_color_purple, 15, R.drawable.c_ramdyp)
                "18" -> ButtonInfo(R.id.btn_color_yellow, 18, R.drawable.c_ramdyy)
                else -> savedData?.selectedColorButtonInfo
            }

            var clothbuttonInfo = when (serverpatchList[1]) {
                "900" -> ButtonInfo(R.id.btn_cloth_basic, 900, R.drawable.custom_empty)
                "41" -> ButtonInfo(R.id.btn_cloth_dev, 41, R.drawable.set_dev)
                "44" -> ButtonInfo(R.id.btn_cloth_movie, 44, R.drawable.set_movie)
                "40" -> ButtonInfo(R.id.btn_cloth_caffK, 40, R.drawable.set_caffk)
                "46" -> ButtonInfo(R.id.btn_cloth_v, 46, R.drawable.set_v)
                "39" -> ButtonInfo(R.id.btn_cloth_astronauts, 39, R.drawable.set_astronauts,)
                "47" -> ButtonInfo(R.id.btn_cloth_zzim, 47, R.drawable.set_zzim)
                "42" -> ButtonInfo(R.id.btn_cloth_hanbokF, 42, R.drawable.set_hanbokf)
                "43" -> ButtonInfo(R.id.btn_cloth_hanbokM, 43, R.drawable.set_hanbokm)
                "45" -> ButtonInfo(R.id.btn_cloth_snowman, 45, R.drawable.set_snowman)
                else -> savedData?.selectedClothButtonInfo
            }

            var itembuttonInfo = when (serverpatchList[2]) {
                "800" -> ButtonInfo(R.id.btn_item_basic, 800, R.drawable.custom_empty)
                "22" -> ButtonInfo(R.id.btn_item_glass_normal, 22,R.drawable.g_nomal)
                "30" -> ButtonInfo(R.id.btn_item_hat_ber, 30, R.drawable.hat_ber)
                "33" -> ButtonInfo(R.id.btn_item_hat_grad, 33, R.drawable.hat_grad)
                "21" -> ButtonInfo(R.id.btn_item_glass_8bit, 21,R.drawable.g_8bit)
                "25" -> ButtonInfo(R.id.btn_item_glass_woig, 25, R.drawable.g_woig)
                "35" -> ButtonInfo(R.id.btn_item_hat_ipod , 35, R.drawable.hat_ipod)
                "24" -> ButtonInfo(R.id.btn_item_glass_sunR , 24,R.drawable.g_sunr)
                "23" -> ButtonInfo(R.id.btn_item_glass_sunB,23, R.drawable.g_sunb)
                "32" -> ButtonInfo(R.id.btn_item_hat_flower, 32, R.drawable.hat_flower)
                "37" -> ButtonInfo(R.id.btn_item_hat_v, 37, R.drawable.hat_v)
                "31" -> ButtonInfo(R.id.btn_item_hat_dinof, 31,R.drawable.hat_dinof)
                "36" -> ButtonInfo(R.id.btn_item_hat_sheep, 36, R.drawable.hat_sheep)
                "19" -> ButtonInfo(R.id.btn_item_bag_e,19, R.drawable.bag_e)
                "20" -> ButtonInfo(R.id.btn_item_bag_luck,20, R.drawable.bag_luck)
                "34" -> ButtonInfo(R.id.btn_item_hat_heart,34, R.drawable.hat_heart)
                "29" -> ButtonInfo(R.id.btn_item_hat_bee, 29, R.drawable.hat_bee)
                "38" -> ButtonInfo(R.id.btn_item_hat_heads, 38, R.drawable.heads)
                else -> savedData?.selectedItemButtonInfo
            }

            var backgroundbuttonInfo = when (serverpatchList[3]) {
                "700" -> ButtonInfo(R.id.btn_back_basic, 700, R.drawable.custom_empty)
                "1" -> ButtonInfo(R.id.btn_back_brid_s, 1, R.drawable.back_brid)
                "3" -> ButtonInfo(R.id.btn_back_n_s, 3, R.drawable.back_n)
                "8" -> ButtonInfo(R.id.btn_back_win_s, 8, R.drawable.back_win)
                "4" -> ButtonInfo(R.id.btn_back_normal_s, 4, R.drawable.back_nomal)
                "5" -> ButtonInfo(R.id.btn_back_store_s, 5, R.drawable.back_store)
                "9" -> ButtonInfo(R.id.btn_back_zzim_s, 9, R.drawable.back_zzim)
                "7" -> ButtonInfo(R.id.btn_back_uni_s, 7, R.drawable.back_uni)
                "2" -> ButtonInfo(R.id.btn_back_cin_s, 2, R.drawable.back_cin)
                "6" -> ButtonInfo(R.id.btn_back_sum_s, 6, R.drawable.back_sum)
                else -> savedData?.selectedBackgroundButtonInfo
            }


            binding.customRamdi.setImageResource(
                colorbuttonInfo?.selectedImageResource ?: 0
            )
            binding.imgCustomCloth.setImageResource(
                clothbuttonInfo?.selectedImageResource ?: 0
            )
            binding.imgCustomItem.setImageResource(
                itembuttonInfo?.selectedImageResource ?: 0
            )
            binding.imgCustomBackground.setImageResource(
                backgroundbuttonInfo?.selectedImageResource ?: 0
            )


            DataRepo.buttonInfoEntity?.colorButtonInfo = colorbuttonInfo
            DataRepo.buttonInfoEntity?.clothButtonInfo = clothbuttonInfo
            DataRepo.buttonInfoEntity?.itemButtonInfo = itembuttonInfo
            DataRepo.buttonInfoEntity?.backgroundButtonInfo = backgroundbuttonInfo

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


    /*private fun getCustomPrint() {
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
    }*/


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