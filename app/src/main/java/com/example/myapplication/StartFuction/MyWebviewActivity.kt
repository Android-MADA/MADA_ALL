package com.example.myapplication.StartFuction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.CustomFunction.ButtonDatabase
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.CustomFunction.ButtonInfoEntity
import com.example.myapplication.CustomFunction.DataRepo
import com.example.myapplication.CustomFunction.RetrofitServiceCustom
import com.example.myapplication.CustomFunction.customPrintDATA
import com.example.myapplication.Fragment.IdAndItemType
import com.example.myapplication.MainActivity
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.PreferenceUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.MyWebviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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


                    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
                        .addConverterFactory(GsonConverterFactory.create()).build()
                    val service = retrofit.create(RetrofitServiceCustom::class.java)
                    val token = Splash2Activity.prefs.getString("token", "")

                    val migration1to2 = object : Migration(1, 2) {
                        override fun migrate(database: SupportSQLiteDatabase) {
                            // 마이그레이션 로직을 여기에 작성합니다.
                            // 이전 버전에서 새 버전으로의 데이터베이스 스키마 변경을 정의합니다.
                            database.execSQL("ALTER TABLE old_table_name ADD COLUMN new_column_name TEXT")
                        }
                    }


                    val appDatabase = Room.databaseBuilder(applicationContext, ButtonDatabase::class.java, "my_database")
                        .addMigrations(migration1to2) // 마이그레이션 경로 추가
                        .build()

                    // DataRepo 클래스나 다른 곳에서 이 데이터베이스 인스턴스를 사용할 수 있습니다.
                    //DataRepo.initializeDatabase(appDatabase)

                    val buttonInfoDao = appDatabase.buttonInfoDao()

                    val printIds = mutableListOf<IdAndItemType>()
                    var printId: Int = 0
                    var itemType: String = "z"

                    val call: Call<customPrintDATA> = service.customPrint(token)
                    call.enqueue(object : Callback<customPrintDATA> {
                        override fun onResponse(
                            call: Call<customPrintDATA>,
                            response: Response<customPrintDATA>
                        ) {
                            val printInfo = response.body()
                            val responseCode = response.code()
                            val datas = printInfo?.data?.wearingItems

                            datas?.forEachIndexed { index, item ->
                                printId = item.id
                                itemType = item.itemType
                                Log.d(
                                    "getCustomPrint",
                                    "Item $index - id: ${item.id} itemType: ${item.itemType}"
                                )
                                Log.d("getCustomPrint", "Response Code: $responseCode")
                            }

                            datas?.forEachIndexed { index, item ->
                                val idAndItemType = IdAndItemType(item.id, item.itemType)
                                printIds.add(idAndItemType)
                            }

                            var colorid: Int? = null // 변수를 초기화

                            var clothid: Int? = null // 변수를 초기화

                            var itemid: Int? = null // 변수를 초기화

                            var backgroundid: Int? = null // 변수를 초기화

                            datas?.forEach { item ->
                                if (item.itemType == "color") {
                                    colorid = item.id
                                } else if (item.itemType == "set") {
                                    clothid = item.id
                                } else if (item.itemType == "item") {
                                    itemid = item.id
                                } else if (item.itemType == "background") {
                                    backgroundid = item.id
                                }

                                var buttonInfoEntity = ButtonInfoEntity(
                                    id = 0,
                                    colorButtonInfo = ButtonInfo(
                                        buttonId = colorid ?: 0, // 기본값을 설정할 수 있음
                                        serverID = colorid ?: 10, // 기본값을 설정할 수 있음
                                        selectedImageResource = R.drawable.c_ramdi
                                    ),
                                    clothButtonInfo = ButtonInfo(
                                        buttonId = clothid ?: 0, // 기본값을 설정할 수 있음
                                        serverID = clothid ?: 900, // 기본값을 설정할 수 있음
                                        selectedImageResource = R.drawable.custom_empty
                                    ),
                                    itemButtonInfo = ButtonInfo(
                                        buttonId = itemid ?: 0, // 기본값을 설정할 수 있음
                                        serverID = itemid ?: 800, // 기본값을 설정할 수 있음
                                        selectedImageResource = R.drawable.custom_empty
                                    ),
                                    backgroundButtonInfo = ButtonInfo(
                                        buttonId = backgroundid ?: 0, // 기본값을 설정할 수 있음
                                        serverID = backgroundid ?: 700, // 기본값을 설정할 수 있음
                                        selectedImageResource = R.drawable.custom_empty
                                    )
                                )
                                DataRepo.buttonInfoEntity = buttonInfoEntity
                                CoroutineScope(Dispatchers.IO).launch {
                                    buttonInfoDao.insertButtonInfo(buttonInfoEntity)
                                }
                            }

                            datas?.forEachIndexed { index, item ->
                                val idAndItemType = IdAndItemType(item.id, item.itemType)
                                printIds.add(idAndItemType)
                            }
                            getResponseLogin(url)
                            startActivity(intent)
                            finish()
                        }
                        override fun onFailure(call: Call<customPrintDATA>, t: Throwable) {
                            Log.d("error", t.message.toString())
                        }
                    })
                    return true
                    // 처리됨
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
