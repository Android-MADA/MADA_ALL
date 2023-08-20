package com.example.myapplication.HomeFunction.api


import com.example.myapplication.HomeFunction.Model.Schedule
import com.example.myapplication.CalenderFuntion.Model.CalendarDatas
import com.example.myapplication.HomeFunction.Model.CategoryList
import com.example.myapplication.HomeFunction.Model.PactchResponseCategory
import com.example.myapplication.HomeFunction.Model.PostRequestCategory
import com.example.myapplication.HomeFunction.Model.ScheduleAdd
import com.example.myapplication.HomeFunction.Model.ScheduleList
import com.example.myapplication.HomeFunction.Model.ScheduleListData
import com.example.myapplication.HomeFunction.Model.ScheduleResponse
import com.example.myapplication.HomeFunction.Model.ScheduleTodoCalList
import com.example.myapplication.HomeFunction.Model.TodoList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApi {

    //todo조회(1) -> 확인 완
//    @GET("/api/home/todo/date/{date}")
//    fun getAllTodo(
//        @Header("Authorization") token: String?,
//        @Path("date", encoded = true) date: String
//    ): Call<TodoList>

    //todo조회(2) -> 확인 완
    @GET("/api/home/todo/date/{date}")
    suspend fun getAllTodo(
        @Header("Authorization") token: String?,
        @Path("date", encoded = true) date: String
    ): TodoList


    //todo추가
    @POST("/api/home/todo")
    suspend fun addTodo(
        @Header("Authorization") token: String?,
    )

    //todo수정
    @PATCH("/api/home/todo/todoId/{todoId}")
    suspend fun editTodo(
        @Header("Authorization") token: String?,
    )

    //todo삭제
    @DELETE("/api/home/todo/todoId/{todoId}")
    suspend fun deleteTodo(
        @Header("Authorization") token: String?,
    )

    //시간표 추가 시 일정 및 todo조회
    @GET("/api/home/time/search/date/{date}")
    fun getCalendarTodo2(
        @Header("Authorization") token : String?, @Path("date") date : String
    ) :Call<ScheduleTodoCalList>

    @GET("/api/home/time/search/date/{date}")
    fun getCalendarTodo(
        @Header("Authorization") token: String?, @Path("date") date: String
    ): Call<ScheduleTodoCalList>

    //시간표 조회
    @GET("/api/home/time/date/{date}")
    fun getTimetable(
        @Header("Authorization") token: String?, @Path("date") date: String
    ): Call<ScheduleListData>

    //시간표 추가
    @POST("/api/home/time")
    fun addTime(@Header("Authorization") token : String?, @Body data: ScheduleAdd
    ): Call<ScheduleResponse>

    //시간표 수정
    @PATCH("/api/home/time/scheduleId/{scheduleId}")
    fun editTime(@Header("Authorization") token : String?, @Path("scheduleId") scheduleId : Int, @Body data : ScheduleAdd
    ): Call<ScheduleResponse>

    //시간표 삭제
    @DELETE("/api/home/time/scheduleId/{scheduleId}")
    fun deleteTime(@Header("Authorization") token : String?, @Path("scheduleId") scheduleId : Int
    ): Call<ScheduleResponse>


    //카테고리 조회 -> 확인 완
    @GET("/api/home/category")
    suspend fun getCategory(
        @Header("Authorization") token : String?
    ): CategoryList

    //카테고리 추가 -> 확인 완
    @POST("/api/home/category")
    suspend fun postCategory(
        @Header("Authorization") token : String?,
        @Body data : PostRequestCategory
    ): PactchResponseCategory

    // 카테고리 추가(2) -> 확인 완
//    @POST("/api/home/category")
//    fun postCategory(
//        @Header("Authorization") token : String?,
//        @Body data : PatchRequestCategory
//    ): Call<PactchResponseCategory>

    //카테고리 수정 -> 확인 완
    @PATCH("/api/home/category/{categoryId}")
    suspend fun editCategory(
        @Header("Authorization") token : String?,
        @Path("categoryId", encoded = true) categoryId: Int,
        @Body data : PostRequestCategory
    ): PactchResponseCategory

    //카테고리 삭제 -> 확인 완, todo test만 삭제 오류
    @DELETE("/api/home/category/{categoryId}")
    suspend fun deleteCategory(
        @Header("Authorization") token : String?,
        @Path("categoryId", encoded = true) categoryId: Int
    )
}