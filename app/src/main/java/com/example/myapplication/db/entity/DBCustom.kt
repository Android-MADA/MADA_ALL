package com.example.myapplication.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DBCustom(
    @PrimaryKey
    @ColumnInfo
    val id : Int
)
