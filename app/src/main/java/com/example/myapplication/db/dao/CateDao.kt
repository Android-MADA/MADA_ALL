package com.example.myapplication.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.db.entity.RepeatEntity
import com.example.myapplication.db.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CateDao  {

    //CRUD

    //카테고리 추가
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createCate(cateEntity: CateEntity)

    //카테고리 읽어오기
    @Query("SELECT * FROM cate_table")
    fun readCate() : Flow<List<CateEntity>>

    //특정 id 값을 가진 카테고리 읽어오기
    @Query("SELECT * FROM cate_table WHERE cateId LIKE :cateId")
    fun readCateId(cateId : Int) : Flow<CateEntity>

    @Update
    fun updateCate(cateEntity : CateEntity)

    @Delete
    fun deleteCate(cateEntity: CateEntity)

    //두투 추가
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createTodo(todoEntity: TodoEntity)

    //특정 투두 가져오기
    @Query("SELECT * FROM todo_table WHERE category LIKE :cateId")
    fun readTodo(cateId : Int) : Flow<List<TodoEntity>>

    //전체 투두 가져오기
    @Query("SELECT *FROM todo_table")
    fun readAllTodo() : Flow<List<TodoEntity>>

    @Update
    fun updateTodo(todoEntity: TodoEntity)

    @Delete
    fun deleteTodo(todoEntity: TodoEntity)

    //특정 카테고리 내 모든 투두 삭제
    @Query("DELETE FROM todo_table WHERE id LIKE :cateId")
    fun deleteTodoCate(cateId : Int)

    //모든 투두 삭제
    @Query("DELETE FROM todo_table")
    fun deleteAllTodo()

    //반복두투 추가
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createRepeatTodo(repeatTodoEntity: RepeatEntity)

    //특정 반복두투 가져오기
    @Query("SELECT * FROM repeat_table WHERE category LIKE :cateId")
    fun readRepeatTodo(cateId : Int) : Flow<List<RepeatEntity>>

    //전체 반복두투 가져오기
    @Query("SELECT *FROM repeat_table")
    fun readAllRepeatTodo() : Flow<List<RepeatEntity>>

    @Update
    fun updateRepeatTodo(repeatTodoEntity: RepeatEntity)

    @Delete
    fun deleteRepeatTodo(repeatTodoEntity: RepeatEntity)

    //특정 카테고리 내 모든 반복두투 삭제
    @Query("DELETE FROM repeat_table WHERE id LIKE :cateId")
    fun deleteRepeatTodoCate(cateId : Int)

    //모든 반복두투 삭제
    @Query("DELETE FROM repeat_table")
    fun deleteAllRepeatTodo()




}