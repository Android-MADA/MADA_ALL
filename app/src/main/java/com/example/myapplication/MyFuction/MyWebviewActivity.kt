package com.example.myapplication.MyFuction

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.MyWebviewBinding
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class MyWebviewActivity : AppCompatActivity() {

    private lateinit var binding: MyWebviewBinding

    // retrofit
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceMy::class.java)
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjI3MDA0MSwiZXhwIjoxNjkyMzA2MDQxfQ.D9x35qStdNFEFhXLCpNbkKMPD8wRY5-0BGrDZQEj8H-_acrsAub7H1ZXYb6tStgo7_0fkqFDv7sVOU8q_6spOg"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.myWebview.loadUrl("http://15.165.210.13:8080/oauth2/authorization/naver")



    }


}

