package com.example.myapplication.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.db.entity.DBCategory
import com.example.myapplication.db.entity.DBTodo

@Dao
interface HomeDao {

    //모든 카테고리 가져오기
    @Query("SELECT * FROM category_table")
    fun getAllCategory() : ArrayList<DBCategory>

    //모든 투두 가져오기 -> 수정 : 특정 카테고리의 id값을 넣어서 해당 데이터만 가져오기
    @Query("SELECT * FROM todo_table WHERE id LIKE :cateId AND date LIKE :date")
    fun getAllTodo(cateId : Int, date : String) : ArrayList<DBTodo>

    //투두 추가하기
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTodo(todo : DBTodo)

    //투두 변경하기
    @Update
    fun updateTodo(vararg todo: DBTodo)

    //투두 삭제하기
    @Delete
    fun deleteTodo(vararg todo : DBTodo)

    //해당 카테고리 내 모든 투두 삭제하기
    @Query("DELETE FROM todo_table WHERE category LIKE :cate")
    fun deleteTodos(cate : Category)

    //카테고리 추가하기
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCategory(category : DBCategory)

    //카테고리 변경하기
    @Update
    fun updateUsers(vararg category: DBCategory)

    //카테고리 삭제하기
    @Delete
    fun deleteCategory(vararg  category : DBCategory)

    //모든 카테고리 삭제하기 -> 서버에서 새로 받아올 때
    @Query("DELETE FROM category_table")
    fun deleteAllCategory()

    //모든 투두 삭제하기 -> 서버에서 새로 받아올 때
    @Query("DELETE FROM todo_table")
    fun deleteAllTodo()

}