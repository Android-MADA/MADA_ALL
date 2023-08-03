package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.CustomItemBinding

class custom_item : Fragment() {
    lateinit var binding: CustomItemBinding
    private var selectedButton: ImageButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomItemBinding.inflate(inflater, container, false)

        binding.btnItemBasic.setOnClickListener{ onImageButtonClick(binding.btnItemBasic) }
        binding.btnItemGlassNormal.setOnClickListener{ onImageButtonClick(binding.btnItemGlassNormal) }
        binding.btnItemHatBer.setOnClickListener{ onImageButtonClick(binding.btnItemHatBer) }
        binding.btnItemHatGrad.setOnClickListener{ onImageButtonClick(binding.btnItemHatGrad) }
        binding.btnItemGlass8bit.setOnClickListener{ onImageButtonClick(binding.btnItemGlass8bit) }
        binding.btnItemGlassWoig.setOnClickListener{ onImageButtonClick(binding.btnItemGlassWoig) }
        binding.btnItemHatIpod.setOnClickListener{ onImageButtonClick(binding.btnItemHatIpod) }
        binding.btnItemGlassSunR.setOnClickListener{ onImageButtonClick(binding.btnItemGlassSunR) }
        binding.btnItemGlassSunB.setOnClickListener{ onImageButtonClick(binding.btnItemGlassSunB) }
        binding.btnItemHatFlower.setOnClickListener{ onImageButtonClick(binding.btnItemHatFlower) }
        binding.btnItemHatV.setOnClickListener{ onImageButtonClick(binding.btnItemHatV) }
        binding.btnItemHatDinof.setOnClickListener{ onImageButtonClick(binding.btnItemHatDinof) }
        binding.btnItemHatSheep.setOnClickListener{ onImageButtonClick(binding.btnItemHatSheep) }
        binding.btnItemBagE.setOnClickListener{ onImageButtonClick(binding.btnItemBagE) }
        binding.btnItemBagLuck.setOnClickListener{ onImageButtonClick(binding.btnItemBagLuck) }
        binding.btnItemHatHeart.setOnClickListener{ onImageButtonClick(binding.btnItemHatHeart) }
        binding.btnItemHatBee.setOnClickListener{ onImageButtonClick(binding.btnItemHatBee) }
        binding.btnItemHatHeads.setOnClickListener{ onImageButtonClick(binding.btnItemHatHeads) }


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
            R.id.btn_item_basic -> R.drawable.custom_nullchoice
            R.id.btn_item_glass_normal -> R.drawable.gh_normal_choice
            R.id.btn_item_hat_ber -> R.drawable.hat_ber_choice
            R.id.btn_item_hat_grad -> R.drawable.hat_grad_choice
            R.id.btn_item_glass_8bit -> R.drawable.g_8bit_choice
            R.id.btn_item_glass_woig -> R.drawable.g_woig_choice
            R.id.btn_item_hat_ipod -> R.drawable.hat_ipod_choice
            R.id.btn_item_glass_sunR -> R.drawable.g_sunr_choice
            R.id.btn_item_glass_sunB -> R.drawable.g_sunb_choice
            R.id.btn_item_hat_flower -> R.drawable.hat_flower_chocie
            R.id.btn_item_hat_v -> R.drawable.hat_v_choice
            R.id.btn_item_hat_dinof -> R.drawable.hat_dinof_choice
            R.id.btn_item_hat_sheep -> R.drawable.hat_sheep_choice
            R.id.btn_item_bag_e -> R.drawable.back_e_choice
            R.id.btn_item_bag_luck -> R.drawable.back_luck_choice
            R.id.btn_item_hat_heart -> R.drawable.hat_heart_choice
            R.id.btn_item_hat_bee -> R.drawable.hat_bee_choice
            R.id.btn_item_hat_heads -> R.drawable.heads_choice

            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    private fun getUnselectedImageResource(button: ImageButton): Int {
        return when (button.id) {
            R.id.btn_item_basic -> R.drawable.custom_null
            R.id.btn_item_glass_normal -> R.drawable.gh_normal_s
            R.id.btn_item_hat_ber -> R.drawable.hat_ber_s
            R.id.btn_item_hat_grad -> R.drawable.hat_grad_s
            R.id.btn_item_glass_8bit -> R.drawable.g_8bit_s
            R.id.btn_item_glass_woig -> R.drawable.g_woig_s
            R.id.btn_item_hat_ipod -> R.drawable.hat_ipod_s
            R.id.btn_item_glass_sunR -> R.drawable.g_sunr_s
            R.id.btn_item_glass_sunB -> R.drawable.g_sunb_s
            R.id.btn_item_hat_flower -> R.drawable.hat_flower_s
            R.id.btn_item_hat_v -> R.drawable.hat_v_s
            R.id.btn_item_hat_dinof -> R.drawable.hat_dinof_s
            R.id.btn_item_hat_sheep -> R.drawable.hat_sheep_s
            R.id.btn_item_bag_e -> R.drawable.bag_e_s
            R.id.btn_item_bag_luck -> R.drawable.bag_luck_s
            R.id.btn_item_hat_heart -> R.drawable.hat_heart_s
            R.id.btn_item_hat_bee -> R.drawable.hat_bee_s
            R.id.btn_item_hat_heads -> R.drawable.heads_s
            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

}