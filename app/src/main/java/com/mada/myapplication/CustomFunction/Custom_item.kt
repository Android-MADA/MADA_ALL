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
import com.mada.myapplication.Fragment.OnClothImageChangeListener
import com.mada.myapplication.Fragment.OnItemImageChangeListener
import com.mada.myapplication.Fragment.onCategorySelected
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.CustomClothBinding
import com.mada.myapplication.databinding.CustomColorBinding
import com.mada.myapplication.databinding.CustomItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class custom_item(val binding: CustomItemBinding) : Fragment() {
    private var selectedButton: ImageButton? = null

    private var imageChangeListener: OnItemImageChangeListener? = null
    //private var resetButtonClickListener: OnResetButtonClickListener? = null


    val retrofit = Retrofit.Builder().baseUrl(BuildConfig.MADA_BASE)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCustom::class.java)

    val token = Splash2Activity.prefs.getString("token","")


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnClothImageChangeListener) {
            imageChangeListener = parentFragment as? OnItemImageChangeListener
        } else {
            throw IllegalArgumentException("The parent fragment must implement OnImageChangeListener.")
        }
        /*if (context is OnItemSelectionListener) {
            ItemSelectionListener = context
        } else {
            throw RuntimeException("$context must implement OnItemSelectionListener")
        }*/

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = CustomItemBinding.inflate(inflater, container, false)
        getCustomItemCheck()

        /*val defaultButton = binding.btnItemBasic
        onImageButtonClick(defaultButton)
        onItemButtonClick(defaultButton)*/


        binding.btnItemGlassNormal.setOnClickListener{
            //onCategorySelected(R.id.btn_item_glass_normal)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemGlassNormal)
            }
        binding.btnItemHatBer.setOnClickListener{
            //onCategorySelected(R.id.btn_item_hat_ber)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemHatBer)
            }
        binding.btnItemHatGrad.setOnClickListener{
            //onCategorySelected(R.id.btn_item_hat_grad)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemHatGrad)
            }
        binding.btnItemGlass8bit.setOnClickListener{
            //onCategorySelected(R.id.btn_item_glass_8bit)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemGlass8bit)
            }
        binding.btnItemGlassWoig.setOnClickListener{
            //onCategorySelected(R.id.btn_item_glass_woig)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemGlassWoig)
            }
        binding.btnItemHatIpod.setOnClickListener{
            //onCategorySelected(R.id.btn_item_hat_ipod)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemHatIpod)
            }
        binding.btnItemGlassSunR.setOnClickListener{
            //onCategorySelected(R.id.btn_item_glass_sunR)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemGlassSunR)
            }
        binding.btnItemGlassSunB.setOnClickListener{
            //onCategorySelected(R.id.btn_item_glass_sunB)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemGlassSunB)
            }
        binding.btnItemHatFlower.setOnClickListener{
            //onCategorySelected(R.id.btn_item_hat_flower)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemHatFlower)
            }
        binding.btnItemHatV.setOnClickListener{
            //onCategorySelected(R.id.btn_item_hat_v)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemHatV)
            }
        binding.btnItemHatDinof.setOnClickListener{
            //onCategorySelected(R.id.btn_item_hat_dinof)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemHatDinof)
            }
        binding.btnItemHatSheep.setOnClickListener{
            //onCategorySelected(R.id.btn_item_hat_sheep)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemHatSheep)
            }
        binding.btnItemBagE.setOnClickListener{
            //onCategorySelected(R.id.btn_item_bag_e)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemBagE)
            }
        binding.btnItemBagLuck.setOnClickListener{
            //onCategorySelected(R.id.btn_item_bag_luck)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemBagLuck)
            }
        binding.btnItemHatHeart.setOnClickListener{
            //onCategorySelected(R.id.btn_item_hat_heart)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemHatHeart)
            }
        binding.btnItemHatBee.setOnClickListener{
            //onCategorySelected(R.id.btn_item_hat_bee)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemHatBee)
            }
        binding.btnItemHatHeads.setOnClickListener{
            //onCategorySelected(R.id.btn_item_hat_heads)
            onItemButtonClick(it as ImageButton)
            onImageButtonClick(binding.btnItemHatHeads)
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
            R.id.btn_item_glass_normal -> R.drawable.g_normal_choice
            R.id.btn_item_hat_ber -> R.drawable.hat_ber_choice
            R.id.btn_item_hat_grad -> R.drawable.hat_grad_choice
            R.id.btn_item_glass_8bit -> R.drawable.g_8bit_choice
            R.id.btn_item_glass_woig -> R.drawable.g_woig_choice
            R.id.btn_item_hat_ipod -> R.drawable.hat_ipod_choice
            R.id.btn_item_glass_sunR -> R.drawable.g_sunr_choice
            R.id.btn_item_glass_sunB -> R.drawable.g_sunb_choice
            R.id.btn_item_hat_flower -> R.drawable.hat_flower_choice
            R.id.btn_item_hat_v -> R.drawable.hat_v_choice
            R.id.btn_item_hat_dinof -> R.drawable.hat_dinof_choice
            R.id.btn_item_hat_sheep -> R.drawable.hat_sheep_choice
            R.id.btn_item_bag_e -> R.drawable.bag_e_choice
            R.id.btn_item_bag_luck -> R.drawable.bag_luck_choice
            R.id.btn_item_hat_heart -> R.drawable.hat_heart_choice
            R.id.btn_item_hat_bee -> R.drawable.hat_bee_choice
            R.id.btn_item_hat_heads -> R.drawable.heads_choice

            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    private fun getUnselectedImageResource(button: ImageButton): Int {
        return when (button.id) {
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
            R.id.btn_item_glass_normal -> ButtonInfo(clickedButton.id, 22,R.drawable.g_nomal)
            R.id.btn_item_hat_ber -> ButtonInfo(clickedButton.id, 30, R.drawable.hat_ber)
            R.id.btn_item_hat_grad -> ButtonInfo(clickedButton.id, 33, R.drawable.hat_grad)
            R.id.btn_item_glass_8bit -> ButtonInfo(clickedButton.id, 21,R.drawable.g_8bit)
            R.id.btn_item_glass_woig -> ButtonInfo(clickedButton.id, 25, R.drawable.g_woig)
            R.id.btn_item_hat_ipod -> ButtonInfo(clickedButton.id, 35, R.drawable.hat_ipod)
            R.id.btn_item_glass_sunR -> ButtonInfo(clickedButton.id, 24,R.drawable.g_sunr)
            R.id.btn_item_glass_sunB -> ButtonInfo(clickedButton.id,23, R.drawable.g_sunb)
            R.id.btn_item_hat_flower -> ButtonInfo(clickedButton.id, 32, R.drawable.hat_flower)
            R.id.btn_item_hat_v -> ButtonInfo(clickedButton.id, 37, R.drawable.hat_v)
            R.id.btn_item_hat_dinof -> ButtonInfo(clickedButton.id, 31,R.drawable.hat_dinof)
            R.id.btn_item_hat_sheep -> ButtonInfo(clickedButton.id, 36, R.drawable.hat_sheep)
            R.id.btn_item_bag_e -> ButtonInfo(clickedButton.id,19, R.drawable.bag_e)
            R.id.btn_item_bag_luck -> ButtonInfo(clickedButton.id,20, R.drawable.bag_luck)
            R.id.btn_item_hat_heart -> ButtonInfo(clickedButton.id,34, R.drawable.hat_heart)
            R.id.btn_item_hat_bee -> ButtonInfo(clickedButton.id, 29, R.drawable.hat_bee)
            R.id.btn_item_hat_heads -> ButtonInfo(clickedButton.id, 38, R.drawable.heads)
            else -> throw IllegalArgumentException("Unknown button ID")
        }
        //onCategorySelected(buttonInfo)

        imageChangeListener?.onItemButtonSelected(buttonInfo)
    }

    /*fun resetButtonItem() {
        binding.btnItemGlassNormal.setImageResource(R.drawable.gh_normal_s)
        binding.btnItemHatBer.setImageResource(R.drawable.hat_ber_s)
        binding.btnItemHatGrad.setImageResource(R.drawable.hat_grad_s)
        binding.btnItemGlass8bit.setImageResource(R.drawable.g_8bit_s)
        binding.btnItemGlassWoig.setImageResource(R.drawable.g_woig_s)
        binding.btnItemHatIpod.setImageResource(R.drawable.hat_ipod_s)
        binding.btnItemGlassSunR.setImageResource(R.drawable.g_sunr_s)
        binding.btnItemGlassSunB.setImageResource(R.drawable.g_sunb_s)
        binding.btnItemHatFlower.setImageResource(R.drawable.hat_flower_s)
        binding.btnItemHatV.setImageResource(R.drawable.hat_v_s)
        binding.btnItemHatDinof.setImageResource(R.drawable.hat_dinof_s)
        binding.btnItemHatSheep.setImageResource(R.drawable.hat_sheep_s)
        binding.btnItemBagE.setImageResource(R.drawable.bag_e_s)
        binding.btnItemBagLuck.setImageResource(R.drawable.bag_luck_s)
        binding.btnItemHatHeart.setImageResource(R.drawable.hat_heart_s)
        binding.btnItemHatBee.setImageResource(R.drawable.hat_bee_s)
        binding.btnItemHatHeads.setImageResource(R.drawable.heads_s)
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
                            if (item.itemType == "item" && item.have) { // Check if item.have is true
                                Log.d(
                                    "ItemShowButton",
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
            19 -> binding.btnItemBagE
            20 -> binding.btnItemBagLuck
            21 -> binding.btnItemGlass8bit
            22 -> binding.btnItemGlassNormal
            23 -> binding.btnItemGlassSunB
            24 -> binding.btnItemGlassSunR
            25 -> binding.btnItemGlassWoig
            26 -> binding.btnItemGlassNormal
            27 -> binding.btnItemGlassSunB
            28 -> binding.btnItemGlassSunR
            29 -> binding.btnItemHatBee
            30 -> binding.btnItemHatBer
            31 -> binding.btnItemHatDinof
            32 -> binding.btnItemHatFlower
            33 -> binding.btnItemHatGrad
            34 -> binding.btnItemHatHeart
            35 -> binding.btnItemHatIpod
            36 -> binding.btnItemHatSheep
            37 -> binding.btnItemHatV
            38 -> binding.btnItemHatHeads
            50 -> return //basic
            51 -> return
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