package com.example.myapplication.MyFuction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.PreferenceUtil
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.MyWebviewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.URL


class MyWebviewActivity : AppCompatActivity() {


    private lateinit var binding: MyWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Splash2Activity.prefs = PreferenceUtil(applicationContext)
        super.onCreate(savedInstanceState)
        binding = MyWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //웹 페이지 속성 설정
        binding.myWebview.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
        }
        binding.myWebview.loadUrl("http://www.madaumc.store/oauth2/authorization/naver")
        binding.myWebview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                Log.d("url",url)
                binding.myWebview.loadUrl(url)
                if (url.startsWith("http://www.madaumc.store/user/test?")) {
                    //회원가입 한 상태라면
                    val intent = Intent(this@MyWebviewActivity, MainActivity::class.java)
                    getResponseLogin(url)
                    startActivity(intent)
                    finish()
                    return true // 처리됨
                } else if (url.startsWith("http://www.madaumc.store/user/signup")) {
                    //회원가입 안한 상태라면
                    getResponseSign(url)
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
    private fun getResponseLogin(url : String) {
        GlobalScope.launch(Dispatchers.IO) {
            val headers = fetchHeadersFromUrl(url)
            withContext(Dispatchers.Main) {
                for ((key, value) in headers) {
                    if(key!=null&&key.equals("Authorization")) {
                        Splash2Activity.prefs.setString("token",value.toString().substring(1, value.toString().length - 1))
                    }
                }
            }
        }
    }
    private fun getResponseSign(url : String) {
        GlobalScope.launch(Dispatchers.IO) {
            val headers = fetchHeadersFromUrl(url)

            withContext(Dispatchers.Main) {
                for ((key, value) in headers) {
                    if(key!=null&&key.equals("Authorization")) {
                        Splash2Activity.prefs.setString("token",value.toString().substring(1, value.toString().length - 1))
                    }
                }
                val intent = Intent(this@MyWebviewActivity, MySignup1Activity::class.java)
                startActivity(intent)
                finish()
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

