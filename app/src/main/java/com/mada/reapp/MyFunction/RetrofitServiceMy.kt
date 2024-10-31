package com.mada.reapp.MyFunction

import com.mada.reapp.CalenderFuntion.Model.CharacterResponse
import com.mada.reapp.MyFunction.Data.FragMyData
import com.mada.reapp.MyFunction.Data.MyAlarmData
import com.mada.reapp.MyFunction.Data.MyChangeNicknameData
import com.mada.reapp.MyFunction.Data.MyGetNoticesData
import com.mada.reapp.MyFunction.Data.MyGetProfileData
import com.mada.reapp.MyFunction.Data.MyGetSetPageData
import com.mada.reapp.MyFunction.Data.MyGetStampData
import com.mada.reapp.MyFunction.Data.MyPatchStampData
import com.mada.reapp.MyFunction.Data.MyPostSetPageData
import com.mada.reapp.MyFunction.Data.MyPremiumData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
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
    ): Call<MyGetSetPageData>

    // 화면 설정 저장 -> 확인
    @POST("/user/pageInfo/change")
    fun myPostSettingPage(@Header("Authorization") token: String?, @Body data : MyPostSetPageData
    ) : Call<MyPostSetPageData>

    // 알림 설정 조회 -> 삭제
    @GET("/user/alarmInfo")
    fun myGetAlarm(@Header("Authorization") token: String?
    ): Call<MyAlarmData>

    // 알림 설정 저장-> 삭제
    @PATCH("/user/alarmInfo")
    fun mySetAlarm(@Header("Authorization") token: String?,  @Body isSettings: Boolean
    ): Call<MyAlarmData>

    // 프리미엄 구독 저장-> 삭제
    @PATCH("/user/subscribe")
    fun mySetPremium(@Header("Authorization") token: String?, @Body is_subscribe: Boolean
    ): Call<MyPremiumData>

    // 출석 카운트 조회
    @GET("/user/attendance/total")
    fun myGetStamp(@Header("Authorization") token : String?
    ): Call<MyGetStampData>

    // 출석 카운트 계산
    @PATCH("/user/attendance")
    fun myPatchStamp(@Header("Authorization") token: String?
    ): Call<MyPatchStampData>
}
