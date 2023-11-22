package com.example.myapplication.HomeFunction.adapter.todo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPagerAdapter(f: Fragment) : FragmentStateAdapter(f) {

    private val PAGENUMBER = 1

    override fun getItemCount(): Int {
        return PAGENUMBER
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeViewpagerTodoFragment.newInstance()
            else -> HomeViewpagerTodoFragment.newInstance()
        }
    }
}