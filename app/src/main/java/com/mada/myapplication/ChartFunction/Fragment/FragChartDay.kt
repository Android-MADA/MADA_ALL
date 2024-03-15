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
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.mada.myapplication.ChartFunction.Adaptor.MyRecordCategoryAdapter
import com.mada.myapplication.ChartFunction.Calendar.MyMonthSliderlAdapter
import com.mada.myapplication.ChartFunction.Data.ChartDayData
import com.mada.myapplication.ChartFunction.RetrofitServiceChart
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.ChartDayBinding
import retrofit2.Call
import retrofit2.Response

class FragChartDay : Fragment() {
    private var curMenuItem : Int = 0
    private lateinit var binding: ChartDayBinding
    private lateinit var navController: NavController
    private var mInterstitialAd: InterstitialAd? = null
    val datas = mutableListOf<ChartDayData>()
    val api = RetrofitInstance.getInstance().create(RetrofitServiceChart::class.java)
    val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChartDayBinding.inflate(inflater, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isGone = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전면 광고 부분
        setupInterstitialAd()

        navController = binding.navHostFragmentContainer.findNavController()

        binding.btnWEEK.setOnClickListener {
            navController.navigate(R.id.action_fragChartDay_to_fragChartWeek)
        }

        binding.btnMONTH.setOnClickListener {
            navController.navigate(R.id.action_fragChartDay_to_fragChartMonth)
        }

        //달력 부분
        val calendarAdapter = MyMonthSliderlAdapter(this,binding.textCalendar,binding.calendar2)
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
                navController.navigate(R.id.action_fragChartDay_to_fragHome)
                return@OnKeyListener true
            }
            false
        })

    }

    //날짜 클릭시 실행되는 함수
    fun dayChange(date: String) {
        setBarChartView(date)
        setPieChartView(date)
        setLineChartView(date)
        initCategoryRecycler(date)
        initCategoryPieChart(date)
        initBarChart(date)
        initLineChart(date)
    }


    // 원형그래프 뷰 설정
    private fun setPieChartView(sdate: String) {

        // 서버 데이터 연결
        api.chartGetDay(token,date = sdate).enqueue(object : retrofit2.Callback<ChartDayData> {
            override fun onResponse(
                call: Call<ChartDayData>,
                response: Response<ChartDayData>
            ) {
                val responseCode = response.code()
                Log.d("setPieChartView", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("setPieChartView 성공", response.body().toString())

                    val categoryStatistics = response.body()?.categoryStatistics
                    val size = response.body()?.categoryStatistics?.size
                    val mostCategory = response.body()?.mostCategory
                    val nowCategoryCount = response.body()?.nowCategoryCount
                    val beforeCategoryCount = response.body()?.beforeCategoryCount
                    val beforeCategoryCountText = when {
                        beforeCategoryCount == null -> "NULL"
                        beforeCategoryCount > 0 -> "${beforeCategoryCount}개 많이"
                        beforeCategoryCount < 0 -> "${beforeCategoryCount}개 적게"
                        else -> "똑같이"
                    }

                    val formattedText1 = "${mostCategory}"
                    val formattedText2 = when {
                        categoryStatistics.isNullOrEmpty() -> "추가한 카테고리가 없어요"
                        else -> "오늘은 ${mostCategory} 카테고리에서 ${nowCategoryCount}개 완료했어요." +
                                "\n어제에 비해 ${beforeCategoryCountText} 해내셨네요!"
                    }

                    binding.recordTitle2.text = createSpannableString(formattedText1, formattedText1)
                    binding.recordContext2.text = formattedText2

                } else {
                    Log.d("setPieChartView 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChartDayData>, t: Throwable) {
                Log.d("서버 오류", "setPieChartView 실패")
            }
        })

    }

    // 막대그래프 뷰 설정
    private fun setBarChartView(sdate: String) {

        // 서버 데이터 연결
        api.chartGetDay(token,date = sdate).enqueue(object : retrofit2.Callback<ChartDayData> {
            override fun onResponse(
                call: Call<ChartDayData>,
                response: Response<ChartDayData>
            ) {
                val responseCode = response.code()
                Log.d("setBarChartView", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("setBarChartView 성공", response.body().toString())

                    val nowTotalCount = response.body()?.nowTotalCount
                    val nowCountCompleted = response.body()?.nowCountCompleted
                    val diffCountB = response.body()?.diffCount
                    val diffCountBText = when {
                        diffCountB == null -> "NULL"
                        diffCountB > 0 -> "어제보다 ${diffCountB}개 더"
                        diffCountB < 0 -> "어제보다 ${diffCountB}개 덜"
                        else -> "어제만큼"
                    }

                    val colorText0 = "총 ${nowCountCompleted}개"

                    val formattedText0 = "오늘 ${colorText0} 완료했어요"
                    val formattedText1 = "오늘은 ${nowTotalCount}개의 투두 중에서" +
                            "\n${nowCountCompleted}개의 투두를 완료했어요." +
                            "\n${diffCountBText} 열심히 하루를 보내셨네요!"

                    binding.recordTitle2.text = createSpannableString(formattedText0, colorText0)
                    binding.recordContext2.text = formattedText1

                } else {
                    Log.d("setBarChartView 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<ChartDayData>, t: Throwable) {
                Log.d("서버 오류", "setBarChartView 실패")
            }
        })
    }

    // 꺾은선그래프 뷰 설정
    private fun setLineChartView(sdate: String) {

        // 서버 데이터 연결
        api.chartGetDay(token,date = sdate).enqueue(object : retrofit2.Callback<ChartDayData> {
            override fun onResponse(
                call: Call<ChartDayData>,
                response: Response<ChartDayData>
            ) {
                val responseCode = response.code()
                Log.d("setLineChartView", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("setLineChartView 성공", response.body().toString())

                    val nowAchievementRate = response.body()?.nowAchievementRate
                    val nowCountCompleted = response.body()?.nowCountCompletedA
                    val diffCountC = response.body()?.diffCount
                    val diffCountCText = when {
                        diffCountC == null -> "NULL"
                        diffCountC > 0 -> "상승"
                        diffCountC < 0 -> "하강"
                        else -> "유지"
                    }

                    val colorText0 = "${nowAchievementRate}%"

                    val formattedText0 = "투두 달성도가 ${colorText0} ${diffCountCText}했어요"
                    val formattedText1 = when {
                        nowCountCompleted == null -> "완료한 투두가 없어요"
                        else -> "전체 투두에서 평균적으로 ${nowCountCompleted}개 이상의 투두를 완료했어요!+" +
                                "\n어제보다 약 ${diffCountC}개의 할일을 더 많이 해냈네요!"
                    }

                    binding.recordTitle3.text = createSpannableString(formattedText0, colorText0)
                    binding.recordContext3.text = formattedText1

                } else {
                    Log.d("setLineChartView 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChartDayData>, t: Throwable) {
                Log.d("서버 오류", "setLineChartView 실패")
            }
        })

    }

    // 원형그래프 우측 카테고리 리사이클러뷰 설정
    private fun initCategoryRecycler(sdate: String) {
        val adapter = MyRecordCategoryAdapter(requireContext())
        val manager = LinearLayoutManager(requireContext())

        // 서버 데이터 연결
        api.chartGetDay(token,date = sdate).enqueue(object : retrofit2.Callback<ChartDayData> {
            override fun onResponse(
                call: Call<ChartDayData>,
                response: Response<ChartDayData>
            ) {
                val responseCode = response.code()
                Log.d("initCategoryRecycler", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("initCategoryRecycler 성공", response.body().toString())

                    datas.apply{
                        val categoryStatistics = response.body()?.categoryStatistics
                        val CategoryDatas = ArrayList<PieData>()

                        categoryStatistics?.forEach { dayPieData ->
//                            val categoryName = dayPieData.categoryName
//                            val rate = dayPieData.rate
//                            val colorCode = dayPieData.color

                            //CategoryDatas.add(dayPieData)
                        }
                    }

                    binding.myCategoryRecycler.adapter= adapter
                    binding.myCategoryRecycler.layoutManager= manager

                } else {
                    Log.d("initCategoryRecycler 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<ChartDayData>, t: Throwable) {
                Log.d("서버 오류", "initCategoryRecycler 실패")
            }
        })

    }

    // 원형그래프 데이터셋 설정
    private fun initCategoryPieChart(sdate: String) {
        binding.PieChart.setUsePercentValues(true)

        // 서버 데이터 연결
        api.chartGetDay(token,date = sdate).enqueue(object : retrofit2.Callback<ChartDayData> {
            override fun onResponse(
                call: Call<ChartDayData>,
                response: Response<ChartDayData>
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
                        //com.mada.myapplication.HomeFunction.Model.data = pieData
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

            override fun onFailure(call: Call<ChartDayData>, t: Throwable) {
                Log.d("서버 오류", "initCategoryPieChart 실패")
            }
        })


    }

    // 막대그래프 데이터셋 설정
    private fun initBarChart(date: String) {

        // 서버 데이터 연결
        api.chartGetDay(token, date = "${date}").enqueue(object : retrofit2.Callback<ChartDayData> {
            override fun onResponse(
                call: Call<ChartDayData>,
                response: Response<ChartDayData>
            ) {
                val responseCode = response.code()
                Log.d("initBarChart", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("initBarChart 성공", response.body().toString())
                } else {
                    Log.d("initBarChart 실패", response.body().toString())

                    // 서버 연결 전 UI 확인용으로 임시 작성

                    val entries = ArrayList<BarEntry>()
                    entries.add(BarEntry(1.0f,55.0f))
                    entries.add(BarEntry(2.0f,65.0f))
                    entries.add(BarEntry(3.0f,90.0f))
                    entries.add(BarEntry(4.0f,80.0f))

                    binding.BarChart.run {
                        description.isEnabled = false // 차트 옆에 별도로 표기되는 description을 안보이게 설정 (false)
                        setMaxVisibleValueCount(4) // 최대 보이는 그래프 개수를 4개로 지정
                        setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
                        setDrawBarShadow(false) //그래프의 그림자
                        setDrawGridBackground(false)//격자구조 넣을건지

                        axisLeft.run { //왼쪽 축. 즉 Y방향 축
                            axisMaximum = 101f //100 위치에 선을 그리기 위해 101f로 맥시멈값 설정
                            axisMinimum = 0f // 최소값 0
                            granularity = 25f // 25 단위마다 선을 그리려고 설정.
                            setDrawLabels(true) // 값 적는거 허용 (0, 25, 50, 75, 100)
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
                    var set = BarDataSet(entries,"DataSet") // 데이터셋 초기화
                    if(entries.equals(entries[3])) {
                        set.color = ContextCompat.getColor(requireContext(), R.color.main) // 마지막 바 그래프 색 설정
                    }
                    else{
                        set.color = ContextCompat.getColor(requireContext(), R.color.grey2) // 나머지 바 그래프 색 설정
                    }

                    val dataSet :ArrayList<IBarDataSet> = ArrayList()
                    dataSet.add(set)
                    val data = BarData(dataSet)
                    data.barWidth = 0.25f //막대 너비 설정
                    binding.BarChart.run {
                        this.data = data //차트의 데이터를 data로 설정해줌.
                        setFitBars(true)
                        invalidate()
                    }
                }
            }

            override fun onFailure(call: Call<ChartDayData>, t: Throwable) {
                Log.d("서버 오류", "initBarChart 실패")
            }
        })


    }

    // 꺾은선그래프 데이터셋 설정
    private fun initLineChart(date: String){
        // 서버 데이터 연결
        api.chartGetDay(token, date = "${date}").enqueue(object : retrofit2.Callback<ChartDayData> {
            override fun onResponse(
                call: Call<ChartDayData>,
                response: Response<ChartDayData>
            ) {
                val responseCode = response.code()
                Log.d("initLineChart", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("initLineChart 성공", response.body().toString())

                } else {
                    Log.d("initLineChart 실패", response.body().toString())

                    // 서버 연결 전 UI 확인용으로 임시 데이터 작성


                }
            }

            override fun onFailure(call: Call<ChartDayData>, t: Throwable) {
                Log.d("서버 오류", "initLineChart 실패")
            }
        })

    }

    // 텍스트에 색상을 적용하는 함수
    private fun createSpannableString(text: String, targetText: String): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(text)
        val start = text.indexOf(targetText)
        val end = start + targetText.length

        if (start != -1) {
            val colorSpan = ForegroundColorSpan(Color.parseColor("#2AA1B7"))
            spannableStringBuilder.setSpan(colorSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        return spannableStringBuilder
    }

    // 텍스트를 굵기를 적용하는 함수
    private fun createSpannableStringBold(text: String, targetText: String): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(text)
        val start = text.indexOf(targetText)
        val end = start + targetText.length

        if (start != -1) {
            spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return spannableStringBuilder
    }

    // 전면 광고
    private fun setupInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),
            "ca-app-pub-4086521113003670/4380505190",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("DEBUG: ", adError?.message.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("DEBUG: ", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
    }
}
