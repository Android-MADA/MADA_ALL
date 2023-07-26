package com.example.myapplication.HomeFunction.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.HomeFunction.category.HomeCategoryAdapter
import com.example.myapplication.HomeFunction.category.sampleCategoryData
import com.example.myapplication.databinding.HomeFragmentCategoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeCategoryFragment : Fragment() {

    lateinit var binding : HomeFragmentCategoryBinding
    val sampleCategoryArray = ArrayList<sampleCategoryData>()
    val categoryAdapter = HomeCategoryAdapter(sampleCategoryArray)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArrayList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_category, container, false)
        hideBootomNavigation(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivHomeCategoryBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_fragHome)
        }

        binding.btnHomeCategory.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_categoryAddFragment)
        }

        categoryAdapter.setItemClickListener(object: HomeCategoryAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //페이지 이동 + 데이터 전달
                Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_categoryAddFragment)
            }
        })

        binding.rvHomeCategory.adapter = categoryAdapter
        binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBootomNavigation(false)
    }



    private fun initArrayList(){
        with(sampleCategoryArray){
            sampleCategoryArray.add(sampleCategoryData( R.drawable.ic_home_cate_study, "공부", resources.getColor(R.color.sub5)))
            sampleCategoryArray.add(sampleCategoryData(R.drawable.ic_home_cate_plan, "약속", Color.parseColor("#F0768C")))
            sampleCategoryArray.add(sampleCategoryData(R.drawable.ic_home_cate_study, "잠", resources.getColor(R.color.point_main)))
            sampleCategoryArray.add(sampleCategoryData(R.drawable.ic_home_cate_study, "친구만나기", Color.parseColor("#F8D141")))
            sampleCategoryArray.add(sampleCategoryData(R.drawable.ic_home_cate_study, "휴대폰", Color.parseColor("#486DA3")))
        }
    }

    fun hideBootomNavigation(bool : Boolean){
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if(bool){
            bottomNavigation?.isGone = true
        }
        else {
            bottomNavigation?.isVisible = true
        }
    }

}