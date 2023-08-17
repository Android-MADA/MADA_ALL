package com.example.myapplication.MyFuction

import com.example.myapplication.CalenderFuntion.Model.AddCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarDatas
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface RetrofitServiceMy {

    @GET("/oauth2/authorization/naver")
    fun login() : Call<Void>

}