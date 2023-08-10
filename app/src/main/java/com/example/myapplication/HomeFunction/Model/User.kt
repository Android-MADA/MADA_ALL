package com.example.myapplication.HomeFunction.Model

import java.time.LocalDateTime

data class User(
    val id: Int,
    val authId: String,
    val nickname: String,
    val email: String,
    val subscribe: Boolean,
    val provider: String,
    val role: String,
    val account_expired: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val _alarm: Boolean
)