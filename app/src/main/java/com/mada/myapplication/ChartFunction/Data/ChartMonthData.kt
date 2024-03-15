package com.mada.myapplication.ChartFunction.Data

import com.google.gson.annotations.SerializedName

data class ChartMonthData(
    @SerializedName("categoryStatistics") val categoryStatistics: ArrayList<MonthPieData>,
    @SerializedName("mostCategory") val mostCategory: String,
    @SerializedName("nowCategoryCount") val nowCategoryCount: Float,
    @SerializedName("beforeCategoryCount") val beforeCategoryCount: Float,
    @SerializedName("todoStatistics") val todoStatistics: ArrayList<MonthBarData>,
    @SerializedName("nowTotalCount") val nowTotalCount: Float,
    @SerializedName("nowCountCompleted") val nowCountCompleted: Float,
    @SerializedName("diffCount") val diffCount: Float,
    @SerializedName("achievementStatistics") val achievementStatistics: ArrayList<MonthLineData>,
    @SerializedName("nowAchievementRate") val nowAchievementRate: Float,
    @SerializedName("nowCountCompletedA") val nowCountCompletedA: Float,
)

data class MonthPieData(
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("color") val color: String,
    @SerializedName("rate") val rate: Float
)

data class MonthBarData(
    @SerializedName("date") val date: String,
    @SerializedName("countCompleted") val countCompleted: String
)

data class MonthLineData(
    @SerializedName("date") val date: String,
    @SerializedName("achievementRate") val achievementRate: Float
)