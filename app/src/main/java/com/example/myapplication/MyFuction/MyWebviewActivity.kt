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
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM2NTA3OCwiZXhwIjoxNjkyNDAxMDc4fQ.mGHNHLuTpJRc5mFrahf6RCKKVBxfcnvH9B4TDPOA-nEoY-9E8Kl9bw9jH_DjxERx9I3wHg4dwiWqjIImYD1dYQ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = "http://15.165.210.13:8080/oauth2/authorization/naver"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val cookie = response.headers()["set-cookie"]

                // SharedPreferences 또는 다른 데이터 저장 방식을 사용하여 토큰 저장
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("token", cookie)
                if (response.code() == 200 || response.code() == 201) {
                    val responseData = response.headers()
                    val refreshToken = responseData.getDate("Authorization")
                    Log.d("dsa",refreshToken.toString())

                    // 로그인 성공하면 메인 화면으로 이동
                    val intent = Intent(this@MyWebviewActivity, MySignup1Activity::class.java)
                    startActivity(intent)
                } else if (response.code() == 400) {
                    Toast.makeText(this@MyWebviewActivity, "입력한 값을 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 500) {
                    Toast.makeText(this@MyWebviewActivity, "서버와의 연결이 불안정합니다.\n잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.e("fetch error", e.message, e)
            }


        })


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


}

