package com.mada.myapplication.MyFunction.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.MyFunction.Adapter.MyNoticeAdapter
import com.mada.myapplication.MyFunction.Adapter.Notice
import com.mada.myapplication.MyFunction.Data.MyGetNoticesData
import com.mada.myapplication.MyFunction.RetrofitServiceMy
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.MyNoticeBinding
import retrofit2.Call
import retrofit2.Callback
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
        fetchNotices()
    }

    private fun fetchNotices() {
        api.myGetNotices(token).enqueue(object : Callback<MyGetNoticesData> {
            override fun onResponse(
                call: Call<MyGetNoticesData>,
                response: Response<MyGetNoticesData>
            ) {
                val responseCode = response.code()
                Log.d("GetNotices", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("GetNotices 성공", response.body().toString())

                    val dataList = response.body()?.data?.map { Notice(it.title, it.content, it.date) } ?: emptyList()

                    setupRecyclerView(dataList)

                } else {
                    Log.d("GetNotices 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<MyGetNoticesData>, t: Throwable) {
                Log.d("서버 오류", "GetNotices 실패")
            }
        })
    }

    private fun setupRecyclerView(noticeList: List<Notice>) {
        val adapter = MyNoticeAdapter(noticeList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }
}
