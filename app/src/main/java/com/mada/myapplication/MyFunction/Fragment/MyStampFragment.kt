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
import com.mada.myapplication.MyFunction.Data.MyGetStampData
import com.mada.myapplication.MyFunction.Data.MyPatchStampData
import com.mada.myapplication.MyFunction.RetrofitServiceMy
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.MyStampBinding
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

class MyStampFragment : Fragment() {
    private lateinit var binding: MyStampBinding
    lateinit var navController: NavController
    private val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    private val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyStampBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        // 도장판의 개수 조정
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1  // 월의 초기값만 0
        val daysInCurrentMonth = getDaysInMonth(currentYear, currentMonth)
        setStampNumInDaysCnt(daysInCurrentMonth)

        // 도장 바인딩
        val stampViews = arrayOf(
            binding.stamp1,
            binding.stamp2,
            binding.stamp3,
            binding.stamp4,
            binding.stamp5,
            binding.stamp6,
            binding.stamp7,
            binding.stamp8,
            binding.stamp9,
            binding.stamp10,
            binding.stamp11,
            binding.stamp12,
            binding.stamp13,
            binding.stamp14,
            binding.stamp15,
            binding.stamp16,
            binding.stamp17,
            binding.stamp18,
            binding.stamp19,
            binding.stamp20,
            binding.stamp21,
            binding.stamp22,
            binding.stamp23,
            binding.stamp24,
            binding.stamp25,
            binding.stamp26,
            binding.stamp27,
            binding.stamp28,
            binding.stamp29,
            binding.stamp30,
            binding.stamp31
        )
        val stampTexts = arrayOf(
            binding.textView1,
            binding.textView2,
            binding.textView3,
            binding.textView4,
            binding.textView5,
            binding.textView6,
            binding.textView7,
            binding.textView8,
            binding.textView9,
            binding.textView10,
            binding.textView11,
            binding.textView12,
            binding.textView13,
            binding.textView14,
            binding.textView15,
            binding.textView16,
            binding.textView17,
            binding.textView18,
            binding.textView19,
            binding.textView20,
            binding.textView21,
            binding.textView22,
            binding.textView23,
            binding.textView24,
            binding.textView25,
            binding.textView26,
            binding.textView27,
            binding.textView28,
            binding.textView29,
            binding.textView30,
            binding.textView31
        )
        var stampCnt = 0

        // 도장판 불러오기
        api.myGetStamp(token).enqueue(object : retrofit2.Callback<MyGetStampData> {
            override fun onResponse(call: Call<MyGetStampData>, response: Response<MyGetStampData>) {
                val responseCode = response.code()
                Log.d("getStamp", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("getStamp 성공", response.body().toString())
                    val stampCnt = response.body()?.data!!.data
                    Log.d("stampCnt 개수 : ", "${stampCnt}")

                    for (i in 0 until stampCnt) {
                        stampTexts[i].isVisible = false
                        stampViews[i].setBackgroundResource(R.drawable.stamp_my)
                    }

                    stampTexts[stampCnt+1].isVisible = false
                    stampViews[stampCnt+1].setBackgroundResource(R.drawable.stamp_click)
                } else {
                    Log.d("getStamp 실패", "$responseCode")
                }
            }

            override fun onFailure(call: Call<MyGetStampData>, t: Throwable) {
                Log.d("서버 오류", "getStamp 실패")
            }
        })


        // 도장 찍기
        stampViews[stampCnt].setOnClickListener{
            stampViews[stampCnt].setBackgroundResource(R.drawable.stamp_my)
            addStampCnt(stampCnt)
        }

        // 엑스 버튼
        binding.cancelBtn.setOnClickListener {
            navController.navigate(R.id.action_myStampFragment_to_fragMy)
        }

    }

    // 도장 개수를 더하는 함수
    private fun addStampCnt(stampCnt: Int){
        api.myPatchStamp(token, stampCnt).enqueue(object : retrofit2.Callback<MyPatchStampData> {
            override fun onResponse(call: Call<MyPatchStampData>, response: Response<MyPatchStampData>) {
                val responseCode = response.code()

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("stampCnt: ${response.body()?.data!!.data}", "Response Code: $responseCode")
                    } else
                        Log.d("stampCnt: null", "Response Code: $responseCode")
                } else {
                    Log.d("addStampCnt 실패", "Response Code: ${response.code()} ")
                }
            }

            override fun onFailure(call: Call<MyPatchStampData>, t: Throwable) {
                Log.d("서버 오류", "addStampCnt 실패")
            }
        })
    }

    // 해당 월의 마지막 날짜를 받아오는 함수
    fun getDaysInMonth(year: Int, month: Int): Int {
        // Calendar 객체 생성
        val calendar = Calendar.getInstance()

        // 년도와 월 설정
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)  // 월은 0부터 시작하므로 -1 해줍니다.

        // 현재 월의 마지막 날짜를 가져옵니다.
        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        return lastDay
    }

    // 도장의 개수를 현재 월의 날짜 수만큼 조정하는 함수
    fun setStampNumInDaysCnt(lastDay: Int){

        Log.d("MyStamp", "현재 월의 마지막 날짜: $lastDay")

        when (lastDay) {
            30 -> {
                binding.stamp31.isVisible = false
                binding.textView31.isVisible = false
            }
            29 -> {
                binding.stamp30.isVisible = false
                binding.textView30.isVisible = false
                binding.stamp31.isVisible = false
                binding.textView31.isVisible = false
            }
            28 -> {
                binding.stamp29.isVisible = false
                binding.connect7.isVisible = false
                binding.textView29.isVisible = false
                binding.stamp30.isVisible = false
                binding.textView30.isVisible = false
                binding.stamp31.isVisible = false
                binding.textView31.isVisible = false
            }
            else -> {
                // 31일
            }
        }
    }
}