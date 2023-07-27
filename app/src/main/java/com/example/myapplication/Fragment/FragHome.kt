package com.example.myapplication.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.HomeFunction.category.sampleCategoryData
import com.example.myapplication.HomeFunction.viewPager2.HomeViewpager2CategoryAdapter
import com.example.myapplication.HomeFunction.viewPager2.HomeViewPagerAdapter
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeCateData
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeTodoData
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentBinding

class FragHome : Fragment() {

    lateinit var binding: HomeFragmentBinding
    private var myAdapter : HomeViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val homeViewPager = binding.homeViewpager2
        val homeIndicator = binding.homeIndicator

        //date clicklistener -> 추가 예정
        binding.tvHomeCalendar.setOnClickListener {

        }

        //viewpager 연결, indicator 연결
        myAdapter = HomeViewPagerAdapter(this@FragHome)
        homeViewPager.adapter = myAdapter

        homeIndicator.setViewPager(homeViewPager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarHome.inflateMenu(R.menu.home_menu)
        binding.toolbarHome.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.home_menu_timetable -> {
                    Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeTimetableFragment)
                    true
                }
                R.id.home_menu_category -> {
                    Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeCategoryFragment)
                    true
                }
                R.id.home_menu_repeatTodo -> {
                    Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeRepeatTodoFragment)
                    true
                }
                else -> false
            }
        }

    }



}