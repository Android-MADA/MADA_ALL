package com.example.myapplication.HomeFunction.view

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.CustomCircleBarView
import com.example.myapplication.HomeFunction.time.HomeTimeAdapter
import com.example.myapplication.HomeFunction.time.SampleTimeData
import com.example.myapplication.HomeFunction.view.viewpager2.HomeViewpagerTimetableFragment
import com.example.myapplication.R
import com.example.myapplication.YourMarkerView
import com.example.myapplication.databinding.HomeFragmentTimetableBinding
import com.example.myapplication.databinding.HomeFragmentViewpagerTimetableBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar


class HomeTimetableFragment : Fragment() {

    lateinit var binding : HomeFragmentTimetableBinding
    val sampleTimeArray = ArrayList<SampleTimeData>()
    val timeAdapter = HomeTimeAdapter(sampleTimeArray)

    private lateinit var customCircleBarView: CustomCircleBarView       //프로그래스바

    val pieChartDataArray = arrayOf(        //임시 데이터
        HomeViewpagerTimetableFragment.PieChartData("제목1", "메모1", 0, 0, 1, 0, "#486DA3", 0),      //제목, 메모, 시작 시각, 시작 분, 끝 시각, 끝 분, 색깔 코드, 구분 숫자
        HomeViewpagerTimetableFragment.PieChartData("제목2", "메모2", 1, 0, 6, 0, "#516773", 1),
        HomeViewpagerTimetableFragment.PieChartData("제목3", "메모3", 9, 0, 12, 0, "#FDA4B4", 2),
        HomeViewpagerTimetableFragment.PieChartData("제목4", "메모4", 12, 0, 13, 30, "#52B6C9", 3),
        HomeViewpagerTimetableFragment.PieChartData("제목5", "메모5", 13, 30, 14, 30, "#516773", 4),
        HomeViewpagerTimetableFragment.PieChartData("제목6", "메모6", 14, 30, 16, 30, "#52B6C9", 5),
        HomeViewpagerTimetableFragment.PieChartData("제목7", "메모7", 16, 30, 18, 0, "#FCE79A", 6),
        HomeViewpagerTimetableFragment.PieChartData("제목8", "메모8", 20, 0, 22, 0, "#486DA3", 7)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_timetable, container, false)
        customCircleBarView = binding.progressbar
        // 원형 프로그레스 바 진행 상태 변경 (0부터 100까지)
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val progressPercentage = ((hour * 60 + minute).toFloat() / (24 * 60) * 100).toInt()
        customCircleBarView.setProgress(progressPercentage.toInt())

        //Pi Chart
        var chart = binding.chart

        val marker_ = YourMarkerView(requireContext(), R.layout.home_time_custom_label,pieChartDataArray)


        val entries = ArrayList<PieEntry>()
        val colorsItems = ArrayList<Int>()
        if(pieChartDataArray.size==0) {     //그날 정보가 없다면
            entries.add(PieEntry(1f, "2"))
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
            marker = marker_
            setDrawEntryLabels(false) //라벨 끄기
            //rotationAngle = 30f // 회전 각도, 굳이 필요 없을듯
            description.isEnabled = false   //라벨 끄기 (오른쪽아래 간단한 설명)
        }
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e is PieEntry) {
                    val pieEntry = e as PieEntry
                    val label = pieEntry.label

                    if (label == "999") {
                        pieDataSet.selectionShift = 1f //하이라이트 크기
                    } else {
                        pieDataSet.selectionShift = 30f // 다른 라벨의 경우 선택 시 하이라이트 크기 설정
                    }
                }
            }
            override fun onNothingSelected() {
                // 아무 것도 선택되지 않았을 때의 동작을 구현합니다.
            }
        })


        hideBootomNavigation(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivHomeTimetableBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_fragHome)
        }

        binding.tvHomeTimetableSave.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_fragHome)
        }

        binding.fabHomeTime.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("pieChartDataArray", pieChartDataArray)
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

        //pie chart 추가


    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        hideBootomNavigation(false)
    }

    fun hideBootomNavigation(bool : Boolean){
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if(bool){
            bottomNavigation?.isGone = true
        }
        else {
            bottomNavigation?.isVisible = true
        }
    }

    //HomeViewpagerTimetableFragment.PieChartData("제목1", "메모1", 0, 0, 1, 0, "#486DA3", 0)
    private fun initArrayList(){
        with(sampleTimeArray){
            for(data in pieChartDataArray) {
                sampleTimeArray.add(SampleTimeData(data.title, Color.parseColor(data.colorCode)))
            }
            //sampleTimeArray.add(SampleTimeData("잠", resources.getColor(R.color.sub2), "오전 10:30", "오전 11:00", "2023-07-20"))
            //sampleTimeArray.add(SampleTimeData("오전수업", Color.parseColor("#FDA4B4"), "오전 10:00", "오전 10:30", "2023-07-20"))
        }
    }


}

