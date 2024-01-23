package com.mada.myapplication.StartFuction
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.StartFuction.FragOnBoarding_5
import com.example.myapplication.StartFuction.FragOnBoarding_6

class OnBoardingAdapter(fa: FragmentActivity, private val count: Int) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        val index = getRealPosition(position)

        return when (index) {
            0 -> FragOnBoarding_1()
            1 -> FragOnBoarding_2()
            2 -> FragOnBoarding_3()
            3 -> FragOnBoarding_4()
            4 -> FragOnBoarding_5()
            else -> FragOnBoarding_6()
        }
    }

    private fun getRealPosition(position: Int): Int {
        return position % count
    }
}
