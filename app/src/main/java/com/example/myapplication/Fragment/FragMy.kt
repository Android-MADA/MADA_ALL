package com.example.myapplication.Fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.CharacterResponse
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Activity.MyAlarmActivity
import com.example.myapplication.MyFuction.Data.FragMyData
import com.example.myapplication.MyFuction.Adapter.MyListAdapter
import com.example.myapplication.MyFuction.Adapter.MyListItem
import com.example.myapplication.MyFuction.Activity.MyNoticeActivity
import com.example.myapplication.MyFuction.Activity.MyPremiumActivity
import com.example.myapplication.MyFuction.Activity.MyProfileActivity
import com.example.myapplication.MyFuction.Activity.MyRecordDayActivity
import com.example.myapplication.MyFuction.Activity.MySetActivity
import com.example.myapplication.MyFuction.Activity.MyWithdraw1Activity
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.FragMyBinding
import com.example.myapplication.databinding.Splash2Binding
import com.squareup.picasso.Picasso
import retrofit2.converter.gson.GsonConverterFactory


class FragMy : Fragment() {

    private lateinit var binding: FragMyBinding

    //서버연결 시작
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")


    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragMyBinding.inflate(inflater, container, false)

        binding.myRecordBtn.setOnClickListener{
            val intent = Intent(requireContext(), MyRecordDayActivity::class.java)
            startActivity(intent)
        }

        getCustomChar()

        // 리스트
        val MyList = arrayListOf (
            MyListItem("프로필 편집", MyProfileActivity::class.java),
            MyListItem("화면 설정", MySetActivity::class.java),
            MyListItem("알림 설정", MyAlarmActivity::class.java),
            MyListItem("구글 캘린더 연동", MyAlarmActivity::class.java),
            MyListItem("공지사항", MyNoticeActivity::class.java),
            MyListItem("Premium 구독", MyPremiumActivity::class.java),
            MyListItem("로그아웃", Splash2Binding::class.java),
            MyListItem("회원탈퇴", MyWithdraw1Activity::class.java),
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
                    binding.myNickname.text = "안녕하세요, "+"${response.body()!!.data.nickname}"+"님!"
                    binding.sayingContent.text = response.body()!!.data.saying[0].content
                    binding.sayingSayer.text = response.body()!!.data.saying[0].sayer
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
    // 캐릭터 커스텀 불러오기
    private fun getCustomChar() {
        val call2 = api.characterRequest(token)
        call2.enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(call2: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.data.datas
                        if(datas != null) {
                            for (data in datas) {
                                //arrays.add(data)
                                //Log.d("111","datas: ${data.id} ${data.itemType} ${data.filePath}")
                                if(data.itemType=="color") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.myRamdi)
                                } else if(data.itemType=="set") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.imgMyCloth)
                                } else if(data.itemType=="item") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.imgMyItem)
                                }
                                // ...
                            }
                        } else {
                            //Log.d("2221","${response.code()}")
                        }
                    } else {
                        //Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                    //Log.d("3331","itemType: ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                //Log.d("444","itemType: ${t.message}")
            }
        })
    }
}