package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Fragment.FragHome
import com.example.myapplication.HomeFunction.adapter.todo.HomeViewPagerAdapter
import com.example.myapplication.databinding.HomeFragmentBinding
import com.example.myapplication.databinding.MyRecordDayBinding

class MyRecordDayActivity : AppCompatActivity() {
    private lateinit var binding: MyRecordDayBinding
    lateinit var homeFragmentBinding: HomeFragmentBinding
    private var myAdapter : HomeViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyRecordDayBinding.inflate(layoutInflater)
        homeFragmentBinding = HomeFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        // 홈 투두 받아오기
        val homeViewPager = homeFragmentBinding.homeViewpager2
        myAdapter = HomeViewPagerAdapter(FragHome())
        homeViewPager.adapter = myAdapter

    }
}