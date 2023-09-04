package com.example.myapplication.MyFuction.Fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Data.MySetPageData
import com.example.myapplication.MyFuction.Data.MySetPageData2
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.MySetBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySetFragment : Fragment() {
    private lateinit var binding: MySetBinding
    private val token = Splash2Activity.prefs.getString("token", "")
    private val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MySetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 스위치 색 설정 함수 호출
        setupSwitchColor(binding.mySetSwitch1)
        setupSwitchColor(binding.mySetSwitch2)
        setupSwitchColor(binding.mySetSwitch3)

        binding.backBtn.setOnClickListener {
            GlobalScope.launch {
                patchSetting(token, MySetPageData2(binding.mySetSwitch3.isChecked, binding.mySetSwitch2.isChecked, binding.mySetSwitch1.isChecked))
                activity?.finish()
            }
        }

        GetSetPage()
    }

    private fun GetSetPage() {
        api.myGetSettingPage(token).enqueue(object : Callback<MySetPageData> {
            override fun onResponse(
                call: Call<MySetPageData>,
                response: Response<MySetPageData>
            ) {
                val responseCode = response.code()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("GetSetPage 성공", response.body()?.data.toString())
                        binding.mySetSwitch1.isChecked = response.body()?.data!!.startTodoAtMonday
                        binding.mySetSwitch2.isChecked = response.body()?.data!!.newTodoStartSetting
                        binding.mySetSwitch3.isChecked = response.body()?.data!!.endTodoBackSetting
                    } else {
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

    private fun patchSetting(token: String, data: MySetPageData2) {
        val aapi = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
        aapi.mySetPage(token, data)
            .enqueue(object : Callback<MySetPageData> {
                override fun onResponse(
                    call: Call<MySetPageData>,
                    response: Response<MySetPageData>
                ) {
                    if (response.isSuccessful) {
                        // 서버에 세팅 저장 성공
                    } else {
                        // 서버에 세팅 저장 실패
                    }
                }

                override fun onFailure(call: Call<MySetPageData>, t: Throwable) {
                    Log.d("서버 연결 실패", "실패")
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
