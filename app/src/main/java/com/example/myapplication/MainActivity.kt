package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Fragment.FragCalendar
import com.example.myapplication.Fragment.FragCustom
import com.example.myapplication.Fragment.FragDaily
import com.example.myapplication.Fragment.FragHome
import com.example.myapplication.Fragment.FragMy
import com.example.myapplication.HomeFuction.view.HomeCategoryFragment
import com.example.myapplication.HomeFuction.view.HomeTimeEditFragment
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.HomeFragmentHomeCategoryBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()
    }
    private fun initBottomNavigation() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_con, HomeTimeEditFragment() )
            .commitAllowingStateLoss()

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_con, FragHome())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.calendar -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_con, FragCalendar())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.my -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_con, FragMy())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.custom -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_con, FragCustom())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.daily -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_con, FragDaily())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
    }
}