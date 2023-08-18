package com.example.myapplication.MyFuction

import com.example.myapplication.CalenderFuntion.Model.nickName
import com.example.myapplication.MyFuction.Model.MyGetNoticesData
import com.example.myapplication.MyFuction.Model.MyGetProfileData
import com.example.myapplication.MyFuction.Model.MySetPageData
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST


interface RetrofitServiceMy {

//    // 로그인 -> 필요 없음
//    @GET("/oauth2/authorization/naver")
//    fun login() : Call<Void>

    // 로그아웃 -> 확인
    @GET("/user/logout")
    fun logout(@Header("Authorization") token: String?
    ) : Call<Void>

    // 회원탈퇴 -> 확인
    @DELETE("/user/withdrawal")
    fun withdraw(@Header("Authorization") token: String?
    ) : Call<Void>

    // 프로필 편집창 닉네임, 이메일 불러오기 -> 확인
    @GET("/user/profile/change")
    fun myGetProfile(@Header("Authorization") token : String?
    ): Call<MyGetProfileData>

    // 공지사항 -> 미확인
    @GET("/allnotice")
    fun myGetNotices(@Header("Authorization") token : String?
    ): Call<MyGetNoticesData>

    // 화면 설정 -> 미확인
    @PATCH("/user/pageInfo")
    fun setPage(@Header("Authorization") token: String?, @Body data: MySetPageData
    ): Call<MySetPageData>

    // 알림 설정 -> 미확인

}