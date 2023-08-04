package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.Fragment.OnImageChangeListener
import com.example.myapplication.databinding.CustomColorBinding
import com.example.myapplication.databinding.FragCustomBinding


class custom_color : Fragment() {

    lateinit var binding: CustomColorBinding
    lateinit var fragbinding: FragCustomBinding
    private var selectedButton: ImageButton? = null

    private var imageChangeListener: OnImageChangeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 부모 프래그먼트로 캐스팅하여 인터페이스 객체를 가져옴
        if (context is OnImageChangeListener) {
            imageChangeListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomColorBinding.inflate(inflater, container, false)
        fragbinding = FragCustomBinding.inflate(inflater)


        binding.btnColorBasic.setOnClickListener{ onImageButtonClick(binding.btnColorBasic) }
        binding.btnColorBlue.setOnClickListener{ onImageButtonClick(binding.btnColorBlue) }
        binding.btnColorRblue.setOnClickListener{ onImageButtonClick(binding.btnColorRblue)}
        binding.btnColorBluepurple.setOnClickListener { onImageButtonClick(binding.btnColorBluepurple)}
        binding.btnColorGreen.setOnClickListener{onImageButtonClick(binding.btnColorGreen) }
        binding.btnColorOrange.setOnClickListener{onImageButtonClick(binding.btnColorOrange) }
        binding.btnColorPink.setOnClickListener{onImageButtonClick(binding.btnColorPink) }
        binding.btnColorPurple.setOnClickListener{onImageButtonClick(binding.btnColorPurple) }
        binding.btnColorYellow.setOnClickListener{onImageButtonClick(binding.btnColorYellow) }

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


    private fun getUnselectedImageResource(button: ImageButton): Int {
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
            R.id.btn_color_basic -> ButtonInfo(clickedButton.id, R.drawable.c_ramdi)
            R.id.btn_color_blue -> ButtonInfo(clickedButton.id, R.drawable.c_ramdyb)
            R.id.btn_color_Rblue -> ButtonInfo(clickedButton.id, R.drawable.c_ramdyrb)
            R.id.btn_color_bluepurple -> ButtonInfo(clickedButton.id, R.drawable.c_ramdyrb)
            R.id.btn_color_green -> ButtonInfo(clickedButton.id, R.drawable.c_ramdyg)
            R.id.btn_color_orange -> ButtonInfo(clickedButton.id, R.drawable.c_ramdyo)
            R.id.btn_color_pink -> ButtonInfo(clickedButton.id, R.drawable.c_ramdypn)
            R.id.btn_color_purple -> ButtonInfo(clickedButton.id, R.drawable.c_ramdyp)
            R.id.btn_color_yellow -> ButtonInfo(clickedButton.id, R.drawable.c_ramdyy)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        imageChangeListener?.onButtonSelected(buttonInfo)
    }



}