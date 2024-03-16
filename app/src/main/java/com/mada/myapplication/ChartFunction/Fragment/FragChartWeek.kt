package com.mada.myapplication.ChartFunction.Fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.mada.myapplication.ChartFunction.Adaptor.MyRecordCategoryAdapter
import com.mada.myapplication.ChartFunction.Calendar.MyWeekSliderlAdapter
import com.mada.myapplication.ChartFunction.Data.ChartWeekData
import com.mada.myapplication.ChartFunction.RetrofitServiceChart
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.ChartWeekBinding
import retrofit2.Call
import retrofit2.Response

class FragChartWeek : Fragment() {
    private lateinit var binding: ChartWeekBinding
    lateinit var navController: NavController
    val datas = mutableListOf<ChartWeekData>()
    val api = RetrofitInstance.getInstance().create(RetrofitServiceChart::class.java)
    val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChartWeekBinding.inflate(inflater, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isGone = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()

        binding.btnMONTH.setOnClickListener {
            navController.navigate(R.id.action_fragChartWeek_to_fragChartMonth)
        }

        binding.btnTODAY.setOnClickListener {
            navController.navigate(R.id.action_fragChartWeek_to_fragChartDay)
        }


        //달력 부분
        val calendarAdapter = MyWeekSliderlAdapter(this,binding.textCalendar,binding.calendar2)
        binding.calendar2.adapter = calendarAdapter
        binding.calendar2.setCurrentItem(CalendarSliderAdapter.START_POSITION, false)
        binding.preBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(binding.calendar2.currentItem-1, true)
        }

        binding.nextBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(binding.calendar2.currentItem+1, true)
        }


        // 시스템 뒤로가기
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                navController.navigate(R.id.action_fragChartWeek_to_fragChartDay)
                return@OnKeyListener true
            }
            false
        })

    }
    fun weekChange(month : Int, iweek : Int, date : String) {
        setBarChartView(month, iweek, date)
        setPieChartView(month, iweek, date)
        setLineChartView(month, iweek, date)
        initCategoryRecycler(month, iweek, date)
        initCategoryPieChart(month, iweek, date)
    }


    // 막대그래프 뷰 설정
    private fun setBarChartView(month : Int, iweek : Int, sdate : String) {

        // 서버 데이터 연결
        api.chartGetWeek(token, date = sdate).enqueue(object : retrofit2.Callback<ChartWeekData> {
            override fun onResponse(
                call: Call<ChartWeekData>,
                response: Response<ChartWeekData>
            ) {
                val responseCode = response.code()
                Log.d("chartGetWeekWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("chartGetWeekMonth 성공", response.body().toString())

                    val totalTodoCnt = 999
                    val completeTodoCnt = 9
                    val completeTodoPercent = 99
                    val compareTodoCnt = 9.9

                    val weekOfMonthText = when (iweek) {
                        1 -> "첫째"
                        2 -> "둘째"
                        3 -> "셋째"
                        4 -> "넷째"
                        5 -> "다섯째"
                        else -> "Invalid week"
                    }

                    val colorText0 = "${weekOfMonthText} 주"
                    val colorText1 = "총 ${completeTodoCnt}개"

                    // SpannableStringBuilder 생성 및 색상 적용 함수 호출
                    binding.recordTitle0.text = createSpannableString("${colorText0} 통계예요.", colorText0)
                    binding.recordTitle1.text = createSpannableString("이번 주 ${colorText1} 완료했어요", colorText1)
                    binding.recordContext1.text = "이번 주에는 ${totalTodoCnt}개의 투두 중에서\n" +
                            "평균 $completeTodoPercent%인 ${completeTodoCnt}개의 투두를 완료했어요.\n" +
                            "지난 주에 비해 ${compareTodoCnt}개 상승했네요."


                } else {
                    Log.d("chartGetWeekWeek 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeekWeek 실패")
            }
        })
    }

    // 원형그래프 뷰 설정
    private fun setPieChartView(month : Int, iweek : Int, sdate : String) {

        // 서버 데이터 연결
        api.chartGetWeek(token, date = sdate).enqueue(object : retrofit2.Callback<ChartWeekData> {
            override fun onResponse(
                call: Call<ChartWeekData>,
                response: Response<ChartWeekData>
            ) {
                val responseCode = response.code()
                Log.d("chartGetWeekWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("chartGetWeekWeek 성공", response.body().toString())

                    val categoryStatistics = response.body()?.categoryStatistics
                    val size = response.body()?.categoryStatistics?.size
                    val addTodoCnt = 9
                    val averageCompleteTodoCnt = 9.9
                    val compareCompleteTodoText = "{1.2}개 상승"

                    val colorText1 = "총 ${addTodoCnt}개"

                    val formattedText1 = "이번 주 ${colorText1} 추가했어요"
                    var formattedText2 = ""

                    if (categoryStatistics.isNullOrEmpty()) {
                        formattedText2 = "추가한 카테고리가 없어요"
                    } else{
                        val c1 = categoryStatistics[0].categoryName
                        formattedText2 =
                            "이번 주에는 ${c1} 카테고리에서" +
                                    "\n평균 ${averageCompleteTodoCnt}개로 가장 많은 투두를 완료했어요." +
                                    "\n지난 주에 비해 ${compareCompleteTodoText}했네요."
                    }

                    // SpannableStringBuilder 생성 및 색상 적용 함수 호출
                    binding.recordTitle2.text = createSpannableString(formattedText1, colorText1)
                    binding.recordContext2.text = formattedText2

                } else {
                    Log.d("chartGetWeekWeek 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeekWeek 실패")
            }
        })

    }

    // 꺾은선그래프 뷰 설정
    private fun setLineChartView(month : Int, iweek : Int, sdate : String) {

        // 서버 데이터 연결
        api.chartGetWeek(token, date = sdate).enqueue(object : retrofit2.Callback<ChartWeekData> {
            override fun onResponse(
                call: Call<ChartWeekData>,
                response: Response<ChartWeekData>
            ) {
                val responseCode = response.code()
                Log.d("chartGetWeekWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("chartGetWeekWeek 성공", response.body().toString())

                    val totalTodoCnt = 999
                    val completeTodoCnt = 9
                    val compareTodoCnt = 9.9
                    val compareTodoPercent = 99

                    val colorText1 = "${compareTodoPercent}%"

                    val formattedText1 = "투두 달성도가 ${colorText1} 상승했어요"
                    var formattedText2 = ""

                    if (totalTodoCnt==0) {
                        formattedText2 = "추가한 투두가 없어요"
                    } else{
                        formattedText2 =
                            "전체 투두에서 평균적으로 ${completeTodoCnt}개 이상의 투두를 완료하면서 지난 주에 비해서 평균 달성 개수가 ${compareTodoCnt}개 상승했어요"
                    }

                    // SpannableStringBuilder 생성 및 색상 적용 함수 호출
                    binding.recordTitle3.text = createSpannableString(formattedText1, colorText1)
                    binding.recordContext3.text = formattedText2

                } else {
                    Log.d("chartGetWeekMonth 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeek 실패")
            }
        })

    }

    // 통계 우측 카테고리 리사이클러뷰 설정
    private fun initCategoryRecycler(month : Int, iweek : Int, sdate : String) {
        val adapter = MyRecordCategoryAdapter(requireContext())
        val manager = LinearLayoutManager(requireContext())

        // 서버 데이터 연결
        api.chartGetWeek(token, date = sdate).enqueue(object : retrofit2.Callback<ChartWeekData> {
            override fun onResponse(
                call: Call<ChartWeekData>,
                response: Response<ChartWeekData>
            ) {
                val responseCode = response.code()
                Log.d("chartGetWeekWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("chartGetWeekWeek 성공", response.body().toString())
                    datas.clear()

                    datas.apply{
                        val categoryStatistics = response.body()?.categoryStatistics
                        categoryStatistics?.forEach { category ->
                            val categoryName = category.categoryName
                            val percentNum = category.rate
                            val colorCode = category.color

                            //0-3 넣기
                        }

                        //adapter.datas = datas
                        adapter.notifyDataSetChanged()

                    }
                    binding.myCategoryRecycler.adapter= adapter
                    binding.myCategoryRecycler.layoutManager= manager

                } else {
                    Log.d("chartGetWeekWeek 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeekWeek 실패")
            }
        })

    }

    // 통계 좌측 파이차트 뷰 설정
    private fun initCategoryPieChart(month : Int, iweek : Int, sdate : String) {
        binding.PieChart.setUsePercentValues(true)

        // 서버 데이터 연결
        api.chartGetWeek(token, date = sdate).enqueue(object : retrofit2.Callback<ChartWeekData> {
            override fun onResponse(
                call: Call<ChartWeekData>,
                response: Response<ChartWeekData>
            ) {
                val responseCode = response.code()
                Log.d("chartGetWeekWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("chartGetWeekWeek 성공", response.body().toString())

                    val entries = ArrayList<PieEntry>()
                    val colorsItems = ArrayList<Int>()

                    val categoryStatistics = response.body()?.categoryStatistics
                    categoryStatistics?.forEach { category ->
                        val categoryName = category.categoryName
                        val percentNum = category.rate
                        val colorCode = Color.parseColor(category.color)

                        // 파이차트 수치, 이름
                        entries.add(PieEntry(percentNum, categoryName))
                        colorsItems.add(colorCode)
                    }

                    // 데이터셋 초기화
                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.apply {
                        colors = colorsItems
                        valueTextColor = Color.BLACK
                        valueTextSize = 12f
                        setDrawValues(false) // 차트 내 수치 값 표시 비활성화
                    }

                    // 데이터셋 세팅
                    val pieData = PieData(pieDataSet)
                    binding.PieChart.apply {
                        data = pieData
                        isRotationEnabled = false
                        description.isEnabled = false // 차트 내 항목 값 표시 비활성화
                        legend.isEnabled = false // 범례 비활성화
                        setDrawEntryLabels(false) // 라벨 비활성화
                        setDrawMarkers(false) // 차트 위에 마우스포인트 올릴 시 마커(필요 시 뷰 구현하겠음!)
                        animateY(1400, Easing.EaseInOutQuad) // 시계방향 애니메이션
                        animate()
                    }
                } else {
                    Log.d("chartGetWeekWeek 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeekWeek 실패")
            }
        })


    }


    // 텍스트에 색상을 적용하는 함수
    private fun createSpannableString(text: String, targetText: String): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(text)
        val start = text.indexOf(targetText)
        val end = start + targetText.length

        if (start != -1) {
            val colorSpan = ForegroundColorSpan(Color.parseColor("#C3497D"))
            spannableStringBuilder.setSpan(colorSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        return spannableStringBuilder
    }

    // 텍스트를 굵게 적용하는 함수
    private fun createSpannableStringBold(text: String, targetText: String): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(text)
        val start = text.indexOf(targetText)
        val end = start + targetText.length

        if (start != -1) {
            spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return spannableStringBuilder
    }

}