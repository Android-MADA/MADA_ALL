//package com.example.myapplication.HomeFunction.viewModel
//
//import android.widget.EditText
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.asLiveData
//import androidx.lifecycle.viewModelScope
//import com.example.myapplication.db.entity.CateEntity
//import com.example.myapplication.db.entity.TodoEntity
//import com.example.myapplication.db.repository.HomeRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class HomeDbViewModel : ViewModel() {
//
//    //repository 선언
//    private val repository = HomeRepository()
//
//    //카테고리 리스트
//    lateinit var cateEntityList : LiveData<List<CateEntity>>
//
//    //특정 카테고리 1개 저장(수정, 삭제 시)
//    var _cate = MutableLiveData<CateEntity>(null)
//    val cate : LiveData<CateEntity>
//        get() = _cate
//
//    lateinit var todoEntityList : LiveData<List<TodoEntity>>
//
//    var _todoList = MutableLiveData<List<TodoEntity>>(null)
//    var todoList : LiveData<List<TodoEntity>>? = null
//
//    val startMonday = false
//    val completeBottom = false
//    val newTodoTop = false
//
//    val date = "2023-09-08"
//
//
//
//    //CRUD 선언
//    fun createCate(cateEntity: CateEntity) = viewModelScope.launch(Dispatchers.IO) {
//        repository.createCate(cateEntity)
//    }
//
////    fun readCate() {
////        cateEntityList = repository.readActiveCate().asLiveData()
////    }
//
//    fun updateCate(cateEntity: CateEntity) = viewModelScope.launch(Dispatchers.IO) {
//        repository.updateCate(cateEntity)
//    }
//
//    fun deleteCate(cateEntity: CateEntity) = viewModelScope.launch(Dispatchers.IO) {
//        repository.deleteCate(cateEntity)
//    }
//
//    //TODO
//
//    fun createTodo(todoEntity: TodoEntity, edt : EditText) = viewModelScope.launch(Dispatchers.IO) {
//        repository.createTodo(todoEntity)
//        edt.text.clear()
//        //readAllTodo()
//    }
//
////    fun readTodo(cateId : Int, adapter: HomeTodoListAdapter) = viewModelScope.launch(Dispatchers.IO){
////        //todoList = repository.readTodo(cateId).asLiveData()
////        repository.readTodo(cateId).collect{
////            Log.d("todoList 확인", it.toString())
////            withContext(Dispatchers.Main){
////                adapter.submitList(it)
////            }
////        }
////
////    }
//
//    fun updateTodo(todoEntity: TodoEntity) = viewModelScope.launch(Dispatchers.IO) {
//        repository.updateTodo(todoEntity)
//    }
//
//    fun deleteTodo(todoEntity: TodoEntity) = viewModelScope.launch(Dispatchers.IO) {
//        repository.deleteTodo(todoEntity)
//    }
//
//    fun deleteTodoCate(cateId : Int) = viewModelScope.launch(Dispatchers.IO) {
//        repository.deleteTodoCate(cateId)
//    }
//
//    fun deleteAllTodo() = viewModelScope.launch(Dispatchers.IO) {
//        repository.deleteAllTodo()
//    }
//
//    fun readAllTodo() {
//        todoEntityList = repository.readAllTodo().asLiveData()
//    }
//}