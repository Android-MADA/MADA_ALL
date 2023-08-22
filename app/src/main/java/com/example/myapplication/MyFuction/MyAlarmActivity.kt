package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Model.MyAlarmData2
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
            PatchSetAlarm(binding.mySetSwitch2.isChecked)
            PatchSetAlarm(binding.mySetSwitch3.isChecked)
            finish()
        }
    }

    // 서버에서 알림 저장하기
    private fun GetSetAlarm(){
        api.myGetAlarm(token).enqueue(object : retrofit2.Callback<MyAlarmData> {
            override fun onResponse(
                call: Call<MyAlarmData>,
                response: Response<MyAlarmData>
            ) {
                val responseCode = response.code()
                Log.d("getAlarm", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("getAlarm 성공", response.body().toString())
                    binding.mySetSwitch1.isChecked = response.body()!!.data.calendarAlarmSetting
                    binding.mySetSwitch2.isChecked = response.body()!!.data.dDayAlarmSetting
                    binding.mySetSwitch3.isChecked = response.body()!!.data.timetableAlarmSetting
                } else {
                    Log.d("getAlarm 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<MyAlarmData>, t: Throwable) {
                Log.d("서버 오류", "getAlarm 실패")
            }
        })
    }


    // 서버에 알림 저장하기
    private fun PatchSetAlarm(isAlarm: Boolean){
        api.mySetAlarm(token, isAlarm).enqueue(object : retrofit2.Callback<MyAlarmData2> {
            override fun onResponse(
                call: Call<MyAlarmData2>,
                response: Response<MyAlarmData2>
            ) {
                val responseCode = response.code()
                Log.d("setAlarm", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("setAlarm 성공", response.body().toString())
                } else {
                    Log.d("setAlarm 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<MyAlarmData2>, t: Throwable) {
                Log.d("서버 오류", "setAlarm 실패")
            }
        })
    }


}