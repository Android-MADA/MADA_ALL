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

    lateinit var binding : HomeFragmentRepeatTodoBinding
    private var bottomFlag = true
    private val viewModel : HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.classifyRepeatTodo()
        Log.d("Repeatarray", viewModel.repeatTodoList.value.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_repeat_todo, container, false)
        hideBottomNavigation(bottomFlag, activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //todolist변경, categoryList변경 시 repeatTpdpList 변경

        binding.ivHomeRepeatBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeRepeatTodoFragment_to_fragHome)
            bottomFlag = false
        }

        if(viewModel.categoryList.value?.isNotEmpty() == true){
            //rv연결
            val cateAdapter = HomeRepeatCategoryAdapter(viewModel.categoryList.value!!)
            cateAdapter.repeatDataSet = viewModel._repeatTodoList.value
            cateAdapter.cateTodoSet = viewModel._cateTodoList.value

            cateAdapter.setItemClickListener(object : HomeRepeatCategoryAdapter.OnItemClickListener{
                override fun onClick(
                    v: View,
                    position: Int,
                    cate: String,
                    edt : EditText,
                    layout: LinearLayout
                ) {
                    if(edt.text.toString() != ""){

                        var todo = Todo(LocalDate.now(), viewModel.categoryList!!.value!![position], edt.text.toString(), false, "day")

//                        viewModel.addRepeatTodo(position, todo)
//                        viewModel.addTodo(position, todo, viewModel.todoTopFlag.value!!)
//                        viewModel.updateTodoNum()
                        //서버 전송 후 todoList 받아오기

                        Log.d("addRepeatTodo", viewModel.repeatTodoList!!.value!![position].toString())

                        binding.rvHomeRepeatTodo.post {
                            cateAdapter!!.notifyDataSetChanged()
                        }
                    }
                    edt.text.clear()
                    layout.isGone = true
                }
            })

            binding.rvHomeRepeatTodo.adapter = cateAdapter
            binding.rvHomeRepeatTodo.layoutManager = LinearLayoutManager(this.activity)
        }

        viewModel.repeatTodoList.observe(viewLifecycleOwner, Observer {
            Log.d("viewmodelObserver-Repeat", "데이터 이동 확인")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }
}