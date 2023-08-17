package com.example.myapplication.MyFuction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.MyWebviewBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyWebviewActivity : AppCompatActivity() {

    private lateinit var binding: MyWebviewBinding

    // retrofit
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceMy::class.java)
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjI2NTQzMywiZXhwIjoxNjkyMzAxNDMzfQ.5ZNMyY3191Th0_Ci3qRw4xPlavx6vFD5VgXJyJC_mBvX1gV1F5-D6q4d7Be6_-gm6clqoJd-3e3eKvxYEAcJ1g"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //웹페이지 호출
        binding.myWebview.loadUrl("http://15.165.210.13:8080/oauth2/authorization/naver");
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MySignup1", "로그인 후 다시 MyWebViewActiviy 돌아오기 성공")

        // 화면 전환
        val intent = Intent(this, MySignup1Activity::class.java)
        startActivity(intent)
        finish()
    }

}