package com.example.myapplication.MyFuction.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Data.MyChangeNicknameData
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.MyProfileNickBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileNickFragment : Fragment() {

    private lateinit var binding: MyProfileNickBinding
    lateinit var navController: NavController
    private val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    private val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyProfileNickBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_myProfileNickFragment_to_fragMy)
        }

        binding.myNickBtn.setOnClickListener {
            changeNickname(binding.editNickname.text.toString())


        }
    }

    private fun changeNickname(nickName: String) {
        val call1 = api.changeNickname(token, nickName)
        call1.enqueue(object : Callback<MyChangeNicknameData> {

            override fun onResponse(
                call: Call<MyChangeNicknameData>,
                response: Response<MyChangeNicknameData>
            ) {
                val responseCode = response.code()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("닉네임 ${response.body()?.data!!.nickname}으로 변경 성공", "Response Code: $responseCode")
                        navController.navigate(R.id.action_myProfileNickFragment_to_fragMy)
                        Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                    } else
                        Log.d("닉네임 ${response.body()?.data!!.nickname}으로 변경 실패", "Response Code: $responseCode")
                } else {
                    Log.d("닉네임 변경 실패", "Response Code: ${response.code()} ")
                }

            }
            override fun onFailure(call: Call<MyChangeNicknameData>, t: Throwable) {
                Log.d("서버 오류", "닉네임 변경 실패")
            }
        })
    }
}
