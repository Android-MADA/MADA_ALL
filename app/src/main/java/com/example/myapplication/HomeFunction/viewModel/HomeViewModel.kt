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

//            Category(2, "약속", "#F0768C", Icon("1", R.drawable.ic_home_cate_plan.toString())),
//            Category(2, "약속", "#F0768C", Icon("1", R.drawable.ic_home_cate_plan.toString())),
//            Category(3, "잠", "#2AA1B7", Icon("1", R.drawable.ic_home_cate_study.toString())),
//            Category(4, "친구만나기", "#F8D141", Icon("1", R.drawable.ic_home_cate_study.toString()))

    //카테고리 리스트
    private val _categoryList = MutableLiveData<ArrayList<Category>>(
        //서버 GET
        arrayListOf()
    )
    val categoryList: LiveData<ArrayList<Category>>
        get() = _categoryList

    //투두 리스트
    private val _todoList = MutableLiveData<ArrayList<Todo>>(
        //서버 GET
    arrayListOf()
    )
    val todoList: LiveData<ArrayList<Todo>>
        get() = _todoList

    //반복 투두 리스트
    private val _repeatTodoList = MutableLiveData<ArrayList<ArrayList<Todo>>>(
        arrayListOf(
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf()

        )
    )
    val repeatTodoList: LiveData<ArrayList<ArrayList<Todo>>>
        get() = _repeatTodoList

    //스케쥴 리스트 -> 추후 작성
    //timetable title -> 추후 작성

    //날짜
    //2023-08-13
    private val _homeDate = MutableLiveData<LocalDate>(LocalDate.now())
    val homeDate: LiveData<LocalDate>
        get() = _homeDate

    //투두 개수
    private val _todoNum = MutableLiveData<Int>(0)
    val todoNum: LiveData<Int>
        get() = _todoNum

    //complete 투두 개수
    private val _completeTodoNum = MutableLiveData<Int>(0)
    val completeTodoNum: LiveData<Int>
        get() = _completeTodoNum

    //카테고리 별로 투두 분류
    var _cateTodoList = MutableLiveData<ArrayList<ArrayList<Todo>>>(
        //일단 카테고리 개수 제한 : 15개
        arrayListOf(
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf()

        )
    )
    val cateTodoList: LiveData<ArrayList<ArrayList<Todo>>>
        get() = _cateTodoList

    var size = categoryList.value?.size?.minus(1)
    fun classifyTodo() {
        //todo 리스트에서 cateName으로 각각 가져오기
        //position별로 대응되도록 작성
        //카테고리 삭제 시 todo list도 전체 삭제
        //adpter에는 cateTodoList.value[position] 넣기

        if(todoList.value?.isEmpty() != true){
            var completeNum = 0
            for(i in todoList.value!!){
                for(j in 0..size!!){
                    if(i.categoryId.id == categoryList.value!![j].id){
                        _cateTodoList.value?.get(j)!!.add(i)
                        if(i.complete){
                            completeNum++
                        }
                    }
                }
            }
            _todoNum.value = todoList.value!!.size
            _completeTodoNum.value = completeNum


        }

    }

    fun classifyRepeatTodo(){
        if(todoList.value?.isEmpty() != true){
            for(i in todoList.value!!){
                for(j in 0..size!!){
                    if(i.categoryId.id == categoryList.value!![j].id && i.repeat == "day"){
                        _repeatTodoList.value?.get(j)!!.add(i)
                    }
                }
            }
        }
    }

    //completeTodoNum 반영 함수
    fun updateCompleteTodo(flag : Boolean) {
        if(flag){
            _completeTodoNum.value!!.plus(1)
        }
        else {
            _completeTodoNum.value!!.minus(1)
        }
    }

    fun updateTodoNum(){
        _todoNum.value = todoList.value!!.size
    }

    fun addCate(id : Int, cateName : String, color : String, iconName : String, iconId : String){
        with(_categoryList.value){
            this?.add(
                Category(id, cateName, color, Icon(iconId, iconName))
            )
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
    fun removeCate(position : Int){
        _categoryList.value!!.removeAt(position)
        if(_cateTodoList.value != null){
            if(_cateTodoList.value!![position].isNotEmpty()){
                _cateTodoList.value!![position].clear()
            }
        }
        //서버 DELETE
    }

    //todo수정
    fun editTodo(){

    }

    //새로운 투두 상단 표시 flag
    private var _todoTopFlag = MutableLiveData<Boolean>(true)
    val todoTopFlag : LiveData<Boolean>
        get() = _todoTopFlag

    //todo추가
    fun addTodo(position : Int, todo : Todo, flag : Boolean){
        if(flag){
            _cateTodoList!!.value!![position].add(0, todo)
        }
        else {
            _cateTodoList!!.value!![position].add(todo)
        }

    }

    //flag에 따른 todo정렬
    fun arrangeTodo() {
        //catetodo 가져와서 각 array마다 complete 확인해서 변경...?
        if(cateTodoList.value?.isEmpty() != true){
            for(i in cateTodoList.value!!){
                if(i.isNotEmpty()){
                    for(j in 0..i.size!!.minus(1)){
                        if(i[j].complete){
                            var todoMove = i[j]
                            i.removeAt(j)
                            i.add(todoMove)
                        }
                    }
                }
            }
        }
    }

    //todo삭제
    fun deleteTodo(){

    }

    //repeatTodo 추가
    fun addRepeatTodo(position : Int, todo : Todo){
        _repeatTodoList!!.value!![position].add(todo)
    }

    //repeatTodo 삭제
    fun deleteRepeatTodo() {

    }

    //date 변경
    fun changeDate(year : Int, month : Int, dayOfWeek : Int){
        var monthString : String = month.toString()
        var dayString = dayOfWeek.toString()

        if(month == 1 || month == 2 || month == 3 || month == 4 || month == 5 || month == 6 || month == 7 || month == 8 || month == 9){
            monthString = "0${month}"
        }
        if(dayOfWeek == 1 || dayOfWeek == 2 || dayOfWeek == 3 || dayOfWeek == 4 || dayOfWeek == 5 || dayOfWeek == 6 || dayOfWeek == 7 || dayOfWeek == 8 || dayOfWeek == 9){
            dayString = "0${dayOfWeek}"
        }

        _homeDate.value = LocalDate.parse("${year}-${monthString}-${dayString}")
        Log.d("date변환", _homeDate.toString())
    }

    //calendarview 시작 요일
    private var _startDay = MutableLiveData<Int>(1)
    val startDay : LiveData<Int>
        get() = _startDay

    //calendarView 시작 요일 변경
    fun changeStartDay(flag : Boolean){
        if(flag){
            _startDay.value = 2
        }
        else {
            _startDay.value = 1
        }
    }



}