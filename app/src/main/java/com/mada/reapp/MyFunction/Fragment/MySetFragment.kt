package com.mada.reapp.MyFunction.Fragment

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
import com.mada.reapp.HomeFunction.api.RetrofitInstance
import com.mada.reapp.MyFunction.Data.MyGetSetPageData
import com.mada.reapp.MyFunction.Data.MyPostSetPageData
import com.mada.reapp.MyFunction.RetrofitServiceMy
import com.mada.reapp.R
import com.mada.reapp.StartFunction.Splash2Activity
import com.mada.reapp.databinding.MySetBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySetFragment : Fragment() {
    private lateinit var binding: MySetBinding
    lateinit var navController: NavController
    private val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    private val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MySetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()

        // 스위치 색 설정 함수 호출
        setupSwitchColor(binding.mySetSwitch1)
        setupSwitchColor(binding.mySetSwitch2)
        setupSwitchColor(binding.mySetSwitch3)

        GetSetPage()

        binding.backBtn.setOnClickListener {
            PostSetPage(token, MyPostSetPageData(binding.mySetSwitch3.isChecked, binding.mySetSwitch2.isChecked, binding.mySetSwitch1.isChecked))
            navController.navigate(R.id.action_mySetFragment_to_fragMy)
        }

    }

    private fun GetSetPage() {
        api.myGetSettingPage(token).enqueue(object : Callback<MyGetSetPageData> {
            override fun onResponse(
                call: Call<MyGetSetPageData>,
                response: Response<MyGetSetPageData>
            ) {
                val responseCode = response.code()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("GetSetPage 성공", response.body().toString())
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

            override fun onFailure(call: Call<MyGetSetPageData>, t: Throwable) {
                Log.d("서버 오류", "GetSetPage 실패")
            }
        })
    }

    private fun PostSetPage(token: String, data: MyPostSetPageData) {
        api.myPostSettingPage(token, data)
            .enqueue(object : Callback<MyPostSetPageData> {
                override fun onResponse(
                    call: Call<MyPostSetPageData>,
                    response: Response<MyPostSetPageData>
                ) {
                    val responseCode = response.code()

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Log.d("patchSetting 성공", response.body().toString())
                            binding.mySetSwitch1.isChecked = response.body()!!.startTodoAtMonday
                            binding.mySetSwitch2.isChecked = response.body()!!.newTodoStartSetting
                            binding.mySetSwitch3.isChecked = response.body()!!.endTodoBackSetting
                        } else {
                            Log.d("patchSetting response null ", "Response Code: $responseCode")
                        }
                    } else {
                        Log.d("patchSetting 실패", "Response Code: $responseCode")
                    }
                }

                override fun onFailure(call: Call<MyPostSetPageData>, t: Throwable) {
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
