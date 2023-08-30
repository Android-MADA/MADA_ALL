package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.db.dao.HomeDao
import com.example.myapplication.db.entity.DBCategory
import com.example.myapplication.db.entity.DBTodo

@Database(entities = [DBTodo::class], [DBCategory::class], version = 1)
abstract class HomeDatabase : RoomDatabase() {

    abstract fun HomeDao() : HomeDao

    companion object {

        @Volatile
        private var INSTANCE : HomeDatabase? = null

        fun getDatabase(
            context: Context
        ) : HomeDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HomeDatabase::class.java,
                    "home_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}