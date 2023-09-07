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
            binding.calendar2.setCurrentItem(1, true)
        }

        binding.nextBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(1, true)
        }

        binding.dayWeekMonthBtn.setOnClickListener {
            navController.navigate(R.id.action_myRecordWeekFragment_to_myRecordMonthFragment)
        }

    }
    fun weekChange(month : Int, iweek : Int, date : String) {
        Log.d("dasdas",date)
        setTodoView(MyRecordOptionData("month", date) , month, iweek)
        setTimetableView(MyRecordOptionData("week", date), month, iweek)
        initCategoryRecycler(MyRecordOptionData("week", date))
    }

    // 투두 뷰 설정
    private fun setTodoView(wdata: MyRecordOptionData, month : Int, weekOfMonth: Int) {

        // 서버 데이터 연결
        api.myGetRecord(token, wdata.option, wdata.date).enqueue(object : retrofit2.Callback<MyRecordData> {
            override fun onResponse(
                call: Call<MyRecordData>,
                response: Response<MyRecordData>
            ) {
                val responseCode = response.code()
                Log.d("myGetRecordWeek, ${wdata.date}", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("myGetRecordWeek 성공", response.body().toString())

                    val todoCnt = response.body()!!.data.completeTodoPercent
                    val todoPercent = response.body()!!.data.todosPercent

                    val formattedText1 = "${month}월 ${weekOfMonth}주 투두"
                    val formattedText2 =
                        "${month}월 ${weekOfMonth}주차의 평균 투두는 ${todoCnt}개이고\n그 중 ${todoPercent}%를 클리어하셨어요"

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
        api.myGetRecord(token, wdata.option, wdata.date).enqueue(object : retrofit2.Callback<MyRecordData> {
            override fun onResponse(
                call: Call<MyRecordData>,
                response: Response<MyRecordData>
            ) {
                val responseCode = response.code()
                Log.d("myGetRecordWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("myGetRecordWeek 성공", response.body().toString())

                    val nickname = response.body()!!.data.nickname
                    val size = response.body()!!.data.categoryStatistics.size
                    val myCategoryList = mutableListOf<String>() // 빈 리스트를 먼저 생성

                    for (i in 0 until size) {
                        val categoryItem = response.body()!!.data.categoryStatistics[i].categoryName
                        myCategoryList.add(categoryItem) // 리스트에 카테고리명 추가
                    }

                    val formattedText1 = "${month}월 ${weekOfMonth}주 시간표"
                    val formattedText2 =
                        "${nickname}님이 가장 많은 시간을 투자한 카테고리는\n${myCategoryList[0]}, ${myCategoryList[1]}, ${myCategoryList[2]} 입니다."

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
        api.myGetRecord(token, wdata.option, wdata.date).enqueue(object : retrofit2.Callback<MyRecordData> {
            override fun onResponse(
                call: Call<MyRecordData>,
                response: Response<MyRecordData>
            ) {
                val responseCode = response.code()
                Log.d("myGetRecordWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("myGetRecordWeek 성공", response.body().toString())

                    datas.apply {

                        val myCategoryNameList = mutableListOf<String>()
                        val myPercentNumList = mutableListOf<String>()
                        val myColorCodeList = mutableListOf<String>()
                        val size = response.body()!!.data.categoryStatistics.size

                        for (i in 0 until size) {
                            val categoryName = response.body()!!.data.categoryStatistics[i].categoryName
                            val percentNum = response.body()!!.data.categoryStatistics[i].rate
                            val colorCode = response.body()!!.data.categoryStatistics[i].color

                            myCategoryNameList.add(categoryName) // 리스트에 카테고리명 추가
                            myPercentNumList.add(percentNum.toString())  // 리스트에 퍼센트 추가
                            myColorCodeList.add(colorCode)  // 리스트에 컬러코드 추가

                            MyRecordCategoryData(percent = myPercentNumList[i], colorCode = myColorCodeList[i], category = myCategoryNameList[i])

                        }

                        adapter.datas = datas
                        adapter.notifyDataSetChanged()

                    }
                    binding.myCategoryRecycler.adapter = adapter
                    binding.myCategoryRecycler.layoutManager = manager

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
        api.myGetRecord(token, wdata.option, wdata.date).enqueue(object : retrofit2.Callback<MyRecordData> {
            override fun onResponse(
                call: Call<MyRecordData>,
                response: Response<MyRecordData>
            ) {
                val responseCode = response.code()
                Log.d("myGetRecordWeek", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("myGetRecordWeek 성공", response.body().toString())

                    // 각 항목 리스트화
                    val myCategoryNameList = mutableListOf<String>()
                    val myPercentNumList = mutableListOf<Float>()
                    val myColorCodeList = mutableListOf<String>()
                    val size = response.body()!!.data.categoryStatistics.size

                    // 각 리스트에 항목 추가
                    for (i in 0 until size) {
                        val categoryName = response.body()!!.data.categoryStatistics[i].categoryName
                        val percentNum = response.body()!!.data.categoryStatistics[i].rate
                        val colorCode = response.body()!!.data.categoryStatistics[i].color

                        myCategoryNameList.add(categoryName)
                        myPercentNumList.add(percentNum)
                        myColorCodeList.add(colorCode)

                        MyRecordCategoryData(percent = myPercentNumList[i].toString(), colorCode = myColorCodeList[i], category = myCategoryNameList[i])

                        // 파이차트 수치 속성 설정
                        val entries = ArrayList<PieEntry>()
                        entries.add(PieEntry(myPercentNumList[i], myCategoryNameList[i]))

                        // 파이차트 색상 속성 설정
                        val colorsItems = ArrayList<Int>()
                        colorsItems.add(myColorCodeList[i].toInt())
//                        // 랜덤색상 임시데이터
//                        for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
//                        for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
//                        for (c in COLORFUL_COLORS) colorsItems.add(c)
//                        for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
//                        for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
//                        colorsItems.add(ColorTemplate.getHoloBlue())

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

                        // 범례 커스텀
//                            binding.myChart.legend.apply {
//                                isEnabled = true // 범례 활성화
//                                verticalAlignment = Legend.LegendVerticalAlignment.CENTER // 세로 위치 중앙
//                                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT // 우측 배치
//                                orientation = Legend.LegendOrientation.VERTICAL // 세로방향으로 배치
//                                setDrawInside(false) // 차트 내부에 그리지 않도록 설정
//                                // 그 외 범례에 대한 스타일 등을 설정할 수 있습니다.
//                            // }
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
