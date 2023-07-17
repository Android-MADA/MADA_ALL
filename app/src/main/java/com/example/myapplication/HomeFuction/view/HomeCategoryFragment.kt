package com.example.myapplication.HomeFuction.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.HomeFuction.category.HomeCategoryAdapter
import com.example.myapplication.HomeFuction.category.sampleCategoryData
import com.example.myapplication.databinding.HomeFragmentHomeCategoryBinding

class HomeCategoryFragment : Fragment() {

    lateinit var binding : HomeFragmentHomeCategoryBinding
    val sampleCategoryArray = ArrayList<sampleCategoryData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_home_category, container, false)

        initArrayList()

        val homeCategoryAdapter = HomeCategoryAdapter(sampleCategoryArray)
        binding.rvHomeCategory.adapter = homeCategoryAdapter
        binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)
        return binding.root
    }

    private fun initArrayList(){
        with(sampleCategoryArray){
            sampleCategoryArray.add(sampleCategoryData(223344, "공부", Color.parseColor("#405059")))
            sampleCategoryArray.add(sampleCategoryData(223344, "약속", Color.parseColor("#F0768C")))
            sampleCategoryArray.add(sampleCategoryData(223344, "잠", Color.parseColor("2AA1B7")))
            sampleCategoryArray.add(sampleCategoryData(223344, "친구만나기", Color.parseColor("#F8D141")))
            sampleCategoryArray.add(sampleCategoryData(223344, "휴대폰", Color.parseColor("#486DA3")))
        }
    }

}