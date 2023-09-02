package com.example.myapplication.MyFuction.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Data.MySetPageData
import com.example.myapplication.MyFuction.Data.MySetPageData2
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.MySetBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySetActivity : AppCompatActivity() {
    private lateinit var binding: MySetBinding

    //서버연결 시작
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MySetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GetSetPage()


        binding.backBtn.setOnClickListener {

            GlobalScope.launch {
                patchSetting(token, MySetPageData2(binding.mySetSwitch3.isChecked, binding.mySetSwitch2.isChecked, binding.mySetSwitch1.isChecked))
                finish()
            }
        }
    }

     //서버에서 세팅 불러오기
    private fun GetSetPage() {
        api.myGetSettingPage(token).enqueue(object : retrofit2.Callback<MySetPageData> {
            override fun onResponse(
                call: Call<MySetPageData>,
                response: Response<MySetPageData>
            ) {
                val responseCode = response.code()

                if (response.isSuccessful) {
                    if (response != null){
                        Log.d("GetSetPage 성공", response.body()?.data.toString())
                        binding.mySetSwitch1.isChecked = response.body()?.data!!.startTodoAtMonday
                        binding.mySetSwitch2.isChecked = response.body()?.data!!.newTodoStartSetting
                        binding.mySetSwitch3.isChecked = response.body()?.data!!.endTodoBackSetting
                    }
                    else{
                        Log.d("response null ", "Response Code: $responseCode")
                    }


                } else {
                    Log.d("GetSetPage 실패", "Response Code: $responseCode")
                }
            }

            override fun onFailure(call: Call<MySetPageData>, t: Throwable) {
                Log.d("서버 오류", "GetSetPage 실패")
            }
        })
    }


    // 서버에 세팅 저장하기
    fun patchSetting(token : String, data : MySetPageData2) : String{
        var a = "fail"
        val aapi = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
        aapi.mySetPage(token, data)
            .enqueue(object : Callback<MySetPageData>{
                override fun onResponse(
                    call: Call<MySetPageData>,
                    response: Response<MySetPageData>
                ) {
                    if(response.isSuccessful){
                    }
                    else {

                    }
                }

                override fun onFailure(call: Call<MySetPageData>, t: Throwable) {
                    Log.d("서버 연결 실패", "실패")

                }

            })

        return a
    }

}