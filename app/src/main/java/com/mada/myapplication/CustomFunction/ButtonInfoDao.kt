package com.mada.myapplication.CustomFunction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ButtonInfoDao {
    @Insert
    suspend fun insertButtonInfo(buttonInfo: ButtonInfoEntity)

    @Update
    suspend fun updateButtonInfo(buttonInfo: ButtonInfoEntity)

    @Delete
    suspend fun deleteButtonInfo(buttonInfo: ButtonInfoEntity)

    @Query("SELECT * FROM table_serverbuttoninfo")
    suspend fun getAllButtonInfo(): List<ButtonInfoEntity>

    @Query("SELECT * FROM table_serverbuttoninfo WHERE colorButtonInfo IS NOT NULL")
    suspend fun getColorButtonInfo(): ButtonInfoEntity?

    @Query("SELECT * FROM table_serverbuttoninfo WHERE clothButtonInfo IS NOT NULL")
    suspend fun getClothButtonInfo(): ButtonInfoEntity?

    @Query("SELECT * FROM table_serverbuttoninfo WHERE itemButtonInfo IS NOT NULL")
    suspend fun getItemButtonInfo(): ButtonInfoEntity?

    @Query("SELECT * FROM table_serverbuttoninfo WHERE backgroundButtonInfo IS NOT NULL")
    suspend fun getBackgroundButtonInfo(): ButtonInfoEntity?
}