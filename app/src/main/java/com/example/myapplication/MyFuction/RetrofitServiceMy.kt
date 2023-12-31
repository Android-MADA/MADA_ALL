package com.example.myapplication.MyFuction

import com.example.myapplication.CalenderFuntion.Model.CharacterResponse
import com.example.myapplication.MyFuction.Data.FragMyData
import com.example.myapplication.MyFuction.Data.MyAlarmData
import com.example.myapplication.MyFuction.Data.MyChangeNicknameData
import com.example.myapplication.MyFuction.Data.MyGetNoticesData
import com.example.myapplication.MyFuction.Data.MyGetProfileData
import com.example.myapplication.MyFuction.Data.MyPremiumData
import com.example.myapplication.ChartFunction.Data.MyRecordData
import com.example.myapplication.ChartFunction.Data.MyRecordOptionData
import com.example.myapplication.MyFuction.Data.MySetPageData
import com.example.myapplication.MyFuction.Data.MySetPageData2
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST


interface RetrofitServiceMy {

    // 로그아웃 -> 확인
    @GET("/user/logout")
    fun logout(@Header("Authorization") token: String?
    ) : Call<Void>

    // 회원탈퇴 -> 확인
    @DELETE("/user/withdrawal")
    fun withdraw(@Header("Authorization") token: String?
    ) : Call<Void>

    // FragMy 데이터 불러오기 -> 확인
    @GET("/my")
    fun selectfragMy(@Header("Authorization") token : String?
    ): Call<FragMyData>

    // FragMy 커스텀 캐릭터 불러오기
    @GET("/api/custom/")
    fun characterRequest(@Header("Authorization") token : String?
    ) : Call<CharacterResponse>

    // 프로필 편집창 닉네임, 이메일 불러오기 -> 확인
    @GET("/user/profile/change")
    fun myGetProfile(@Header("Authorization") token : String?
    ): Call<MyGetProfileData>

    // 닉네임 변경 저장 -> 확인
    @PATCH("/user/profile/change/nickname")
    fun changeNickname(@Header("Authorization") token: String?, @Body nickname: String
    ): Call<MyChangeNicknameData>

    // 공지사항 조회 -> 확인
    @GET("/allnotice")
    fun myGetNotices(@Header("Authorization") token: String?
    ): Call<MyGetNoticesData>

    // 화면 설정 조회 -> 확인
    @GET("/user/pageInfo")
    fun myGetSettingPage(@Header("Authorization") token: String?
    ): Call<MySetPageData>

    // 화면 설정 저장 -> 확인
    @POST("/user/pageInfo/change")
    fun mySetPage(
        @Header("Authorization") token: String?,
        @Body data : MySetPageData2
    ) : Call<MySetPageData>

    // 알림 설정 조회 -> 확인
    @GET("/user/alarmInfo")
    fun myGetAlarm(@Header("Authorization") token: String?
    ): Call<MyAlarmData>

    // 알림 설정 저장-> 미확인
    @PATCH("/user/alarmInfo")
    fun mySetAlarm(@Header("Authorization") token: String?,  @Body isSettings: Boolean
    ): Call<MyAlarmData>

    // 프리미엄 구독 저장-> 미확인
    @PATCH("/user/subscribe")
    fun mySetPremium(@Header("Authorization") token: String?, @Body is_subscribe: Boolean
    ): Call<MyPremiumData>

    // 주, 월별 투두, 카테고리 통계 조회
    @POST("/user/statistics")
    fun myGetRecord(@Header("Authorization") token: String?, @Body data: MyRecordOptionData
    ): Call<MyRecordData>

}
