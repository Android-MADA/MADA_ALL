package com.example.myapplication.HomeFunction.viewModel

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.Icon
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.R
import java.time.LocalDate

class HomeViewModel : ViewModel() {


    //카테고리 리스트
    private val _categoryList = MutableLiveData<ArrayList<Category>>(
        arrayListOf(
            Category(2, "약속", "#F0768C", Icon("1", R.drawable.ic_home_cate_plan.toString())),
            Category(2, "약속", "#F0768C", Icon("1", R.drawable.ic_home_cate_plan.toString())),
            Category(3, "잠", "#2AA1B7", Icon("1", R.drawable.ic_home_cate_study.toString())),
            Category(4, "친구만나기", "#F8D141", Icon("1", R.drawable.ic_home_cate_study.toString()))

        )
    )
    val categoryList: LiveData<ArrayList<Category>>
        get() = _categoryList

    //투두 리스트
    private val _todoList = MutableLiveData<ArrayList<Todo>>()
    val todoList: LiveData<ArrayList<Todo>>
        get() = _todoList

    //반복 투두 리스트
    private val _repeatTodoList = MutableLiveData<ArrayList<Todo>>()
    val repeatTodoList: LiveData<ArrayList<Todo>>
        get() = _repeatTodoList

    //스케쥴 리스트 -> 추후 작성
    //timetable title -> 추후 작성


    //날짜
    private val _homeDate = MutableLiveData<LocalDate>(LocalDate.now())
    val homeDate: LiveData<LocalDate>
        get() = _homeDate

    //투두 개수
    private val _todoNum = MutableLiveData<Int>(categoryList.value?.size)
    val todoNum: LiveData<Int>
        get() = _todoNum

    //complete 투두 개수
    private val _completeTodoNum = MutableLiveData<Int>(0)
    val completeTodoNum: LiveData<Int>
        get() = _completeTodoNum

    //카테고리 별로 투두 분류
    private val _cateTodoList = MutableLiveData<ArrayList<ArrayList<Todo>>>()
    val cateTodoList: LiveData<ArrayList<ArrayList<Todo>>>
        get() = _cateTodoList

    fun classifyTodo() {
        //position별로 대응되도록 작성
        //카테고리 삭제 시 todo list도 전체 삭제
        //adpter에는 cateTodoList.value[position] 넣기
    }

    //completeTodoNum 반영 함수

    fun updateCompleteTodo() {
        //complete 개수 ++
    }

    fun addCate(){
        with(_categoryList.value){
            this?.add(
                Category(4, "친구만나기", "#F8D141", Icon("1", R.drawable.ic_home_cate_study.toString()))
            )
            this?.add(Category(3, "잠", "#2AA1B7", Icon("1", R.drawable.ic_home_cate_study.toString())),)
        }
        //서버 POST
    }

    //cate 수정
    fun editCate(position : Int, name : String, color : String, iconName : String){
        _categoryList.value!![position].categoryName = name
        _categoryList.value!![position].color = color
        _categoryList.value!![position].icon_id.name = iconName

        //서버 PATCH
    }

    //cate 삭제
    fun removeCate(positon : Int){
        _categoryList.value!!.removeAt(positon)

        //서버 DELETE
    }



}