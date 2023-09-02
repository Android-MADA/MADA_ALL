package com.example.myapplication.StartFuction

import android.os.Bundle
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.relex.circleindicator.CircleIndicator3
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.OnboardingBinding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: OnboardingBinding
    private lateinit var mPager: ViewPager2
    private lateinit var pagerAdapter: FragmentStateAdapter
    private lateinit var mIndicator: CircleIndicator3
    private var numPage = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewPager2 초기화
        mPager = binding.onViewpager2
        mPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        // Adapter 초기화
        pagerAdapter = OnBoardingAdapter(this, numPage)
        mPager.adapter = pagerAdapter
        // Indicator 초기화
        mIndicator = binding.onIndicator
        mIndicator.setViewPager(mPager)
        mIndicator.createIndicators(numPage, 0)

        mPager.setCurrentItem(1) // 시작 지점
        mPager.offscreenPageLimit = 4 // 최대 이미지 수

        // 페이지 변경 이벤트 리스너 등록
        mPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position)
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Indicator 업데이트
                mIndicator.animatePageSelected(position % numPage)
            }
        })
    }
}
