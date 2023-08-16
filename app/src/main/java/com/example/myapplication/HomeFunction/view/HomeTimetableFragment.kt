package com.example.myapplication.HomeFunction.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.CalenderFuntion.Model.CalendarDATA
import com.example.myapplication.CalenderFuntion.Model.CalendarDatas
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.CustomCircleBarView
import com.example.myapplication.HomeFunction.Model.ScheduleList
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.time.HomeTimeAdapter
import com.example.myapplication.HomeFunction.time.SampleTimeData
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentTimetableBinding
import com.example.myapplication.hideBottomNavigation
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
import java.util.Calendar


class HomeTimetableFragment : Fragment() {

    lateinit var binding : HomeFragmentTimetableBinding
    val sampleTimeArray = ArrayList<SampleTimeData>()
    val timeAdapter = HomeTimeAdapter(sampleTimeArray)
    private var bottomFlag = true

    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(HomeApi::class.java)
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDVWJlYWF6cDhBem9mWDJQQUlxVHN0NmVxUTN4T1JfeXBWR1VuQUlqZU40IiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjA5MjQ0OCwiZXhwIjoxNjkyMTI4NDQ4fQ.H9X0jEZVqG9FMzwhDh8I05ov6KRVlGfI8C5bXUwoEWB1lrcQQZzVC9shykYX2_4r-IL51KBhA45Qru0zLf5YhA"

    private lateinit var customCircleBarView: CustomCircleBarView       //프로그래스바

    /*
    val pieChartDataArray = arrayOf(        //임시 데이터
        HomeViewpagerTimetableFragment.PieChartData("제목1", "메모1", 0, 0, 1, 0, "#486DA3", 0,"TIME"),      //제목, 메모, 시작 시각, 시작 분, 끝 시각, 끝 분, 색깔 코드, 구분 숫자
        HomeViewpagerTimetableFragment.PieChartData("제목2", "메모2", 1, 0, 6, 0, "#516773", 1,"TIME"),
        HomeViewpagerTimetableFragment.PieChartData("제목3", "메모3", 9, 0, 12, 0, "#FDA4B4", 2,"TIME"),
        HomeViewpagerTimetableFragment.PieChartData("제목4", "메모4", 12, 0, 13, 30, "#52B6C9", 3,"TIME"),
        HomeViewpagerTimetableFragment.PieChartData("제목5", "메모5", 13, 30, 14, 30, "#516773", 4,"TIME"),
        HomeViewpagerTimetableFragment.PieChartData("제목6", "메모6", 14, 30, 16, 30, "#52B6C9", 5,"TIME"),
        HomeViewpagerTimetableFragment.PieChartData("제목7", "메모7", 16, 30, 18, 0, "#FCE79A", 6,"TIME"),
        HomeViewpagerTimetableFragment.PieChartData("제목8", "메모8", 20, 0, 22, 0, "#486DA3", 7,"TIME")
    )*/
    var dataArray= ArrayList<HomeViewpagerTimetableFragment.PieChartData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_timetable, container, false)
        hideBottomNavigation(bottomFlag, activity)
        
        customCircleBarView = binding.progressbar
        // 원형 프로그레스 바 진행 상태 변경 (0부터 100까지)
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val progressPercentage = ((hour * 60 + minute).toFloat() / (24 * 60) * 100).toInt()
        customCircleBarView.setProgress(progressPercentage.toInt())

        val pieChartDataArray = ArrayList<HomeViewpagerTimetableFragment.PieChartData>()
        dataArray = pieChartDataArray
        getTimeDatas("2023-08-15",pieChartDataArray)

        //Pi Chart
        var chart = binding.chart

        val entries = ArrayList<PieEntry>()
        val colorsItems = ArrayList<Int>()
        if(pieChartDataArray.size==0) {     //그날 정보가 없다면
            entries.add(PieEntry(1f, "999"))
            colorsItems.add(R.color.grey4)
        } else {
            var tmp = 0     //시작 시간

            for(data in pieChartDataArray) {
                val start = data.startHour.toString().toInt() * 60 + data.startMin.toString().toInt()
                val end = data.endHour.toString().toInt() * 60 + data.endMin.toString().toInt()
                if(tmp==start) {      //이전 일정과 사이에 빈틈이 없을때
                    entries.add(PieEntry((end-start).toFloat(), data.divisionNumber.toString()))
                    colorsItems.add(Color.parseColor(data.colorCode))
                    tmp = end
                } else {
                    val noScheduleDuration = start - tmp
                    entries.add(PieEntry(noScheduleDuration.toFloat(), "999"))      // 스케줄 없는 시간
                    colorsItems.add(Color.parseColor("#FFFFFF"))
                    entries.add(PieEntry((end-start).toFloat(), data.divisionNumber.toString()))
                    colorsItems.add(Color.parseColor(data.colorCode))
                    tmp = end
                }
            }
        }



        if(pieChartDataArray[pieChartDataArray.size-1].endHour!=24) {
            val h = 23 - pieChartDataArray[pieChartDataArray.size-1].endHour
            val m = 60 - pieChartDataArray[pieChartDataArray.size-1].endMin
            entries.add(PieEntry((h*60+m).toFloat(), "999"))
            colorsItems.add(Color.parseColor("#FFFFFF"))
        }
        // 왼쪽 아래 설명 제거
        val legend = chart.legend
        legend.isEnabled = false
        chart.invalidate()

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.apply {
            colors = colorsItems
            setDrawValues(false) // 비율 숫자 없애기

        }

        val pieData = PieData(pieDataSet)



        chart.apply {
            data = pieData
            isRotationEnabled = false                               //드래그로 회전 x
            isDrawHoleEnabled = false                               //중간 홀 그리기 x
            setExtraOffsets(25f,25f,25f,25f)    //크기 조절
            setUsePercentValues(false)
            setEntryLabelColor(Color.BLACK)
            setDrawEntryLabels(false) //라벨 끄기
            //rotationAngle = 30f // 회전 각도, 굳이 필요 없을듯
            description.isEnabled = false   //라벨 끄기 (오른쪽아래 간단한 설명)
        }
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e is PieEntry) {
                    val pieEntry = e as PieEntry
                    val label = pieEntry.label

                    if (label != "999") {
                        val bundle = Bundle()
                        bundle.putSerializable("pieChartData", pieChartDataArray[label.toInt()])
                        bundle.putSerializable("pieChartDataArray", pieChartDataArray)
                        Navigation.findNavController(requireView()).navigate(R.id.action_homeTimetableFragment_to_timeAddFragment,bundle)
                    }
                }
            }
            override fun onNothingSelected() {
                // 아무 것도 선택되지 않았을 때의 동작을 구현합니다.
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivHomeTimetableBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_fragHome)
            bottomFlag = false
        }

        binding.tvHomeTimetableSave.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_fragHome)
            bottomFlag = false
        }

        binding.fabHomeTime.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("pieChartDataArray", dataArray)
            Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_timeAddFragment,bundle)


        }

        //rv adpter 연결
        timeAdapter.setItemClickListener(object: HomeTimeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //페이지 이동 + 데이터 전달
                Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_timeAddFragment)
            }
        })

        binding.rvHomeTimeSchedule.adapter = timeAdapter
        binding.rvHomeTimeSchedule.layoutManager = LinearLayoutManager(this.activity)

        //edt처리


    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }
    fun extractTime(timeString: String,hourOrMin : Boolean): Int {
        val timeParts = timeString.split(":")
        if (timeParts.size == 3) {
            try {
                val hour = timeParts[0].toInt()
                val minute = timeParts[1].toInt()
                if(hourOrMin)
                    return hour
                else
                    return minute
            } catch (e: NumberFormatException) {
                // 숫자로 변환할 수 없는 경우 또는 잘못된 형식인 경우
                return 0
            }
        }
        return 0
    }

    //HomeViewpagerTimetableFragment.PieChartData("제목1", "메모1", 0, 0, 1, 0, "#486DA3", 0)
    private fun getTimeDatas(date : String, arrays : ArrayList<HomeViewpagerTimetableFragment.PieChartData>) {
        val call = service.getTimetable(token,date)
        call.enqueue(object : Callback<ScheduleList> {
            override fun onResponse(call2: Call<ScheduleList>, response: Response<ScheduleList>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas
                        if(datas != null) {
                            var i = 0
                            for (data in datas) {
                                val tmp = HomeViewpagerTimetableFragment.PieChartData(data.scheduleName,data.memo,extractTime(data.startTime,true),extractTime(data.startTime,false),
                                    extractTime(data.endTime,true),extractTime(data.endTime,false),data.color,i++)
                                arrays.add(tmp)                                                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@이거 수정해야함
                                //Log.d("111","datas: ${data.calendarName}")
                                // ...
                            }
                        } else {
                            Log.d("2222","Request was not successful. Message: hi")
                        }
                    } else {
                        Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                    Log.d("333","itemType: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ScheduleList>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }

}

