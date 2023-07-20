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
    val categoryAdapter = HomeCategoryAdapter(sampleCategoryArray)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_home_category, container, false)

        initArrayList()

        //onclick listener
        categoryAdapter.setItemClickListener(object: HomeCategoryAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //페이지 이동 + 데이터 전달
            }
        })
        binding.btnHomeCategory.setOnClickListener {
            //페이지 이동
        }
        binding.ivHomeCategoryBack.setOnClickListener {
            //페이지 이동
        }

        //rv 어댑터 연결
        val homeCategoryAdapter = HomeCategoryAdapter(sampleCategoryArray)
        binding.rvHomeCategory.adapter = homeCategoryAdapter
        binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)

        return binding.root
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

}