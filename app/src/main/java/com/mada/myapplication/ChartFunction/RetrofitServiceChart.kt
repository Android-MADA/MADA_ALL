package com.mada.myapplication.ChartFunction

import com.mada.myapplication.ChartFunction.Data.ChartDayData
import com.mada.myapplication.ChartFunction.Data.ChartMonthData
import com.mada.myapplication.ChartFunction.Data.ChartWeekData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface RetrofitServiceChart {
    // 일별 통계
    @GET("/chart/day?date ={date}")
    fun chartGetDay(@Header("Authorization") token: String?, @Path("date") date : String
    ): Call<ChartDayData>

    // 주별 통계
    @GET("/chart/week?date ={date}")
    fun chartGetWeek(@Header("Authorization") token: String?, @Path("date") date : String
    ): Call<ChartWeekData>

    // 월별 통계
    @GET("/chart/month?date ={date}")
    fun chartGetMonth(@Header("Authorization") token: String?, @Path("date") date : String
    ): Call<ChartMonthData>
}