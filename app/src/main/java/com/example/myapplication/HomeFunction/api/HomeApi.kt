package com.example.myapplication.HomeFunction.api


import com.example.myapplication.CalenderFuntion.Model.CharacterResponse
import com.example.myapplication.HomeFunction.Model.CategoryList1
import com.example.myapplication.HomeFunction.Model.HomeCharacData
import com.example.myapplication.HomeFunction.Model.HomeUserData1
import com.example.myapplication.HomeFunction.Model.PactchResponseCategory
import com.example.myapplication.HomeFunction.Model.PatchRequestTodo
import com.example.myapplication.HomeFunction.Model.PostRequestCategory
import com.example.myapplication.HomeFunction.Model.PostRequestTodo
import com.example.myapplication.HomeFunction.Model.PostResponseTodo
import com.example.myapplication.HomeFunction.Model.RepeatData1
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

    //todo조회 -> 확인 완
    @GET("/api/home/todo/date/{date}")
    suspend fun getAllTodo(
        @Header("Authorization") token: String?,
        @Path("date", encoded = true) date: String
    ): TodoList

    @GET("/api/home/todo/date/{date}")
    fun getAllMyTodo(
        @Header("Authorization") token: String?,
        @Path("date", encoded = true) date: String
    ): Call<TodoList>


    //todo추가
//    @POST("/api/home/todo")
//    suspend fun addTodo(
//        @Header("Authorization") token: String?,
//        @Body data : PostRequestTodo
//    ) : PostResponseTodo

    @POST("/api/home/todo")
    fun addTodo(
        @Header("Authorization") token: String?,
        @Body data : PostRequestTodo
    ) : Call<PostResponseTodo>

    //todo수정 -> 확인 완
    @PATCH("/api/home/todo/todoId/{todoId}")
    suspend fun editTodo(
        @Header("Authorization") token: String?,
        @Path("todoId", encoded = true) todoId: Int,
        @Body data : PatchRequestTodo
    ): PostResponseTodo

    //todo삭제 -> 확인 완
    @DELETE("/api/home/todo/todoId/{todoId}")
    suspend fun deleteTodo(
        @Header("Authorization") token: String?,
        @Path("todoId", encoded = true) todoId: Int
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
    ): CategoryList1

    @GET("/api/home/category")
    fun getHCategory(
        @Header("Authorization") token : String?
    ): Call<CategoryList1>

    @GET("/api/home/category")
    fun getMyCategory(
        @Header("Authorization") token : String?
    ): Call<CategoryList1>

    //카테고리 추가 -> 확인 완
    @POST("/api/home/category")
    suspend fun postCategory(
        @Header("Authorization") token : String?,
        @Body data : PostRequestCategory
    ): PactchResponseCategory


    //카테고리 수정 -> 확인 완
    @PATCH("/api/home/category/{categoryId}")
    suspend fun editCategory(
        @Header("Authorization") token : String?,
        @Path("categoryId", encoded = true) categoryId: Int,
        @Body data : PostRequestCategory
    ): PactchResponseCategory

    //카테고리 삭제 -> 확인 완, todotest만 삭제 오류
    @DELETE("/api/home/category/{categoryId}")
    suspend fun deleteCategory(
        @Header("Authorization") token : String?,
        @Path("categoryId", encoded = true) categoryId: Int
    )

    @GET("/api/home/todo/repeat")
    suspend fun getRepeatTodo(
        @Header("Authorization") token : String?
    ) : RepeatData1

    @GET("/api/home/todo/repeat")
    fun getHRepeatTodo(
        @Header("Authorization") token : String?
    ) : Call<RepeatData1>

    @GET("/api/custom/")
    fun getHomeRamdi(
        @Header("Authorization") token : String?
    ) : Call<HomeCharacData>

    @GET("/my")
    suspend fun getUsername(
        @Header("Authorization") token : String?
    ) : HomeUserData1
}