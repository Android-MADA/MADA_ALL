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
import com.example.myapplication.Fragment.OnClothImageChangeListener
import com.example.myapplication.Fragment.OnColorImageChangeListener
import com.example.myapplication.Fragment.OnResetButtonClickListener
import com.example.myapplication.MyFuction.MyWebviewActivity
import com.example.myapplication.databinding.CustomClothBinding
import com.example.myapplication.databinding.FragCustomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class custom_cloth() : Fragment() {
    lateinit var binding: CustomClothBinding
    lateinit var fragbinding: FragCustomBinding
    private var selectedButton: ImageButton? = null
    private val buttonLockMap = mutableMapOf<Int, Boolean>()

    private var imageChangeListener: OnClothImageChangeListener? = null
    private var resetButtonClickListener: OnResetButtonClickListener? = null

    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)

    val token = MyWebviewActivity.prefs.getString("token", "")

    private val serverIdToDrawableMap = mapOf(
        900 to R.drawable.custom_empty,
        41 to R.drawable.set_dev,
        44 to R.drawable.set_movie,
        40 to R.drawable.set_caffk,
        46 to R.drawable.set_v,
        39 to R.drawable.set_astronauts,
        47 to R.drawable.set_zzim,
        42 to R.drawable.set_hanbokf,
        43 to R.drawable.set_hanbokm,
        45 to R.drawable.set_snowman
    )


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
        binding = CustomClothBinding.inflate(inflater, container, false)
        fragbinding = FragCustomBinding.inflate(inflater)
        getCustomItemCheck("set")



        binding.btnClothBasic.setOnClickListener {
            onImageButtonClick(binding.btnClothBasic)
            onClothButtonClick(it as ImageButton)
        }
        binding.btnClothDev.setOnClickListener {
            onImageButtonClick(binding.btnClothDev)
            onClothButtonClick(it as ImageButton)
        }
        binding.btnClothMovie.setOnClickListener {
            onImageButtonClick(binding.btnClothMovie)
            onClothButtonClick(it as ImageButton)
        }
        binding.btnClothCaffK.setOnClickListener {
            onImageButtonClick(binding.btnClothCaffK)
            onClothButtonClick(it as ImageButton)
        }
        binding.btnClothV.setOnClickListener {
            onImageButtonClick(binding.btnClothV)
            onClothButtonClick(it as ImageButton)
        }
        binding.btnClothAstronauts.setOnClickListener {
            onImageButtonClick(binding.btnClothAstronauts)
            onClothButtonClick(it as ImageButton)
        }
        binding.btnClothZzim.setOnClickListener {
            onImageButtonClick(binding.btnClothZzim)
            onClothButtonClick(it as ImageButton)
        }
        binding.btnClothHanbokF.setOnClickListener {
            onImageButtonClick(binding.btnClothHanbokF)
            onClothButtonClick(it as ImageButton)
        }
        binding.btnClothHanbokM.setOnClickListener {
            onImageButtonClick(binding.btnClothHanbokM)
            onClothButtonClick(it as ImageButton)
        }
        binding.btnClothSnowman.setOnClickListener {
            onImageButtonClick(binding.btnClothSnowman)
            onClothButtonClick(it as ImageButton)
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

    public fun resetCloth() {
        selectedButton?.setImageResource(getUnselectedImageResource(selectedButton!!))
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
        val buttonId = clickedButton.id
        if (buttonLockMap.containsKey(buttonId) && buttonLockMap[buttonId] == false) {
            // 버튼이 잠겨 있으면 동작하지 않음
            Log.d("onclothbtnclick", "buttonlockmap")
            return
        }
        val buttonInfo = when (clickedButton.id) {
            R.id.btn_cloth_basic -> ButtonInfo(clickedButton.id, 900, R.drawable.custom_empty)
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

    fun resetButtonCloth() {
        binding.btnClothBasic.setImageResource(R.drawable.custom_null)
        binding.btnClothDev.setImageResource(R.drawable.set_dev_s)
        binding.btnClothMovie.setImageResource(R.drawable.set_movie_s)
        binding.btnClothCaffK.setImageResource(R.drawable.set_caffk_s)
        binding.btnClothV.setImageResource(R.drawable.set_v_s)
        binding.btnClothAstronauts.setImageResource(R.drawable.set_astronauts_s)
        binding.btnClothZzim.setImageResource(R.drawable.set_zzim_s)
        binding.btnClothHanbokF.setImageResource(R.drawable.set_hanbokf_s)
        binding.btnClothHanbokM.setImageResource(R.drawable.set_hanbokm_s)
        binding.btnClothSnowman.setImageResource(R.drawable.set_snowman_s)
    }

    fun getCustomItemCheck(itemType: String) {
        val call: Call<customItemCheckDATA> = service.customItemCheck(token, itemType)

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
                                "Item $index - id: ${item.id} ${item.name} ${item.itemType} ${item.itemUnlockCondition} ${item.filePath} ${item.have}"
                            )
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

    private fun initButtonLockStates(checkInfo: customItemCheckDATA?) {
        val itemList = checkInfo?.data?.itemList ?: emptyList()

        itemList.forEach { item ->
            val isLocked = !item.have
            buttonLockMap[item.id] = isLocked
            Log.d("initbtnlock", "Button ID: ${item.id}, Is Locked: $isLocked")
        }

        applyButtonLockStates()
    }

    private fun applyButtonLockStates() {
        val buttons = arrayOf(
            binding.btnClothBasic,
            binding.btnClothDev,
            binding.btnClothMovie,
            binding.btnClothCaffK,
            binding.btnClothV,
            binding.btnClothAstronauts,
            binding.btnClothZzim,
            binding.btnClothHanbokF,
            binding.btnClothHanbokM,
            binding.btnClothSnowman
        )

        for (button in buttons) {
            val buttonId = button.id
            val isLocked = buttonLockMap[buttonId] ?: false

            val serverId = buttonLockMap.keys.firstOrNull { buttonLockMap[it] == isLocked } ?: -1
            val drawableId = serverIdToDrawableMap[serverId] ?: throw IllegalArgumentException("Unknown server ID")

            Log.d("applyButtonLockStates", "Button ID: $buttonId, Server ID: $serverId, Drawable ID: $drawableId, Is Locked: $isLocked")

            button.isEnabled = !isLocked
            button.setImageResource(if (isLocked) getLockedDrawable(drawableId) else drawableId)
        }
    }

    private fun getLockedDrawable(drawableId: Int): Int {
        val lockedDrawableMap = mapOf(
            //R.id.btn_cloth_dev to R.drawable.set_dev_lock,
            //R.id.btn_cloth_movie to R.drawable.set_v_lock,
            //R.id.btn_cloth_caffK to R.drawable.set_v_lock,
            R.drawable.set_v to R.drawable.set_v_lock,
            R.drawable.set_astronauts to R.drawable.set_astronauts_lock,
            R.drawable.set_zzim to R.drawable.set_zzim_lock,
            R.drawable.set_hanbokf to R.drawable.set_hanbokf_lock,
            R.drawable.set_hanbokm to R.drawable.set_hanbokm_lock,
            R.drawable.set_snowman to R.drawable.set_snowman_lock
        )

        return lockedDrawableMap[drawableId] ?: throw IllegalArgumentException("Unknown drawable ID")
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


