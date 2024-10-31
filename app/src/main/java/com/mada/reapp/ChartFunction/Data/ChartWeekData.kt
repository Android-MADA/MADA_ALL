package com.mada.reapp.ChartFunction.Data

import com.google.gson.annotations.SerializedName

data class ChartWeekData(
    @SerializedName("categoryStatistics") val categoryStatistics: ArrayList<WeekPieData>,
    @SerializedName("mostCategory") val mostCategory: String,
    @SerializedName("nowCategoryCount") val nowCategoryCount: Float,
    @SerializedName("beforeCategoryCount") val beforeCategoryCount: Float,
    @SerializedName("todoStatistics") val todoStatistics: ArrayList<WeekBarData>,
    @SerializedName("nowTotalCount") val nowTotalCount: Float,
    @SerializedName("nowCountCompleted") val nowCountCompleted: Float,
    @SerializedName("diffCount") val diffCount: Float,
    @SerializedName("achievementStatistics") val achievementStatistics: ArrayList<WeekLineData>,
    @SerializedName("nowAchievementRate") val nowAchievementRate: Float,
    @SerializedName("nowCountCompletedA") val nowCountCompletedA: Float,
)

data class WeekPieData(
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("color") val color: String,
    @SerializedName("rate") val rate: Float
)

data class WeekBarData(
    @SerializedName("date") val date: String,
    @SerializedName("countCompleted") val countCompleted: Float
)

data class WeekLineData(
    @SerializedName("date") val date: String,
    @SerializedName("achievementRate") val achievementRate: Float
)