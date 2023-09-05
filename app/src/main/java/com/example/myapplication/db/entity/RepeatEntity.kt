package com.example.myapplication.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "repeat_table",
    foreignKeys = [ForeignKey(
        entity = CateEntity::class,
        parentColumns = ["cateId"],
        childColumns = ["category"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )])
data class RepeatEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todoId")
    val todoId : Int = 0,
    @ColumnInfo(name = "id")
    val id : Int?,
    @ColumnInfo(name = "date")
    val date: String?,
    @ColumnInfo(name = "category")
    val category: Int,
    @ColumnInfo(name = "todoName")
    val todoName: String,
    @ColumnInfo(name = "repeat")
    val repeat: String,
    @ColumnInfo(name = "repeatWeek")
    val repeatWeek: String?,
    @ColumnInfo(name = "repeatMonth")
    val repeatMonth: String?,
    @ColumnInfo(name = "startRepeatDate")
    val startRepeatDate: String?,
    @ColumnInfo(name = "endRepeatDate")
    val endRepeatDate: String?
)

data class CategoryRepeats(
    @Embedded val category : CateEntity,
    @Relation(
        parentColumn = "cateId",
        entityColumn = "category"
    )
    val todos : List<RepeatEntity>?
)
