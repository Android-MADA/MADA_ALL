package com.example.myapplication.MyFuction.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Data.MyGetProfileData
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.MyProfileBinding
import retrofit2.Call
import retrofit2.Response

class MyProfileFragment : Fragment() {

    private lateinit var binding: MyProfileBinding
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_myProfileFragment_to_fragMy)
        }

        binding.nextBtn.setOnClickListener {
            navController.navigate(R.id.action_myProfileFragment_to_myProfileNickFragment)
        }

        // 서버 연결 시작
        val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
        val token = Splash2Activity.prefs.getString("token", "")

        api.myGetProfile(token).enqueue(object : retrofit2.Callback<MyGetProfileData> {
            override fun onResponse(call: Call<MyGetProfileData>, response: Response<MyGetProfileData>) {
                val responseCode = response.code()
                Log.d("getProfile", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("getProfile 성공", response.body().toString())
                    binding.nickname.text = response.body()?.data!!.nickname
                    binding.email.text = response.body()?.data!!.email
                } else {
                    Log.d("getProfile 실패", "$responseCode")
                }
            }

            override fun onFailure(call: Call<MyGetProfileData>, t: Throwable) {
                Log.d("서버 오류", "getProfile 실패")
            }
        })


    }
}
