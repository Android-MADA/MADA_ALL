package com.mada.myapplication.ChartFunction

import com.mada.myapplication.ChartFunction.Data.ChartDayData
import com.mada.myapplication.ChartFunction.Data.ChartMonthData
import com.mada.myapplication.ChartFunction.Data.ChartWeekData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitServiceChart {
    // 일별 통계
    @GET("/chart/day")
    fun chartGetDay(@Header("Authorization") token: String?, @Query("date") date : String
    ): Call<ChartDayData>

    // 주별 통계
    @GET("/chart/week")
    fun chartGetWeek(@Header("Authorization") token: String?, @Query("date") date: String
    ): Call<ChartWeekData>

    // 월별 통계
    @GET("/chart/month")
    fun chartGetMonth(@Header("Authorization") token: String?, @Query("date") date : String
    ): Call<ChartMonthData>
}