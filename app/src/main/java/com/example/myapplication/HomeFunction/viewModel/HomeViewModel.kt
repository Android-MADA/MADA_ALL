package com.example.myapplication.HomeFunction.viewModel

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.HomeFunction.Model.PatchRequestTodo
import com.example.myapplication.HomeFunction.adapter.repeatTodo.RepeatTodoListAdapter
import com.example.myapplication.HomeFunction.adapter.todo.HomeTodoListAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.db.entity.RepeatEntity
import com.example.myapplication.db.entity.TodoEntity
import com.example.myapplication.db.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import com.example.myapplication.StartFuction.Splash2Activity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel : ViewModel() {

    //서버 연결
    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

    var userToken = Splash2Activity.prefs.getString("token", "")

    var userHomeName = "김마다"


    //날짜
    //2023-08-13
    private val _homeDate = MutableLiveData<LocalDate>(LocalDate.now())
    val homeDate: LiveData<LocalDate>
        get() = _homeDate

    var homeDay = "월요일"

    //viewPager date 넘기기 확인 코드 시작

    var viewpagerDate: LocalDate? = LocalDate.now()

    //달력 날짜 라이브 데이터
    private var _myLiveToday = MutableLiveData<String>()
    val myLiveToday : LiveData<String>
        get() = _myLiveToday

    fun updateData(newValue: String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        _myLiveToday.value = newValue
        Log.d("update",newValue)
        _homeDate.value = LocalDate.parse(newValue, DateTimeFormatter.ISO_DATE)
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


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //ROOM

    //repository 선언
    private val repository = HomeRepository()

    //카테고리 리스트
    var homeCateEntityList : LiveData<List<CateEntity>>? = null
    lateinit var cateEntityList : LiveData<List<CateEntity>>
    lateinit var quitCateEntityList : LiveData<List<CateEntity>>

    //특정 카테고리 1개 저장(수정, 삭제 시)
    var _cate = MutableLiveData<CateEntity>(null)
    val cate : LiveData<CateEntity>
        get() = _cate

    lateinit var todoEntityList : LiveData<List<TodoEntity>>

    var _dUserName = MutableLiveData<String>("김마다")

    val dUserName : LiveData<String>
        get() = _dUserName

    val isAlarm = false
    val startMonday = false
    val completeBottom = false
    val newTodoTop = false



    //CRUD 선언
    fun createCate(cateEntity: CateEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.createCate(cateEntity)
    }

    fun readHomeCate(){
        homeCateEntityList = repository.readHomeCate().asLiveData()
        Log.d("check readHomecate", "working")
    }

    fun readActiveCate(isActive : Boolean) {
        cateEntityList = repository.readActiveCate(isActive).asLiveData()
        Log.d("readActivecate", "working")
    }

    fun readQuitCate(isActive : Boolean) {
        quitCateEntityList = repository.readQuitCate(isActive).asLiveData()
        Log.d("readQuitcate", "working")
    }

    fun updateCate(cateEntity: CateEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCate(cateEntity)
    }

    fun deleteCate(cateEntity: CateEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCate(cateEntity)
    }

    fun deleteAllCate() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllCate()
    }

    //TODO

    fun createTodo(todoEntity: TodoEntity, edt : EditText?) = viewModelScope.launch(Dispatchers.IO) {
        repository.createTodo(todoEntity)
        if(edt != null){
            edt!!.text.clear()
        }
        Log.d("todo 추가중", todoEntity.toString())
    }

    var inActiveTodoList : List<TodoEntity>? = null

    fun readTodo(cateId : Int, adapter: HomeTodoListAdapter?) = viewModelScope.launch(Dispatchers.IO){
        repository.readTodo(cateId).collect{
            Log.d("todoList 확인", it.toString())

            inActiveTodoList = it
            if(inActiveTodoList.isNullOrEmpty()){
                Log.d("empty list", "empty todo list")
            }
            withContext(Dispatchers.Main){
                if(adapter != null){
                    adapter.submitList(it)
                }
            }
        }

    }

    fun updateTodo(todoEntity: TodoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTodo(todoEntity)
    }

    fun deleteTodo(todoEntity: TodoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTodo(todoEntity)
    }

    fun deleteTodoCate(cateId : Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTodoCate(cateId)
    }

    fun deleteAllTodo() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllTodo()
    }

    fun readAllTodo() {
        todoEntityList = repository.readAllTodo().asLiveData()
    }

    //repeatTodo

    fun createRepeatTodo(repeatTodoEntity: RepeatEntity, edt : EditText?) = viewModelScope.launch(Dispatchers.IO) { repository.createRepeatTodo(repeatTodoEntity)
        if(edt != null) {
            edt!!.text.clear()
        }

        //readAllTodo()
    }

    fun readRepeatTodo(cateId : Int, adapter: RepeatTodoListAdapter) = viewModelScope.launch(Dispatchers.IO){
        repository.readRepeatTodo(cateId).collect{
            Log.d("RtodoList 확인", it.toString())
            withContext(Dispatchers.Main){
                adapter.submitList(it)
            }
        }

    }

    fun updateRepeatTodo(repeatTodoEntity: RepeatEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateRepeatTodo(repeatTodoEntity)
    }

    fun deleteRepeatTodo(repeatTodoEntity: RepeatEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRepeatTodo(repeatTodoEntity)
    }

    fun deleteRepeatTodoCate(cateId : Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRepeatTodoCate(cateId)
    }

    fun deleteAllRepeatTodo() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllRepeatTodo()
    }

    fun readAllRepeatTodo() {
        //todoEntityList = repository.readAllRepeatTodo().asLiveData()
    }

    /////////////////////////////////////////////////////////////////////
    // server 연결

    //투두 수정
    fun patchHTodo(todoId : Int, data : PatchRequestTodo) = viewModelScope.launch {
        api.editTodo(userToken, todoId, data)
    }
}