package com.example.myapplication.CalenderFuntion.api

import com.example.myapplication.CalenderFuntion.Model.AddCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarData2
import com.example.myapplication.CalenderFuntion.Model.CalendarDatas
import com.example.myapplication.CalenderFuntion.Model.CharacterResponse
import com.example.myapplication.CalenderFuntion.Model.ResponseSample
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServiceCalendar {


    @GET("/api/calender/")
    fun allCalRequest(@Header("Authorization") token : String?
    ) : Call<CalendarDatas>

    @GET("/api/calender/")
    fun monthCalRequest(@Header("Authorization") token : String?, @Query("Year") year: String, @Query("Month") month: String
    ) : Call<CalendarDatas>

    @POST("/api/calender/add")
    fun addCal(@Header("Authorization") token: String?, @Body data: AddCalendarData
    ) : Call<ResponseSample>

    @PATCH("/api/calender/edit/{id}")
    fun editCal(@Header("Authorization") token: String?, @Path("id") id : Int
    ) : Call<AddCalendarData>
    @DELETE("/api/calender/edit/{id}")
    fun deleteCal(@Header("Authorization") token: String?, @Path("id") id : Int
    ) : Call<AddCalendarData>

    @GET("/api/calender/date/{Date}")
    fun getDday(@Header("Authorization") token : String?, @Path("Date") date : String
    ) : Call<CalendarDatas>
    @GET("/api/calender/date")
    fun getAllDday(@Header("Authorization") token : String?
    ) : Call<CalendarDatas>

    @GET("/api/custom/")
    fun characterRequest(@Header("Authorization") token : String?
    ) : Call<CharacterResponse>

    @GET("http://localhost:8080/oauth2/authorization/naver")
    fun login() : Call<CharacterResponse>
}