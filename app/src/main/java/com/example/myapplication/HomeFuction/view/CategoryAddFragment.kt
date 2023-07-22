package com.example.myapplication.HomeFuction.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.ViewGroupUtils
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.HomeFuction.category.HomeCateColorAdapter
import com.example.myapplication.HomeFuction.category.HomeCateIconAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentCategoryAddBinding

class CategoryAddFragment : Fragment() {

    val cateIconArray = ArrayList<Drawable>()
    val cateColorArray = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArrayList()
        initColorArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : HomeFragmentCategoryAddBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_category_add, container, false)
        val ivIcon = binding.ivHomeCateIcon
        val ivColor = binding.ivHomeCateColor
        val ivBack = binding.ivHomeCateAddBack
        val iconAdapter = HomeCateIconAdapter(cateIconArray)
        val colorAdapter = HomeCateColorAdapter(cateColorArray)
        val btnSave = binding.btnHomeCateAddSave

        val rvIcon = binding.rvHomeCateIcon
        val rvColor = binding.rvHomeCateColor

        var iconListManager = GridLayoutManager(this.activity, 6)
        var colorListManager = GridLayoutManager(this.activity, 6)

        //clicklistener
        iconAdapter.setItemClickListener(object: HomeCateIconAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                ivIcon.setImageDrawable(cateIconArray[position])
                rvIcon.isGone = true
            }
        })

        ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment2)
        }

        btnSave.setOnClickListener {
            findNavController().navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment2)
        }

        colorAdapter.setItemClickListener(object: HomeCateColorAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                ivColor.imageTintList = ColorStateList.valueOf(cateColorArray[position])
                rvColor.isGone = true
            }
        })

        var iconRecyclerList = rvIcon.apply {
            setHasFixedSize(true)
            layoutManager = iconListManager
            adapter = iconAdapter

        }
        var colorRecyclerList = rvColor.apply{
            setHasFixedSize(true)
            layoutManager = colorListManager
            adapter = colorAdapter
        }

        ivIcon.setOnClickListener {
            if(rvIcon.isVisible){
                rvIcon.isGone = true
            }
            else {
                rvIcon.isVisible = true
            }
        }

        ivColor.setOnClickListener{
            if(rvColor.isVisible){
                rvColor.isGone = true
            }
            else {
                rvColor.isVisible = true
            }
        }

        return binding.root
    }

    private fun initArrayList(){
        with(cateIconArray){
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_meal1))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_meal2))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_chat1))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_health))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_phone))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_rest))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_heart))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_study))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_laptop))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_music))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_lightup))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_lightout))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_pen))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_burn))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_plan))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_work))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_mic))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_sony))
        }
    }

    private fun initColorArray(){
        with(cateColorArray){
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub5))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.main))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub4))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub6))
            cateColorArray.add(Color.parseColor("#FDA4B4"))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub3))
            cateColorArray.add(Color.parseColor("#D4ECF1"))
            cateColorArray.add(Color.parseColor("#7FC7D4"))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.point_main))
            cateColorArray.add(Color.parseColor("#FDF3CF"))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub1))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub2))

        }
    }

}