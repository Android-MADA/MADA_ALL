package com.example.myapplication.HomeFuction.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.HomeFuction.category.HomeCateIconAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentCategoryAddBinding

class CategoryAddFragment : Fragment() {

    val cateIconArray = ArrayList<Drawable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : HomeFragmentCategoryAddBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_category_add, container, false)
        val ivIcon = binding.ivHomeCateIcon
        val iconAdapter = HomeCateIconAdapter(cateIconArray)

        initArrayList()

        val rvIcon = binding.rvHomeCateIcon
        var listManager = GridLayoutManager(this.activity, 6)
        iconAdapter.setItemClickListener(object: HomeCateIconAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                ivIcon.setImageDrawable(cateIconArray[position])
                rvIcon.isGone = true
            }
        })

        var recyclerList = rvIcon.apply {
            setHasFixedSize(true)
            layoutManager = listManager
            adapter = iconAdapter

        }

        ivIcon.setOnClickListener {
            if(rvIcon.isVisible){
                rvIcon.isGone = true
            }
            else {
                rvIcon.isVisible = true
            }
        }


        //binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)
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

}