package com.mada.myapplication.ChartFunction.Fragment

import android.annotation.SuppressLint
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.mada.myapplication.ChartFunction.Adaptor.ChartWeekCategoryAdapter
import com.mada.myapplication.ChartFunction.Calendar.MyWeekSliderlAdapter
import com.mada.myapplication.ChartFunction.Data.ChartWeekData
import com.mada.myapplication.ChartFunction.Data.WeekPieData
import com.mada.myapplication.ChartFunction.RetrofitServiceChart
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.MyFunction.Data.FragMyData
import com.mada.myapplication.MyFunction.RetrofitServiceMy
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.ChartWeekBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragChartWeek : Fragment() {
    private lateinit var binding: ChartWeekBinding
    lateinit var navController: NavController
    val datas = mutableListOf<ChartWeekData>()
    val api = RetrofitInstance.getInstance().create(RetrofitServiceChart::class.java)
    val apiMy = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
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

        apiMy.selectfragMy(token).enqueue(object : Callback<FragMyData> {
            override fun onResponse(call: Call<FragMyData>, response: Response<FragMyData>) {
                if(response.isSuccessful){
                    var nick = response.body()?.data?.nickname
                    binding.recordTitle0.text = "${nick}님의 주별 통계입니다."
                }
                else{
                    Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<FragMyData>, t: Throwable) {
                Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })


        //달력 부분
        val calendarAdapter = MyWeekSliderlAdapter(
            this,
            binding.textCalendar,
            binding.calendar2,
            null
        )
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
        Log.d("weekchange", "${month}월 ${iweek}주 ${date}일")
        setBarChartView(month, iweek, date)
        setPieChartView(month, iweek, date)
        setLineChartView(month, iweek, date)
        initCategoryRecycler(month, iweek, date)
        initCategoryPieChart(month, iweek, date)
        initBarChart(month, iweek, date)
        initLineChart(month, iweek, date)
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
                Log.d("chartGetWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("setBarChartView 성공", response.body().toString())

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
                    Log.d("setBarChartView 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeek 실패")
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

                if (response.isSuccessful) {
                    Log.d("setPieChartView 성공", response.body().toString())

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
                    Log.d("setPieChartView 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeek 실패")
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

                if (response.isSuccessful) {
                    Log.d("setLineChartView 성공", response.body().toString())

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
                    Log.d("setLineChartView 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeek 실패")
            }
        })

    }

    // 원형그래프 우측 카테고리 리사이클러뷰 설정
    private fun initCategoryRecycler(month : Int, iweek : Int, sdate : String) {
        val adapter = ChartWeekCategoryAdapter(requireContext())
        val manager = LinearLayoutManager(requireContext())

        // 서버 데이터 연결
        api.chartGetWeek(token,date = sdate).enqueue(object : retrofit2.Callback<ChartWeekData> {
            override fun onResponse(
                call: Call<ChartWeekData>,
                response: Response<ChartWeekData>
            ) {
                val responseCode = response.code()
                Log.d("initCategoryRecycler", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("initCategoryRecycler 성공", response.body().toString())

                    val categoryStatistics = response.body()?.categoryStatistics
                    val CategoryDatas = ArrayList<WeekPieData>()

                    categoryStatistics?.let { stats ->
                        // 데이터가 4개 이상인 경우
                        if (stats.size >= 4) {
                            for (i in 0 until 4) {
                                val weekPieData = stats[i]
                                CategoryDatas.add(weekPieData)
                            }
                        } else {
                            // 데이터가 4개 미만인 경우
                            val totalRate = stats.sumByDouble { it.rate.toDouble() }
                            var remainingRate = 100.0

                            // 기존 데이터 추가
                            stats.forEach { weekPieData ->
                                CategoryDatas.add(weekPieData)
                                remainingRate -= weekPieData.rate.toDouble()
                            }

                            // 나머지 비율을 "기타" 카테고리로 추가
                            val otherCategory = WeekPieData(
                                categoryName = "기타",
                                rate = remainingRate.toFloat(),
                                color = "#000000"
                            )
                            CategoryDatas.add(otherCategory)
                        }
                    }

                    // 어댑터에 데이터 설정
                    adapter.datas = CategoryDatas

                    // 리사이클러뷰에 어댑터 및 레이아웃 매니저 설정
                    binding.myCategoryRecycler.adapter = adapter
                    binding.myCategoryRecycler.layoutManager = manager
                } else {
                    Log.d("initCategoryRecycler 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeek 실패")
            }
        })

    }

    // 원형그래프 데이터셋 설정
    private fun initCategoryPieChart(month : Int, iweek : Int, sdate : String) {
        binding.PieChart.setUsePercentValues(true)

        // 서버 데이터 연결
        api.chartGetWeek(token,date = sdate).enqueue(object : retrofit2.Callback<ChartWeekData> {
            override fun onResponse(
                call: Call<ChartWeekData>,
                response: Response<ChartWeekData>
            ) {
                val responseCode = response.code()
                Log.d("initCategoryPieChart", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("initCategoryPieChart 성공", response.body().toString())

                    val entries = ArrayList<PieEntry>()
                    val colorsItems = ArrayList<Int>()
                    val categoryStatistics = response.body()?.categoryStatistics

                    categoryStatistics?.forEach { category ->
                        val categoryName = category.categoryName
                        val percentNum = category.rate
                        val colorCode = Color.parseColor(category.color)

                        // PieEntry에 데이터 추가
                        entries.add(PieEntry(percentNum, categoryName))
                        colorsItems.add(colorCode)
                    }

                    // PieDataSet 초기화
                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.apply {
                        colors = colorsItems
                        valueTextColor = Color.BLACK
                        valueTextSize = 12f
                        setDrawValues(false) // 차트 내 수치 값 표시 비활성화
                    }

                    // PieData 초기화
                    val pieData = PieData(pieDataSet)

                    // PieChart 설정
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
                    Log.d("initCategoryPieChart 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeek 실패")
            }
        })


    }

    // 막대그래프 데이터셋 설정
    private fun initBarChart(month : Int, iweek : Int, sdate : String) {

        // 서버 데이터 연결
        api.chartGetWeek(token, date = "${sdate}").enqueue(object : retrofit2.Callback<ChartWeekData> {
            @SuppressLint("ResourceType")
            override fun onResponse(
                call: Call<ChartWeekData>,
                response: Response<ChartWeekData>
            ) {
                val responseCode = response.code()
                Log.d("initBarChart", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("initBarChart 성공", response.body()?.todoStatistics.toString())
                    Log.d("todoStatistics 크기: ", response.body()?.todoStatistics?.size.toString())

                    val entries = ArrayList<BarEntry>()
                    val todoStatistics = response.body()?.todoStatistics ?: ArrayList()
                    val formatSize = minOf(todoStatistics.size, 4) // 최대 4개의 데이터만 사용

                    for (i in 0 until formatSize) {
                        val x = i.toFloat()
                        val y =  todoStatistics[formatSize-(i+1)].countCompleted
                        entries.add(BarEntry(x,y))
                    }

                    binding.BarChart.run {
                        description.isEnabled = false // 차트 옆에 별도로 표기되는 description을 안보이게 설정 (false)
                        setMaxVisibleValueCount(4) // 최대 보이는 그래프 개수를 4개로 지정
                        setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
                        setDrawBarShadow(false) //그래프의 그림자
                        setDrawGridBackground(false)//격자구조 넣을건지
                        val maxValue = entries.maxByOrNull { it.y }?.y ?: 0f // entries에서 y값이 가장 큰 값 찾기

                        axisLeft.run { //왼쪽 축. 즉 Y방향 축
                            axisMaximum = maxValue // 최대값 설정
                            axisMinimum = 0f // 최소값 0
                            granularity = (maxValue - 0f) / 5 // 최대값-최소값을 기준으로 5개의 선을 사이에 그림
                            setDrawLabels(true) // 선 옆에 값 적는거
                            setDrawGridLines(true) //격자 라인 활용
                            setDrawAxisLine(false) // 축 그리기 설정
                            gridColor = ContextCompat.getColor(context, R.color.grey3) // 축 아닌 격자 색깔 설정
                            textColor = ContextCompat.getColor(context, R.color.grey3) // 라벨 텍스트 컬러 설정
                            textSize = 12f //라벨 텍스트 크기
                        }
                        xAxis.run {
                            position = XAxis.XAxisPosition.BOTTOM // X축을 아래에다가 둔다.
                            granularity = 1f // 1 단위만큼 간격 두기
                            setDrawAxisLine(true) // 축 그림
                            setDrawGridLines(false) // 격자 그림
                            setDrawLabels(false) // 라벨 그림
                        }
                        axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌
                        setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
                        animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
                        legend.isEnabled = false //차트 범례 설정
                    }

                    // 막대그래프의 막대 색상을 설정
                    val gradientDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_bar_gradient)
                    val colors = ArrayList<Int>()
                    for (i in 0 until formatSize) {
                        if (i == formatSize - 1) {
                            // 마지막 막대의 색상을 설정
                            Log.d("마지막 막대 색칠", "i=${i}")
                            colors.add(ContextCompat.getColor(requireContext(), R.color.main))
                        } else {
                            // 앞의 막대의 색상을 설정
                            Log.d("앞의 막대 색칠", "i=${i}")
                            colors.add(ContextCompat.getColor(requireContext(), R.color.grey2))
                        }
                    }

                    val set = BarDataSet(entries, "DataSet")
                    set.colors = colors

                    val dataSet :ArrayList<IBarDataSet> = ArrayList()
                    dataSet.add(set)

                    val data = BarData(set)
                    data.barWidth = 0.25f //막대 너비 설정

                    binding.BarChart.run {
                        this.data = data //차트의 데이터를 data로 설정해줌.
                        setFitBars(true)
                        invalidate()
                    }

                } else {
                    Log.d("initBarChart 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeek 실패")
            }
        })

    }

    // 꺾은선그래프 데이터셋 설정
    private fun initLineChart(month : Int, iweek : Int, sdate : String){
        // 서버 데이터 연결
        api.chartGetWeek(token, date = "${sdate}").enqueue(object : retrofit2.Callback<ChartWeekData> {
            override fun onResponse(
                call: Call<ChartWeekData>,
                response: Response<ChartWeekData>
            ) {
                val responseCode = response.code()
                Log.d("initLineChart", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("initLineChart 성공", response.body()?.achievementStatistics.toString())

                    val achievementStatistics = response.body()?.achievementStatistics ?: ArrayList()
                    val formatSize = minOf(achievementStatistics.size, 6) // 최대 6개의 데이터만 사용

                    //y축
                    val entries: MutableList<Entry> = mutableListOf()
                    for (i in 0 until formatSize) {
                        val x = i.toFloat()
                        val y =  achievementStatistics[formatSize-(i+1)].achievementRate
                        entries.add(Entry(x,y))
                    }
                    val lineDataSet = LineDataSet(entries,"entries")
                    val lineChart = binding.LineChart

                    lineDataSet.apply {
                        color = resources.getColor(R.color.linechart1, null)
                        circleRadius = 5f
                        lineWidth = 2f
                        setCircleColor(resources.getColor(R.color.linechart2, null))
                        circleHoleColor = resources.getColor(R.color.linechart2, null)
                        setDrawHighlightIndicators(false)
                        setDrawValues(false) // 숫자표시
                        valueTextColor = resources.getColor(R.color.linechart2, null)
                        valueFormatter = DefaultValueFormatter(1)  // 소숫점 자릿수 설정
                        valueTextSize = 10f
                    }

                    //차트 전체 설정
                    lineChart.apply {
                        axisRight.isEnabled = false
                        axisLeft.isEnabled = false
                        xAxis.isEnabled = false
                        legend.isEnabled = false   //legend 사용여부
                        description.isEnabled = false //주석
                        isDragXEnabled = false   // x 축 드래그 여부
                        isScaleYEnabled = false //y축 줌 사용여부
                        isScaleXEnabled = false //x축 줌 사용여부
                        setPinchZoom(false)
                        setScaleEnabled(false)
                        isDoubleTapToZoomEnabled = false
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
                        legend.setOrientation(Legend.LegendOrientation.VERTICAL)
                        legend.setDrawInside(true)
                        xAxis.setLabelCount(6, true)
                    }

                    binding.LineChart.apply {
                        data = LineData(lineDataSet)
                        notifyDataSetChanged() //데이터 갱신
                        invalidate() // view갱신
                    }

                } else {
                    Log.d("initLineChart 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChartWeekData>, t: Throwable) {
                Log.d("서버 오류", "chartGetWeek 실패")
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