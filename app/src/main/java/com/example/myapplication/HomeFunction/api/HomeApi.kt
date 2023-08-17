package com.example.myapplication.HomeFunction.api

import com.example.myapplication.CalenderFuntion.Model.CalendarDatas
import com.example.myapplication.CalenderFuntion.Model.ResponseSample
import com.example.myapplication.HomeFunction.Model.CategoryList
import com.example.myapplication.HomeFunction.Model.PactchResponseCategory
import com.example.myapplication.HomeFunction.Model.PatchRequestCategory
import com.example.myapplication.HomeFunction.Model.ScheduleList
import com.example.myapplication.HomeFunction.Model.TodoList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDate

interface HomeApi {

    //todo조회 -> 확인 완
    @GET("/api/home/todo/date/{date}")
    fun getAllTodo(
        @Header("Authorization") token: String?,
        @Path("date", encoded = true) date: String
    ): Call<TodoList>

    //todo추가
    @POST("/api/home/todo")
    suspend fun addTodo(
        @Header("Autorization") token: String?,
    )

    //todo수정
    @PATCH("/api/home/todo/todoId/{todoId}")
    suspend fun editTodo(
        @Header("Autorization") token: String?,
    )

    //todo삭제
    @DELETE("/api/home/todo/todoId/{todoId}")
    suspend fun deleteTodo(
        @Header("Autorization") token: String?,
    )

    //시간표 추가 시 일정 및 todo조회
    @GET("/api/home/time/search/date/{date}")
    suspend fun getCalendarTodo()

    //시간표 조회
    @GET("/api/home/time/date/{date}")
    fun getTimetable(
        @Header("Authorization") token: String?, @Path("date") date: String
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

    //카테고리 조회 -> 확인 완
    @GET("/api/home/category")
    suspend fun getCategory(
        @Header("Authorization") token : String?
    ): Response<CategoryList>

    //카테고리 추가
    @POST("/api/home/category")
    suspend fun addCategory(
        @Header("Authorization") token : String?,
        @Body data : PatchRequestCategory
    ): PactchResponseCategory

    // 카테고리 추가(2) -> 확인 완
    @POST("/api/home/category")
    fun postCategory(
        @Header("Authorization") token : String?,
        @Body data : PatchRequestCategory
    ): Call<PactchResponseCategory>

    //카테고리 수정
    @PATCH("/api/home/category/{categoryId}")
    suspend fun editCategory(
        @Header("Authorization") token: String?,
        @Path("categoryId") categoryId: Int,
        @Body data : PatchRequestCategory
    ): PactchResponseCategory

    //카테고리 삭제
    @DELETE("/api/home/category/{categoryId}")
    suspend fun deleteCategory(
        @Header("Authorization") token: String?,
        @Path("categoryId") categoryId: Int
    ): PactchResponseCategory

}