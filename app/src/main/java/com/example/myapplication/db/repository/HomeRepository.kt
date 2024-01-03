package com.example.myapplication.db.repository

import com.example.myapplication.db.HomeDatabase
import com.example.myapplication.db.MyApp
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.db.entity.RepeatEntity
import com.example.myapplication.db.entity.TodoEntity

class HomeRepository {

    //context 가져오기
    private val context = MyApp.context()

    //db 선언
    private val db = HomeDatabase.getDatabase(context)

    //CRUD

    fun createCate(cateEntity: CateEntity) = db.cateDao().createCate(cateEntity)

    fun readHomeCate() = db.cateDao().readHomeCate()

    fun readActiveCate(isActive : Boolean) = db.cateDao().readActiveCate(isActive)

    fun readQuitCate(isActive : Boolean) = db.cateDao().readQuitCate(isActive)

    fun updateCate(cateEntity: CateEntity) = db.cateDao().updateCate(cateEntity)

    fun deleteCate(cateEntity: CateEntity) = db.cateDao().deleteCate(cateEntity)

    fun deleteAllCate() = db.cateDao().deleteAllCate()

    fun getCateId(cateId : Int) = db.cateDao().readCateId(cateId)

    //todo

    fun createTodo(todoEntity: TodoEntity) = db.cateDao().createTodo(todoEntity)

    fun readTodo(cateId : Int) = db.cateDao().readTodo(cateId)

    fun readAllTodo() = db.cateDao().readAllTodo()

    fun updateTodo(todoEntity: TodoEntity) = db.cateDao().updateTodo(todoEntity)

    fun deleteTodo(todoEntity: TodoEntity) = db.cateDao().deleteTodo(todoEntity)

    fun deleteTodoCate(cateId : Int) = db.cateDao().deleteTodoCate(cateId)

    fun deleteAllTodo() = db.cateDao().deleteAllTodo()


    //repeatTodo

    fun createRepeatTodo(repeatTodoEntity: RepeatEntity) = db.cateDao().createRepeatTodo(repeatTodoEntity)

    fun readRepeatTodo(cateId : Int) = db.cateDao().readRepeatTodo(cateId)

    fun readAllRepeatTodo() = db.cateDao().readAllRepeatTodo()

    fun updateRepeatTodo(repeatTodoEntity: RepeatEntity) = db.cateDao().updateRepeatTodo(repeatTodoEntity)

    fun deleteRepeatTodo(repeatTodoEntity: RepeatEntity) = db.cateDao().deleteRepeatTodo(repeatTodoEntity)

    fun deleteRepeatTodoCate(cateId : Int) = db.cateDao().deleteRepeatTodoCate(cateId)

    fun deleteAllRepeatTodo() = db.cateDao().deleteAllRepeatTodo()


}