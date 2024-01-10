package com.mada.myapplication.CalenderFuntion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mada.myapplication.CalenderFuntion.Calendar.DdayAdapter
import com.mada.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.mada.myapplication.R
import com.mada.myapplication.databinding.CalendarDdayFragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalendarDdayFragment: Fragment() {
    lateinit var binding: CalendarDdayFragmentBinding
    private val CalendarViewModel: CalendarViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBootomNavigation(false)
        binding = CalendarDdayFragmentBinding.inflate(inflater, container, false)
        CalendarViewModel.getDdayDataArray { result ->
            when (result) {
                1 -> {
                    val adapter = DdayAdapter(CalendarViewModel,this)
                    var manager: RecyclerView.LayoutManager = LinearLayoutManager(this.requireActivity())
                    binding.ddayRecyclerView.layoutManager = manager
                    binding.ddayRecyclerView.adapter = adapter
                }
                2 -> {
                    Toast.makeText(context, "서버와의 통신 불안정", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeBase.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_calendarDday_to_fragCalendar)
        }
        binding.ddayAddBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("today", CalendarViewModel.todayDate())
            Navigation.findNavController(view).navigate(R.id.action_calendarDday_to_calendarAddDday,bundle)
        }

    }

    fun hideBootomNavigation(bool : Boolean){
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if(bool){
            bottomNavigation?.isGone = true
        }
        else {
            bottomNavigation?.isVisible = true
        }
    }
    public fun updateDdayData() {
        CalendarViewModel.getDdayDataArray { result ->
            when (result) {
                1 -> {
                    val adapter = DdayAdapter(CalendarViewModel,this)
                    var manager: RecyclerView.LayoutManager = GridLayoutManager(context,1)
                    binding.ddayRecyclerView.layoutManager = manager
                    binding.ddayRecyclerView.adapter = adapter
                }
                2 -> {
                    Toast.makeText(context, "서버와의 통신 불안정", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}