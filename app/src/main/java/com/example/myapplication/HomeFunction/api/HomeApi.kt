package com.example.myapplication.HomeFunction.api

import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.User
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDate

interface HomeApi {

    //유저 정보
//    @GET()
//    fun getUser(
//        @Path()
//    ) : Array<>
//
    //특정 일 투두 받아오기
    @GET("/api/home/todo/date/{date}")
    fun getAllTodo(
        @Path("date") date : LocalDate
    ) : Array<User>

    //모든 카테고리 받아오기
    @GET("/api/home/category/{categoryId}")
    fun getAllCategory(
        @Path("categoryId") categoryId : String
    ) : Array<Category>

    //특정 일 시간표 받아오기
}