package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Model.MyAlarmData
import com.example.myapplication.MyFuction.Model.MyAlarmData2
import com.example.myapplication.MyFuction.Model.MySetPageData
import com.example.myapplication.MyFuction.Model.MySetPageData2
import com.example.myapplication.databinding.MyAlarmBinding
import retrofit2.Call
import retrofit2.Response

class MyAlarmActivity : AppCompatActivity() {
    private lateinit var binding: MyAlarmBinding

    //서버연결 시작
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = MyWebviewActivity.prefs.getString("token", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GetSetAlarm()

        binding.backBtn.setOnClickListener {
            PatchSetAlarm(binding.mySetSwitch1.isChecked)
            finish()
        }
    }

    // 서버에서 알림 불러오기
    private fun GetSetAlarm(){
        api.myGetAlarm(token).enqueue(object : retrofit2.Callback<MyAlarmData> {
            override fun onResponse(
                call: Call<MyAlarmData>,
                response: Response<MyAlarmData>
            ) {
                val responseCode = response.code()
                Log.d("GetSetAlarm", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("GetSetAlarm 성공", response.body().toString())
                    binding.mySetSwitch1.isChecked = response.body()!!.data.calendarAlarmSetting
                    binding.mySetSwitch2.isChecked = response.body()!!.data.dDayAlarmSetting
                    binding.mySetSwitch3.isChecked = response.body()!!.data.timetableAlarmSetting
                } else {
                    Log.d("GetSetAlarm 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<MyAlarmData>, t: Throwable) {
                Log.d("서버 오류", "GetSetAlarm 실패")
            }
        })
    }


    // 서버에 알림 저장하기
    private fun PatchSetAlarm(is_setting: Boolean){
        api.mySetAlarm(token, is_setting).enqueue(object : retrofit2.Callback<MyAlarmData> {
            override fun onResponse(
                call: Call<MyAlarmData>,
                response: Response<MyAlarmData>
            ) {
                val responseCode = response.code()
                Log.d("PatchSetAlarm", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("PatchSetAlarm 성공", response.body().toString())

                } else {
                    Log.d("PatchSetAlarm 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<MyAlarmData>, t: Throwable) {
                Log.d("서버 오류", "PatchSetAlarm 실패")
            }
        })
    }


}