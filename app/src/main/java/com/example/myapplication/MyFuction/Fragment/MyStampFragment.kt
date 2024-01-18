package com.example.myapplication.MyFuction.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.MyStampBinding
import java.util.Calendar

class MyStampFragment : Fragment() {
    private lateinit var binding: MyStampBinding
    lateinit var navController: NavController
    private var isButtonClicked = false

    // retrofit
    private var token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2NGpySjgxclkxMEY5OEduM01VM3NON3huRkQ4SEhnN3hmb18xckZFdmRZIiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjM3NDYwOCwiZXhwIjoxNjkyNDEwNjA4fQ.FWaurv6qy-iiha07emFxGIZjAnwL3fluFsZSQY-AvlmBBsHe5ZtfRL69l6zP1ntOGIWEGb5IbCLd5JP4MjWu4w"

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
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1  // 월은 0부터 시작하므로 +1
        val daysInCurrentMonth = getDaysInMonth(currentYear, currentMonth)
        setStampNumInDaysCnt(daysInCurrentMonth)

        // 도장 찍기 & 이미 찍은 거 불러오기(임시데이터)
        val stampCnt = 21

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

        for (i in 0 until stampCnt) {
            stampTexts[i].isVisible = false
            stampViews[i].setBackgroundResource(R.drawable.stamp_my)
        }

        stampTexts[stampCnt].isVisible = false
        stampViews[stampCnt].setBackgroundResource(R.drawable.stamp_click)

        // 스탬프 클릭
        stampViews[stampCnt].setOnClickListener{
            stampViews[stampCnt].setBackgroundResource(R.drawable.stamp_my)
            // 서버 연결
        }

        // 엑스 버튼
        binding.cancelBtn.setOnClickListener {
            navController.navigate(R.id.action_myStampFragment_to_fragMy)
        }

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
            }
            28 -> {
                binding.stamp29.isVisible = false
                binding.connect7.isVisible = false
                binding.textView29.isVisible = false
            }
            else -> {
                // 31일
            }
        }
    }
}