package com.example.myapplication.MyFuction.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Data.MyGetNoticesData
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.MyNoticeBinding
import retrofit2.Call
import retrofit2.Response

class MyNoticeFragment : Fragment() {

    private lateinit var binding: MyNoticeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyNoticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            // nav
        }

        // 서버연결 시작
        val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
        val token = Splash2Activity.prefs.getString("token", "")

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
                    binding.title1.text = response.body()!!.data[0].title
                    binding.title2.text = response.body()!!.data[1].title
                    binding.content1.text = response.body()!!.data[0].content
                    binding.content2.text = response.body()!!.data[1].content
                    binding.date1.text = response.body()!!.data[0].date
                    binding.date2.text = response.body()!!.data[1].date

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
