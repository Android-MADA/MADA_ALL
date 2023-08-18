package com.example.myapplication.MyFuction

import com.example.myapplication.MyFuction.Model.MyGetProfileData
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Header


interface RetrofitServiceMy {

    // 로그인 -> 확인 필요
    @GET("/oauth2/authorization/naver")
    fun login() : Call<Void>

    // 프로필 편집창 닉네임, 이메일 조회
    @GET("/user/profile/change")
    fun myGetProfile(@Header("Authorization") token : String?
    ): Call<MyGetProfileData>


}