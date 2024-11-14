package com.mada.reapp.HomeFunction.Model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") val id: Int,
    @SerializedName("categoryName") var categoryName: String,
    @SerializedName("color") var color: String,
    @SerializedName("iconId") var iconId: Int,
    @SerializedName("isInActive") var isInActive: Boolean
)

data class CategoryList1(
    @SerializedName("data") var data: categorylist2
)

data class categorylist2(
    @SerializedName("CategoryList") var CategoryList : ArrayList<Category>
)

data class PatchResponseCategory(
    @SerializedName("data") val data: PatchResponseCate2
)

data class PatchResponseCate2(
    @SerializedName("Category") val Category : Category
)

data class PostRequestCategory(
    @SerializedName("categoryName") var categoryName: String,
    @SerializedName("color") var color: String,
    @SerializedName("iconId") var iconId : Int,
    @SerializedName("isInActive") var isInActive : Boolean,
    @SerializedName("isDeleted") var isDeleted : Boolean
)