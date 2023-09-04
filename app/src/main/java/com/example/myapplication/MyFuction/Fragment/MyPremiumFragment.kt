package com.example.myapplication.MyFuction.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Data.MyPremiumData
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.MyPremiumBinding
import retrofit2.Call
import retrofit2.Response

class MyPremiumFragment : Fragment() {

    private lateinit var binding: MyPremiumBinding
    private val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    private val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyPremiumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            // nav
        }

        binding.myPremiumBtn.setOnClickListener {
            // nav
        }
    }

    private fun patchPremiumSetting(isSetting: Boolean) {
        api.mySetPremium(token, isSetting).enqueue(object : retrofit2.Callback<MyPremiumData> {
            override fun onResponse(
                call: Call<MyPremiumData>, response: Response<MyPremiumData>
            ) {
                val responseCode = response.code()

                if (response.isSuccessful) {
                    Log.d("프리미엄 변경 성공, ${response.body()?.is_subscribe}", "Response Code: $responseCode")
                } else {
                    Log.d("프리미엄 변경 실패", "Response Code: $responseCode")
                }
            }

            override fun onFailure(call: Call<MyPremiumData>, t: Throwable) {
                Log.d("서버 오류", "프리미엄 변경 실패")
            }
        })
    }
}
