package com.example.myapplication.HomeFunction.adapter.todo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.HomeFunction.view.HomeViewpagerDdayFragment
import com.example.myapplication.HomeFunction.view.HomeViewpagerTimetableFragment
import com.example.myapplication.HomeFunction.view.HomeViewpagerTodoFragment

class HomeViewPagerAdapter(f: Fragment) : FragmentStateAdapter(f) {

    private val PAGENUMBER = 3

    override fun getItemCount(): Int {
        return PAGENUMBER
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeViewpagerDdayFragment.newInstance()
            1 -> HomeViewpagerTodoFragment.newInstance()
            else -> HomeViewpagerTimetableFragment.newInstance()
        }
    }
}