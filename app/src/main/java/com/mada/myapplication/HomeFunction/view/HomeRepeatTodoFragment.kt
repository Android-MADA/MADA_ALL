package com.mada.myapplication.HomeFunction.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mada.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.mada.myapplication.HomeFunction.adapter.repeatTodo.RepeatCateListAdapter
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.R
import com.mada.myapplication.databinding.HomeFragmentRepeatTodoBinding
import com.mada.myapplication.databinding.TodoRepeatLayoutBinding
import com.mada.myapplication.db.entity.CateEntity
import com.mada.myapplication.getRepeatTodo
import com.mada.myapplication.hideBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepeatTodoFragment : Fragment(){

    //lateinit var binding: HomeFragmentRepeatTodoBinding
    lateinit var binding : TodoRepeatLayoutBinding
    private var bottomFlag = true
    private val viewModel: HomeViewModel by activityViewModels()
    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
    private val calendarViewModel : CalendarViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.todo_repeat_layout, container, false)
        hideBottomNavigation(bottomFlag, activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //카테고리 데이터 가져와서 adapter 넣기
        viewModel.readActiveCate(false)
        viewModel.deleteAllRepeatTodo()
        getRepeatTodo(api, viewModel, context = this.requireActivity())

        viewModel.cateEntityList.observe(viewLifecycleOwner, Observer {
            val cateList = it as List<CateEntity>
            Log.d("cateList", cateList.toString())
            val mAdapter = RepeatCateListAdapter(view, requireFragmentManager(), this.requireActivity())
            mAdapter.viewModel = viewModel
            mAdapter.calendarViewModel = calendarViewModel
            mAdapter.submitList(cateList)
            binding.repeatRv.adapter = mAdapter
            binding.repeatRv.layoutManager = LinearLayoutManager(this.requireActivity())

        })

        binding.repeatBackIv.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeRepeatTodoFragment_to_fragHome)
            bottomFlag = false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }

}