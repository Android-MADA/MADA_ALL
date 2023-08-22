package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Model.MyAlarmData
import com.example.myapplication.MyFuction.Model.MyAlarmData2
import com.example.myapplication.MyFuction.Model.MyChangeNicknameData
import com.example.myapplication.MyFuction.Model.MySetPageData
import com.example.myapplication.MyFuction.Model.MySetPageData2
import com.example.myapplication.databinding.MySetBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySetActivity : AppCompatActivity() {
    private lateinit var binding: MySetBinding

    //서버연결 시작
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = MyWebviewActivity.prefs.getString("token", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MySetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GetSetPage()

        binding.backBtn.setOnClickListener {
            PostSetPage(binding.mySetSwitch1.isChecked)
            finish()
        }
    }

    // 서버에서 세팅 불러오기
    private fun GetSetPage() {
        api.myGetSettingPage(token).enqueue(object : retrofit2.Callback<MySetPageData> {
            override fun onResponse(
                call: Call<MySetPageData>,
                response: Response<MySetPageData>
            ) {
                val responseCode = response.code()

                if (response.isSuccessful) {
                    if (response != null){
                        Log.d("GetSetPage 성공", "Response Code: $responseCode"+"")
//                        binding.mySetSwitch1.isChecked = response.body()?.data!!.endTodobackSetting
//                        binding.mySetSwitch2.isChecked = response.body()?.data!!.newTodoStartSetting
//                        binding.mySetSwitch3.isChecked = response.body()?.data!!.startTodoAtMonday
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
    private fun PostSetPage(isSetting: Boolean) {
        api.mySetPage(token, isSetting).enqueue(object : retrofit2.Callback<MySetPageData2> {
            override fun onResponse(
                call: Call<MySetPageData2>,
                response: Response<MySetPageData2>
            ) {
                val responseCode = response.code()

                if (response.isSuccessful) {
                    Log.d("PostSetPage 성공", "Response Code: $responseCode")
                } else {
                    Log.d("PostSetPage 실패", "Response Code: $responseCode")
                }
            }
            override fun onFailure(call: Call<MySetPageData2>, t: Throwable) {
                Log.d("서버 오류", "PostSetPage 실패")
            }
        })
    }
}