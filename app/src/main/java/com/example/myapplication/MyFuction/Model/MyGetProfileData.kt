package com.example.myapplication.MyFuction.Model


import android.provider.ContactsContract

data class MyGetProfileData(
    val data : ArrayList<MyProfileData>
)
data class MyProfileData(
    val nickname: String?,
    val email: String?,
)

