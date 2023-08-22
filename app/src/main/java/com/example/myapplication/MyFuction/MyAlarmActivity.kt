package com.example.myapplication.MyFuction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Model.MyAlarmData
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

        binding.backBtn.setOnClickListener {
            finish()
        }

        MysetAlarm(binding.mySetSwitch1.isChecked)

    }

    // 서버에 알림 저장하기
    private fun MysetAlarm(isAlarm: Boolean){
        api.mySetAlarm(token, isAlarm).enqueue(object : retrofit2.Callback<MyAlarmData2> {
            override fun onResponse(
                call: Call<MyAlarmData2>,
                response: Response<MyAlarmData2>
            ) {
                val responseCode = response.code()
                Log.d("setAlarm", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("setAlarm 성공", response.body().toString())
                    binding.mySetSwitch1.isChecked = response.body()!!.calendarAlarmSetting
                    binding.mySetSwitch2.isChecked = response.body()!!.dDayAlarmSetting
                    binding.mySetSwitch3.isChecked = response.body()!!.timetableAlarmSetting
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