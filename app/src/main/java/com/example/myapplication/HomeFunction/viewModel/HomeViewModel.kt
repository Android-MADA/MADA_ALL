package com.example.myapplication.HomeFunction.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.PostRequestCategory
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.R
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel : ViewModel() {

    //서버 연결
    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)


    var userToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyUldNdDc0LVN2aUljMnh6SE5pQXJQNzZwRnB5clNaXzgybWJNMTJPR000IiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM1NTkyOCwiZXhwIjoxNjkyMzkxOTI4fQ.uSwbzX81QGrWSE44LxkX700sGN_NycpkXKMWBQ_gzfXdDFYJbVGYGtOz78YfJiU66ZrZ3y3SPY6F_xwYlP8hag"

    fun getCategory(token: String?) = viewModelScope.launch {
        val category = api.getCategory(token)
        Log.d("HomeViewModel 카테고리 값 확인", category.toString())
        //서버에서 받은 카테고리 데이터를 livedata에 넣기
        _categoryList.value = category.data
        Log.d("viewmodel 값 넣기", _categoryList.value.toString())
    }

    fun getTodo(token : String?, date : String) = viewModelScope.launch {
        val todo = api.getAllTodo(token, date)
        Log.d("HomeViewModel todo 값 확인", todo.toString())
    }

    fun patchCategory(token: String?, categoryId: Int, data: PostRequestCategory, view : View) =
        viewModelScope.launch {
            val response = api.editCategory(token, categoryId, data)
            Log.d("카테고리 patch", response.toString())
            Navigation.findNavController(view).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
        }

    fun postCategory(token: String?, data: PostRequestCategory, view : View) = viewModelScope.launch {
        val response = api.postCategory(token, data)
        Log.d("카테고리 post", response.toString())
        Navigation.findNavController(view).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
    }

    fun deleteCategory(token: String?, categoryId: Int, view : View) = viewModelScope.launch {
        val response = api.deleteCategory(token, categoryId)
        Log.d("카테고리 delete", "확인")
        Navigation.findNavController(view).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
    }

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
        _cateTodoList.value = arrayListOf(
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

        if (todoList.value?.isEmpty() != true) {
            var completeNum = 0
            for (i in todoList.value!!) {
                for (j in 0..size!!) {
                    if (i.category.id == categoryList.value!![j].id) {
                        _cateTodoList.value?.get(j)!!.add(i)
                    }
                }
            }
        }

    }

    //cate 수정
    fun editCate(position: Int, name: String, color: String, iconName: String) {
        _categoryList.value!![position].categoryName = name
        _categoryList.value!![position].color = color
        //_categoryList.value!![position].iconId = iconName.toInt()

        //서버 PATCH
    }

    //cate 삭제
    fun removeCate(position: Int) {
        _categoryList.value!!.removeAt(position)
        if (_cateTodoList.value != null) {
            if (_cateTodoList.value!![position].isNotEmpty()) {
                _cateTodoList.value!![position].clear()
            }
        }
        //서버 DELETE
    }

    //todo수정
    fun editTodo(
        cateIndex: Int,
        position: Int,
        todoName: String,
        start: LocalDate?,
        end: LocalDate?,
        repeat: String
    ) {
        _cateTodoList.value!![cateIndex][position].todoName = todoName
        _cateTodoList.value!![cateIndex][position].repeat = repeat
        //_cateTodoList.value!![cateIndex][position].start = start
        //_cateTodoList.value!![cateIndex][position].end = end

    }

    //새로운 투두 상단 표시 flag
    private var _todoTopFlag = MutableLiveData<Boolean>(true)
    val todoTopFlag: LiveData<Boolean>
        get() = _todoTopFlag

    fun changeTopFlag(flag: Boolean) {
        _todoTopFlag.value = flag
    }

    //todo추가
    fun addTodo(position: Int, todo: Todo, flag: Boolean) {
//        if(flag){
//            _cateTodoList!!.value!![position].add(0, todo)
//        }
//        else {
//            _cateTodoList!!.value!![position].add(todo)
//        }

        _cateTodoList!!.value!![position].add(todo)

    }

    private var _completeBottomFlag = MutableLiveData<Boolean>(true)
    val completeBottomFlag: LiveData<Boolean>
        get() = _completeBottomFlag

    //flag에 따른 todo정렬
    fun arrangeTodo() {
        //catetodo 가져와서 각 array마다 complete 확인해서 변경...?
        if (cateTodoList.value?.isEmpty() != true) {
            for (i in cateTodoList.value!!) {
                if (i.isNotEmpty()) {
                    for (j in 0..i.size!!.minus(1)) {
                        if (i[j].complete) {
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
    fun deleteTodo(cateIndex: Int, position: Int) {
        _cateTodoList.value!![cateIndex].removeAt(position)
    }


    //date 변경
    fun changeDate(year: Int, month: Int, dayOfWeek: Int) {
        var monthString: String = month.toString()
        var dayString = dayOfWeek.toString()

        if (month == 1 || month == 2 || month == 3 || month == 4 || month == 5 || month == 6 || month == 7 || month == 8 || month == 9) {
            monthString = "0${month}"
        }
        if (dayOfWeek == 1 || dayOfWeek == 2 || dayOfWeek == 3 || dayOfWeek == 4 || dayOfWeek == 5 || dayOfWeek == 6 || dayOfWeek == 7 || dayOfWeek == 8 || dayOfWeek == 9) {
            dayString = "0${dayOfWeek}"
        }

        _homeDate.value = LocalDate.parse("${year}-${monthString}-${dayString}")
    }

    //calendarview 시작 요일
    private var _startDay = MutableLiveData<Int>(1)
    val startDay: LiveData<Int>
        get() = _startDay

    //calendarView 시작 요일 변경
    fun changeStartDay(flag: Boolean) {
        if (flag) {
            _startDay.value = 2
        } else {
            _startDay.value = 1
        }
    }

    var _test = "22"

    private val __test = MutableLiveData<String>(_test)
    val test: LiveData<String>
        get() = __test

    fun updateTest() {
        __test.value = _test
    }

    var todoCateList: ArrayList<ArrayList<Todo>>? = _cateTodoList.value

    fun updateCateTodoList() {
        _cateTodoList.value = todoCateList
        _todoNum.value = todoNumber
        _completeTodoNum.value = completeNumber
    }


    var todoNumber: Int? = _todoNum.value
    var completeNumber: Int? = _completeTodoNum.value

    //completeTodoNum 반영 함수
    fun updateCompleteTodo() {
        if (cateTodoList.value?.isEmpty() != true) {
            completeNumber = 0
            for (i in cateTodoList.value!!) {
                if (i.isNotEmpty()) {
                    for (j in 0..i.size!!.minus(1)) {
                        if (i[j].complete) {
                            completeNumber = completeNumber!! + 1
                        }
                    }
                }
            }
            _completeTodoNum.value = completeNumber
        } else {
            _completeTodoNum.value = 0
        }
    }

    fun updateTodoNum() {

        if (cateTodoList.value?.isEmpty() != true) {
            todoNumber = 0
            for (i in cateTodoList.value!!) {
                if (i.isNotEmpty()) {
                    for (j in 0..i.size!!.minus(1)) {
                        todoNumber = todoNumber!! + 1
                    }
                }
            }
            _todoNum.value = todoNumber
        } else {
            _todoNum.value = 0
        }
    }


}