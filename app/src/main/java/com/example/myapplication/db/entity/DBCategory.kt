package com.example.myapplication.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "category_table")
data class DBCategory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryId")
    val categoryId : Int,
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "categoryName")
    var categoryName: String,
    @ColumnInfo(name = "color")
    var color: String,
    @ColumnInfo(name = "iconId")
    var iconId : Int
)
