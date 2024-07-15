package com.mada.myapplication.MyFunction.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.MyFunction.Data.MyGetNoticesData
import com.mada.myapplication.MyFunction.RetrofitServiceMy
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.MyNoticeBinding
import retrofit2.Call
import retrofit2.Response

class MyNoticeFragment : Fragment() {

    private lateinit var binding: MyNoticeBinding
    lateinit var navController: NavController
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyNoticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_myNoticeFragment_to_fragMy)
        }

        // 서버에서 공지사항 불러오기
        api.myGetNotices(token).enqueue(object : retrofit2.Callback<MyGetNoticesData> {
            override fun onResponse(
                call: Call<MyGetNoticesData>,
                response: Response<MyGetNoticesData>
            ) {
                val responseCode = response.code()
                Log.d("GetNotices", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("GetNotices 성공", response.body().toString())
                    if(response.body()!!.data.isNotEmpty()){
                        when (id) {
                            0 -> {
                                binding.title1.text = response.body()!!.data[1].title
                                binding.content1.text = response.body()!!.data[1].content
                                binding.date1.text = response.body()!!.data[1].date
                            }
                            1 -> {
                                binding.title2.text = response.body()!!.data[2].title
                                binding.content2.text = response.body()!!.data[2].content
                                binding.date2.text = response.body()!!.data[2].date
                            }
                            // 배열크기 받아서 수정 필요
                        }
                    }
                    else{
                        Log.d("GetNotices", "DB가 비어있음")
                        binding.title1.isVisible = false
                        binding.title2.isVisible = false
                        binding.content1.isVisible = false
                        binding.content2.isVisible = false
                        binding.date1.isVisible = false
                        binding.date2.isVisible = false
                        binding.divider4.isVisible = false
                        binding.divider5.isVisible = false
                    }

                } else {
                    Log.d("GetNotices 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<MyGetNoticesData>, t: Throwable) {
                Log.d("서버 오류", "GetNotices 실패")
            }
        })
    }
}
