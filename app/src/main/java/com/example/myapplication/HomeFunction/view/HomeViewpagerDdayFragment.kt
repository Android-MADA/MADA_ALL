package com.example.myapplication.HomeFunction.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Calendar.DdayAdapter
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.databinding.HomeFragmentViewpagerDdayBinding

class HomeViewpagerDdayFragment : Fragment() {

    lateinit var binding : HomeFragmentViewpagerDdayBinding
    private val CalendarViewModel: CalendarViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentViewpagerDdayBinding.inflate(inflater, container, false)/*
        CalendarViewModel.getDdayDataArray { result ->
            when (result) {
                1 -> {
                    val adapter = DdayAdapter(CalendarViewModel,this)
                    var manager: RecyclerView.LayoutManager = LinearLayoutManager(this.requireActivity())
                    binding.ddayRecyclerview.layoutManager = manager
                    binding.ddayRecyclerview.adapter = adapter
                }
                2 -> {
                    Toast.makeText(context, "서버와의 통신 불안정", Toast.LENGTH_SHORT).show()
                }
            }
        }*/

        return binding.root
    }/*
    public fun updateDdayData() {
        CalendarViewModel.getDdayDataArray { result ->
            when (result) {
                1 -> {
                    val adapter = DdayAdapter(CalendarViewModel,this)
                    var manager: RecyclerView.LayoutManager = GridLayoutManager(context,1)
                    binding.ddayRecyclerview.layoutManager = manager
                    binding.ddayRecyclerview.adapter = adapter
                }
                2 -> {
                    Toast.makeText(context, "서버와의 통신 불안정", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeViewpagerDdayFragment()
    }



}