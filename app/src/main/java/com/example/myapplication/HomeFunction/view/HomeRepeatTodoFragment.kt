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
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.repeatTodo.HomeRepeatCategoryAdapter
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeCateData
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeTodoData
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentRepeatTodoBinding
import com.example.myapplication.hideBottomNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeRepeatTodoFragment : Fragment() {

    lateinit var binding : HomeFragmentRepeatTodoBinding
    private var bottomFlag = true

    private val cateList = ArrayList<SampleHomeCateData>()
    private var todo1List = ArrayList<SampleHomeTodoData>()
    private var todo2List = ArrayList<SampleHomeTodoData>()
    private var todo3List = ArrayList<SampleHomeTodoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArrayList()
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

        binding.ivHomeRepeatBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeRepeatTodoFragment_to_fragHome)
            bottomFlag = false
        }

        //rv연결
        val cateAdapter = HomeRepeatCategoryAdapter(cateList)

        cateAdapter.setItemClickListener(object : HomeRepeatCategoryAdapter.OnItemClickListener{
            override fun onClick(
                v: View,
                position: Int,
                cate: String,
                edt : EditText,
                layout: LinearLayout
            ) {
                if(edt.text.toString() != ""){
                    when(cate){
                        cateList[0].cateName -> {cateList[0].todoList.add(SampleHomeTodoData("약속",edt.text.toString(), false))}
                        cateList[1].cateName -> {cateList[1].todoList.add(SampleHomeTodoData("운동",edt.text.toString(), false))}
                        cateList[2].cateName -> {cateList[2].todoList.add(SampleHomeTodoData("공부",edt.text.toString(),  false))}
                        else -> {}
                    }
                    cateAdapter!!.notifyDataSetChanged()
                    Log.d("edt", cateList[0].todoList.toString())
                }
                edt.text.clear()
                layout.isGone = true
            }
        })

        binding.rvHomeRepeatTodo.adapter = cateAdapter
        binding.rvHomeRepeatTodo.layoutManager = LinearLayoutManager(this.activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }

    fun initArrayList(){
        // cate1 todo init
        with(todo1List){
            this.add(SampleHomeTodoData("약속","친구랑 저녁 약속!", false))
            this.add(SampleHomeTodoData("약속","스터디 회의", true))
        }
        // cate2 todo init
        with(todo2List){
            this.add(SampleHomeTodoData("운동","런데이 하기", false))
        }
        //cate3 todo init
        with(todo3List){
            this.add(SampleHomeTodoData("공부","서점에 전공 책 사러 가기", false))
            this.add(SampleHomeTodoData("공부","과제 제출하기", false))
        }
        // total array init
        with(cateList){
            this.add(SampleHomeCateData(com.example.myapplication.R.drawable.ic_home_cate_plan, "약속", todo1List))
            this.add(SampleHomeCateData(com.example.myapplication.R.drawable.ic_home_cate_health, "운동", todo2List))
            this.add(SampleHomeCateData(com.example.myapplication.R.drawable.ic_home_cate_study, "공부", todo3List))
        }
    }
}