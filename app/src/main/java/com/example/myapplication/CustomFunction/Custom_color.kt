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
import com.example.myapplication.Fragment.OnColorImageChangeListener
import com.example.myapplication.Fragment.OnResetButtonClickListener
import com.example.myapplication.MyFuction.MyWebviewActivity
import com.example.myapplication.databinding.CustomColorBinding
import com.example.myapplication.databinding.FragCustomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class custom_color : Fragment() {

    lateinit var binding: CustomColorBinding
    lateinit var fragbinding: FragCustomBinding
    private var selectedButton: ImageButton? = null


    private var imageChangeListener: OnColorImageChangeListener? = null
    private var resetButtonClickListener: OnResetButtonClickListener? = null

    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)

    val token = MyWebviewActivity.prefs.getString("token","")


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
        binding = CustomColorBinding.inflate(inflater, container, false)
        fragbinding = FragCustomBinding.inflate(inflater)
        getCustomItemCheck("color")



        binding.btnColorBasic.setOnClickListener{
            onImageButtonClick(binding.btnColorBasic)
            onColorButtonClick(it as ImageButton) }
        binding.btnColorBlue.setOnClickListener{
            onImageButtonClick(binding.btnColorBlue)
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
            R.id.btn_color_basic -> R.drawable.color_basic_choice
            R.id.btn_color_blue -> R.drawable.color_blue_choice
            R.id.btn_color_Rblue -> R.drawable.color_rblue_choice
            R.id.btn_color_bluepurple -> R.drawable.color_bluepurple_choice
            R.id.btn_color_green -> R.drawable.color_green_choice
            R.id.btn_color_orange -> R.drawable.color_orange_choice
            R.id.btn_color_pink -> R.drawable.color_pink_choice
            R.id.btn_color_purple -> R.drawable.color_purple_choice
            R.id.btn_color_yellow -> R.drawable.color_yellow_choice
            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }


    fun getUnselectedImageResource(button: ImageButton): Int {
        return when (button.id) {
            R.id.btn_color_basic -> R.drawable.color_basic
            R.id.btn_color_blue -> R.drawable.color_blue
            R.id.btn_color_Rblue -> R.drawable.color_rblue
            R.id.btn_color_bluepurple -> R.drawable.color_bluepurple
            R.id.btn_color_green -> R.drawable.color_green
            R.id.btn_color_orange -> R.drawable.color_orange
            R.id.btn_color_pink -> R.drawable.color_pink
            R.id.btn_color_purple -> R.drawable.color_purple
            R.id.btn_color_yellow -> R.drawable.color_yellow
            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    fun onColorButtonClick(clickedButton: ImageButton) {
        val buttonInfo = when (clickedButton.id) {
            R.id.btn_color_basic -> ButtonInfo(clickedButton.id, 10, R.drawable.c_ramdi)
            R.id.btn_color_blue -> ButtonInfo(clickedButton.id, 11, R.drawable.c_ramdyb)
            R.id.btn_color_Rblue -> ButtonInfo(clickedButton.id, 17, R.drawable.c_ramdyrb)
            R.id.btn_color_bluepurple -> ButtonInfo(clickedButton.id, 12, R.drawable.c_ramdybp)
            R.id.btn_color_green -> ButtonInfo(clickedButton.id, 13, R.drawable.c_ramdyg)
            R.id.btn_color_orange -> ButtonInfo(clickedButton.id, 14, R.drawable.c_ramdyo)
            R.id.btn_color_pink -> ButtonInfo(clickedButton.id, 16, R.drawable.c_ramdypn)
            R.id.btn_color_purple -> ButtonInfo(clickedButton.id, 15, R.drawable.c_ramdyp)
            R.id.btn_color_yellow -> ButtonInfo(clickedButton.id, 18, R.drawable.c_ramdyy)
            else -> throw IllegalArgumentException("Unknown button ID")

        }

        imageChangeListener?.onColorButtonSelected(buttonInfo)

    }

    fun resetButtonColor() {
        Log.d("CustomColorFragment", "resetButtonImage() called")
        binding.btnColorBasic.setImageResource(R.drawable.color_basic)
        binding.btnColorBlue.setImageResource(R.drawable.color_blue)
        binding.btnColorRblue.setImageResource(R.drawable.color_rblue)
        binding.btnColorBluepurple.setImageResource(R.drawable.color_bluepurple)
        binding.btnColorGreen.setImageResource(R.drawable.color_green)
        binding.btnColorOrange.setImageResource(R.drawable.color_orange)
        binding.btnColorPink.setImageResource(R.drawable.color_pink)
        binding.btnColorPurple.setImageResource(R.drawable.color_purple)
        binding.btnColorYellow.setImageResource(R.drawable.color_yellow)
    }

    fun getCustomItemCheck(itemType: String) {
        val call: Call<customItemCheckDATA> = service.customItemCheck(token, itemType)

        call.enqueue(object : Callback<customItemCheckDATA> {
            override fun onResponse(call: Call<customItemCheckDATA>, response: Response<customItemCheckDATA>) {
                if (response.isSuccessful) {
                    val checkInfo = response.body()
                    checkInfo?.data?.forEachIndexed { index, item ->
                        Log.d("getCustomItemCheckColor", "Item $index - id: ${item.id} ${item.itemType} ${item.itemUnlockCondition} ${item.filePath} ${item.have}")
                    }
                } else {
                    Log.d("getCustomItemCheckColor", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<customItemCheckDATA>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }





}