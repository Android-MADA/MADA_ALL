package com.mada.reapp.CustomFunction

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServiceCustom {

    @GET("/api/custom/reset")
    fun customReset(@Header("Authorization") token : String?
    ) : Call<Void> //완료


    //캐릭터 출력
    @GET("/api/custom/")
    fun customPrint(@Header("Authorization") token : String?
    ) : Call<customPrintDATA>


    //아이템 조회
    @GET("/api/custom/item")
    fun customItemCheck(@Header("Authorization") token : String?
    ) : Call<customItemCheckDATA> //완료




    //아이템 변경
    // 완료
    @PATCH("/api/custom/change")
    fun customItemChange(@Header("Authorization") token: String?, @Query("item_id") itemIds: List<Int>): Call<customItemChangeDATA>

    @POST("/api/custom/buy/{item_id}")
    fun customItemBuy(@Header("Authorization") token : String?, @Path("item_id") item_id : Int
    ) : Call<Void>



}