package com.mada.reapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableRow
import androidx.fragment.app.Fragment
import com.mada.reapp.CustomFunction.ButtonInfo
import com.mada.reapp.CustomFunction.RetrofitServiceCustom
import com.mada.reapp.CustomFunction.customItemCheckDATA
import com.mada.reapp.Fragment.OnColorImageChangeListener
import com.mada.reapp.StartFunction.Splash2Activity
import com.mada.reapp.databinding.CustomColorBinding
import com.mada.reapp.databinding.FragCustomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class custom_color(val binding: CustomColorBinding) : Fragment() {
    lateinit var fragbinding: FragCustomBinding
    private var selectedButton: ImageButton? = null


    private var imageChangeListener: OnColorImageChangeListener? = null

    val retrofit = Retrofit.Builder().baseUrl(BuildConfig.MADA_BASE)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)

    val token = Splash2Activity.prefs.getString("token","")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 부모 프래그먼트로 캐스팅하여 인터페이스 객체를 가져옴
        if (parentFragment is OnColorImageChangeListener) {
            imageChangeListener = parentFragment as? OnColorImageChangeListener
            // imageChangeListener = context
        } else {
            throw IllegalArgumentException("The parent fragment must implement OnImageChangeListener.")
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragbinding = FragCustomBinding.inflate(inflater)
        getCustomItemCheck()


        binding.btnColorBasic.setOnClickListener{
            onImageButtonClick(binding.btnColorBasic)
            onColorButtonClick(it as ImageButton) }
        binding.btnColorMint.setOnClickListener{
            onImageButtonClick(binding.btnColorMint)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorRblue.setOnClickListener{
            onImageButtonClick(binding.btnColorRblue)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorBluepurple.setOnClickListener {
            onImageButtonClick(binding.btnColorBluepurple)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorGreen.setOnClickListener{
            onImageButtonClick(binding.btnColorGreen)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorOrange.setOnClickListener{
            onImageButtonClick(binding.btnColorOrange)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorPink.setOnClickListener{
            onImageButtonClick(binding.btnColorPink)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorPurple.setOnClickListener{
            onImageButtonClick(binding.btnColorPurple)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorYellow.setOnClickListener{
            onImageButtonClick(binding.btnColorYellow)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorYellow2.setOnClickListener{
            onImageButtonClick(binding.btnColorYellow2)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorPink2.setOnClickListener{
            onImageButtonClick(binding.btnColorPink2)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorPink3.setOnClickListener{
            onImageButtonClick(binding.btnColorPink3)
            onColorButtonClick(it as ImageButton)}
        binding.btnColorOrange2.setOnClickListener{
            onImageButtonClick(binding.btnColorOrange2)
            onColorButtonClick(it as ImageButton)}





        return binding.root
    }

    fun onImageButtonClick(clickedButton: ImageButton) {
        val prevSelectedButton = selectedButton
        if (prevSelectedButton != clickedButton) {
            prevSelectedButton?.setImageResource(getUnselectedImageResource(prevSelectedButton))
            //clickedButton.setImageResource(getSelectedImageResource(clickedButton))
            selectedButton = clickedButton
        }
    }

    /*private fun getSelectedImageResource(button: ImageButton): Int {
        return when (button.id) {
            R.id.btn_color_basic -> R.drawable.color_basic_choice
            R.id.btn_color_mint -> R.drawable.color_blue_choice
            R.id.btn_color_Rblue -> R.drawable.color_rblue_choice
            R.id.btn_color_bluepurple -> R.drawable.color_bluepurple_choice
            R.id.btn_color_green -> R.drawable.color_green_choice
            R.id.btn_color_orange -> R.drawable.color_orange_choice
            R.id.btn_color_pink -> R.drawable.color_pink_choice
            R.id.btn_color_purple -> R.drawable.color_purple_choice
            R.id.btn_color_yellow -> R.drawable.color_yellow_choice
            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }*/


    fun getUnselectedImageResource(button: ImageButton): Int {
        return when (button.id) {
            R.id.btn_color_basic -> R.drawable.color_basic
            R.id.btn_color_mint -> R.drawable.color_mint
            R.id.btn_color_Rblue -> R.drawable.color_rblue
            R.id.btn_color_bluepurple -> R.drawable.color_bluepurple
            R.id.btn_color_green -> R.drawable.color_green
            R.id.btn_color_orange -> R.drawable.color_orange
            R.id.btn_color_pink -> R.drawable.color_pink
            R.id.btn_color_purple -> R.drawable.color_purple
            R.id.btn_color_yellow -> R.drawable.color_yellow
            R.id.btn_color_yellow2 -> R.drawable.color_yellow2
            R.id.btn_color_orange2 -> R.drawable.color_orange2
            R.id.btn_color_pink2 -> R.drawable.color_pink2
            R.id.btn_color_pink3-> R.drawable.color_pink3
            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }


    fun onColorButtonClick(clickedButton: ImageButton) {
        val buttonInfo = when (clickedButton.id) {
            R.id.btn_color_basic -> ButtonInfo(clickedButton.id, 10, R.drawable.c_ramdi)
            R.id.btn_color_bluepurple -> ButtonInfo(clickedButton.id, 11, R.drawable.c_ramdybp)
            R.id.btn_color_green -> ButtonInfo(clickedButton.id, 12, R.drawable.c_ramdyg)
            R.id.btn_color_mint -> ButtonInfo(clickedButton.id, 13, R.drawable.c_ramdymint)
            R.id.btn_color_orange -> ButtonInfo(clickedButton.id, 14, R.drawable.c_ramdyo)
            R.id.btn_color_orange2 -> ButtonInfo(clickedButton.id, 15, R.drawable.c_ramdyoy)
            R.id.btn_color_pink -> ButtonInfo(clickedButton.id, 16, R.drawable.c_ramdypn)
            R.id.btn_color_pink2 -> ButtonInfo(clickedButton.id, 17, R.drawable.c_ramdypink)
            R.id.btn_color_pink3-> ButtonInfo(clickedButton.id, 18, R.drawable.c_ramdydp)
            R.id.btn_color_purple -> ButtonInfo(clickedButton.id, 26, R.drawable.c_ramdyp)
            R.id.btn_color_Rblue -> ButtonInfo(clickedButton.id, 27, R.drawable.c_ramdyrb)
            R.id.btn_color_yellow -> ButtonInfo(clickedButton.id, 28, R.drawable.c_ramdyy)
            R.id.btn_color_yellow2 -> ButtonInfo(clickedButton.id, 51, R.drawable.c_ramdyyellow)


            else -> throw IllegalArgumentException("Unknown button ID")

        }

        imageChangeListener?.onColorButtonSelected(buttonInfo)

    }

    /*fun resetButtonColor() {
        Log.d("CustomColorFragment", "resetButtonImage() called")
        binding.btnColorBasic.setImageResource(R.drawable.color_basic)
        binding.btnColorMint.setImageResource(R.drawable.color_blue)
        binding.btnColorRblue.setImageResource(R.drawable.color_rblue)
        binding.btnColorBluepurple.setImageResource(R.drawable.color_bluepurple)
        binding.btnColorGreen.setImageResource(R.drawable.color_green)
        binding.btnColorOrange.setImageResource(R.drawable.color_orange)
        binding.btnColorPink.setImageResource(R.drawable.color_pink)
        binding.btnColorPurple.setImageResource(R.drawable.color_purple)
        binding.btnColorYellow.setImageResource(R.drawable.color_yellow)
        binding.btnColorYellow2.setImageResource(R.drawable.color_yellow2)
        binding.btnColorPink2.setImageResource(R.drawable.color_pink2)
        binding.btnColorPink3.setImageResource(R.drawable.color_pink3)
        binding.btnColorOrange2.setImageResource(R.drawable.color_orange2)
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
                    val responseCode = response.code()
                    val datas = checkInfo?.data?.itemList
                    datas?.forEachIndexed { index, item ->
                        Log.d(
                            "getCustomItemCheckCloth",
                            "Item $index - id: ${item.id} itemType:${item.itemType} have:${item.have} itemCategory:${item.itemCategory}"
                        )
                        if (item.itemType == "color" && item.have) {
                            Log.d(
                                "ColorShowButton",
                                "Item $index - id: ${item.id} have:${item.have} itemCategory:${item.itemCategory}"
                            )
                            showButton(item.id)
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
            10 -> binding.btnColorBasic
            13 -> binding.btnColorMint
            27 -> binding.btnColorRblue
            11 -> binding.btnColorBluepurple
            12 -> binding.btnColorGreen
            14 -> binding.btnColorOrange
            16 -> binding.btnColorPink
            26 -> binding.btnColorPurple
            28 -> binding.btnColorYellow
            17 -> binding.btnColorPink2
            18 -> binding.btnColorPink3
            51 -> binding.btnColorYellow2
            15 -> binding.btnColorOrange2
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