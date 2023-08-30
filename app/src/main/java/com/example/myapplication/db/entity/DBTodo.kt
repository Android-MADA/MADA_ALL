package com.example.myapplication.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.HomeFunction.Model.Category

@Entity(tableName = "todo_table")
data class DBTodo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todoId")
    val todoId : Int,
    @ColumnInfo(name = "id")
    val id : Int?,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "category")
    var category: Category,
    @ColumnInfo(name = "todoName")
    var todoName: String,
    @ColumnInfo(name = "complete")
    var complete: Boolean,
    @ColumnInfo(name = "repeat")
    var repeat: String,
    @ColumnInfo(name = "repeatWeek")
    var repeatWeek: String?,
    @ColumnInfo(name = "repeatMonth")
    var repeatMonth: String?,
    @ColumnInfo(name = "startRepeatDate")
    var startRepeatDate: String?,
    @ColumnInfo(name = "endRepeatDate")
    var endRepeatDate: String?,
    @ColumnInfo(name = "isAlarm")
    var isAlarm: Boolean,
    @ColumnInfo(name = "startTodoAtMonday")
    var startTodoAtMonday: Boolean,
    @ColumnInfo(name = "endTodoBackSetting")
    var endTodoBackSetting: Boolean,
    @ColumnInfo(name = "newTodoStartSetting")
    var newTodoStartSetting: Boolean
)
