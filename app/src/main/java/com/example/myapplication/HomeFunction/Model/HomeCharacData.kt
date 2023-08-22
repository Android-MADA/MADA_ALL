package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class HomeCharacData(
    @SerializedName("data") var data : HomeWearingData
)

data class HomeWearingData(
    @SerializedName("wearingItems") var wearingItems : ArrayList<HomeWear>
)

data class HomeWear(
    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("itemType") var itemType : String,
    @SerializedName("filePath") var filePath : String

)