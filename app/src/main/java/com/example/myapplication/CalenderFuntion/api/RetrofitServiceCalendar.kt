package com.example.myapplication.CalenderFuntion.api

import com.example.myapplication.CalenderFuntion.Model.AddCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarData2
import com.example.myapplication.CalenderFuntion.Model.CalendarDatas
import com.example.myapplication.CalenderFuntion.Model.CharacterResponse
import com.example.myapplication.CalenderFuntion.Model.ResponseSample
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServiceCalendar {


    @GET("/api/calendar/")
    fun allCalRequest(@Header("Authorization") token : String?
    ) : Call<CalendarDatas>

    @GET("/api/calendar/")
    fun monthCalRequest(@Header("Authorization") token : String?, @Query("year") year: String, @Query("month") month: String
    ) : Call<CalendarDatas>
    @DELETE("/api/calendar/edit/{id}")
    fun deleteCal(@Header("Authorization") token: String?, @Path("id") id : Int
    ) : Call<AddCalendarData>



    //안해봄
    @POST("/api/calendar/add")
    fun addCal(@Header("Authorization") token: String?, @Body data: AddCalendarData
    ) : Call<ResponseSample>


    @PATCH("/api/calendar/edit/{id}")
    fun editCal(@Header("Authorization") token: String?, @Path("id") id : Int
    ) : Call<AddCalendarData>

    @GET("/api/calendar/date/{date}")           //최 후순위
    fun getDday(@Header("Authorization") token : String?, @Path("date") date : String
    ) : Call<CalendarDatas>

    @GET("/api/calendar/dday")
    fun getAllDday(@Header("Authorization") token : String?
    ) : Call<CalendarDatas>


    @GET("/api/custom/")
    fun characterRequest(@Header("Authorization") token : String?
    ) : Call<CharacterResponse>

    @GET("/oauth2/authorization/naver")
    fun login() : Call<Void>
}