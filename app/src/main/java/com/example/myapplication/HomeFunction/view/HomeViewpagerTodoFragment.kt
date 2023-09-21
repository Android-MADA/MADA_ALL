package com.example.myapplication.HomeFunction.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.adapter.todo.HomeCateListAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentViewpagerTodoBinding
import com.example.myapplication.db.entity.CateEntity

class HomeViewpagerTodoFragment : Fragment() {

    lateinit var binding: HomeFragmentViewpagerTodoBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_viewpager_todo, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //카테고리 데이터 가져와서 adapter 넣기
        viewModel.readActiveCate(false)
        viewModel.cateEntityList.observe(viewLifecycleOwner, Observer {
            val cateList = it as List<CateEntity>
            Log.d("cateList", cateList.toString())
            val mAdapter = HomeCateListAdapter(binding.rvHomeCategory)
            mAdapter.viewModel = viewModel
            mAdapter.submitList(cateList)
            binding.rvHomeCategory.adapter = mAdapter
            binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.requireActivity())

        })

        viewModel.readQuitCate(true)
        viewModel.quitCateEntityList.observe(viewLifecycleOwner, Observer {
            val cateList = it as List<CateEntity>
            Log.d("cateList", cateList.toString())
            val mAdapter = HomeCateListAdapter(binding.rvHomeCategoryInactive)
            mAdapter.viewModel = viewModel
            mAdapter.submitList(cateList)
            binding.rvHomeCategoryInactive.adapter = mAdapter
            binding.rvHomeCategoryInactive.layoutManager = LinearLayoutManager(this.requireActivity())
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("viewpagerView", "destoryView")
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeViewpagerTodoFragment()
    }
}