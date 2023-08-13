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
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.HomeFunction.viewPager2.HomeViewpager2CategoryAdapter
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeCateData
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeTodoData
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentViewpagerTodoBinding
import java.time.LocalDate


class HomeViewpagerTodoFragment : Fragment() {

    lateinit var binding : HomeFragmentViewpagerTodoBinding
    private val viewModel : HomeViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("array", viewModel.categoryList.value.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_viewpager_todo, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(viewModel.categoryList.value?.isNotEmpty() == true){

            val cateAdapter = HomeViewpager2CategoryAdapter(viewModel.categoryList.value!!)
            cateAdapter.todoDataSet = viewModel._cateTodoList.value

            cateAdapter?.setItemClickListener(object : HomeViewpager2CategoryAdapter.OnItemClickListener{
                override fun onClick(
                    v: View,
                    position: Int,
                    cate: String,
                    edt : EditText,
                    layout: LinearLayout
                ) {
                    if(edt.text.toString() != ""){
                        var todo = Todo(LocalDate.now(), viewModel.categoryList!!.value!![position], edt.text.toString(), false, "N")
                        viewModel.addTodo(position, todo)
                        Log.d("addTodo", viewModel.cateTodoList!!.value!![position].toString())
                        viewModel.cateTodoList.observe(viewLifecycleOwner, Observer {
                            Log.d("viewmodelObserver", "데이터 이동 확인")
                            cateAdapter.todoDataSet = viewModel._cateTodoList.value
                            Log.d("viewmodelObserver", cateAdapter.todoDataSet.toString())
                            //cateAdapter!!.todoAdapter?.notifyDataSetChanged()
                        })
                        binding.rvHomeCategory.post {
                            cateAdapter!!.notifyDataSetChanged()
                        }
                    }
                    edt.text.clear()
                    layout.isGone = true
                }
            })

            binding.rvHomeCategory.adapter = cateAdapter
            binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)


        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeViewpagerTodoFragment()
    }

}