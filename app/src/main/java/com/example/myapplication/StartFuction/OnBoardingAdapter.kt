package com.example.myapplication.StartFuction
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingAdapter(fa: FragmentActivity, private val count: Int) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        val index = getRealPosition(position)

        return when (index) {
            0 -> FragOnBoarding_1()
            1 -> FragOnBoarding_2()
            2 -> FragOnBoarding_3()
            else -> FragOnBoarding_4()
        }
    }

    private fun getRealPosition(position: Int): Int {
        return position % count
    }
}
