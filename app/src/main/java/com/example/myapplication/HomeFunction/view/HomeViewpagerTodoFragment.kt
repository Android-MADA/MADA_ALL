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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.HomeFunction.adapter.todo.HomeViewpager2CategoryAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentViewpagerTodoBinding
import java.time.LocalDate


class HomeViewpagerTodoFragment : Fragment() {

    lateinit var binding: HomeFragmentViewpagerTodoBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private var cateAdapter: HomeViewpager2CategoryAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_fragment_viewpager_todo,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.categoryList.value?.isNotEmpty() == true) {

            cateAdapter = HomeViewpager2CategoryAdapter()
            cateAdapter!!.dataSet = viewModel.categoryList.value!!
            cateAdapter!!.todoDataSet = viewModel._cateTodoList.value
            cateAdapter!!.completeFlag = viewModel.completeBottomFlag.value!!
            cateAdapter!!.topFlag = viewModel.todoTopFlag.value!!

            cateAdapter!!.setItemClickListener(object :
                HomeViewpager2CategoryAdapter.OnItemClickListener {
                override fun onClick(
                    v: View,
                    position: Int,
                    cate: String,
                    edt: EditText,
                    layout: LinearLayout
                ) {
                    if (edt.text.toString() != "") {
                        var todo = Todo(viewModel.homeDate.value!!, viewModel.categoryList!!.value!![position], edt.text.toString(), false, "N")
                        //서버에 데이터 보내서 catetodo 새로 받아오기
                    }
                    edt.text.clear()
                    layout.isGone = true
                }
            })
            binding.rvHomeCategory.adapter = cateAdapter
            binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)
        }

        //1. todo추가삭제수정 2.date 변경, 3. repeatTodo 변경으로 서버 연결로 catetodo변경시
        viewModel.cateTodoList.observe(viewLifecycleOwner, Observer {

            if (viewModel.categoryList.value?.isNotEmpty() == true) {
                cateAdapter!!.dataSet = viewModel.categoryList.value!!
                cateAdapter!!.todoDataSet = viewModel._cateTodoList.value
                binding.rvHomeCategory.post { cateAdapter!!.notifyDataSetChanged() }
            }
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