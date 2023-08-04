package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.Fragment.OnClothImageChangeListener
import com.example.myapplication.Fragment.OnColorImageChangeListener
import com.example.myapplication.databinding.CustomClothBinding
import com.example.myapplication.databinding.FragCustomBinding


class custom_cloth : Fragment() {
    lateinit var binding: CustomClothBinding
    lateinit var fragbinding: FragCustomBinding
    private var selectedButton: ImageButton? = null

    private var imageChangeListener: OnClothImageChangeListener? = null

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

        binding.btnClothBasic.setOnClickListener{ onImageButtonClick(binding.btnClothBasic) }
        binding.btnClothDev.setOnClickListener{ onImageButtonClick(binding.btnClothDev) }
        binding.btnClothMovie.setOnClickListener{ onImageButtonClick(binding.btnClothMovie) }
        binding.btnClothCaffK.setOnClickListener{ onImageButtonClick(binding.btnClothCaffK) }
        binding.btnClothV.setOnClickListener{ onImageButtonClick(binding.btnClothV) }
        binding.btnClothAstronauts.setOnClickListener{ onImageButtonClick(binding.btnClothAstronauts) }
        binding.btnClothZzim.setOnClickListener{ onImageButtonClick(binding.btnClothZzim) }
        binding.btnClothHanbokF.setOnClickListener{ onImageButtonClick(binding.btnClothHanbokF) }
        binding.btnClothHanbokM.setOnClickListener{ onImageButtonClick(binding.btnClothHanbokM) }
        binding.btnClothSnowman.setOnClickListener{ onImageButtonClick(binding.btnClothSnowman) }


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
            R.id.btn_cloth_basic -> ButtonInfo(clickedButton.id, R.drawable.c_ramdi)
            R.id.btn_cloth_dev -> ButtonInfo(clickedButton.id, R.drawable.set_dev)
            R.id.btn_cloth_movie -> ButtonInfo(clickedButton.id, R.drawable.set_movie)
            R.id.btn_cloth_caffK -> ButtonInfo(clickedButton.id, R.drawable.set_caffk)
            R.id.btn_cloth_v -> ButtonInfo(clickedButton.id, R.drawable.set_v)
            R.id.btn_cloth_astronauts -> ButtonInfo(clickedButton.id, R.drawable.set_astronauts)
            R.id.btn_cloth_zzim -> ButtonInfo(clickedButton.id, R.drawable.set_zzim)
            R.id.btn_cloth_hanbokF -> ButtonInfo(clickedButton.id, R.drawable.set_hanbokf)
            R.id.btn_cloth_hanbokM -> ButtonInfo(clickedButton.id, R.drawable.set_hanbokm)
            R.id.btn_cloth_snowman -> ButtonInfo(clickedButton.id, R.drawable.set_snowman)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        imageChangeListener?.onClothButtonSelected(buttonInfo)
    }


}