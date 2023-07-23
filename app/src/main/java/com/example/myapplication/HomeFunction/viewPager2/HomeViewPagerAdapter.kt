package com.example.myapplication.HomeFunction.viewPager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.HomeFunction.view.viewpager2.HomeViewpagerTimetableFragment
import com.example.myapplication.HomeFunction.view.viewpager2.HomeViewpagerTodoFragment

class HomeViewPagerAdapter(f: Fragment) : FragmentStateAdapter(f) {

    private val PAGENUMBER = 3

    override fun getItemCount(): Int {
        return PAGENUMBER
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeViewpagerTodoFragment.newInstance()
            else -> HomeViewpagerTimetableFragment.newInstance()
        }
    }
}