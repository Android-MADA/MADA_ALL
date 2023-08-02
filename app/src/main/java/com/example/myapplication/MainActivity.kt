package com.example.myapplication

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.Fragment.FragCalendar
import com.example.myapplication.Fragment.FragCustom
import com.example.myapplication.Fragment.FragDaily
import com.example.myapplication.Fragment.FragHome
import com.example.myapplication.Fragment.FragMy
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fl_con) as NavHostFragment
        val navController =navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}

fun hideBottomNavigation(bool : Boolean, activity: Activity?){
    val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    if(bool){
        bottomNavigation?.isGone = true
    }
    else {
        bottomNavigation?.isVisible = true
    }
}
