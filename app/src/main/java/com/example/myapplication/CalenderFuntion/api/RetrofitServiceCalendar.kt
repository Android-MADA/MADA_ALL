package com.example.myapplication.CalenderFuntion.api

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

interface RetrofitServiceCalendar {


    @GET("/api/calender/")
    fun allCalRequest(@Header("Authorization") token : String?
    ) : Call<CalendarDatas>

    @GET("/api/calender/{Month}")
    fun monthCalRequest(@Header("Authorization") token : String?, @Path("Month") month : String
    ) : Call<CalendarDatas>

    @POST("/api/calender/add")
    fun addCal(@Header("Authorization") token: String?, @Body data: String
    ) : Call<ResponseSample>



    @PATCH("/api/calender/eidt/{Id}")
    fun editCal(@Header("Authorization") token: String?, @Path("Id") id : String
    ) : Call<ResponseSample>
    @DELETE("/api/calender/eidt/{Id}")
    fun deleteCal(@Header("Authorization") token: String?, @Path("Id") id : String
    ) : Call<ResponseSample>
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