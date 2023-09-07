package com.example.myapplication.db.entity

import android.content.res.AssetFileDescriptor.AutoCloseInputStream
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cate_table")
data class CateEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "categoryName")
    val categoryName: String,
    @ColumnInfo(name = "color")
    val color: String,
    @ColumnInfo(name = "isInActive")
    val isInActive : Boolean,
    @ColumnInfo(name = "iconId")
    val iconId : Int
)
