package com.mada.myapplication.CustomFunction

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mada.myapplication.Fragment.FragCustom
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.mada.myapplication.R

class CustomViewModel : ViewModel() {

    private var savedButtonInfo: FragCustom.selectedButtonInfo? = null

    fun saveButtonInfo(buttonInfo: FragCustom.selectedButtonInfo) {
        savedButtonInfo = buttonInfo
    }

    fun getSavedButtonInfo(): FragCustom.selectedButtonInfo? {
        return savedButtonInfo
    }

    // MutableLiveData to hold the saved button information
    private val savedButtonInfoLiveData: MutableLiveData<FragCustom.selectedButtonInfo> = MutableLiveData()

    // Function to retrieve saved button information from SharedPreferences
    fun getSavedSelectedButtonInfo(fragment: Fragment): FragCustom.selectedButtonInfo? {
        val sharedPreferences = fragment.requireActivity().getSharedPreferences("selected_info", Context.MODE_PRIVATE)

        val colorButtonId = sharedPreferences.getInt("color_button_id", 10)
        val clothButtonId = sharedPreferences.getInt("cloth_button_id", 49)
        val itemButtonId = sharedPreferences.getInt("item_button_id", 50)
        val backgroundButtonId = sharedPreferences.getInt("background_button_id", 48)

        // Create ButtonInfo objects based on the data retrieved from SharedPreferences
        val selectedColorButtonInfo = when (colorButtonId) {
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
            else -> null // Handle default case or fallback to saved data
        }

        val selectedClothButtonInfo = when (clothButtonId) {
            0 -> ButtonInfo(0, 49, R.drawable.custom_empty)
            49 -> ButtonInfo(0, 49, R.drawable.custom_empty)
            41 -> ButtonInfo(R.id.btn_cloth_dev, 41, R.drawable.set_dev)
            44 -> ButtonInfo(R.id.btn_cloth_movie, 44, R.drawable.set_movie)
            40 -> ButtonInfo(R.id.btn_cloth_caffK, 40, R.drawable.set_caffk)
            46 -> ButtonInfo(R.id.btn_cloth_v, 46, R.drawable.set_v)
            39 -> ButtonInfo(R.id.btn_cloth_astronauts, 39, R.drawable.set_astronauts)
            47 -> ButtonInfo(R.id.btn_cloth_zzim, 47, R.drawable.set_zzim)
            42 -> ButtonInfo(R.id.btn_cloth_hanbokF, 42, R.drawable.set_hanbokf)
            43 -> ButtonInfo(R.id.btn_cloth_hanbokM, 43, R.drawable.set_hanbokm)
            45 -> ButtonInfo(R.id.btn_cloth_snowman, 45, R.drawable.set_snowman)

            else -> null // Handle default case or fallback to saved data
        }

        val selectedItemButtonInfo = when (itemButtonId) {
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

            else -> null // Handle default case or fallback to saved data
        }

        val selectedBackgroundButtonInfo = when (backgroundButtonId) {
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
            else -> null // Handle default case or fallback to saved data
        }

        return FragCustom.selectedButtonInfo(
            selectedColorButtonInfo,
            selectedClothButtonInfo,
            selectedItemButtonInfo,
            selectedBackgroundButtonInfo
        )
    }
}