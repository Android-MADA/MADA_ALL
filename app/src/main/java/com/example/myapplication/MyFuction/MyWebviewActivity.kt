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
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjI3MDA0MSwiZXhwIjoxNjkyMzA2MDQxfQ.D9x35qStdNFEFhXLCpNbkKMPD8wRY5-0BGrDZQEj8H-_acrsAub7H1ZXYb6tStgo7_0fkqFDv7sVOU8q_6spOg"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //웹페이지 호출
        binding.myWebview.loadUrl("http://15.165.210.13:8080/oauth2/authorization/naver")
        Log.d("MyWebviewActivity", "웹페이지 호출 성공")
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.d("MyWebviewActivity", "웹페이지에서 다시 MyWebViewActiviy 돌아오기 성공")
//
//        // 화면 전환
//        val intent = Intent(this, MySignup1Activity::class.java)
//        startActivity(intent)
//        finish()
//    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if(hasFocus){
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var pasteData: String=""

            if(!clipboard.hasPrimaryClip()){

            }else if((clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN)) == false) {

                }else{
                    val item = clipboard.primaryClip?.getItemAt(0)!!.coerceToText(applicationContext)
                    if(!item.isNullOrEmpty()){
                        pasteData = item.toString()
                    }
            }
        }
    }

}

