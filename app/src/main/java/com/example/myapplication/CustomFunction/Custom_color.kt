package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.CustomColorBinding
import com.example.myapplication.databinding.FragCustomBinding


class custom_color : Fragment() {

    lateinit var binding: CustomColorBinding
    lateinit var fragbinding: FragCustomBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomColorBinding.inflate(inflater, container, false)
        fragbinding = FragCustomBinding.inflate(inflater)
        // val onClickbtncolorInstance = onClickbtncolor()
        var btn_pushed = true

        var btncolorbasic = binding.btnColorBasic
        var btncolorblue = binding.btnColorBlue
        var btncolorbluepurple = binding.btnColorBluepurple
        var btncolorgreen = binding.btnColorGreen
        var btncolororange = binding.btnColorOrange
        var btncolorpink = binding.btnColorPink
        var btncolorpurple = binding.btnColorPurple
        var btncolorRblue = binding.btnColorRblue
        var btncoloryellow = binding.btnColorYellow
        btncolorbasic.setOnClickListener{
            Log.d("dsa","fsaf")
            if (btn_pushed == true){
                binding.btnColorBasic.setImageResource(R.drawable.color_basic_choice);
                btn_pushed = false;

            }else {
                binding.btnColorBasic.setImageResource(R.drawable.color_basic);
                btn_pushed = true;
            }
            // binding.btnColorBasic.setImageResource(R.drawable.color_basic_choice)
            // onClickbtncolorInstance.onClickbtncolor("btncolorbasic")

        }
        btncolorblue.setOnClickListener{
            binding.btnColorBlue.setImageResource(R.drawable.color_blue_choice)
            fragbinding.customRamdi.setImageResource(R.drawable.c_ramdyb)
        }
        btncolorRblue.setOnClickListener{
            binding.btnColorRblue.setImageResource(R.drawable.color_rblue_choice)
            fragbinding.customRamdi.setImageResource(R.drawable.c_ramdyrb)
        }
        btncolorbluepurple.setOnClickListener {
            binding.btnColorBluepurple.setImageResource(R.drawable.color_bluepurple_choice)
            fragbinding.customRamdi.setImageResource(R.drawable.c_ramdybp)
        }

        btncolorgreen.setOnClickListener{
            binding.btnColorGreen.setImageResource(R.drawable.color_green_choice)
            fragbinding.customRamdi.setImageResource(R.drawable.c_ramdyg)
        }

        btncolororange.setOnClickListener{
            binding.btnColorOrange.setImageResource(R.drawable.color_orange_choice)
            fragbinding.customRamdi.setImageResource(R.drawable.c_ramdyo)
        }

        btncolorpink.setOnClickListener{
            binding.btnColorPink.setImageResource(R.drawable.color_pink_choice)
            fragbinding.customRamdi.setImageResource(R.drawable.c_ramdyp)
        }
        btncolorpurple.setOnClickListener{
            binding.btnColorPurple.setImageResource(R.drawable.color_purple_choice)
            fragbinding.customRamdi.setImageResource(R.drawable.c_ramdypn)
        }
        btncoloryellow.setOnClickListener{
            binding.btnColorYellow.setImageResource(R.drawable.color_yellow_choice)
            fragbinding.customRamdi.setImageResource(R.drawable.c_ramdyy)
        }


        return binding.root
    }






}