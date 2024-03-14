package com.mada.myapplication.HomeFunction.api


import com.mada.myapplication.HomeFunction.Model.CategoryList1
import com.mada.myapplication.HomeFunction.Model.PatchCheckboxTodo
import com.mada.myapplication.HomeFunction.Model.PatchResponseCategory
import com.mada.myapplication.HomeFunction.Model.PatchRequestTodo
import com.mada.myapplication.HomeFunction.Model.PostRequestCategory
import com.mada.myapplication.HomeFunction.Model.PostRequestTodo
import com.mada.myapplication.HomeFunction.Model.PostResponseTodo
import com.mada.myapplication.HomeFunction.Model.RepeatData1
import com.mada.myapplication.HomeFunction.Model.ScheduleAdd
import com.mada.myapplication.HomeFunction.Model.ScheduleListData
import com.mada.myapplication.HomeFunction.Model.ScheduleResponse
import com.mada.myapplication.HomeFunction.Model.ScheduleTodoCalList
import com.mada.myapplication.HomeFunction.Model.TodoList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApi {


    @GET("/api/home/todo/date/{date}")
    fun getAllMyTodo(
        @Header("Authorization") token: String?,
        @Path("date", encoded = true) date: String
    ): Call<TodoList>



    @POST("/api/home/todo")
    fun addTodo(
        @Header("Authorization") token: String?,
        @Body data : PostRequestTodo
    ) : Call<PostResponseTodo>

@PATCH("/api/home/todo/update/{todoId}")
fun editTodo(
    @Header("Authorization") token: String?,
    @Path("todoId", encoded = true) todoId: Int,
    @Body data : PatchRequestTodo
): Call<Void>

    @PATCH("/api/home/todo/update/{todoId}")
    fun changeCheckox(
        @Header("Authorization") token: String?,
        @Path("todoId", encoded = true) todoId: Int,
        @Body data : PatchCheckboxTodo
    ): Call<Void>

    @PATCH("/api/home/todo/repeat/update/{repeatTodoId}")
    fun changeRepeatCheckox(
        @Header("Authorization") token: String?,
        @Path("repeatTodoId", encoded = true) repeatTodoId: Int,
        @Body data : PatchCheckboxTodo
    ): Call<Void>


    @PATCH("/api/home/todo/delete/{todoId}")
    fun deleteTodo(
        @Header("Authorization") token: String?,
        @Path("todoId", encoded = true) todoId: Int
    ) : Call<Void>


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
        @Header("Authorization") token: String?,
        @Path("date") date: String
    ): Call<ScheduleListData>

    //시간표 추가
    @POST("/api/home/time/daily")
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
    @GET("/api/home/category/all")
    fun getCategory(
        @Header("Authorization") token : String?
    ): Call<CategoryList1>

    @GET("/api/home/category/date/{date}")
    fun getHCategory(
        @Header("Authorization") token : String?,
        @Path("date", encoded = true) date: String,
    ): Call<CategoryList1>

    @GET("/api/home/category/date/{date}")
    fun getHCategory2(
        @Header("Authorization") token : String?,
        @Path("date", encoded = true) date: String,
    ): Call<CategoryList1>



    @POST("/api/home/category")
    fun postHCategory(
        @Header("Authorization") token : String?,
        @Body data : PostRequestCategory
    ): Call<PatchResponseCategory>



    @PATCH("/api/home/category/{categoryId}")
    fun editHCategory(
        @Header("Authorization") token : String?,
        @Path("categoryId", encoded = true) categoryId: Int,
        @Body data : PostRequestCategory
    ): Call<PatchResponseCategory>

    //카테고리 종료
    @PATCH("/api/home/category/inactive/{categoryId}")
    fun quitCategory(
        @Header("Authorization") token : String?,
        @Path("categoryId", encoded = true) categoryId: Int
    ): Call<Void>

    //카테고리 복원
    @PATCH("/api/home/category/active/{categoryId}")
    fun activeCategory(
        @Header("Authorization") token : String?,
        @Path("categoryId", encoded = true) categoryId: Int
    ) : Call<Void>


    @PATCH("/api/home/category/delete/{categoryId}")
    fun deleteHCategory(
        @Header("Authorization") token : String?,
        @Path("categoryId", encoded = true) categoryId: Int
    ) : Call<Void>

    @PATCH("/api/home/category/delete/{categoryId}")
    fun deleteCategory(
        @Header("Authorization") token : String?,
        @Path("categoryId", encoded = true) categoryId : Int
    ) : Call<Void>


    @GET("/api/home/todo/repeat/all")
    fun getHRepeatTodo(
        @Header("Authorization") token : String?
    ) : Call<RepeatData1>

    //반복투두 삭제 - one
    @PATCH("/api/home/todo/repeat/delete/{repeatTodoId}")
    fun deleteRepeatTodoOne(
        @Header("Authorization") token : String?,
        @Path("repeatTodoId", encoded = true) repeatTodoId : Int
    ) : Call<Void>

    //반복투두 삭제 - after
    @PATCH("/api/home/todo/repeat/delete-all-future/{repeatTodoId}")
    fun deleteRepeatTodoAfter(
        @Header("Authorization") token : String?,
        @Path("repeatTodoId", encoded = true) repeatTodoId : Int
    ) : Call<Void>

    //반복투두 삭제 - all
    @PATCH("/api/home/todo/repeat/delete-all/{repeatTodoId}")
    fun deleteRepeatTodoAll(
        @Header("Authorization") token : String?,
        @Path("repeatTodoId", encoded = true) repeatTodoId : Int
    ) : Call<Void>

}