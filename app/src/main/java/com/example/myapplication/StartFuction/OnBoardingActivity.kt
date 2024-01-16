package com.example.myapplication.StartFuction

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import me.relex.circleindicator.CircleIndicator3
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.OnboardingBinding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: OnboardingBinding
    private lateinit var mPager: ViewPager2
    private lateinit var mIndicator: CircleIndicator3
    private var numPage = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [마다 시작하기] 버튼 클릭 시
        binding.onStartBtn.setOnClickListener {
            val intent = Intent(this, Splash2Activity::class.java)
            startActivity(intent)
            finish()
        }

        // ViewPager2, Adapter, Indicator 초기화
        mPager = binding.onViewpager2
        mPager.setCurrentItem(0) // 시작 지점
        mPager.offscreenPageLimit = 6 // 최대 이미지 수
        mPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 스크롤 방향
        mPager.adapter = OnBoardingAdapter(this, numPage) // 자체 어댑터 연결
        mIndicator = binding.onIndicator
        mIndicator.setViewPager(mPager)
        mIndicator.createIndicators(numPage, 0)

        // [건너뛰기] 버튼 클릭 시
        binding.onSkipBtn.setOnClickListener {
            mPager.setCurrentItem(5)
            mIndicator.setViewPager(mPager)
        }


        // 페이지 변경 이벤트 리스너 등록
        mPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                // 스크롤이 멈추면 (positionOffsetPixels: 현재 페이지와 다음 페이지 사이의 픽셀 거리)
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position)
                }
                // on4 마지막 페이지 특이설정
                if (position == 5) {
                    binding.onStartBtn.visibility = View.VISIBLE
                    binding.onSkipBtn.visibility = View.INVISIBLE
                    binding.onIndicator.visibility = View.INVISIBLE
                    binding.onFramelayout.setBackgroundColor(Color.parseColor("#28374C"))
                    binding.onLinearlayout.setBackgroundColor(Color.parseColor("#28374C"))
                }
                // on1,2,3 다시 돌아오면 기본값으로 복구
                if (position == 0 || position == 1 || position == 2 || position == 3 || position == 4){
                    binding.onStartBtn.visibility = View.INVISIBLE
                    binding.onSkipBtn.visibility = View.VISIBLE
                    binding.onIndicator.visibility = View.VISIBLE
                    binding.onFramelayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding.onLinearlayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
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
