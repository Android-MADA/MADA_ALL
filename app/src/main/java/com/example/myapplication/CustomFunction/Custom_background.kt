package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.CustomFunction.RetrofitServiceCustom
import com.example.myapplication.CustomFunction.customItemCheckDATA
import com.example.myapplication.Fragment.OnBackgroundImageChangeListener
import com.example.myapplication.Fragment.OnClothImageChangeListener
import com.example.myapplication.Fragment.OnResetButtonClickListener
import com.example.myapplication.databinding.CustomBackgroundBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class custom_background : Fragment() {
    lateinit var binding: CustomBackgroundBinding
    private var selectedButton: ImageButton? = null

    private var imageChangeListener: OnBackgroundImageChangeListener? = null
    private var resetButtonClickListener: OnResetButtonClickListener? = null
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdE12X0lfS3VlbFYwTWZJUUVfZll3ZTdic2tYc1Yza28zdktXeTF1OXFVIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjI2NTcxNiwiZXhwIjoxNjkyMzAxNzE2fQ.WjCYi164TS0TXB1MhK7BqT38ze_x_vgC9Pute9sxpSatYsQxvGg5yp1hIFbWinB65lqWGSZ34hRmU0LAJZkNKA"


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnBackgroundImageChangeListener) {
            imageChangeListener = parentFragment as? OnBackgroundImageChangeListener
        } else {
            throw IllegalArgumentException("The parent fragment must implement OnImageChangeListener.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomBackgroundBinding.inflate(inflater, container, false)
        getCustomItemCheck("background")


        binding.btnBackBasic.setOnClickListener{
            onImageButtonClick(binding.btnBackBasic)
            onBackgroundButtonClick(it as ImageButton)}
        binding.btnBackBridS.setOnClickListener{
            onImageButtonClick(binding.btnBackBridS)
            onBackgroundButtonClick(it as ImageButton)}
        binding.btnBackNS.setOnClickListener{
            onImageButtonClick(binding.btnBackNS)
            onBackgroundButtonClick(it as ImageButton)}
        binding.btnBackWinS.setOnClickListener{
            onImageButtonClick(binding.btnBackWinS)
            onBackgroundButtonClick(it as ImageButton)}
        binding.btnBackNormalS.setOnClickListener{
            onImageButtonClick(binding.btnBackNormalS)
            onBackgroundButtonClick(it as ImageButton)}
        binding.btnBackStoreS.setOnClickListener{
            onImageButtonClick(binding.btnBackStoreS)
            onBackgroundButtonClick(it as ImageButton)}
        binding.btnBackZzimS.setOnClickListener{
            onImageButtonClick(binding.btnBackZzimS)
            onBackgroundButtonClick(it as ImageButton)}
        binding.btnBackUniS.setOnClickListener{
            onImageButtonClick(binding.btnBackUniS)
            onBackgroundButtonClick(it as ImageButton)}
        binding.btnBackCinS.setOnClickListener{
            onImageButtonClick(binding.btnBackCinS)
            onBackgroundButtonClick(it as ImageButton)}
        binding.btnBackSumS.setOnClickListener{
            onImageButtonClick(binding.btnBackSumS)
            onBackgroundButtonClick(it as ImageButton)}

        return binding.root
    }

    fun onImageButtonClick(clickedButton: ImageButton) {
        val prevSelectedButton = selectedButton

        if (clickedButton == prevSelectedButton) {
            // If the same button is clicked again, deselect it
            clickedButton.setImageResource(getUnselectedImageResource(clickedButton))
            selectedButton = null
        } else {
            // Deselect the previously selected button (if any)
            prevSelectedButton?.setImageResource(getUnselectedImageResource(prevSelectedButton))

            // Select the newly clicked button
            clickedButton.setImageResource(getSelectedImageResource(clickedButton))
            selectedButton = clickedButton
        }
    }

    private fun getSelectedImageResource(button: ImageButton): Int {
        return when (button.id) {
            R.id.btn_back_basic -> R.drawable.custom_nullchoice
            R.id.btn_back_brid_s -> R.drawable.back_brid_choice
            R.id.btn_back_n_s -> R.drawable.back_n_choice
            R.id.btn_back_win_s -> R.drawable.back_win_choice
            R.id.btn_back_normal_s -> R.drawable.back_normal_choice
            R.id.btn_back_store_s -> R.drawable.back_store_choice
            R.id.btn_back_zzim_s -> R.drawable.back_zzim_choice
            R.id.btn_back_uni_s -> R.drawable.back_uni_choice
            R.id.btn_back_cin_s -> R.drawable.back_cin_choice
            R.id.btn_back_sum_s -> R.drawable.back_sum_choice

            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    private fun getUnselectedImageResource(button: ImageButton): Int {
        return when (button.id) {
            R.id.btn_back_basic -> R.drawable.custom_null
            R.id.btn_back_brid_s -> R.drawable.back_brid_s_1
            R.id.btn_back_n_s -> R.drawable.back_n_s_1
            R.id.btn_back_win_s -> R.drawable.back_win_s_1
            R.id.btn_back_normal_s -> R.drawable.back_normal_s_1
            R.id.btn_back_store_s -> R.drawable.back_store_s_1
            R.id.btn_back_zzim_s -> R.drawable.back_zzim_s_1
            R.id.btn_back_uni_s -> R.drawable.back_uni_s_1
            R.id.btn_back_cin_s -> R.drawable.back_cin_s_1
            R.id.btn_back_sum_s -> R.drawable.back_sum_s_1

            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    fun onBackgroundButtonClick(clickedButton: ImageButton) {
        val buttonInfo = when (clickedButton.id) {
            R.id.btn_back_basic -> ButtonInfo(
                clickedButton.id,
                R.drawable.custom_empty
            )
            R.id.btn_back_brid_s -> ButtonInfo(clickedButton.id, R.drawable.back_brid)
            R.id.btn_back_n_s -> ButtonInfo(clickedButton.id, R.drawable.back_n)
            R.id.btn_back_win_s -> ButtonInfo(clickedButton.id, R.drawable.back_win)
            R.id.btn_back_normal_s -> ButtonInfo(
                clickedButton.id,
                R.drawable.back_nomal
            )
            R.id.btn_back_store_s -> ButtonInfo(
                clickedButton.id,
                R.drawable.back_store
            )
            R.id.btn_back_zzim_s -> ButtonInfo(clickedButton.id, R.drawable.back_zzim)
            R.id.btn_back_uni_s -> ButtonInfo(clickedButton.id, R.drawable.back_uni)
            R.id.btn_back_cin_s -> ButtonInfo(clickedButton.id, R.drawable.back_cin)
            R.id.btn_back_sum_s -> ButtonInfo(clickedButton.id, R.drawable.back_sum)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        imageChangeListener?.onBackgroundButtonSelected(buttonInfo)
    }
    fun resetButtonBackground() {
        binding.btnBackBasic.setImageResource(R.drawable.custom_null)
        binding.btnBackBridS.setImageResource(R.drawable.back_brid_s_1)
        binding.btnBackNS.setImageResource(R.drawable.back_n_s_1)
        binding.btnBackWinS.setImageResource(R.drawable.back_win_s_1)
        binding.btnBackNormalS.setImageResource(R.drawable.back_normal_s_1)
        binding.btnBackStoreS.setImageResource(R.drawable.back_store_s_1)
        binding.btnBackZzimS.setImageResource(R.drawable.back_zzim_s_1)
        binding.btnBackUniS.setImageResource(R.drawable.back_uni_s_1)
        binding.btnBackCinS.setImageResource(R.drawable.back_cin_s_1)
        binding.btnBackSumS.setImageResource(R.drawable.back_sum_s_1)

    }
    fun getCustomItemCheck(itemType: String) {
        val call: Call<customItemCheckDATA> = service.customItemCheck(token, itemType)

        call.enqueue(object : Callback<customItemCheckDATA> {
            override fun onResponse(call: Call<customItemCheckDATA>, response: Response<customItemCheckDATA>) {
                if (response.isSuccessful) {
                    val checkInfo = response.body()
                    Log.d("response", "${checkInfo?.id} ${checkInfo?.itemType} ${checkInfo?.itemUnlockCondition} ${checkInfo?.filePath} ${checkInfo?.have}")
                } else {
                    Log.d("response", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<customItemCheckDATA>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }


}
