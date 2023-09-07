package com.example.myapplication.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.myapplication.HomeFunction.Model.Category

@Entity(tableName = "todo_table",
foreignKeys = [ForeignKey(
    entity = CateEntity::class,
    parentColumns = ["id"],
    childColumns = ["category"],
    onDelete = CASCADE,
    onUpdate = CASCADE
)])
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todoId")
    val todoId : Int = 0,
    @ColumnInfo(name = "id")
    val id : Int?,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "category")
    var category: Int,
    @ColumnInfo(name = "todoName")
    val todoName: String,
    @ColumnInfo(name = "complete")
    var complete: Boolean,
    @ColumnInfo(name = "repeat")
    val repeat: String,
    @ColumnInfo(name = "repeatWeek")
    val repeatWeek: String?,
    @ColumnInfo(name = "repeatMonth")
    val repeatMonth: String?,
    @ColumnInfo(name = "startRepeatDate")
    val startRepeatDate: String?,
    @ColumnInfo(name = "endRepeatDate")
    val endRepeatDate: String?,
    @ColumnInfo(name = "isAlarm")
    val isAlarm: Boolean,
    @ColumnInfo(name = "startTodoAtMonday")
    val startTodoAtMonday: Boolean,
    @ColumnInfo(name = "endTodoBackSetting")
    val endTodoBackSetting: Boolean,
    @ColumnInfo(name = "newTodoStartSetting")
    val newTodoStartSetting: Boolean
)

data class CategoryTodos(
    @Embedded val category : CateEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "category"
    )
    val todos : List<TodoEntity>?
)
