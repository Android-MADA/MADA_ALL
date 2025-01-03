package com.mada.reapp.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation

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
    @ColumnInfo(name = "repeatId")
    val repeatId : Int? = null,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "category")
    var category: Int,
    @ColumnInfo(name = "todoName")
    val todoName: String,
    @ColumnInfo(name = "complete")
    var complete: Boolean,
    @ColumnInfo(name = "repeat")
    val repeat: String? = null,
    @ColumnInfo(name = "repeatInfo")
    val repeatInfo : Int? = null,
    @ColumnInfo(name = "startRepeatDate")
    val startRepeatDate: String? = null,
    @ColumnInfo(name = "endRepeatDate")
    val endRepeatDate: String? = null
)

data class CategoryTodos(
    @Embedded val category : CateEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "category"
    )
    val todos : List<TodoEntity>?
)
