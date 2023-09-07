package com.example.myapplication.MyFuction.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.CalenderFuntion.Calendar.CalendarSliderAdapter
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.CategoryList1
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.Model.TodoList
import com.example.myapplication.HomeFunction.adapter.todo.HomeViewpager2CategoryAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.time.TimeViewModel
import com.example.myapplication.MyFuction.Calendar.MyMonthSliderlAdapter
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.YourMarkerView
import com.example.myapplication.databinding.MyRecordDayBinding
import com.example.myapplication.databinding.MyRecordMonthBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MyRecordDayFragment : Fragment() {
    private lateinit var binding: MyRecordDayBinding
    lateinit var navController: NavController
    lateinit var chartBackground : PieChart

    private val viewModelTime: TimeViewModel by activityViewModels()

    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(HomeApi::class.java)
    var token = Splash2Activity.prefs.getString("token", "")

    private var cateAdapter: HomeViewpager2CategoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyRecordDayBinding.inflate(inflater, container, false)
        chartBackground = binding.chart2
        val entries = ArrayList<PieEntry>()
        val colorsItems = ArrayList<Int>()

        val pieDataSet = PieDataSet(entries, "")
        val pieData = PieData(pieDataSet)


        entries.add(PieEntry(10f, "999"))
        colorsItems.add(Color.parseColor("#232323"))
        pieDataSet.apply {
            colors = colorsItems
            setDrawValues(false) // 비율 숫자 없애기
        }
        chartBackground.apply {
            invalidate()
            legend.isEnabled = false
            data = pieData
            isRotationEnabled = false                               //드래그로 회전 x
            isDrawHoleEnabled = false
            setUsePercentValues(false)
            setEntryLabelColor(Color.BLACK)
            setDrawEntryLabels(false) //라벨 끄기
            description.isEnabled = false   //라벨 끄기 (오른쪽아래 간단한 설명)
        }
        chartBackground.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e is PieEntry) {
                    pieDataSet.selectionShift = 0f
                }
            }
            override fun onNothingSelected() {
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        token = Splash2Activity.prefs.getString("token", "")

        navController = binding.navHostFragmentContainer.findNavController()


        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_myRecordDayFragment_to_fragMy)
        }

        //달력 부분


        val calendarAdapter = MyMonthSliderlAdapter(this,binding.textCalendar,binding.calendar2,"DAY")
        binding.calendar2.adapter = calendarAdapter
        binding.calendar2.setCurrentItem(CalendarSliderAdapter.START_POSITION, false)
        binding.preBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(binding.calendar2.currentItem-1, true)
        }

        binding.nextBtn.setOnClickListener {
            binding.calendar2.setCurrentItem(binding.calendar2.currentItem+1, true)
        }

        // 일 주 월 버튼 클릭 이동
        binding.dayWeekMonthBtn.setOnClickListener {
            navController.navigate(R.id.action_myRecordDayFragment_to_myRecordWeekFragment)
        }


        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 설정한 LayoutParams를 다시 설정

                dayChange(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                // 필요한 작업 수행 후 리스너 제거
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        // 홈 투두 받아오기
        //카테 데이터 받아오기
        //카태 데이터 있는지 확인하고
        //카테 어댑터 연결

        //투두 데이터 있는지 확인하고
        //out rv 연결하고
        // in rv 연결하기

    }
    //날짜 클릭시 실행되는 함수
    fun dayChange(theDate : String) {
        //서버에게서 정보 얻어오기
        viewModelTime.getScheduleDatas(theDate) { result ->
            when (result) {

                1 -> {
                    //통신이 된다면
                    pirChartOn(viewModelTime.getTimeDatas(theDate))

                }
                2 -> {
                    //통신이 불안정하면
                    Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                }
            }
        }
        findRv(theDate)
    }

    private fun pirChartOn(arrays : ArrayList<TimeViewModel.PieChartData>) {
        val pieChartDataArray = arrays
        //Pi Chart
        var chart = binding.chart
        chart.clear()
        var tmp = 0     //시작 시간
        val marker_ = YourMarkerView(requireContext(), R.layout.home_time_custom_label,pieChartDataArray)
        val entries = ArrayList<PieEntry>()
        val colorsItems = ArrayList<Int>()

        val pieDataSet = PieDataSet(entries, "")
        val pieData = PieData(pieDataSet)

        val range = chart.width/60f

        chartBackground.setExtraOffsets(range*0.98f,range*0.98f,range*0.98f,range*0.98f)

        if(pieChartDataArray.size==0) {
            entries.add(PieEntry(10f, "999"))
            colorsItems.add(Color.parseColor("#F0F0F0"))
            pieDataSet.apply {
                colors = colorsItems
                setDrawValues(false) // 비율 숫자 없애기
            }
            chart.apply {
                invalidate()
                legend.isEnabled = false
                data = pieData
                isRotationEnabled = false                               //드래그로 회전 x
                isDrawHoleEnabled = false                               //중간 홀 그리기 x
                setExtraOffsets(range,range,range,range)    //크기 조절
                setUsePercentValues(false)
                setEntryLabelColor(Color.BLACK)
                marker = marker_
                setDrawEntryLabels(false) //라벨 끄기
                description.isEnabled = false   //라벨 끄기 (오른쪽아래 간단한 설명)

            }
            chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    if (e is PieEntry) {
                        pieDataSet.selectionShift = 0f
                    }
                }
                override fun onNothingSelected() {
                }
            })
        } else {
            for(data in pieChartDataArray) {
                val start = data.startHour.toString().toInt() * 60 + data.startMin.toString().toInt()
                val end = data.endHour.toString().toInt() * 60 + data.endMin.toString().toInt()
                if(tmp==start) {      //이전 일정과 사이에 빈틈이 없을때
                    entries.add(PieEntry((end-start).toFloat(), data.divisionNumber.toString()))
                    colorsItems.add(Color.parseColor(data.colorCode.toString()))
                    tmp = end
                } else {
                    val noScheduleDuration = start - tmp
                    entries.add(PieEntry(noScheduleDuration.toFloat(), "999"))      // 스케줄 없는 시간
                    colorsItems.add(Color.parseColor("#FFFFFF"))
                    entries.add(PieEntry((end-start).toFloat(), data.divisionNumber.toString()))
                    colorsItems.add(Color.parseColor(data.colorCode.toString()))
                    tmp = end
                }
            }
            if(pieChartDataArray[pieChartDataArray.size-1].endHour!=24) {
                val h = 23 - pieChartDataArray[pieChartDataArray.size-1].endHour
                val m = 60 - pieChartDataArray[pieChartDataArray.size-1].endMin
                entries.add(PieEntry((h*60+m).toFloat(), "999"))
                colorsItems.add(Color.parseColor("#FFFFFF"))
            }
            pieDataSet.apply {
                colors = colorsItems
                setDrawValues(false) // 비율 숫자 없애기
            }
            chart.apply {
                invalidate()
                legend.isEnabled = false
                data = pieData
                isRotationEnabled = false                               //드래그로 회전 x
                isDrawHoleEnabled = false                               //중간 홀 그리기 x
                setExtraOffsets(range,range,range,range)    //크기 조절
                setUsePercentValues(false)
                setEntryLabelColor(Color.BLACK)
                marker = marker_
                setDrawEntryLabels(false) //라벨 끄기
                description.isEnabled = false   //라벨 끄기 (오른쪽아래 간단한 설명)
            }

            chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    if (e is PieEntry) {
                        val pieEntry = e as PieEntry
                        val label = pieEntry.label
                        if (label == "999") {
                            pieDataSet.selectionShift = 0f //하이라이트 크기
                        }else {
                            pieDataSet.selectionShift = 30f// 다른 라벨의 경우 선택 시 하이라이트 크기 설정
                        }
                    }
                }
                override fun onNothingSelected() {
                }
            })
        }



    }

    private fun attachAdapter(dataSet: ArrayList<Category>, cateTodoSet: ArrayList<ArrayList<Todo>>) {

        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

        cateAdapter = HomeViewpager2CategoryAdapter("my")
        cateAdapter!!.dataSet = dataSet
        cateAdapter!!.cateTodoSet = cateTodoSet

        cateAdapter!!.setItemClickListener(object :
            HomeViewpager2CategoryAdapter.OnItemClickListener {
            override fun onClick(
                v: View,
                position: Int,
                cate: Int,
                edt: EditText,
                layout: LinearLayout
            ) {

            }
        })

        binding.myViewpager2.adapter = cateAdapter
        binding.myViewpager2.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun findRv(date: String) {
        service.getMyCategory(token).enqueue(object : Callback<CategoryList1> {
            override fun onResponse(call: Call<CategoryList1>, response: Response<CategoryList1>) {
                val category = response.body()?.data?.CategoryList
                if (category?.isEmpty() != true) {
                    val cate = response.body()?.data?.CategoryList
                    service.getAllMyTodo(token, date).enqueue(object : Callback<TodoList> {
                        override fun onResponse(
                            call: Call<TodoList>,
                            response: Response<TodoList>
                        ) {
                            val cateTodo = classifyTodo(category!!, response.body()!!.data.TodoList)
                            attachAdapter(category, cateTodo)
                        }

                        override fun onFailure(call: Call<TodoList>, t: Throwable) {
                        }

                    })
                }

            }

            override fun onFailure(call: Call<CategoryList1>, t: Throwable) {
            }
        })
    }

    private fun classifyTodo(dataSet: ArrayList<Category>, todo: ArrayList<Todo>): ArrayList<ArrayList<Todo>> {

        var size = if (dataSet.size != 0) {
            dataSet.size.minus(1)
        } else {
            -1
        }

        var todoCateList: ArrayList<ArrayList<Todo>> = arrayListOf(
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf()
        )
        if (size > 0) {
            for (i in todo) {
                for (j in 0..size) {
                    if (i.category.id == dataSet[j].id) {
                        todoCateList[j].add(i)
                    }
                }
            }
        } else if (size == 0) {
            for (i in todo) {
                todoCateList[0].add(i)
            }
        }
        return todoCateList
    }
}
