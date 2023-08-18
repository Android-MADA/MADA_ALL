package com.example.myapplication.CustomFunction

import com.example.myapplication.CalenderFuntion.Model.ResponseSample
import com.example.myapplication.Fragment.FragCustom
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT

interface RetrofitServiceCustom {

    @GET("/api/custom/reset")
    fun customReset(@Header("Authorization") token : String?
    ) : Call<Void> //완료

    @GET("/api/custom/")
    fun customPrint(@Header("Authorization") token : String?
    ) : Call<customPrintDATA>

    @GET("/api/custom/item/{item_type}")
    fun customItemCheck(@Header("Authorization") token : String?, @Path("item_type") item_type : String
    ) : Call<customItemCheckDATA>

    @PATCH("/api/custom/change/{item_id}/")
    fun customItemChange(@Header("Authorization") token : String?, @Path("item_id") item_id : Int
    ) : Call<Void>

    @POST("/api/custom/buy/{item_id}")
    fun customItemBuy(@Header("Authorization") token : String?, @Path("item_id") item_id : Int
    ) : Call<Void>



}