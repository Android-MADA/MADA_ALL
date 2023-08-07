package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.CustomBackgroundBinding

class custom_background : Fragment() {
    lateinit var binding: CustomBackgroundBinding
    private var selectedButton: ImageButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomBackgroundBinding.inflate(inflater, container, false)

        binding.btnBackBasic.setOnClickListener{ onImageButtonClick(binding.btnBackBasic) }
        binding.btnBackBridS.setOnClickListener{ onImageButtonClick(binding.btnBackBridS) }
        binding.btnBackNS.setOnClickListener{ onImageButtonClick(binding.btnBackNS) }
        binding.btnBackWinS.setOnClickListener{ onImageButtonClick(binding.btnBackWinS) }
        binding.btnBackNormalS.setOnClickListener{ onImageButtonClick(binding.btnBackNormalS) }
        binding.btnBackStoreS.setOnClickListener{ onImageButtonClick(binding.btnBackStoreS) }
        binding.btnBackZzimS.setOnClickListener{ onImageButtonClick(binding.btnBackZzimS) }
        binding.btnBackUniS.setOnClickListener{ onImageButtonClick(binding.btnBackUniS) }
        binding.btnBackCinS.setOnClickListener{ onImageButtonClick(binding.btnBackCinS) }
        binding.btnBackSumS.setOnClickListener{ onImageButtonClick(binding.btnBackSumS) }

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
}
