package com.mada.myapplication.MyFuction.Fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.MyFuction.Data.MyAlarmData
import com.mada.myapplication.MyFuction.RetrofitServiceMy
import com.mada.myapplication.R
import com.mada.myapplication.StartFuction.Splash2Activity
import com.mada.myapplication.databinding.MyAlarmBinding
import retrofit2.Call
import retrofit2.Response

class MyAlarmFragment : Fragment() {

    private lateinit var binding: MyAlarmBinding
    lateinit var navController: NavController
    private val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    private val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyAlarmBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()

        binding.backBtn.setOnClickListener {
            PatchSetAlarm(binding.mySetSwitch1.isChecked)
            //navController.navigate(R.id.action_myAlarmFragment_to_fragMy)
        }

        GetSetAlarm()

        // 스위치 색 설정 함수 호출
        setupSwitchColor(binding.mySetSwitch1)
        setupSwitchColor(binding.mySetSwitch2)
        setupSwitchColor(binding.mySetSwitch3)


        binding.backBtn.setOnClickListener {
            PatchSetAlarm(binding.mySetSwitch1.isChecked)
            //navController.navigate(R.id.action_myAlarmFragment_to_fragMy)
        }
    }

    // 서버에서 알림 불러오기
    private fun GetSetAlarm() {
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
    private fun PatchSetAlarm(is_setting: Boolean) {
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

    // 스위치 색 설정 함수
    private fun setupSwitchColor(mySwitch: SwitchCompat) {
        mySwitch.setOnCheckedChangeListener { _, isChecked ->
            val trackColor = if (isChecked) {
                ContextCompat.getColor(requireContext(), R.color.main)
            } else {
                ContextCompat.getColor(requireContext(), R.color.grey5)
            }
            val thumbColor = if (isChecked) {
                ContextCompat.getColor(requireContext(),  R.color.sub4)
            } else {
                ContextCompat.getColor(requireContext(), R.color.grey2)
            }
            mySwitch.trackDrawable?.setColorFilter(trackColor, PorterDuff.Mode.SRC_IN)
            mySwitch.thumbDrawable?.setColorFilter(thumbColor, PorterDuff.Mode.SRC_IN)
        }
    }
}
