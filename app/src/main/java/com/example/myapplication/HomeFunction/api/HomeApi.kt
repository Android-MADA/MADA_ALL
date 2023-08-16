package com.example.myapplication.HomeFunction.api

import com.example.myapplication.CalenderFuntion.Model.CalendarDatas
import com.example.myapplication.HomeFunction.Model.ScheduleList
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApi {

    //todo조회
    @GET("/api/home/todo/date/{date}")
    fun getAllTodo()

    //todo추가
    @POST("/api/home/todo")
    fun addTodo()

    //todo수정
    @PATCH("/api/home/todo/todoId/{todoId}")
    fun editTodo()

    //todo삭제
    @DELETE("/api/home/todo/todoId/{todoId}")
    fun deleteTodo()

    //시간표 추가 시 일정 및 todo조회
    @GET("/api/home/time/search/date/{date}")
    fun getCalendarTodo()

    //시간표 조회
    @GET("/api/home/time/date/{date}")
    fun getTimetable(@Header("Authorization") token : String?, @Path("date") date : String
    ): Call<ScheduleList>

    //시간표 추가
    @POST("/api/home/time")
    fun addTime()

    //시간표 수정
    @PATCH("/api/home/time/scheduleId/{scheduleId}")
    fun editTime()

    //시간표 삭제
    @DELETE("/api/home/time/scheduleId/{scheduleId}")
    fun deleteTime()

    //카테고리 조회
    @GET("/api/home/category")
    fun getCategory()

    //카테고리 추가
    @POST("/api/home/category")
    fun addCategory()

    //카테고리 수정
    @PATCH("/api/home/category/{categoryId}")
    fun editCategory()

    //카테고리 삭제
    @DELETE("/api/home/category/{categoryId}")
    fun deleteCategory()

}