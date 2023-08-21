package com.example.myapplication.Fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CustomFunction.RetrofitServiceCustom
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Model.FragMyData
import com.example.myapplication.MyFuction.Model.MyChangeNicknameData2
import com.example.myapplication.MyFuction.Model.MyGetNoticesData
import com.example.myapplication.MyFuction.Model.MyGetProfileData
import com.example.myapplication.MyFuction.MyListAdapter
import com.example.myapplication.MyFuction.MyListItem
import com.example.myapplication.MyFuction.MyLogoutPopupActivity
import com.example.myapplication.MyFuction.MyNoticeActivity
import com.example.myapplication.MyFuction.MyNoticeSetActivity
import com.example.myapplication.MyFuction.MyPremiumActivity
import com.example.myapplication.MyFuction.MyProfileActivity
import com.example.myapplication.MyFuction.MyRecordDayActivity
import com.example.myapplication.MyFuction.MySetActivity
import com.example.myapplication.MyFuction.MyWebviewActivity
import com.example.myapplication.MyFuction.MyWithdraw1Activity
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.FragMyBinding
import retrofit2.converter.gson.GsonConverterFactory


class FragMy : Fragment() {

    private lateinit var binding: FragMyBinding

    //서버연결 시작
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = MyWebviewActivity.prefs.getString("token", "")


    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragMyBinding.inflate(inflater, container, false)

        binding.myRecordBtn.setOnClickListener{
            val intent = Intent(requireContext(), MyRecordDayActivity::class.java)
            startActivity(intent)
        }

        // 리스트
        val MyList = arrayListOf (
            MyListItem("프로필 편집", MyProfileActivity::class.java),
            MyListItem("화면 설정", MySetActivity::class.java),
            MyListItem("알림", MyNoticeSetActivity::class.java),
            MyListItem("공지사항", MyNoticeActivity::class.java),
            MyListItem("Premium 구독", MyPremiumActivity::class.java),
            MyListItem("로그아웃", Splash2Activity::class.java),
            MyListItem("회원 탈퇴", MyWithdraw1Activity::class.java),
        )

        binding.rvMyitem.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
        binding.rvMyitem.setHasFixedSize(true)
        binding.rvMyitem.adapter = MyListAdapter(MyList)

        // 구분선 커스텀
        val dividerItemDecoration = object : DividerItemDecoration(binding.rvMyitem.getContext(),
            LinearLayoutManager(this.context).orientation) {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val itemCount = MyList.size

                // 마지막 아이템이 아닌 경우에만 구분선 추가
                if (position < itemCount - 1) {
                    super.getItemOffsets(outRect, view, parent, state)
                } else {
                    outRect.setEmpty() // 마지막 아이템인 경우 구분선 간격을 0으로 설정하여 표시하지 않음 (적용되지 않음, 일단 보류)
                }
            }
        }

        // 서버 데이터 연결
        api.selectfragMy(token).enqueue(object : retrofit2.Callback<FragMyData> {
            override fun onResponse(
                call: Call<FragMyData>,
                response: Response<FragMyData>
            ) {
                val responseCode = response.code()
                Log.d("selectfragMy", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("selectfragMy 성공", response.body().toString())
                    if (response.body()!!.data.subscribe == true) binding.userType.text = "프리미엄 유저"
                    else binding.userType.text = "일반 유저"
                    binding.myNickname.text = "안녕하세요"+"${response.body()!!.data.nickname}"+"님!"
                    binding.sayingContent.text = response.body()!!.data.saying.content
                    binding.sayingSayer.text = response.body()!!.data.saying.sayer
                } else {
                    Log.d("selectfragMy 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<FragMyData>, t: Throwable) {
                Log.d("서버 오류", "selectfragMy 실패")
            }
        })

        binding.rvMyitem.addItemDecoration(dividerItemDecoration)
        return binding.root

    }

}