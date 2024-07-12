package com.mada.myapplication.ChartFunction.Data

import com.google.gson.annotations.SerializedName

data class ChartDayData(
    @SerializedName("categoryStatistics") val categoryStatistics: ArrayList<DayPieData>,
    @SerializedName("mostCategory") val mostCategory: String,
    @SerializedName("nowCategoryCount") val nowCategoryCount: Float,
    @SerializedName("beforeCategoryCount") val beforeCategoryCount: Float,
    @SerializedName("todoStatistics") val todoStatistics: ArrayList<DayBarData>,
    @SerializedName("nowTotalCount") val nowTotalCount: Float,
    @SerializedName("nowCountCompleted") val nowCountCompleted: Float,
    @SerializedName("diffCount") val diffCount: Float,
    @SerializedName("achievementStatistics") val achievementStatistics: ArrayList<DayLineData>,
    @SerializedName("nowAchievementRate") val nowAchievementRate: Float,
    @SerializedName("nowCountCompletedA") val nowCountCompletedA: Float,
)

data class DayPieData(
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("color") val color: String,
    @SerializedName("rate") val rate: Float
)

data class DayBarData(
    @SerializedName("date") val date: String,
    @SerializedName("countCompleted") val countCompleted: Float
)

data class DayLineData(
    @SerializedName("date") val date: String,
    @SerializedName("achievementRate") val achievementRate: Float
)