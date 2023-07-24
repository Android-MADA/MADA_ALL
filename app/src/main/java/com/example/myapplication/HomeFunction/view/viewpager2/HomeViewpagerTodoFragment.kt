package com.example.myapplication.HomeFunction.view.viewpager2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.viewPager2.HomeViewpager2CategoryAdapter
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeCateData
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeTodoData
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentViewpagerTodoBinding


class HomeViewpagerTodoFragment : Fragment() {

    lateinit var binding : HomeFragmentViewpagerTodoBinding
    private var cateAdapter : HomeViewpager2CategoryAdapter? = null
    private var cateList = ArrayList<SampleHomeCateData>()
    private var todo1List = ArrayList<SampleHomeTodoData>()
    private var todo2List = ArrayList<SampleHomeTodoData>()
    private var todo3List = ArrayList<SampleHomeTodoData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArrayList()
        Log.d("array", cateList.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_viewpager_todo, container, false)

        //rv 연결
        cateAdapter = HomeViewpager2CategoryAdapter(cateList)
        binding.rvHomeCategory.adapter = cateAdapter
        binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeViewpagerTodoFragment()
    }

    fun initArrayList(){
        // cate1 todo init
        with(todo1List){
            this.add(SampleHomeTodoData("친구랑 저녁 약속!", false))
            this.add(SampleHomeTodoData("스터디 회의", true))
        }
        // cate2 todo init
        with(todo2List){
            this.add(SampleHomeTodoData("런데이 하기", false))
        }
        //cate3 todo init
        with(todo3List){
            this.add(SampleHomeTodoData("서점에 전공 책 사러 가기", false))
            this.add(SampleHomeTodoData("과제 제출하기", false))
        }
        // total array init
        with(cateList){
            this.add(SampleHomeCateData(R.drawable.ic_home_cate_plan, "약속", todo1List))
            this.add(SampleHomeCateData(R.drawable.ic_home_cate_health, "운동", todo2List))
            this.add(SampleHomeCateData(R.drawable.ic_home_cate_study, "공부", todo3List))
        }
    }
}