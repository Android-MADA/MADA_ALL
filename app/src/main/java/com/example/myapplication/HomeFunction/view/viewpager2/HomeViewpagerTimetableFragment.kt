package com.example.myapplication.HomeFunction.view.viewpager2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.CalenderFuntion.CalendarAddDday
import com.example.myapplication.CustomCircleBarView
import com.example.myapplication.Fragment.FragHome
import com.example.myapplication.HomeFunction.view.HomeTimetableFragment
import com.example.myapplication.R
import com.example.myapplication.YourMarkerView
import com.example.myapplication.databinding.HomeFragmentViewpagerTimetableBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeViewpagerTimetableFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeViewpagerTimetableFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var customCircleBarView: CustomCircleBarView       //프로그래스바
    data class PieChartData(
        val title: String,
        val memo: String,
        val startHour: Int,
        val startMin: Int,
        val endHour: Int,
        val endMin: Int,
        val colorCode: String,
        val divisionNumber: Int
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = HomeFragmentViewpagerTimetableBinding.inflate((layoutInflater))
        customCircleBarView = binding.progressbar
        // 원형 프로그레스 바 진행 상태 변경 (0부터 100까지)
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val progressPercentage = ((hour * 60 + minute).toFloat() / (24 * 60) * 100).toInt()

        customCircleBarView.setProgress(progressPercentage.toInt())
        //파이차트
        var chart = binding.chart
        val pieChartDataArray = arrayOf(        //임시 데이터
            PieChartData("제목1", "메모1", 0,0,1,0, "#486DA3",0),      //제목, 메모, 시작 시각, 시작 분, 끝 시각, 끝 분, 색깔 코드, 구분 숫자
            PieChartData("제목2", "메모2", 1,0,6,0, "#516773",1),
            PieChartData("제목3", "메모3", 9,0,12,0, "#FDA4B4",2),
            PieChartData("제목4", "메모4", 12,0,13,30, "#52B6C9",3),
            PieChartData("제목5", "메모5", 13,30,14,30, "#516773",4),
            PieChartData("제목6", "메모6", 14,30,16,30, "#52B6C9",5),
            PieChartData("제목7", "메모7", 16,30,18,0, "#FCE79A",6),
            PieChartData("제목8", "메모8", 20,0,22,0, "#486DA3",7)
        )
        if(pieChartDataArray.size==8) {     //그날 정보가 없다면
            binding.none.visibility = View.VISIBLE
        } else
            binding.none.visibility = View.GONE
        val marker_ = YourMarkerView(requireContext(), R.layout.home_time_custom_label,pieChartDataArray)
        val entries = ArrayList<PieEntry>()
        val colorsItems = ArrayList<Int>()

        var tmp = 0     //시작 시간

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

        binding.noneBtn.setOnClickListener {

        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeViewpagerTimetableFragment()
    }
}