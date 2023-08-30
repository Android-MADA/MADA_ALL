package com.example.myapplication.HomeFunction.viewModel

import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.PatchRequestTodo
import com.example.myapplication.HomeFunction.Model.PostRequestCategory
import com.example.myapplication.HomeFunction.Model.PostRequestTodo
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.Model.repeatTodo
import com.example.myapplication.HomeFunction.Model.todoData
import com.example.myapplication.HomeFunction.adapter.repeatTodo.HomeRepeatTodoAdapter
import com.example.myapplication.HomeFunction.adapter.todo.HomeViewpager2TodoAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.MyWebviewActivity
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel : ViewModel() {

    //서버 연결
    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)


    //var userToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyUldNdDc0LVN2aUljMnh6SE5pQXJQNzZwRnB5clNaXzgybWJNMTJPR000IiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM1NTkyOCwiZXhwIjoxNjkyMzkxOTI4fQ.uSwbzX81QGrWSE44LxkX700sGN_NycpkXKMWBQ_gzfXdDFYJbVGYGtOz78YfJiU66ZrZ3y3SPY6F_xwYlP8hag"
    var userToken = Splash2Activity.prefs.getString("token", "")

    var userHomeName = "김마다"

    fun getUsername(text : TextView) = viewModelScope.launch {
        val response = api.getUsername(userToken)
        userHomeName = response.data.nickname
        text.text = "${userHomeName}님, "
    }
    fun getCategory(token: String?) = viewModelScope.launch {
        val response = api.getCategory(token)
        val category = response.data.CategoryList
        Log.d("HomeViewModel 카테고리 값 확인", category.toString())
        _categoryList.value = category
    }


    fun getTodo(token: String?, date: String) = viewModelScope.launch {
        val todo = api.getAllTodo(token, date)
        Log.d("HomeViewModel todo 값 확인", todo.toString())
        _todoList.value = todo.data.TodoList
        if(todo.data.TodoList.isNullOrEmpty() != true){
////            _completeBottomFlag.value = todo.data.TodoList[0].endTodoBackSetting
//            _completeBottomFlag.value = true
//            _todoTopFlag.value = todo.data.TodoList[0].newTodoStartSetting
            changeStartDay(todo.data.TodoList[0].startTodoAtMonday)
        }
        classifyTodo()
//        if(_completeBottomFlag.value == true){
//            arrangeTodo()
//            Log.d("completeBottom", todoCateList.toString())
//        }
        updateTodoNum(null)
        updateCateTodoList()
        updateCompleteTodo(null)
    }


    fun getRepeatTodo() = viewModelScope.launch {
        val response = api.getRepeatTodo(userToken)
        Log.d("HomeViewModel repeatTodo GET", response.data.RepeatTodoList.toString())
        _repeatTodoL.value = response.data.RepeatTodoList
        classifyRepeatTodo()
        updateRepeatList()
        Log.d("repeatTodo", repeatList.value.toString())
    }

    fun patchCategory(token: String?, categoryId: Int, data: PostRequestCategory, view: View) =
        viewModelScope.launch {
            val response = api.editCategory(token, categoryId, data)
            Log.d("카테고리 patch", response.toString())
            Navigation.findNavController(view)
                .navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
        }

    fun postCategory(token: String?, data: PostRequestCategory, view: View) =
        viewModelScope.launch {
            val response = api.postCategory(token, data)
            Log.d("카테고리 post", response.toString())
            Navigation.findNavController(view)
                .navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
        }

    fun deleteCategory(token: String?, categoryId: Int, view: View) = viewModelScope.launch {
        val response = api.deleteCategory(token, categoryId)
        Log.d("카테고리 delete", "확인")
        Navigation.findNavController(view)
            .navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
    }

//    fun postTodo(data : PostRequestTodo) = viewModelScope.launch{
//        val response = api.addTodo(userToken, data)
//        Log.d("todo POST", response.data.toString())
//        todoId = response.data.id
//    }

    fun deleteRepeatTodo(
        todoId: Int,
        cateIndex: Int,
        todoIndex: Int,
        adapater: HomeRepeatTodoAdapter
    ) = viewModelScope.launch {
        //서버 전송
        val response = api.deleteTodo(userToken, todoId)
        Log.d("repeattodo DELETE", "todo delete 실행")
        //live data 수정
        _repeatList.value!![cateIndex].removeAt(todoIndex)
        adapater.notifyDataSetChanged()
    }

    fun deleteTodo(
        todoId: Int,
        cateIndex: Int,
        todoIndex: Int,
        complete : Boolean,
        adapater: HomeViewpager2TodoAdapter
    ) = viewModelScope.launch {
        //서버 전송
        val response = api.deleteTodo(userToken, todoId)
        Log.d("todo DELETE", "todo delete 실행")
        //live data 수정
        _cateTodoList.value!![cateIndex].removeAt(todoIndex)
        adapater.notifyDataSetChanged()
        updateTodoNum("delete")
        if(complete){
            updateCompleteTodo("delete")
        }

    }

    fun patchTodo(
        todoId: Int,
        data: PatchRequestTodo,
        cateId: Int,
        todoPosition: Int,
        view: View?
    ) = viewModelScope.launch {
        api.editTodo(userToken, todoId, data)
        Log.d("todo patch", "todo 변경 확인")
        if (view == null) {
            _cateTodoList.value!![cateId][todoPosition].todoName = data.todoName
            _cateTodoList.value!![cateId][todoPosition].repeat = data.repeat
            _cateTodoList.value!![cateId][todoPosition].startRepeatDate = data.startRepeatDate
            _cateTodoList.value!![cateId][todoPosition].endRepeatDate = data.endRepeatDate
            _cateTodoList.value!![cateId][todoPosition].repeatWeek = data.repeatWeek
            _cateTodoList.value!![cateId][todoPosition].repeatMonth = data.repeatMonth
            _cateTodoList.value!![cateId][todoPosition].complete = data.complete

        } else {
            Navigation.findNavController(view)
                .navigate(R.id.action_repeatTodoAddFragment_to_homeRepeatTodoFragment)
        }
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

    private val _repeatTodoL = MutableLiveData<ArrayList<repeatTodo>>(
        arrayListOf()
    )

    val repeatTodoL: LiveData<ArrayList<repeatTodo>>
        get() = _repeatTodoL

    //날짜
    //2023-08-13
    private val _homeDate = MutableLiveData<LocalDate>(LocalDate.now())
    val homeDate: LiveData<LocalDate>
        get() = _homeDate

    //viewPager date 넘기기 확인 코드 시작

    var viewpagerDate: LocalDate? = LocalDate.now()

    fun updateDate() {
        _homeDate.value = viewpagerDate
    }

    //viewPager date 넘기기 확인 코드 끝

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

    var _repeatList = MutableLiveData<ArrayList<ArrayList<repeatTodo>>>(
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

    val repeatList: LiveData<ArrayList<ArrayList<repeatTodo>>>
        get() = _repeatList

    //var size = categoryList.value?.size?.minus(1)
    fun classifyTodo() {
        var size = if (categoryList.value!!.size != 0) {
            categoryList.value!!.size.minus(1)
        } else {
            -1
        }

        todoCateList = arrayListOf(
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

        if (size > 0) {
            for (i in todoList.value!!) {
                for (j in 0..size) {
                    if (i.category.id == categoryList.value!![j].id) {
                        todoCateList!![j].add(i)
                    }
                }
            }

        } else if (size == 0) {
            for (i in todoList.value!!) {
                todoCateList!![0].add(i)
            }
        }
        Log.d("classify실행 완", todoCateList.toString())
    }

    fun classifyRepeatTodo() {

        var size = if (categoryList.value!!.size != 0) {
            categoryList.value!!.size.minus(1)
        } else {
            -1
        }

        repeatTodoList = arrayListOf(
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

        if (size > 0) {
            for (i in repeatTodoL.value!!) {
                for (j in 0..size) {
                    if (i.category.id == categoryList.value!![j].id) {
                        repeatTodoList!![j].add(i)
                    }
                }
            }

        } else if (size == 0) {
            for (i in repeatTodoL.value!!) {
                repeatTodoList!![0].add(i)
            }
        }
        Log.d("repeatclassify실행2", repeatTodoList.toString())
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
    private var _todoTopFlag = MutableLiveData<Boolean>(false)
    val todoTopFlag: LiveData<Boolean>
        get() = _todoTopFlag

    fun changeTopFlag(flag: Boolean) {
        _todoTopFlag.value = flag
    }

    //todo추가
    var todoId = 0
    fun addTodo(position: Int, todo: Todo, flag : Boolean) {
        if(flag == true){
            _cateTodoList!!.value!![position].add(0, todo)
        }
        else{
            _cateTodoList!!.value!![position].add(todo)
        }
        updateTodoNum("add")
        //updateCompleteTodo("add")
    }

    fun addRepeatTodo(position: Int, todo: repeatTodo) {
        _repeatList!!.value!![position].add(todo)
    }

    private var _completeBottomFlag = MutableLiveData<Boolean>(true)
    val completeBottomFlag: LiveData<Boolean>
        get() = _completeBottomFlag

    //flag에 따른 todo정렬
    fun arrangeTodo() {
        //catetodo 가져와서 각 array마다 complete 확인해서 변경...?
        if (todoCateList!!.isEmpty() != true) {
            for (i in todoCateList!!) {
                if (i.isNotEmpty()  && i.size > 1) {
                    for (j in 0..i.size!!.minus(1)) {
                        if (i[j].complete) {
                            var todoMove = i[j]
                            i.removeAt(j)
                            i.add(todoMove)
                        }
                    }
                }
            }
            Log.d("arrangetodo", todoCateList.toString())
        }

    }

    //todo삭제
    fun deleteTodo(cateIndex: Int, position: Int) {
        _cateTodoList.value!![cateIndex].removeAt(position)
    }


    //date 변경
    fun changeDate(year: Int, month: Int, dayOfWeek: Int, flag: String?): String {
        var date = homeDate.value!!.toString()
        var monthString: String = month.toString()
        var dayString = dayOfWeek.toString()

        if (month == 1 || month == 2 || month == 3 || month == 4 || month == 5 || month == 6 || month == 7 || month == 8 || month == 9) {
            monthString = "0${month}"
        }
        if (dayOfWeek == 1 || dayOfWeek == 2 || dayOfWeek == 3 || dayOfWeek == 4 || dayOfWeek == 5 || dayOfWeek == 6 || dayOfWeek == 7 || dayOfWeek == 8 || dayOfWeek == 9) {
            dayString = "0${dayOfWeek}"
        }
        date = "${year}-${monthString}-${dayString}"

        if (flag == "home") {
            viewpagerDate = LocalDate.parse("${year}-${monthString}-${dayString}")
            _homeDate.value = viewpagerDate
        }

        return date
    }

    //calendarview 시작 요일
    private var _startDay = MutableLiveData<Int>(2)
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

    var todoCateList: ArrayList<ArrayList<Todo>>? = _cateTodoList.value
    fun updateCateTodoList() {
        classifyRepeatTodo()
        _cateTodoList.value = todoCateList
        _todoNum.value = todoNumber
        _completeTodoNum.value = completeNumber
    }

    var repeatTodoList: ArrayList<ArrayList<repeatTodo>>? = _repeatList.value

    fun updateRepeatList() {
        _repeatList.value = repeatTodoList
    }


    var todoNumber: Int = 0
    var completeNumber: Int = 0

    //completeTodoNum 반영 함수
    fun updateCompleteTodo(flag : String?) {
        if(flag == null){
            if (cateTodoList.value?.isEmpty() != true) {
                completeNumber = 0
                for (i in todoList.value!!) {
                    if (i.complete) {
                        completeNumber = completeNumber.plus(1)
                    }
                }
                _completeTodoNum.value = completeNumber
            } else {
                _completeTodoNum.value = 0
            }
        }
        //todo add
        else if(flag == "add"){
            _completeTodoNum.value = ++completeNumber
        }
        //todo 삭제
        else {
            completeNumber = when(completeNumber){
                0 -> {0}
                else -> completeNumber.minus(1)
            }
            _completeTodoNum.value = completeNumber
        }
        //cb 클릭 -> add, delete 상황 적용

    }

    fun updateTodoNum(flag: String?) {
        if (flag == null) {
            if (cateTodoList.value?.isEmpty() != true) {
                todoNumber = todoList.value!!.size
            } else {
                todoNumber = 0
            }
            _todoNum.value = todoNumber
        } else if (flag == "add") {
            _todoNum.value = ++todoNumber
        } else {
            //delete
            _todoNum.value = --todoNumber
        }

    }


}