package com.example.myapplication.CustomFunction

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ButtonInfoEntity::class], version = 1)
abstract class ButtonDatabase : RoomDatabase() {
    abstract fun buttonInfoDao(): ButtonInfoDao

    companion object {
        private var instance: ButtonDatabase? = null

        fun getInstance(context: Context): ButtonDatabase {
            if (instance == null) {
                synchronized(ButtonDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ButtonDatabase::class.java,
                        "app_database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}