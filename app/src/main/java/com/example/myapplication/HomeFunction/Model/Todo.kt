package com.example.myapplication.HomeFunction.Model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Todo(
    //@SerializedName("id") val id : Int,
    //@SerializedName("date") val date : LocalDate,
    @SerializedName("categoryId") var categoryId : Category,
    @SerializedName("todoName") var todoName : String,
    @SerializedName("complete") var complete : Boolean,
    @SerializedName("repeat") var repeat : String,
    //@SerializedName("repeatWeek") var repeatWeek: String?,
    //@SerializedName("startRepeatDate") var startRepeatDate: String?,
    //@SerializedName("endRepeatDate") var endRepeatDate: String?
)

