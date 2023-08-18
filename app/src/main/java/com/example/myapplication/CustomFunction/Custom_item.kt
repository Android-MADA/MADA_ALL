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
import com.example.myapplication.Fragment.OnItemImageChangeListener
import com.example.myapplication.Fragment.OnResetButtonClickListener
import com.example.myapplication.databinding.CustomItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class custom_item : Fragment() {
    lateinit var binding: CustomItemBinding
    private var selectedButton: ImageButton? = null

    private var imageChangeListener: OnItemImageChangeListener? = null
    private var resetButtonClickListener: OnResetButtonClickListener? = null
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdE12X0lfS3VlbFYwTWZJUUVfZll3ZTdic2tYc1Yza28zdktXeTF1OXFVIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM0OTI2MCwiZXhwIjoxNjkyMzg1MjYwfQ.CWqyzV85HTGF5X7HX8gL5f1c1kZJBFqAgVMkagtup23vs57hcYMfu29YOdleuBfGNOLOBTfZXdvDavfLoKZ8Ew"




    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnClothImageChangeListener) {
            imageChangeListener = parentFragment as? OnItemImageChangeListener
        } else {
            throw IllegalArgumentException("The parent fragment must implement OnImageChangeListener.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomItemBinding.inflate(inflater, container, false)
        getCustomItemCheck("item")


        binding.btnItemBasic.setOnClickListener{
            onImageButtonClick(binding.btnItemBasic)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemGlassNormal.setOnClickListener{
            onImageButtonClick(binding.btnItemGlassNormal)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemHatBer.setOnClickListener{
            onImageButtonClick(binding.btnItemHatBer)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemHatGrad.setOnClickListener{
            onImageButtonClick(binding.btnItemHatGrad)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemGlass8bit.setOnClickListener{
            onImageButtonClick(binding.btnItemGlass8bit)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemGlassWoig.setOnClickListener{
            onImageButtonClick(binding.btnItemGlassWoig)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemHatIpod.setOnClickListener{
            onImageButtonClick(binding.btnItemHatIpod)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemGlassSunR.setOnClickListener{
            onImageButtonClick(binding.btnItemGlassSunR)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemGlassSunB.setOnClickListener{
            onImageButtonClick(binding.btnItemGlassSunB)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemHatFlower.setOnClickListener{
            onImageButtonClick(binding.btnItemHatFlower)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemHatV.setOnClickListener{
            onImageButtonClick(binding.btnItemHatV)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemHatDinof.setOnClickListener{
            onImageButtonClick(binding.btnItemHatDinof)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemHatSheep.setOnClickListener{
            onImageButtonClick(binding.btnItemHatSheep)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemBagE.setOnClickListener{
            onImageButtonClick(binding.btnItemBagE)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemBagLuck.setOnClickListener{
            onImageButtonClick(binding.btnItemBagLuck)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemHatHeart.setOnClickListener{
            onImageButtonClick(binding.btnItemHatHeart)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemHatBee.setOnClickListener{
            onImageButtonClick(binding.btnItemHatBee)
            onItemButtonClick(it as ImageButton)}
        binding.btnItemHatHeads.setOnClickListener{
            onImageButtonClick(binding.btnItemHatHeads)
            onItemButtonClick(it as ImageButton)}


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

    fun onItemButtonClick(clickedButton: ImageButton) {
        val buttonInfo = when (clickedButton.id) {
            R.id.btn_item_basic -> ButtonInfo(
                clickedButton.id,
                R.drawable.custom_empty
            )
            R.id.btn_item_glass_normal -> ButtonInfo(
                clickedButton.id,
                R.drawable.g_nomal
            )
            R.id.btn_item_hat_ber -> ButtonInfo(clickedButton.id, R.drawable.hat_ber)
            R.id.btn_item_hat_grad -> ButtonInfo(
                clickedButton.id,
                R.drawable.hat_grad
            )
            R.id.btn_item_glass_8bit -> ButtonInfo(
                clickedButton.id,
                R.drawable.g_8bit
            )
            R.id.btn_item_glass_woig -> ButtonInfo(
                clickedButton.id,
                R.drawable.g_woig
            )
            R.id.btn_item_hat_ipod -> ButtonInfo(
                clickedButton.id,
                R.drawable.hat_ipod
            )
            R.id.btn_item_glass_sunR -> ButtonInfo(
                clickedButton.id,
                R.drawable.g_sunr
            )
            R.id.btn_item_glass_sunB -> ButtonInfo(
                clickedButton.id,
                R.drawable.g_sunb
            )
            R.id.btn_item_hat_flower -> ButtonInfo(
                clickedButton.id,
                R.drawable.hat_flower
            )
            R.id.btn_item_hat_v -> ButtonInfo(clickedButton.id, R.drawable.hat_v)
            R.id.btn_item_hat_dinof -> ButtonInfo(
                clickedButton.id,
                R.drawable.hat_dinof
            )
            R.id.btn_item_hat_sheep -> ButtonInfo(
                clickedButton.id,
                R.drawable.hat_sheep
            )
            R.id.btn_item_bag_e -> ButtonInfo(clickedButton.id, R.drawable.bag_e)
            R.id.btn_item_bag_luck -> ButtonInfo(
                clickedButton.id,
                R.drawable.bag_luck
            )
            R.id.btn_item_hat_heart -> ButtonInfo(
                clickedButton.id,
                R.drawable.hat_heart
            )
            R.id.btn_item_hat_bee -> ButtonInfo(clickedButton.id, R.drawable.hat_bee)
            R.id.btn_item_hat_heads -> ButtonInfo(clickedButton.id, R.drawable.heads)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        imageChangeListener?.onItemButtonSelected(buttonInfo)
    }

    fun resetButtonItem() {
        binding.btnItemBasic.setImageResource(R.drawable.custom_null)
        binding.btnItemGlassNormal.setImageResource(R.drawable.gh_normal_s)
        binding.btnItemHatBer.setImageResource(R.drawable.hat_ber_s)
        binding.btnItemHatGrad.setImageResource(R.drawable.hat_grad_s)
        binding.btnItemGlass8bit.setImageResource(R.drawable.g_8bit_s)
        binding.btnItemGlassWoig.setImageResource(R.drawable.g_woig_s)
        binding.btnItemHatIpod.setImageResource(R.drawable.hat_ipod_s)
        binding.btnItemGlassSunR.setImageResource(R.drawable.g_sunr_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.g_sunb_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.hat_flower_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.hat_v_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.hat_dinof_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.hat_sheep_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.bag_e_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.bag_luck_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.hat_heart_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.hat_bee_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.heads_s)
    }

    fun getCustomItemCheck(itemType: String) {
        val call: Call<customItemCheckDATA> = service.customItemCheck(token, itemType)

        call.enqueue(object : Callback<customItemCheckDATA> {
            override fun onResponse(call: Call<customItemCheckDATA>, response: Response<customItemCheckDATA>) {
                if (response.isSuccessful) {
                    val checkInfo = response.body()
                    Log.d("getCustomItemCheckBackground", "${checkInfo?.arrayList?.id} ${checkInfo?.arrayList?.itemType} ${checkInfo?.arrayList?.itemUnlockCondition} ${checkInfo?.arrayList?.filePath} ${checkInfo?.arrayList?.have}")
                } else {
                    Log.d("getCustomItemCheckItem", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<customItemCheckDATA>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }

}