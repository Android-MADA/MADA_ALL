package com.example.myapplication.MyFuction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.CalenderFuntion.Model.CalendarDATA
import com.example.myapplication.CalenderFuntion.Model.CalendarDatas
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.MainActivity
import com.example.myapplication.PreferenceUtil
import com.example.myapplication.databinding.MyWebviewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MyWebviewActivity : AppCompatActivity() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    private lateinit var binding: MyWebviewBinding

    // retrofit
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceMy::class.java)
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjI3MDA0MSwiZXhwIjoxNjkyMzA2MDQxfQ.D9x35qStdNFEFhXLCpNbkKMPD8wRY5-0BGrDZQEj8H-_acrsAub7H1ZXYb6tStgo7_0fkqFDv7sVOU8q_6spOg"


    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate(savedInstanceState)
        binding = MyWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myWebview.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
        }
        var isPageLoaded = false;
        binding.myWebview.loadUrl("http://15.165.210.13:8080/oauth2/authorization/naver")
        binding.myWebview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                //Log.d("url",url)
                binding.myWebview.loadUrl(url)
                if (url.startsWith("http://15.165.210.13:8080/user/test?")) {
                    val intent = Intent(this@MyWebviewActivity, MainActivity::class.java)
                    getResponse(url)
                    startActivity(intent)
                    finish()
                    return true // 처리됨
                } else if (url.startsWith("http://15.165.210.13:8080/user/signup")) {
                    val intent = Intent(this@MyWebviewActivity, MySignup1Activity::class.java)
                    getResponse(url)
                    //startActivity(intent)
                    //finish()
                    return true // 처리됨
                } else {
                    return true
                }

                return true
            }
            override fun onPageFinished(view: WebView, url: String) {

            }
        })
    }
    override fun onBackPressed() {
        if (binding.myWebview.canGoBack())
        {
            binding.myWebview.goBack()
        }
        else
        {
            finish()
        }
    }
    private fun getResponse(url : String) {
        GlobalScope.launch(Dispatchers.IO) {
            val headers = fetchHeadersFromUrl(url)
            withContext(Dispatchers.Main) {
                for ((key, value) in headers) {
                    if(key!=null&&key.equals("Authorization")) {
                        //Log.d("dsa",value.toString())
                        prefs.setString("token",value.toString().substring(1, value.toString().length - 1))
                    }

                }
            }
        }
    }
    private fun fetchHeadersFromUrl(urlString: String): Map<String, List<String>> {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        try {
            // 요청 보내기 (헤더 정보를 얻기 위해 빈 응답을 받아옴)
            connection.requestMethod = "HEAD"
            connection.connect()

            // 헤더 정보 가져오기
            return connection.headerFields
        } finally {
            connection.disconnect()
        }
    }

}

