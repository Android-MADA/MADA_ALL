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
import com.example.myapplication.HomeFunction.Model.repeatTodo
import com.example.myapplication.HomeFunction.adapter.repeatTodo.RepeatCateListAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentRepeatTodoBinding
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.hideBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepeatTodoFragment : Fragment(){

    lateinit var binding: HomeFragmentRepeatTodoBinding
    private var bottomFlag = true
    private val viewModel: HomeViewModel by activityViewModels()



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

        //카테고리 데이터 가져와서 adapter 넣기
        viewModel.readActiveCate(false)
        viewModel.cateEntityList.observe(viewLifecycleOwner, Observer {
            val cateList = it as List<CateEntity>
            Log.d("cateList", cateList.toString())
            val mAdapter = RepeatCateListAdapter(view)
            mAdapter.viewModel = viewModel
            mAdapter.submitList(cateList)
            binding.rvHomeRepeatTodo.adapter = mAdapter
            binding.rvHomeRepeatTodo.layoutManager = LinearLayoutManager(this.requireActivity())

        })
//        //서버 연결
//        viewModel.getRepeatTodo()
//
//        viewModel.repeatList.observe(viewLifecycleOwner, Observer {
//                    if(cateAdapter != null){
//                        if(viewModel.repeatList.value!!.isEmpty() != true){
//                            cateAdapter!!.cateTodoSet = viewModel.repeatList.value
//                            binding.rvHomeRepeatTodo.post { cateAdapter!!.notifyDataSetChanged() }
//                        }
//                        //카테고리를 다 지우고 왔을 때
//                        else {
//                            cateAdapter = null
//                        }
//
//                    }
//                    else{
//                        if (viewModel.categoryList.value?.isNotEmpty() == true) {
//                            //rv연결
//                            attachAdapter(view)
//                }
//            }
//            Log.d("repeatTodo 데이터 확인 중", "cateTodoList observer 작동")
//        })

        binding.ivHomeRepeatBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeRepeatTodoFragment_to_fragHome)
            bottomFlag = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }

}