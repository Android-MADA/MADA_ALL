package com.mada.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableRow
import androidx.fragment.app.Fragment
import com.mada.myapplication.CustomFunction.ButtonInfo
import com.mada.myapplication.CustomFunction.RetrofitServiceCustom
import com.mada.myapplication.CustomFunction.customItemCheckDATA
import com.mada.myapplication.Fragment.OnBackgroundImageChangeListener
import com.mada.myapplication.StartFuction.Splash2Activity
import com.mada.myapplication.databinding.CustomBackgroundBinding
import com.mada.myapplication.databinding.CustomClothBinding
import com.mada.myapplication.databinding.CustomColorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class custom_background(val binding: CustomBackgroundBinding) : Fragment() {
    private var selectedButton: ImageButton? = null

    private var imageChangeListener: OnBackgroundImageChangeListener? = null
    //private var resetButtonClickListener: OnResetButtonClickListener? = null

    val retrofit = Retrofit.Builder().baseUrl(BuildConfig.MADA_BASE)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)

    val token = Splash2Activity.prefs.getString("token","")
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
        //binding = CustomBackgroundBinding.inflate(inflater, container, false)
        getCustomItemCheck()



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
        if (prevSelectedButton != clickedButton) {
            prevSelectedButton?.setImageResource(getUnselectedImageResource(prevSelectedButton))
            clickedButton.setImageResource(getSelectedImageResource(clickedButton))
            selectedButton = clickedButton
        }
    }

    private fun getSelectedImageResource(button: ImageButton): Int {
        return when (button.id) {
            R.id.btn_back_brid_s -> R.drawable.back_bird_choice
            R.id.btn_back_n_s -> R.drawable.back_n_choice
            R.id.btn_back_win_s -> R.drawable.back_win_choice
            R.id.btn_back_normal_s -> R.drawable.back_normal_choice
            R.id.btn_back_store_s -> R.drawable.back_store_choice
            R.id.btn_back_zzim_s -> R.drawable.back_zzim_choice
            R.id.btn_back_uni_s -> R.drawable.back_sp_choice
            R.id.btn_back_cin_s -> R.drawable.back_cin_choice
            R.id.btn_back_sum_s -> R.drawable.back_sr_choice

            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    private fun getUnselectedImageResource(button: ImageButton): Int {
        return when (button.id) {
            R.id.btn_back_brid_s -> R.drawable.back_bird_s_1
            R.id.btn_back_n_s -> R.drawable.back_n_s_1
            R.id.btn_back_win_s -> R.drawable.back_win_s_1
            R.id.btn_back_normal_s -> R.drawable.back_normal_s_1
            R.id.btn_back_store_s -> R.drawable.back_store_s_1
            R.id.btn_back_zzim_s -> R.drawable.back_zzim_s_1
            R.id.btn_back_uni_s -> R.drawable.back_sp_s_1
            R.id.btn_back_cin_s -> R.drawable.back_cin_s_1
            R.id.btn_back_sum_s -> R.drawable.back_sr_s_1

            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    fun onBackgroundButtonClick(clickedButton: ImageButton) {
        val buttonInfo = when (clickedButton.id) {
            R.id.btn_back_brid_s -> ButtonInfo(clickedButton.id, 1, R.drawable.back_brid)
            R.id.btn_back_n_s -> ButtonInfo(clickedButton.id, 3, R.drawable.back_n)
            R.id.btn_back_win_s -> ButtonInfo(clickedButton.id, 8, R.drawable.back_win)
            R.id.btn_back_normal_s -> ButtonInfo(clickedButton.id, 4, R.drawable.back_nomal)
            R.id.btn_back_store_s -> ButtonInfo(clickedButton.id, 5, R.drawable.back_store)
            R.id.btn_back_zzim_s -> ButtonInfo(clickedButton.id, 9, R.drawable.back_zzim)
            R.id.btn_back_uni_s -> ButtonInfo(clickedButton.id, 7, R.drawable.back_uni)
            R.id.btn_back_cin_s -> ButtonInfo(clickedButton.id, 2, R.drawable.back_cin)
            R.id.btn_back_sum_s -> ButtonInfo(clickedButton.id, 6, R.drawable.back_sum)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        imageChangeListener?.onBackgroundButtonSelected(buttonInfo)
    }

    /*fun resetButtonBackground() {
        Log.d("BackgroundFrag", "resetButtonBackground()")
        binding.btnBackBridS.setImageResource(R.drawable.back_bird_s_1)
        binding.btnBackNS.setImageResource(R.drawable.back_n_s_1)
        binding.btnBackWinS.setImageResource(R.drawable.back_win_s_1)
        binding.btnBackNormalS.setImageResource(R.drawable.back_normal_s_1)
        binding.btnBackStoreS.setImageResource(R.drawable.back_store_s_1)
        binding.btnBackZzimS.setImageResource(R.drawable.back_zzim_s_1)
        binding.btnBackUniS.setImageResource(R.drawable.back_sp_s_1)
        binding.btnBackCinS.setImageResource(R.drawable.back_cin_s_1)
        binding.btnBackSumS.setImageResource(R.drawable.back_sr_s_1)

    }*/

    fun getCustomItemCheck() {
        val call: Call<customItemCheckDATA> = service.customItemCheck(token)

        call.enqueue(object : Callback<customItemCheckDATA> {
            override fun onResponse(
                call: Call<customItemCheckDATA>,
                response: Response<customItemCheckDATA>
            ) {
                if (response.isSuccessful) {
                    val checkInfo = response.body()
                    checkInfo?.data?.let { itemList ->
                        itemList.itemList.forEachIndexed { index, item ->
                            Log.d(
                                "getCustomItemCheckCloth",
                                "Item $index - id: ${item.id} itemType:${item.itemType} have:${item.have} itemCategory:${item.itemCategory}"
                            )

                            if (item.itemType == "background" && item.have) {
                                Log.d(
                                    "BackgroundShowButton",
                                    "Item $index - id: ${item.id} have:${item.have} itemCategory:${item.itemCategory}"
                                )
                                showButton(item.id)
                            }
                        }
                    }
                } else {
                    Log.d("getCustomItemCheckCloth", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<customItemCheckDATA>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }

    private fun showButton(itemCategory: Int) {
        val buttonToShow = when (itemCategory) {
            1 -> binding.btnBackBridS
            2 -> binding.btnBackCinS
            3 -> binding.btnBackNS
            4 -> binding.btnBackNormalS
            5 -> binding.btnBackStoreS
            6 -> binding.btnBackSumS
            7 -> binding.btnBackUniS
            8 -> binding.btnBackWinS
            9 -> binding.btnBackZzimS
            48 -> return //basic
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        val parent = buttonToShow.parent as? TableRow
        parent?.let {
            // 버튼을 보이도록 설정
            buttonToShow.visibility = View.VISIBLE

            // 남은 버튼의 개수 확인
            val remainingButtonsCount = it.childCount

            // 남은 버튼의 개수가 0이 아니라면 가중치 조정
            if (remainingButtonsCount > 0) {
                val weight = 1.0f / (remainingButtonsCount + 1) // 새 버튼을 고려하여 +1
                for (i in 0 until remainingButtonsCount) {
                    val child = it.getChildAt(i)
                    val layoutParams = child.layoutParams as TableRow.LayoutParams
                    layoutParams.weight = weight
                    child.layoutParams = layoutParams
                }
                // 새 버튼에도 가중치 설정
                val layoutParams = buttonToShow.layoutParams as TableRow.LayoutParams
                layoutParams.weight = weight
                buttonToShow.layoutParams = layoutParams
            }
        }
    }



}
