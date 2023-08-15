package com.example.myapplication.HomeFunction.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.adapter.repeatTodo.HomeRepeatCategoryAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentRepeatTodoBinding
import com.example.myapplication.hideBottomNavigation
import java.time.LocalDate

class HomeRepeatTodoFragment : Fragment() {

    lateinit var binding: HomeFragmentRepeatTodoBinding
    private var bottomFlag = true
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //repeatTodo가져오기
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment_repeat_todo, container, false)
        hideBottomNavigation(bottomFlag, activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivHomeRepeatBack.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeRepeatTodoFragment_to_fragHome)
            bottomFlag = false
        }

        if (viewModel.categoryList.value?.isNotEmpty() == true) {
            //rv연결
            val cateAdapter = HomeRepeatCategoryAdapter(view)
            cateAdapter.dataSet = viewModel.categoryList.value!!
            cateAdapter.cateTodoSet = viewModel.cateTodoList.value
            cateAdapter.topFlag = viewModel.todoTopFlag.value!!

            cateAdapter.setItemClickListener(object :
                HomeRepeatCategoryAdapter.OnItemClickListener {
                override fun onClick(
                    v: View,
                    position: Int,
                    cate: String,
                    edt: EditText,
                    layout: LinearLayout
                ) {
                    if (edt.text.toString() != "") {

                        var todo = Todo(LocalDate.now(), viewModel.categoryList!!.value!![position], edt.text.toString(), false, "day")
                        viewModel.addTodo(position, todo, viewModel.todoTopFlag.value!!)

                        //서버 전송(POST)

                        viewModel.cateTodoList.observe(viewLifecycleOwner, Observer {

                            cateAdapter.cateTodoSet = viewModel.cateTodoList.value
                            binding.rvHomeRepeatTodo.post { cateAdapter!!.notifyDataSetChanged() }
                            Log.d("repeatTodo", "catetodo변경 확인")
                        })

                    }
                    edt.text.clear()
                    layout.isGone = true
                }
            })

            binding.rvHomeRepeatTodo.adapter = cateAdapter
            binding.rvHomeRepeatTodo.layoutManager = LinearLayoutManager(this.activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }
}