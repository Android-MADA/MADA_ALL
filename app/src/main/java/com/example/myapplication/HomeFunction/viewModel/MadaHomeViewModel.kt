package com.example.myapplication.HomeFunction.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.Todo
import java.time.LocalDate

class MadaHomeViewModel : ViewModel() {

    //date
    private var _date = MutableLiveData<LocalDate>(LocalDate.now())
    val date: LiveData<LocalDate>
        get() = _date

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

        _date.value = LocalDate.parse("${year}-${monthString}-${dayString}")
    }

    //User

    //Category
    private var _categoryList = MutableLiveData<ArrayList<Category>>(
        //서버에서 받아오기
        arrayListOf()
    )
    val categoryList: LiveData<ArrayList<Category>>
        get() = _categoryList

    //Todo
    private var _todoList = MutableLiveData<ArrayList<Todo>>(
        //서버에서 받아오기
        arrayListOf()
    )
    val todoList : LiveData<ArrayList<Todo>>
        get() = _todoList

    //character

    //Time
    //private var _timeList = MutableLiveData<ArrayList<Time>>
    //val timeList : LiveData<ArrayList<Time>>
    //    get() = _timeList

    //todoNum
    private val _todoNum = MutableLiveData<Int>(0) //서버에서 받아오기
    val todoNum: LiveData<Int>
        get() = _todoNum

    //completeNum
    private val _completeTodoNum = MutableLiveData<Int>(0) //서버에서 받아오기
    val completeTodoNum: LiveData<Int>
        get() = _completeTodoNum


    //카테고리별로 나눠지는 투두 리스트 또는 그 리스트 만드는 함수

    //카테고리별로 다눠지는 반복투두 리스트 또는 그 리스트 만드는 함수

    //월요일부터 시작 flag
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

    //완료투두 맨 뒤로 보내기 flag

    //생성 투두 맨 위로 flag
    private var _todoTopFlag = MutableLiveData<Boolean>(false)
    val todoTopFlag : LiveData<Boolean>
        get() = _todoTopFlag

    fun changeTopFlag(flag : Boolean){
        _todoTopFlag.value = flag
    }

}

