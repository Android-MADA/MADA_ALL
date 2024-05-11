package com.mada.myapplication.CalenderFuntion.api

import com.mada.myapplication.CalenderFuntion.Model.AddCalendarData1
import com.mada.myapplication.CalenderFuntion.Model.CalendarData
import com.mada.myapplication.CalenderFuntion.Model.CalendarDataEdit
import com.mada.myapplication.CalenderFuntion.Model.CalendarDatas
import com.mada.myapplication.CalenderFuntion.Model.CalendarDatasData
import com.mada.myapplication.CalenderFuntion.Model.nickName
import com.mada.myapplication.CalenderFuntion.Model.subscribe
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

    //월별 데이터 불러오기
    @GET("/api/calendar/")
    fun monthCalRequest(@Header("Authorization") token : String?, @Query("year") year: Int, @Query("month") month: Int
    ) : Call<CalendarDatasData>

    //디데이 데이터 불러오기
    @GET("/api/calendar/dday")
    fun getAllDday(@Header("Authorization") token : String?
    ) : Call<CalendarDatasData>

    //반복 데이터 불러오기
    @GET("/api/calendar/repeat")
    fun getRepeat(@Header("Authorization") token : String?
    ) : Call<CalendarDatasData>

    //데이터 추가
    @POST("/api/calendar/add")
    fun addCal(@Header("Authorization") token: String?, @Body data: CalendarData
    ) : Call<AddCalendarData1>


    //데이터 수정
    @PATCH("/api/calendar/edit/{id}")
    fun editCal(@Header("Authorization") token: String?, @Path("id") id : Int, @Body data: CalendarDataEdit
    ) : Call<CalendarData>

    @DELETE("/api/calendar/edit/{id}/{option}/{date}")
    fun delRepeatCal(@Header("Authorization") token: String?, @Path("id") id : Int,  @Path("option") option : Int, @Path("date") date : String
    ) : Call<CalendarData>

    //데이터 삭제
    @DELETE("/api/calendar/edit/{id}")
    fun deleteCal(@Header("Authorization") token: String?, @Path("id") id : Int
    ) : Call<AddCalendarData1>



    @GET("/api/calendar/date/{date}")           //최 후순위
    fun getDday(@Header("Authorization") token : String?, @Path("date") date : String
    ) : Call<CalendarDatas>


    @POST("/user/signup/nickName")
    fun singup(@Header("Authorization") token: String?, @Body name: nickName
    ) : Call<Void>

    // 프리미엄 구독
    @PATCH("/user/subscribe")
    fun patchSubscribe(
        @Header("Authorization") token : String?,
        @Body is_subscribe : subscribe
    ) : Call<Void>
}