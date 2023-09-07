package com.example.myapplication.MyFuction.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Adapter.MyRecordCategoryAdapter
import com.example.myapplication.MyFuction.Calendar.MyWeekSliderlAdapter
import com.example.myapplication.MyFuction.Data.MyRecordCategoryData
import com.example.myapplication.MyFuction.Data.MyRecordCategoryData2
import com.example.myapplication.MyFuction.Data.MyRecordData
import com.example.myapplication.MyFuction.Data.MyRecordOptionData
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.MyRecordWeekBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

class MyRecordWeekFragment : Fragment() {
    private lateinit var binding: MyRecordWeekBinding
    private lateinit var calendar: Calendar
    lateinit var navController: NavController
    val datas = mutableListOf<MyRecordCategoryData>()
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyRecordWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isGone = true
        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_myRecordWeekFragment_to_fragMy)
        }


        val calendarAdapter = MyWeekSliderlAdapter(this,binding.textCalendar,binding.calendar2)
        binding.calendar2.adapter = calendarAdapter
        binding.calendar2.setCurrentItem(CalendarSliderAdapter.START_POSITION, false)
        binding.preBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(binding.calendar2.currentItem-1, true)
        }

        binding.nextBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(binding.calendar2.currentItem+1, true)
        }

        binding.dayWeekMonthBtn.setOnClickListener {
            navController.navigate(R.id.action_myRecordWeekFragment_to_myRecordMonthFragment)
        }

    }
    fun weekChange(month : Int, iweek : Int, date : String) {
        Log.d("dasdas",date)
        setTodoView(MyRecordOptionData("week", date) , month, iweek)
        setTimetableView(MyRecordOptionData("week", date), month, iweek)
        initCategoryRecycler(MyRecordOptionData("week", date))
        initCategoryPieChart(MyRecordOptionData("week", date))
    }

    // 투두 뷰 설정
    private fun setTodoView(wdata: MyRecordOptionData, month : Int, weekOfMonth: Int) {

        // 서버 데이터 연결
        api.myGetRecord(token, wdata).enqueue(object : retrofit2.Callback<MyRecordData> {
            override fun onResponse(
                call: Call<MyRecordData>,
                response: Response<MyRecordData>
            ) {
                val responseCode = response.code()
                Log.d("myGetRecordWeek, ${wdata.date}", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("myGetRecordWeek 성공", response.body().toString())

                    val todoCnt = response.body()?.data?.todosPercent
                    val todoPercent = response.body()?.data?.completeTodoPercent

                    val formattedText1 = "${month}월 ${weekOfMonth}주 투두"
                    val formattedText2 =
                        "${month}월 ${weekOfMonth}주차의 평균 투두는 ${todoCnt}개이고\n그 중 ${todoPercent}%를 클리어하셨어요"

                    Log.d("평균 투두 개수!!", "${todoCnt}")
                    Log.d("클리어 투두 퍼센트!!", "${todoPercent}")

                    binding.recordTitleTodo.text = formattedText1
                    binding.recordContextTodo.text = formattedText2

                } else {
                    Log.d("myGetRecordWeek 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<MyRecordData>, t: Throwable) {
                Log.d("서버 오류", "myGetRecordWeek 실패")
            }
        })
    }

    // 시간표 뷰 설정
    private fun setTimetableView(wdata: MyRecordOptionData, month : Int, weekOfMonth: Int) {

        // 서버 데이터 연결
        api.myGetRecord(token, wdata).enqueue(object : retrofit2.Callback<MyRecordData> {
            override fun onResponse(
                call: Call<MyRecordData>,
                response: Response<MyRecordData>
            ) {
                val responseCode = response.code()
                Log.d("myGetRecordWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("myGetRecordWeek 성공", response.body().toString())

                    val nickName = response.body()?.data?.nickName
                    val categoryStatistics = response.body()?.data?.categoryStatistics
                    val size = response.body()?.data?.categoryStatistics?.size

                    val formattedText1 = "${month}월 ${weekOfMonth}주 시간표"
                    var formattedText2 = ""

                    if (categoryStatistics.isNullOrEmpty()) {
                        formattedText2 = "${nickName}님이 가장 많은 시간을 투자한 카테고리는 없습니다."
                    } else if(size == 1 || size == 2){
                        var s = ""
                        for(data in categoryStatistics) {
                            s+= ", "+ data.categoryName
                        }
                        formattedText2 = "${nickName}님이 가장 많은 시간을 투자한 카테고리는\n${s.replaceFirst(",","")} 입니다."
                    }else{
                        val s1 = categoryStatistics[0].categoryName
                        val s2 = categoryStatistics[1].categoryName
                        val s3 = categoryStatistics[2].categoryName
                        formattedText2 = "${nickName}님이 가장 많은 시간을 투자한 카테고리는\n${s1}, ${s2}, ${s3} 입니다."
                    }

                    binding.recordTitleTimetable.text = formattedText1
                    binding.recordContextTimetable.text = formattedText2

                } else {
                    Log.d("myGetRecordWeek 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<MyRecordData>, t: Throwable) {
                Log.d("서버 오류", "myGetRecordWeek 실패")
            }
        })

    }

    // 통계 우측 카테고리 리사이클러뷰 설정
    private fun initCategoryRecycler(wdata: MyRecordOptionData) {
        val adapter = MyRecordCategoryAdapter(requireContext())
        val manager = LinearLayoutManager(requireContext())

        // 서버 데이터 연결
        api.myGetRecord(token, wdata).enqueue(object : retrofit2.Callback<MyRecordData> {
            override fun onResponse(
                call: Call<MyRecordData>,
                response: Response<MyRecordData>
            ) {
                val responseCode = response.code()
                Log.d("myGetRecordWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("myGetRecordWeek 성공", response.body().toString())

                    datas.apply{
                        val categoryStatistics = response.body()?.data?.categoryStatistics
                        categoryStatistics?.forEach { category ->
                            val categoryName = category.categoryName
                            val percentNum = category.rate
                            val colorCode = category.color

                            add(MyRecordCategoryData(percent = "${percentNum}%", colorCode = colorCode, category = categoryName))
                        }

                        adapter.datas = datas
                        adapter.notifyDataSetChanged()

                    }
                    binding.myCategoryRecycler.adapter= adapter
                    binding.myCategoryRecycler.layoutManager= manager

                } else {
                    Log.d("myGetRecordWeek 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<MyRecordData>, t: Throwable) {
                Log.d("서버 오류", "myGetRecordWeek 실패")
            }
        })

    }

    // 통계 좌측 파이차트 뷰 설정
    private fun initCategoryPieChart(wdata: MyRecordOptionData) {
        binding.myChart.setUsePercentValues(true)

        // 서버 데이터 연결
        api.myGetRecord(token, wdata).enqueue(object : retrofit2.Callback<MyRecordData> {
            override fun onResponse(
                call: Call<MyRecordData>,
                response: Response<MyRecordData>
            ) {
                val responseCode = response.code()
                Log.d("myGetRecordWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("myGetRecordWeek 성공", response.body().toString())

                    val entries = ArrayList<PieEntry>()
                    val colorsItems = ArrayList<Int>()

                    val categoryStatistics = response.body()?.data?.categoryStatistics
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
                    binding.myChart.apply {
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
                    Log.d("myGetRecordWeek 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<MyRecordData>, t: Throwable) {
                Log.d("서버 오류", "myGetRecordWeek 실패")
            }
        })


    }

}
