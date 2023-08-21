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
import com.example.myapplication.HomeFunction.Model.PostRequestTodo
import com.example.myapplication.HomeFunction.Model.PostRequestTodoCateId
import com.example.myapplication.HomeFunction.Model.PostResponseTodo
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.adapter.repeatTodo.HomeRepeatCategoryAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentRepeatTodoBinding
import com.example.myapplication.hideBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepeatTodoFragment : Fragment() {

    lateinit var binding: HomeFragmentRepeatTodoBinding
    private var bottomFlag = true
    private val viewModel: HomeViewModel by activityViewModels()
    private var cateAdapter : HomeRepeatCategoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        //서버 연결
        viewModel.getRepeatTodo()

        viewModel.repeatList.observe(viewLifecycleOwner, Observer {
            if(cateAdapter != null){
                if(viewModel.repeatList.value!!.isEmpty() != true){
                    cateAdapter!!.cateTodoSet = viewModel.repeatList.value
                    cateAdapter!!.notifyDataSetChanged()
                }
                //카테고리를 다 지우고 왔을 때
                else {
                    cateAdapter = null
                }

            }
            else{
                if (viewModel.categoryList.value?.isNotEmpty() == true) {
                    //rv연결
                    attachAdapter(view)
                }
            }
            Log.d("repeatTodo 데이터 확인 중", "cateTodoList observer 작동")
        })

        binding.ivHomeRepeatBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeRepeatTodoFragment_to_fragHome)
            bottomFlag = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }

    private fun attachAdapter(view : View) {

        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

        cateAdapter = HomeRepeatCategoryAdapter(view)
        cateAdapter!!.dataSet = viewModel.categoryList.value!!
        cateAdapter!!.cateTodoSet = viewModel.repeatList.value
        cateAdapter!!.topFlag = viewModel.todoTopFlag.value!!
        cateAdapter!!.viewModel = viewModel
        cateAdapter!!.api = api

        cateAdapter!!.setItemClickListener(object :
            HomeRepeatCategoryAdapter.OnItemClickListener {
            override fun onClick(
                v: View,
                position: Int,
                cate: Int,
                edt: EditText,
                layout: LinearLayout
            ) {
                if (edt.text.toString() != "") {
//                    var endDate = "${viewModel.homeDate.value!!.year + 1}-${viewModel.homeDate.value!!.month}-${viewModel.homeDate.value!!.dayOfMonth}"
//                    //var todo = PostRequestTodo(viewModel.homeDate.value.toString(), PostRequestTodoCateId(cate) , edt.text.toString(), false, "day", null, null, viewModel.homeDate.value.toString(), endDate, false, false, false, false)
//                    var todo = PostRequestTodo(viewModel.homeDate.value.toString(), PostRequestTodoCateId(cate) , edt.text.toString(), false, "N", null, null, null, null, false, false, false, false)
//
//                    api.addTodo(viewModel.userToken, todo).enqueue(object : Callback<PostResponseTodo> {
//                        override fun onResponse(
//                            call: Call<PostResponseTodo>,
//                            response: Response<PostResponseTodo>
//                        ) {
//                            if(response.isSuccessful){
//                                Log.d("todo post", response.body()!!.data.toString())
//                                viewModel.addTodo(position, response.body()!!.data.Todo)
//                                cateAdapter!!.notifyDataSetChanged()
//                            }
//                            else {
//                                Log.d("todo post", "안드 쪽 오류")
//                            }
//                        }
//
//                        override fun onFailure(call: Call<PostResponseTodo>, t: Throwable) {
//                            Log.d("todo post", "서버 연결 실패")
//                        }
//
//                    })
                }
                edt.text.clear()
                layout.isGone = true
            }
        })

        binding.rvHomeRepeatTodo.adapter = cateAdapter
        binding.rvHomeRepeatTodo.layoutManager = LinearLayoutManager(this.activity)
    }
}