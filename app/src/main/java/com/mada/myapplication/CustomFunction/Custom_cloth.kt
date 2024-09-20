package com.mada.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableRow
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mada.myapplication.CustomFunction.ButtonInfo
import com.mada.myapplication.CustomFunction.RetrofitServiceCustom
import com.mada.myapplication.CustomFunction.customItemCheckDATA
import com.mada.myapplication.Fragment.OnClothImageChangeListener
//import com.mada.myapplication.Fragment.onCategorySelected
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.CustomClothBinding
import com.mada.myapplication.databinding.CustomColorBinding
import com.mada.myapplication.databinding.FragCustomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class custom_cloth(val binding: CustomClothBinding) : Fragment() {
    lateinit var fragbinding: FragCustomBinding
    private var selectedButton: ImageButton? = null

    private var imageChangeListener: OnClothImageChangeListener? = null
    //private var resetButtonClickListener: OnResetButtonClickListener? = null

    val retrofit = Retrofit.Builder().baseUrl(BuildConfig.MADA_BASE)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)

    val token = Splash2Activity.prefs.getString("token", "")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnClothImageChangeListener) {
            imageChangeListener = parentFragment as? OnClothImageChangeListener
        } else {
            throw IllegalArgumentException("The parent fragment must implement OnImageChangeListener.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = CustomClothBinding.inflate(inflater, container, false)
        fragbinding = FragCustomBinding.inflate(inflater)
        getCustomItemCheck()


        binding.btnClothBasic.setOnClickListener {
            //onCategorySelected(R.id.btn_cloth_dev)
            onClothButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnClothBasic)
        }
        binding.btnClothDev.setOnClickListener {
            //onCategorySelected(R.id.btn_cloth_dev)
            onClothButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnClothDev)
        }
        binding.btnClothMovie.setOnClickListener {
            //onCategorySelected(R.id.btn_cloth_movie)
            onClothButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnClothMovie)
        }
        binding.btnClothCaffK.setOnClickListener {
            //onCategorySelected(R.id.btn_cloth_caffK)
            onClothButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnClothCaffK)
        }
        binding.btnClothV.setOnClickListener {
            //onCategorySelected(R.id.btn_cloth_v)
            onClothButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnClothV)
        }
        binding.btnClothAstronauts.setOnClickListener {
            //onCategorySelected(R.id.btn_cloth_astronauts)
            onClothButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnClothAstronauts)
        }
        binding.btnClothZzim.setOnClickListener {
            //onCategorySelected(R.id.btn_cloth_zzim)
            onClothButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnClothZzim)
        }
        binding.btnClothHanbokF.setOnClickListener {
            //onCategorySelected(R.id.btn_cloth_hanbokF)
            onClothButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnClothHanbokF)
        }
        binding.btnClothHanbokM.setOnClickListener {
            //onCategorySelected(R.id.btn_cloth_hanbokM)
            onClothButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnClothHanbokM)
        }
        binding.btnClothSnowman.setOnClickListener {
            //onCategorySelected(R.id.btn_cloth_snowman)
            onClothButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnClothSnowman)
        }



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
            R.id.btn_cloth_basic -> R.drawable.custom_nullchoice
            R.id.btn_cloth_dev -> R.drawable.set_dev_choice
            R.id.btn_cloth_movie -> R.drawable.set_movie_choice
            R.id.btn_cloth_caffK -> R.drawable.set_caffk_choice
            R.id.btn_cloth_v -> R.drawable.set_v_choice
            R.id.btn_cloth_astronauts -> R.drawable.set_astronauts_choice
            R.id.btn_cloth_zzim -> R.drawable.set_zzim_choice
            R.id.btn_cloth_hanbokF -> R.drawable.set_hanbokf_choice
            R.id.btn_cloth_hanbokM -> R.drawable.set_hanbokm_choice
            R.id.btn_cloth_snowman -> R.drawable.set_snowman_choice


            // Add other cases for Button 3 to Button 9
            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    private fun getUnselectedImageResource(button: ImageButton): Int {
        return when (button.id) {
            R.id.btn_cloth_basic -> R.drawable.custom_null
            R.id.btn_cloth_dev -> R.drawable.set_dev_s
            R.id.btn_cloth_movie -> R.drawable.set_movie_s
            R.id.btn_cloth_caffK -> R.drawable.set_caffk_s
            R.id.btn_cloth_v -> R.drawable.set_v_s
            R.id.btn_cloth_astronauts -> R.drawable.set_astronauts_s
            R.id.btn_cloth_zzim -> R.drawable.set_zzim_s
            R.id.btn_cloth_hanbokF -> R.drawable.set_hanbokf_s
            R.id.btn_cloth_hanbokM -> R.drawable.set_hanbokm_s
            R.id.btn_cloth_snowman -> R.drawable.set_snowman_s


            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    fun onClothButtonClick(clickedButton: ImageButton) {
        val buttonInfo = when (clickedButton.id) {
            R.id.btn_cloth_basic -> ButtonInfo(0, 49, R.drawable.custom_empty)
            R.id.btn_cloth_dev -> ButtonInfo(clickedButton.id, 41, R.drawable.set_dev)
            R.id.btn_cloth_movie -> ButtonInfo(clickedButton.id, 44, R.drawable.set_movie)
            R.id.btn_cloth_caffK -> ButtonInfo(clickedButton.id, 40, R.drawable.set_caffk)
            R.id.btn_cloth_v -> ButtonInfo(clickedButton.id, 46, R.drawable.set_v)
            R.id.btn_cloth_astronauts -> ButtonInfo(clickedButton.id, 39, R.drawable.set_astronauts,)
            R.id.btn_cloth_zzim -> ButtonInfo(clickedButton.id, 47, R.drawable.set_zzim)
            R.id.btn_cloth_hanbokF -> ButtonInfo(clickedButton.id, 42, R.drawable.set_hanbokf)
            R.id.btn_cloth_hanbokM -> ButtonInfo(clickedButton.id, 43, R.drawable.set_hanbokm)
            R.id.btn_cloth_snowman -> ButtonInfo(clickedButton.id, 45, R.drawable.set_snowman)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        imageChangeListener?.onClothButtonSelected(buttonInfo)
    }

    fun getCustomItemCheck() {
        val call: Call<customItemCheckDATA> = service.customItemCheck(token)

        call.enqueue(object : Callback<customItemCheckDATA> {
            override fun onResponse(
                call: Call<customItemCheckDATA>,
                response: Response<customItemCheckDATA>
            ) {
                if (response.isSuccessful) {
                    val checkInfo = response.body()
                    val datas = checkInfo?.data?.itemList
                    datas?.forEachIndexed { index, item ->
                        Log.d(
                            "getCustomItemCheckCloth",
                            "Item $index - id: ${item.id} itemType:${item.itemType} have:${item.have} itemCategory:${item.itemCategory}"
                        )

                        if (item.itemType == "set" && item.have) {
                            Log.d(
                                "ClothShowButton",
                                "Item $index - id: ${item.id} have:${item.have} itemCategory:${item.itemCategory}"
                            )
                            showButton(item.id)
                            showButton(49)
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
            39 -> binding.btnClothAstronauts
            40 -> binding.btnClothCaffK
            41 -> binding.btnClothDev
            42 -> binding.btnClothHanbokF
            43 -> binding.btnClothHanbokM
            44 -> binding.btnClothMovie
            45 -> binding.btnClothSnowman
            46 -> binding.btnClothV
            47 -> binding.btnClothZzim
            49 -> binding.btnClothBasic
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


    /*private fun applyButtonLockStates() {
        Log.d("applybtnlock","button lock apply success")
        binding.btnClothV.isEnabled = !buttonLockMap.containsKey(R.id.btn_cloth_v) || buttonLockMap[R.id.btn_cloth_v]!!
        binding.btnClothAstronauts.isEnabled = !buttonLockMap.containsKey(R.id.btn_cloth_astronauts) || buttonLockMap[R.id.btn_cloth_astronauts]!!
        binding.btnClothZzim.isEnabled = !buttonLockMap.containsKey(R.id.btn_cloth_zzim) || buttonLockMap[R.id.btn_cloth_zzim]!!
        binding.btnClothHanbokF.isEnabled = !buttonLockMap.containsKey(R.id.btn_cloth_hanbokF) || buttonLockMap[R.id.btn_cloth_hanbokF]!!
        binding.btnClothHanbokM.isEnabled = !buttonLockMap.containsKey(R.id.btn_cloth_hanbokM) || buttonLockMap[R.id.btn_cloth_hanbokM]!!
        binding.btnClothSnowman.isEnabled = !buttonLockMap.containsKey(R.id.btn_cloth_snowman) || buttonLockMap[R.id.btn_cloth_snowman]!!
        binding.btnClothV.setImageResource(
            if (buttonLockMap.containsKey(R.id.btn_cloth_v) && !buttonLockMap[R.id.btn_cloth_v]!!)
                R.drawable.set_v_lock
            else
                R.drawable.set_v_s
        )
        binding.btnClothAstronauts.setImageResource(
            if (buttonLockMap.containsKey(R.id.btn_cloth_astronauts) && !buttonLockMap[R.id.btn_cloth_astronauts]!!)
                R.drawable.set_astronauts_lock
            else
                R.drawable.set_astronauts_s
        )
        binding.btnClothZzim.setImageResource(
            if (buttonLockMap.containsKey(R.id.btn_cloth_zzim) && !buttonLockMap[R.id.btn_cloth_zzim]!!)
                R.drawable.set_zzim_lock
            else
                R.drawable.set_zzim_s
        )
        binding.btnClothHanbokF.setImageResource(
            if (buttonLockMap.containsKey(R.id.btn_cloth_hanbokF) && !buttonLockMap[R.id.btn_cloth_hanbokF]!!)
                R.drawable.set_hanbokf_lock
            else
                R.drawable.set_hanbokf_s
        )
        binding.btnClothHanbokM.setImageResource(
            if (buttonLockMap.containsKey(R.id.btn_cloth_hanbokM) && !buttonLockMap[R.id.btn_cloth_hanbokM]!!)
                R.drawable.set_hanbokm_lock
            else
                R.drawable.set_hanbokm_s
        )
        binding.btnClothSnowman.setImageResource(
            if (buttonLockMap.containsKey(R.id.btn_cloth_snowman) && !buttonLockMap[R.id.btn_cloth_snowman]!!)
                R.drawable.set_snowman_lock
            else
                R.drawable.set_snowman_s
        )
    }*/


