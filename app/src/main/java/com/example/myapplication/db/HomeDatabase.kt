package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.db.dao.CateDao
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.db.entity.RepeatEntity
import com.example.myapplication.db.entity.TodoEntity

@Database(entities = [CateEntity::class, TodoEntity::class, RepeatEntity::class], version = 1)
abstract class HomeDatabase : RoomDatabase() {

    abstract fun cateDao() : CateDao

    companion object{

        @Volatile
        private var INSTANCE : HomeDatabase? = null

        fun getDatabase(
            context : Context
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