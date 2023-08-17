package com.example.myapplication.MyFuction

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Response


interface RetrofitServiceMy {

    @GET("/oauth2/authorization/naver")
    fun login() : Call<Void>
}