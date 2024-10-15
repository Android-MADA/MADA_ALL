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
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.children
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mada.myapplication.CustomFunction.DataRepo
import com.mada.myapplication.StartFunction.Splash2Activity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mada.myapplication.BuildConfig
import com.mada.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.mada.myapplication.CustomBottomSheetViewPager
import com.mada.myapplication.CustomFunction.ButtonInfoEntity
import com.mada.myapplication.CustomFunction.customItemChangeDATA
import com.mada.myapplication.CustomFunction.customItemCheckDATA
import com.mada.myapplication.CustomFunction.customPrintDATA
import com.mada.myapplication.databinding.CustomBackgroundBinding
import com.mada.myapplication.databinding.CustomClothBinding
import com.mada.myapplication.databinding.CustomColorBinding
import com.mada.myapplication.databinding.CustomItemBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface OnColorImageChangeListener {
    fun onColorButtonSelected(buttonInfo: ButtonInfo)
}

interface OnClothImageChangeListener {
    fun onClothButtonSelected(buttonInfo: ButtonInfo)
}

interface OnItemImageChangeListener {
    fun onItemButtonSelected(buttonInfo: ButtonInfo)
}

interface OnBackgroundImageChangeListener {
    fun onBackgroundButtonSelected(buttonInfo: ButtonInfo)
}

data class IdAndItemType(
    val id: Int,
    val itemType: String
)


//카테고리매핑
val serverIdToCategoryMap = mapOf(
    R.id.btn_item_bag_e to "goods",
    R.id.btn_item_bag_luck to "goods",
    R.id.btn_item_glass_8bit to "glasses",
    R.id.btn_item_glass_normal to "glasses",
    R.id.btn_item_glass_sunB to "glasses",
    R.id.btn_item_glass_sunR to "glasses",
    R.id.btn_item_glass_woig to "glasses",
    R.id.btn_item_hat_bee to "hat,earring",
    R.id.btn_item_hat_ber to "hat,earring",
    R.id.btn_item_hat_dinof  to "hat",
    R.id.btn_item_hat_flower to "hat,earring",
    R.id.btn_item_hat_grad to "hat,earring",
    R.id.btn_item_hat_heart to "hat,earring",
    R.id.btn_item_hat_ipod to "hat,earring",
    R.id.btn_item_hat_sheep to "hat,earring",
    R.id.btn_item_hat_v to "hat",
    R.id.btn_item_hat_heads to "earring",
    R.id.btn_cloth_astronauts to "hat,earring,glasses,clothes",
    R.id.btn_cloth_caffK to "hat,clothes",
    R.id.btn_cloth_dev to "hat,earring,clothes,goods",
    R.id.btn_cloth_hanbokF to "hat,clothes",
    R.id.btn_cloth_hanbokM to "hat,clothes",
    R.id.btn_cloth_movie to "hat,earring,clothes,goods",
    R.id.btn_cloth_snowman to "hat,earring,glasses,clothes",
    R.id.btn_cloth_v to "hat,clothes",
    R.id.btn_cloth_zzim to "hat,earring,clothes,goods"
)

val itemsCategory = ArrayList<String>(100) //카테고리 중복 확인 변수


class FragCustom : Fragment(), OnColorImageChangeListener, OnClothImageChangeListener,
    OnItemImageChangeListener, OnBackgroundImageChangeListener {
    lateinit var binding: FragCustomBinding
    private var selectedColorButtonInfo: ButtonInfo? = null
    private var selectedClothButtonInfo: ButtonInfo? = null
    private var selectedItemButtonInfo: ButtonInfo? = null
    private var selectedBackgroundButtonInfo: ButtonInfo? = null
    private val viewModel: CustomViewModel by activityViewModels()
    private lateinit var viewPager: ViewPager2
    lateinit var savedData: selectedButtonInfo



    private var adapter: CustomBottomSheetViewPager? = null

    private val printIds = mutableListOf<IdAndItemType>()
    private var curMenuItem : Int = 0

    private var unsavedChanges = false

    private lateinit var alertDialog: AlertDialog


    // HttpLoggingInterceptor 설정
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient에 Interceptor 추가
    val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()


    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.MADA_BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)  // HttpClient를 추가해야 로그 인터셉터가 동작
        .build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)

    val token = Splash2Activity.prefs.getString("token", "")



    data class selectedButtonInfo(
        var selectedColorButtonInfo: ButtonInfo?,
        var selectedClothButtonInfo: ButtonInfo?,
        var selectedItemButtonInfo: ButtonInfo?,
        var selectedBackgroundButtonInfo: ButtonInfo?
    )

    // sharedPreferences 데이터 저장하기
    fun saveSelectedButtonInfo(selectedInfo: selectedButtonInfo) {
        val sharedPreferences = requireActivity().getSharedPreferences("selected_info", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // 기존 SharedPreferences 값을 불러옴
        val currentInfo = loadSelectedButtonInfo()

        // 변경된 값만 저장
        editor.putInt("color_button_id", selectedInfo.selectedColorButtonInfo?.serverID ?: currentInfo.selectedColorButtonInfo?.serverID ?: 10)
        editor.putInt("cloth_button_id", selectedInfo.selectedClothButtonInfo?.serverID ?: currentInfo.selectedClothButtonInfo?.serverID ?: 49)
        editor.putInt("item_button_id", selectedInfo.selectedItemButtonInfo?.serverID ?: currentInfo.selectedItemButtonInfo?.serverID ?: 50)
        editor.putInt("background_button_id", selectedInfo.selectedBackgroundButtonInfo?.serverID ?: currentInfo.selectedBackgroundButtonInfo?.serverID ?: 48)

        editor.apply()  // SharedPreferences에 변경사항 적용
    }

    // sharedPreferences 데이터 불러오기
    fun loadSelectedButtonInfo(): selectedButtonInfo {
        val sharedPreferences = requireActivity().getSharedPreferences("selected_info", Context.MODE_PRIVATE)

        val colorButtonId = sharedPreferences.getInt("color_button_id", 10)
        val clothButtonId = sharedPreferences.getInt("cloth_button_id", 49)
        val itemButtonId = sharedPreferences.getInt("item_button_id", 50)
        val backgroundButtonId = sharedPreferences.getInt("background_button_id", 48)

        val colorButtonInfo = when (colorButtonId) {
            10 -> ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi)
            11 -> ButtonInfo(R.id.btn_color_bluepurple, 11, R.drawable.c_ramdybp)
            12 -> ButtonInfo(R.id.btn_color_green, 12, R.drawable.c_ramdyg)
            13 -> ButtonInfo(R.id.btn_color_mint, 13, R.drawable.c_ramdymint)
            14 -> ButtonInfo(R.id.btn_color_orange, 14, R.drawable.c_ramdyo)
            15 -> ButtonInfo(R.id.btn_color_orange2, 15, R.drawable.c_ramdyoy)
            16 -> ButtonInfo(R.id.btn_color_pink, 16, R.drawable.c_ramdypn)
            17 -> ButtonInfo(R.id.btn_color_pink2, 17, R.drawable.c_ramdypink)
            18 -> ButtonInfo(R.id.btn_color_pink3, 18, R.drawable.c_ramdydp)
            26 -> ButtonInfo(R.id.btn_color_purple, 26, R.drawable.c_ramdyp)
            27 -> ButtonInfo(R.id.btn_color_Rblue, 27, R.drawable.c_ramdyrb)
            28 -> ButtonInfo(R.id.btn_color_yellow, 28, R.drawable.c_ramdyy)
            51 -> ButtonInfo(R.id.btn_color_yellow2, 51, R.drawable.c_ramdyyellow)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        val clothButtonInfo = when (clothButtonId) {
            49 -> ButtonInfo(0, 49, R.drawable.custom_empty)
            41 -> ButtonInfo(R.id.btn_cloth_dev, 41, R.drawable.set_dev)
            44 -> ButtonInfo(R.id.btn_cloth_movie, 44, R.drawable.set_movie)
            40 -> ButtonInfo(R.id.btn_cloth_caffK, 40, R.drawable.set_caffk)
            46 -> ButtonInfo(R.id.btn_cloth_v, 46, R.drawable.set_v)
            39 -> ButtonInfo(R.id.btn_cloth_astronauts, 39, R.drawable.set_astronauts,)
            47 -> ButtonInfo(R.id.btn_cloth_zzim, 47, R.drawable.set_zzim)
            42 -> ButtonInfo(R.id.btn_cloth_hanbokF, 42, R.drawable.set_hanbokf)
            43 -> ButtonInfo(R.id.btn_cloth_hanbokM, 43, R.drawable.set_hanbokm)
            45 -> ButtonInfo(R.id.btn_cloth_snowman, 45, R.drawable.set_snowman)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        val itemButtonInfo = when (itemButtonId) {
            50 -> ButtonInfo(0, 50, R.drawable.custom_empty)
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
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        val backgroundButtonInfo = when (backgroundButtonId) {
            48 -> ButtonInfo(0, 48, R.drawable.custom_empty)
            1 -> ButtonInfo(R.id.btn_back_brid_s, 1, R.drawable.back_brid)
            3 -> ButtonInfo(R.id.btn_back_n_s, 3, R.drawable.back_n)
            8 -> ButtonInfo(R.id.btn_back_win_s, 8, R.drawable.back_win)
            4 -> ButtonInfo(R.id.btn_back_normal_s, 4, R.drawable.back_nomal)
            5 -> ButtonInfo(R.id.btn_back_store_s, 5, R.drawable.back_store)
            9 -> ButtonInfo(R.id.btn_back_zzim_s, 9, R.drawable.back_zzim)
            7 -> ButtonInfo(R.id.btn_back_uni_s, 7, R.drawable.back_uni)
            2 -> ButtonInfo(R.id.btn_back_cin_s, 2, R.drawable.back_cin)
            6 -> ButtonInfo(R.id.btn_back_sum_s, 6, R.drawable.back_sum)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        return selectedButtonInfo(colorButtonInfo, clothButtonInfo, itemButtonInfo, backgroundButtonInfo)
    }







    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragCustomBinding.inflate(inflater, container, false)
        var view = binding.root
        var customtabLayout = binding.CustomPagetabLayout

        //binding.CustomBottomSheetViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL



        //바텀시트 바인딩
        val colorBinding = CustomColorBinding.inflate(inflater, container, false)
        val clothBinding = CustomClothBinding.inflate(inflater, container, false)
        val itemBinding = CustomItemBinding.inflate(inflater, container, false)
        val backgroundBinding = CustomBackgroundBinding.inflate(inflater, container, false)

        val colorFragment = custom_color(colorBinding)
        val clothFragment = custom_cloth(clothBinding)
        val itemFragment = custom_item(itemBinding)
        val backgroundFragment = custom_background(backgroundBinding)

        binding.CustomBottomSheetViewPager.adapter = CustomBottomSheetViewPager(clothFragment,colorFragment,itemFragment,backgroundFragment,this)
        viewPager = binding.CustomBottomSheetViewPager
        viewPager.setUserInputEnabled(false);




        adapter = CustomBottomSheetViewPager(clothFragment,colorFragment,itemFragment,backgroundFragment,this)
        binding.CustomBottomSheetViewPager.adapter = adapter

        val tabTitles = listOf("색깔", "의상", "소품", "배경")


        //getCustomPrint()
        val savedData = viewModel.getSavedButtonInfo()




        // DataRepo에서 가져온 정보를 바탕으로 ButtonInfo 객체를 만듭니다.
        val colorbuttonInfo = when (DataRepo.buttonInfoEntity?.colorButtonInfo?.serverID) {
            10 -> ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi)
            11 -> ButtonInfo(R.id.btn_color_bluepurple, 11, R.drawable.c_ramdybp)
            12 -> ButtonInfo(R.id.btn_color_green, 12, R.drawable.c_ramdyg)
            13 -> ButtonInfo(R.id.btn_color_mint, 13, R.drawable.c_ramdymint)
            14 -> ButtonInfo(R.id.btn_color_orange, 14, R.drawable.c_ramdyo)
            15 -> ButtonInfo(R.id.btn_color_orange2, 15, R.drawable.c_ramdyoy)
            16 -> ButtonInfo(R.id.btn_color_pink, 16, R.drawable.c_ramdypn)
            17 -> ButtonInfo(R.id.btn_color_pink2, 17, R.drawable.c_ramdypink)
            18 -> ButtonInfo(R.id.btn_color_pink3, 18, R.drawable.c_ramdydp)
            26 -> ButtonInfo(R.id.btn_color_purple, 26, R.drawable.c_ramdyp)
            27 -> ButtonInfo(R.id.btn_color_Rblue, 27, R.drawable.c_ramdyrb)
            28 -> ButtonInfo(R.id.btn_color_yellow, 28, R.drawable.c_ramdyy)
            51 -> ButtonInfo(R.id.btn_color_yellow2, 51, R.drawable.c_ramdyyellow)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        val clothbuttonInfo = when (DataRepo.buttonInfoEntity?.clothButtonInfo?.serverID) {
            49 -> ButtonInfo(0, 49, R.drawable.custom_empty)
            41 -> ButtonInfo(R.id.btn_cloth_dev, 41, R.drawable.set_dev)
            44 -> ButtonInfo(R.id.btn_cloth_movie, 44, R.drawable.set_movie)
            40 -> ButtonInfo(R.id.btn_cloth_caffK, 40, R.drawable.set_caffk)
            46 -> ButtonInfo(R.id.btn_cloth_v, 46, R.drawable.set_v)
            39 -> ButtonInfo(R.id.btn_cloth_astronauts, 39, R.drawable.set_astronauts,)
            47 -> ButtonInfo(R.id.btn_cloth_zzim, 47, R.drawable.set_zzim)
            42 -> ButtonInfo(R.id.btn_cloth_hanbokF, 42, R.drawable.set_hanbokf)
            43 -> ButtonInfo(R.id.btn_cloth_hanbokM, 43, R.drawable.set_hanbokm)
            45 -> ButtonInfo(R.id.btn_cloth_snowman, 45, R.drawable.set_snowman)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        val itembuttonInfo = when (DataRepo.buttonInfoEntity?.itemButtonInfo?.serverID) {
            50 -> ButtonInfo(0, 50, R.drawable.custom_empty)
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
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        val backgroundbuttonInfo = when (DataRepo.buttonInfoEntity?.backgroundButtonInfo?.serverID) {
            48 -> ButtonInfo(0, 48, R.drawable.custom_empty)
            1 -> ButtonInfo(R.id.btn_back_brid_s, 1, R.drawable.back_brid)
            3 -> ButtonInfo(R.id.btn_back_n_s, 3, R.drawable.back_n)
            8 -> ButtonInfo(R.id.btn_back_win_s, 8, R.drawable.back_win)
            4 -> ButtonInfo(R.id.btn_back_normal_s, 4, R.drawable.back_nomal)
            5 -> ButtonInfo(R.id.btn_back_store_s, 5, R.drawable.back_store)
            9 -> ButtonInfo(R.id.btn_back_zzim_s, 9, R.drawable.back_zzim)
            7 -> ButtonInfo(R.id.btn_back_uni_s, 7, R.drawable.back_uni)
            2 -> ButtonInfo(R.id.btn_back_cin_s, 2, R.drawable.back_cin)
            6 -> ButtonInfo(R.id.btn_back_sum_s, 6, R.drawable.back_sum)
            else -> throw IllegalArgumentException("Unknown button ID")
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

        TabLayoutMediator(customtabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        val fragmentManager: FragmentManager = childFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.commit()


        //초기화 버튼 클릭 리스너
        binding.btnCustomReset.setOnClickListener {
            itemsCategory.clear() //카테고리 선택 초기화
            if (::binding.isInitialized) {
                val colorbtninfo = ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi)
                selectedColorButtonInfo = colorbtninfo
                val clothbtninfo = ButtonInfo(0, 49, R.drawable.custom_empty)
                selectedClothButtonInfo = clothbtninfo
                val itembtninfo = ButtonInfo(0, 50, R.drawable.custom_empty)
                selectedItemButtonInfo = itembtninfo
                val backgroundbtninfo = ButtonInfo(0, 48, R.drawable.custom_empty)
                selectedBackgroundButtonInfo = backgroundbtninfo
                binding.customRamdi.setImageResource(R.drawable.c_ramdi)
                binding.imgCustomCloth.setImageResource(R.drawable.custom_empty)
                binding.imgCustomItem.setImageResource(R.drawable.custom_empty)
                binding.imgCustomBackground.setImageResource(R.drawable.custom_empty)

                //아이템 선택 버튼 초기화
                if (colorBinding != null && clothBinding != null && backgroundBinding != null && itemBinding != null) {
                    //item 초기화
                    itemBinding.btnItemGlassNormal?.setImageResource(R.drawable.gh_normal_s)
                    itemBinding.btnItemHatBer?.setImageResource(R.drawable.hat_ber_s)
                    itemBinding.btnItemHatGrad?.setImageResource(R.drawable.hat_grad_s)
                    itemBinding.btnItemGlass8bit?.setImageResource(R.drawable.g_8bit_s)
                    itemBinding.btnItemGlassWoig?.setImageResource(R.drawable.g_woig_s)
                    itemBinding.btnItemHatIpod?.setImageResource(R.drawable.hat_ipod_s)
                    itemBinding.btnItemGlassSunR?.setImageResource(R.drawable.g_sunr_s)
                    itemBinding.btnItemGlassSunB?.setImageResource(R.drawable.g_sunb_s)
                    itemBinding.btnItemHatFlower?.setImageResource(R.drawable.hat_flower_s)
                    itemBinding.btnItemHatV?.setImageResource(R.drawable.hat_v_s)
                    itemBinding.btnItemHatDinof?.setImageResource(R.drawable.hat_dinof_s)
                    itemBinding.btnItemHatSheep?.setImageResource(R.drawable.hat_sheep_s)
                    itemBinding.btnItemBagE?.setImageResource(R.drawable.bag_e_s)
                    itemBinding.btnItemBagLuck?.setImageResource(R.drawable.bag_luck_s)
                    itemBinding.btnItemHatHeart?.setImageResource(R.drawable.hat_heart_s)
                    itemBinding.btnItemHatBee?.setImageResource(R.drawable.hat_bee_s)
                    itemBinding.btnItemHatHeads?.setImageResource(R.drawable.heads_s)
                    //cloth 초기화
                    clothBinding.btnClothDev?.setImageResource(R.drawable.set_dev_s)
                    clothBinding.btnClothMovie?.setImageResource(R.drawable.set_movie_s)
                    clothBinding.btnClothCaffK?.setImageResource(R.drawable.set_caffk_s)
                    clothBinding.btnClothV?.setImageResource(R.drawable.set_v_s)
                    clothBinding.btnClothAstronauts?.setImageResource(R.drawable.set_astronauts_s)
                    clothBinding.btnClothZzim?.setImageResource(R.drawable.set_zzim_s)
                    clothBinding.btnClothHanbokF?.setImageResource(R.drawable.set_hanbokf_s)
                    clothBinding.btnClothHanbokM?.setImageResource(R.drawable.set_hanbokm_s)
                    clothBinding.btnClothSnowman?.setImageResource(R.drawable.set_snowman_s)
                    //background 초기화
                    backgroundBinding.btnBackBridS?.setImageResource(R.drawable.back_bird_s_1)
                    backgroundBinding.btnBackNS?.setImageResource(R.drawable.back_n_s_1)
                    backgroundBinding.btnBackWinS?.setImageResource(R.drawable.back_win_s_1)
                    backgroundBinding.btnBackNormalS?.setImageResource(R.drawable.back_normal_s_1)
                    backgroundBinding.btnBackStoreS?.setImageResource(R.drawable.back_store_s_1)
                    backgroundBinding.btnBackZzimS?.setImageResource(R.drawable.back_zzim_s_1)
                    backgroundBinding.btnBackUniS?.setImageResource(R.drawable.back_sp_s_1)
                    backgroundBinding.btnBackCinS?.setImageResource(R.drawable.back_cin_s_1)
                    backgroundBinding.btnBackSumS?.setImageResource(R.drawable.back_sr_s_1)

                    Log.d("FragCustom", "onResetButtonClicked()")

                    //초기화 api 호출
                    getcustomReset()
                } else {
                    // 자식 프래그먼트의 바인딩 중 하나라도 초기화되지 않았으면 초기화를 건너뜁니다.
                    Toast.makeText(this.requireActivity(), "초기화 할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("FragCustom", "Some fragment bindings are not initialized. Reset skipped.")
                }
            } else {
                // binding 변수가 초기화되지 않은 경우에 대한 처리
                Toast.makeText(this.requireActivity(), "초기화 할 수 없습니다.", Toast.LENGTH_SHORT).show()
                Log.d("FragCustom", "Binding is not initialized. Reset skipped.")
            }
        }

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

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


        binding.btnCustomSave.setOnClickListener {
            // 기존 SharedPreferences에서 값 불러오기
            val currentData = loadSelectedButtonInfo()

            // 선택된 데이터를 불러와서 저장
            val newData = getSelectedButtonInfo()

            // 변경된 부분만 업데이트하도록 로직 수정
            val updatedData = selectedButtonInfo(
                selectedColorButtonInfo = newData.selectedColorButtonInfo ?: currentData.selectedColorButtonInfo,
                selectedClothButtonInfo = newData.selectedClothButtonInfo ?: currentData.selectedClothButtonInfo,
                selectedItemButtonInfo = newData.selectedItemButtonInfo ?: currentData.selectedItemButtonInfo,
                selectedBackgroundButtonInfo = newData.selectedBackgroundButtonInfo ?: currentData.selectedBackgroundButtonInfo
            )

            // 서버 업데이트 후, 성공 시에만 SharedPreferences에 데이터를 저장
            patchCustomItemChange(listOf(
                updatedData.selectedColorButtonInfo?.serverID ?: 10,
                updatedData.selectedClothButtonInfo?.serverID ?: 49,
                updatedData.selectedItemButtonInfo?.serverID ?: 50,
                updatedData.selectedBackgroundButtonInfo?.serverID ?: 48
            )) { serverCode ->
                if (serverCode == 200) {
                    // 서버에 성공적으로 저장되면 SharedPreferences에 변경 사항을 저장
                    saveSelectedButtonInfo(updatedData)

                    // UI 업데이트
                    unsavedChanges = false
                    Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()

                    // UI에 반영
                    binding.customRamdi.setImageResource(updatedData.selectedColorButtonInfo?.selectedImageResource ?: 0)
                    binding.imgCustomCloth.setImageResource(updatedData.selectedClothButtonInfo?.selectedImageResource ?: 0)
                    binding.imgCustomItem.setImageResource(updatedData.selectedItemButtonInfo?.selectedImageResource ?: 0)
                    binding.imgCustomBackground.setImageResource(updatedData.selectedBackgroundButtonInfo?.selectedImageResource ?: 0)
                } else if (serverCode == 409) {
                    // 서버 저장 실패 시 메시지 출력
                    Toast.makeText(requireContext(), "이미 착용하고 있는 카테고리입니다.", Toast.LENGTH_SHORT).show()
                    //Toast.makeText(requireContext(), "소유하지 않은 아이템을 저장할 수 없습니다.", Toast.LENGTH_SHORT).show()

                    // 실패한 경우 UI 업데이트를 하지 않고, 기존 상태를 유지
                    // 필요 시 기존 상태를 그대로 유지
                    // 예를 들어, 기본 값을 다시 적용할 수 있음

                }
                else{
                    Toast.makeText(requireContext(), "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
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
        Log.d("customcolorbtncheck", "${colorbuttonInfo.selectedImageResource}")
        binding.customRamdi.setImageResource(colorbuttonInfo.selectedImageResource)
        selectedColorButtonInfo = colorbuttonInfo
        unsavedChanges = true
    }

    override fun onClothButtonSelected(clothbuttonInfo: ButtonInfo) {
        binding.imgCustomCloth.setImageResource(clothbuttonInfo.selectedImageResource)
        selectedClothButtonInfo = clothbuttonInfo
        unsavedChanges = true
    }

    override fun onItemButtonSelected(itembuttonInfo: ButtonInfo) {
        binding.imgCustomItem.setImageResource(itembuttonInfo.selectedImageResource)
        selectedItemButtonInfo = itembuttonInfo
        unsavedChanges = true
    }

    override fun onBackgroundButtonSelected(backgroundbuttonInfo: ButtonInfo) {
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

        savedData = viewModel.getSavedButtonInfo() ?:
        selectedButtonInfo(ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi),
            ButtonInfo(0, 49, R.drawable.custom_empty),
            ButtonInfo(0, 50, R.drawable.custom_empty),
            ButtonInfo(0, 48, R.drawable.custom_empty))

        val selectedButtonInfo: selectedButtonInfo? = viewModel.getSavedSelectedButtonInfo(this)

        if (selectedButtonInfo == null){
            val colorbuttonInfo = when (DataRepo.buttonInfoEntity?.colorButtonInfo?.serverID) {
                10 -> ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi)
                11 -> ButtonInfo(R.id.btn_color_bluepurple, 11, R.drawable.c_ramdybp)
                12 -> ButtonInfo(R.id.btn_color_green, 12, R.drawable.c_ramdyg)
                13 -> ButtonInfo(R.id.btn_color_mint, 13, R.drawable.c_ramdymint)
                14 -> ButtonInfo(R.id.btn_color_orange, 14, R.drawable.c_ramdyo)
                15 -> ButtonInfo(R.id.btn_color_orange2, 15, R.drawable.c_ramdyoy)
                16 -> ButtonInfo(R.id.btn_color_pink, 16, R.drawable.c_ramdypn)
                17 -> ButtonInfo(R.id.btn_color_pink2, 17, R.drawable.c_ramdypink)
                18 -> ButtonInfo(R.id.btn_color_pink3, 18, R.drawable.c_ramdydp)
                26 -> ButtonInfo(R.id.btn_color_purple, 26, R.drawable.c_ramdyp)
                27 -> ButtonInfo(R.id.btn_color_Rblue, 27, R.drawable.c_ramdyrb)
                28 -> ButtonInfo(R.id.btn_color_yellow, 28, R.drawable.c_ramdyy)
                51 -> ButtonInfo(R.id.btn_color_yellow2, 51, R.drawable.c_ramdyyellow)
                else -> savedData?.selectedColorButtonInfo
            }

            val clothbuttonInfo = when (DataRepo.buttonInfoEntity?.clothButtonInfo?.serverID) {
                49 -> ButtonInfo(0, 49, R.drawable.custom_empty)
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
                50 -> ButtonInfo(0, 50, R.drawable.custom_empty)
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
                48 -> ButtonInfo(0, 48, R.drawable.custom_empty)
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
                colorbuttonInfo?.selectedImageResource ?: R.drawable.custom_empty
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
        }
        else{
            binding.customRamdi.setImageResource(
                selectedButtonInfo.selectedColorButtonInfo?.selectedImageResource ?: R.drawable.custom_empty
            )

            binding.imgCustomCloth.setImageResource(
                selectedButtonInfo.selectedClothButtonInfo?.selectedImageResource ?: 0
            )
            binding.imgCustomItem.setImageResource(
                selectedButtonInfo.selectedItemButtonInfo?.selectedImageResource ?: 0
            )
            binding.imgCustomBackground.setImageResource(
                selectedButtonInfo.selectedBackgroundButtonInfo?.selectedImageResource ?: 0
            )
        }

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
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

    fun getCustomPrint() {
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
                    //printId = item.id
                    //itemType = item.itemType
                    //printfilePath = item.filePath
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
            }

            override fun onFailure(call: Call<customPrintDATA>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }

    fun patchCustomItemChange(itemIds: List<Int>, callback: (Int) -> Unit) {
        val call: Call<customItemChangeDATA> = service.customItemChange(token, itemIds)

        call.enqueue(object : Callback<customItemChangeDATA> {
            override fun onResponse(call: Call<customItemChangeDATA>, response: Response<customItemChangeDATA>) {
                val responseCode = response.code()
                if (response.isSuccessful) {
                    Log.d("patchCustomItemChange", "Response Code: $responseCode")
                    val responseBody = response.body()
                    Log.d("patchCustomItemChange", "Response Body: ${responseBody?.data}")
                    callback(responseCode)
                    //Toast.makeText(binding.root.context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("patchCustomItemChangeFail", "Response Code: $responseCode, Error: ${response.errorBody()?.string()}")
                    callback(responseCode)
                }
            }
            override fun onFailure(call: Call<customItemChangeDATA>, t: Throwable) {
                Log.d("patchCustomItemChange", t.message.toString())
                callback(500)
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

    /*fun postcustomItemBuy(itemID: Int) {
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
    }*/
    private fun showBackConfirmationDialog() {
        alertDialog.show()
        //사이즈 조절
        val size = Point()
        val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getSize(size)
        val screenWidth = size.x
        val popupWidth = (screenWidth * 0.8).toInt()
        alertDialog?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)
    }

}