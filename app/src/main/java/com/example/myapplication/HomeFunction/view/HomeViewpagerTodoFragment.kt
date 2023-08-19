package com.example.myapplication.HomeFunction.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.adapter.repeatTodo.HomeRepeatCategoryAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.HomeFunction.adapter.todo.HomeViewpager2CategoryAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentViewpagerTodoBinding
import java.time.LocalDate


class HomeViewpagerTodoFragment : Fragment() {

    lateinit var binding: HomeFragmentViewpagerTodoBinding
    private val viewModel: HomeViewModel by activityViewModels()
    var cateAdapter: HomeViewpager2CategoryAdapter? = null
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

        //date 변경 감지 확인 코드 시작

        viewModel.homeDate.observe(viewLifecycleOwner, Observer {
            Log.d("viewpagerDate", "date 변경 감지")
        })

        //date 변경 감지 확인 코드 끝
        if (viewModel.categoryList.value?.isNotEmpty() == true) {
            //rv연결
            cateAdapter = HomeViewpager2CategoryAdapter()
            cateAdapter!!.dataSet = viewModel.categoryList.value!!
            cateAdapter!!.cateTodoSet = viewModel.cateTodoList.value
            cateAdapter!!.topFlag = viewModel.todoTopFlag.value!!
            cateAdapter!!.viewModel = viewModel

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

//                        var todo = Todo(
//                            viewModel.categoryList!!.value!![position],
//                            edt.text.toString(),
//                            false,
//                            "N"
//                        )
                        //var todo = Todo( LocalDate.now(), viewModel.categoryList!!.value!![position], edt.text.toString(), false, "N", null, null, null)
                        //viewModel.addTodo(position, todo, viewModel.todoTopFlag.value!!)

                        //서버 전송(POST)

                        viewModel.cateTodoList.observe(viewLifecycleOwner, Observer {

                            cateAdapter!!.cateTodoSet = viewModel.cateTodoList.value
                            binding.rvHomeCategory.post { cateAdapter!!.notifyDataSetChanged() }
                            Log.d(
                                "viewpagerTodo",
                                "catetodo변경 확인 ${viewModel.cateTodoList.toString()}"
                            )
                            viewModel.updateCompleteTodo()
                            viewModel.updateTodoNum()
                        })

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

//            if (viewModel.categoryList.value?.isNotEmpty() == true) {
//                cateAdapter!!.dataSet = viewModel.categoryList.value!!
//                cateAdapter!!.todoDataSet = viewModel._cateTodoList.value
//                binding.rvHomeCategory.post { cateAdapter!!.notifyDataSetChanged() }
//            }
            Log.d("viewpagerepeatTodo", "catetodo변경 확인")
        })
        viewModel.categoryList.observe(viewLifecycleOwner, Observer {

            if (viewModel.categoryList.value?.isNotEmpty() == true) {
                //rv연결
                cateAdapter = HomeViewpager2CategoryAdapter()
                cateAdapter!!.dataSet = viewModel.categoryList.value!!
                cateAdapter!!.cateTodoSet = viewModel.cateTodoList.value
                cateAdapter!!.topFlag = viewModel.todoTopFlag.value!!
                cateAdapter!!.viewModel = viewModel

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

//                            var todo = Todo(
//                                viewModel.categoryList!!.value!![position],
//                                edt.text.toString(),
//                                false,
//                                "N"
//                            )
                            //var todo = Todo( LocalDate.now(), viewModel.categoryList!!.value!![position], edt.text.toString(), false, "N", null, null, null)
                            //viewModel.addTodo(position, todo, viewModel.todoTopFlag.value!!)

                            //서버 전송(POST)

                            viewModel.cateTodoList.observe(viewLifecycleOwner, Observer {

                                cateAdapter!!.cateTodoSet = viewModel.cateTodoList.value
                                binding.rvHomeCategory.post { cateAdapter!!.notifyDataSetChanged() }
                                Log.d(
                                    "viewpagerTodo",
                                    "catetodo변경 확인 ${viewModel.cateTodoList.toString()}"
                                )
                                viewModel.updateCompleteTodo()
                                viewModel.updateTodoNum()
                            })

                        }
                        edt.text.clear()
                        layout.isGone = true
                    }
                })

                binding.rvHomeCategory.adapter = cateAdapter
                binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)
            }
                Log.d("viewpagerCate", cateAdapter?.dataSet.toString())
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