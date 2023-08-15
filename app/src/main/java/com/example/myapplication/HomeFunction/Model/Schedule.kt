package com.example.myapplication.HomeFunction.Model

import java.sql.Time
import java.time.LocalDate

data class Schedule(
    val date: LocalDate,
    val scheduleName: String,
    val color: String,
    //time 변수형 수정 필요
    val startTime: Time,
    val endTime: Time,
    val memo: String
)
