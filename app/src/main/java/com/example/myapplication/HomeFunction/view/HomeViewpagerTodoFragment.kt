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
import com.example.myapplication.HomeFunction.Model.PostRequestTodo
import com.example.myapplication.HomeFunction.Model.PostRequestTodoCateId
import com.example.myapplication.HomeFunction.Model.PostResponseTodo
import com.example.myapplication.HomeFunction.adapter.todo.HomeCateListAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.HomeFunction.adapter.todo.HomeViewpager2CategoryAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentViewpagerTodoBinding
import com.example.myapplication.db.entity.CateEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewpagerTodoFragment : Fragment() {

    lateinit var binding: HomeFragmentViewpagerTodoBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private var cateAdapter: HomeViewpager2CategoryAdapter? = null

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
        viewModel.readActiveCate(true)
        viewModel.cateEntityList.observe(viewLifecycleOwner, Observer {
            val cateList = it as List<CateEntity>
            Log.d("cateList", cateList.toString())
            val mAdapter = HomeCateListAdapter()
            mAdapter.viewModel = viewModel
            mAdapter.submitList(cateList)
            binding.rvHomeCategory.adapter = mAdapter
            binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.requireActivity())

        })


//        viewModel.homeDate.observe(viewLifecycleOwner, Observer {
//            Log.d("viewpagerDate", "date 변경 감지")
//            viewModel.getCategory(viewModel.userToken)
//        })
//
//        viewModel.categoryList.observe(viewLifecycleOwner, Observer {
//            if (viewModel.categoryList.value!!.isNotEmpty()){
//                Log.d("viewpager cate", viewModel.categoryList.value.toString())
//                viewModel.getTodo(viewModel.userToken, viewModel.homeDate.value.toString())
//                if (cateAdapter != null) {
//                    if (viewModel.categoryList.value!!.isNotEmpty() == true) {
//                        Log.d("viewpager cate", "어댑터 연결 갱신")
//                        Log.d("viewpager cate", viewModel.todoList.value.toString())
//                    }
//                } else {
//                    if (viewModel.categoryList.value?.isNotEmpty() == true) {
//                        //rv연결
//                        attachCateAdapter()
//                    }
//                }
//            }
//        })
//
//
//
//        viewModel.cateTodoList.observe(viewLifecycleOwner, Observer {
//            if (viewModel.cateTodoList.value!!.isNotEmpty()) {
//                Log.d("viewpager todo", viewModel.todoCateList.toString())
//                if(cateAdapter != null){
//                    attachCateAdapter()
//                }
//            }
//        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("viewpagerView", "destoryView")
        cateAdapter = null
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeViewpagerTodoFragment()
    }

    private fun attachCateAdapter() {

        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

        cateAdapter = HomeViewpager2CategoryAdapter(null)
        cateAdapter!!.dataSet = viewModel.categoryList.value!!
        cateAdapter!!.cateTodoSet = viewModel.cateTodoList.value
        cateAdapter!!.topFlag = viewModel.todoTopFlag.value!!
        cateAdapter!!.viewModel = viewModel
        cateAdapter!!.completeFlag = viewModel.completeBottomFlag.value!!

        cateAdapter!!.setItemClickListener(object :
            HomeViewpager2CategoryAdapter.OnItemClickListener {
            override fun onClick(
                v: View,
                position: Int,
                cate: Int,
                edt: EditText,
                layout: LinearLayout
            ) {
                if (edt.text.toString() != "") {

                    var todo = PostRequestTodo(viewModel.homeDate.value.toString(), PostRequestTodoCateId(cate) , edt.text.toString(), false, "N", null, null, null, null, false, false, false, false)

                    api.addTodo(viewModel.userToken, todo).enqueue(object :
                        Callback<PostResponseTodo> {
                        override fun onResponse(

                            call: Call<PostResponseTodo>,
                            response: Response<PostResponseTodo>
                        ) {
                            if(response.isSuccessful){
                                Log.d("todo post", response.body()!!.data.toString())
//                                viewModel.addTodo(position, response.body()!!.data.Todo, viewModel.todoTopFlag.value!!)
                                viewModel.addTodo(position, response.body()!!.data.Todo, viewModel.todoTopFlag.value!!  )
                                binding.rvHomeCategory.post { cateAdapter!!.notifyDataSetChanged() }

                            }
                            else {
                                Log.d("todo post", "안드 쪽 오류")
                            }
                        }

                        override fun onFailure(call: Call<PostResponseTodo>, t: Throwable) {
                            Log.d("todo post", "서버 연결 실패")
                        }

                    })

                    viewModel.cateTodoList.observe(viewLifecycleOwner, Observer {

                        cateAdapter!!.cateTodoSet = viewModel.cateTodoList.value
                        binding.rvHomeCategory.post { cateAdapter!!.notifyDataSetChanged() }
                        Log.d(
                            "viewpagerTodo",
                            "catetodo변경 확인 ${viewModel.cateTodoList.toString()}"
                        )
                    })

                }
                edt.text.clear()
                layout.isGone = true
            }
        })

        binding.rvHomeCategory.adapter = cateAdapter
        binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)
    }

}