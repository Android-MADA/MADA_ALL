package com.example.myapplication.Fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.TimeFunction.adapter.HomeTimeAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.TimeFunction.TimeViewModel
import com.example.myapplication.TimeFunction.util.CustomCircleBarView
import com.example.myapplication.databinding.FragTimeBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class FragTime : Fragment() {

    lateinit var binding : FragTimeBinding
    private val viewModel : HomeViewModel by activityViewModels()
    private val viewModelTime: TimeViewModel by activityViewModels()

    lateinit var today : String
    lateinit var pieChartDataArray : ArrayList<TimeViewModel.PieChartData>

    private lateinit var customCircleBarView: CustomCircleBarView       //프로그래스바

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragTimeBinding.inflate(layoutInflater)
        today = viewModel.homeDate.value.toString()
        customCircleBarView = binding.progressbar
        // 원형 프로그레스 바 진행 상태 변경 (0부터 100까지)
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val progressPercentage = ((hour * 60 + minute).toFloat() / (24 * 60) * 100).toInt()
        customCircleBarView.setProgress(progressPercentage)

        //파이차트
        viewModelTime.getScheduleDatas(today) { result ->
            when (result) {
                1 -> {
                    val tmpList = viewModelTime.getTimeDatas(today)
                    pirChartOn(tmpList)
                    val timeAdapter = HomeTimeAdapter(tmpList)
                    //rv adpter 연결
                    timeAdapter.setItemClickListener(object: HomeTimeAdapter.OnItemClickListener{
                        override fun onClick(v: View, position: Int) {
                            //Navigation.findNavController(requireView()).navigate(R.id.action_homeTimetableFragment_to_timeAddFragment)
                        }
                    })
                    binding.rvHomeTimeSchedule.adapter = timeAdapter
                    binding.rvHomeTimeSchedule.layoutManager = LinearLayoutManager(requireContext())
                }
                2 -> {
                    Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en","US"))
        val outputDateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale("ko", "KR"))
        binding.textHomeTimeName.text = outputDateFormat.format(inputDateFormat.parse(today))

        binding.fabHomeTime.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("today",today)
            bundle.putSerializable("pieChartDataArray", pieChartDataArray)
            Navigation.findNavController(view).navigate(R.id.action_homeTimetableFragment_to_timeAddFragment,bundle)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun pirChartOn(arrays : ArrayList<TimeViewModel.PieChartData>) {
        pieChartDataArray = arrays
        //Pi Chart
        var chart = binding.chart

        val entries = ArrayList<PieEntry>()
        val colorsItems = ArrayList<Int>()

        if(pieChartDataArray.size==0) {     //그날 정보가 없다면
            entries.add(PieEntry(10f, "999"))
            colorsItems.add(Color.parseColor("#F0F0F0"))
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
                    colorsItems.add(Color.parseColor("#F0F0F0"))
                    entries.add(PieEntry((end-start).toFloat(), data.divisionNumber.toString()))
                    colorsItems.add(Color.parseColor(data.colorCode))
                    tmp = end
                }
            }
        }
        if(pieChartDataArray.size>0&&pieChartDataArray[pieChartDataArray.size-1].endHour!=24) {
            val h = 23 - pieChartDataArray[pieChartDataArray.size-1].endHour
            val m = 60 - pieChartDataArray[pieChartDataArray.size-1].endMin
            entries.add(PieEntry((h*60+m).toFloat(), "999"))
            colorsItems.add(Color.parseColor("#F0F0F0"))
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
        val smallXY = if(chart.width > chart.height) chart.height else chart.width
        if(smallXY/60f > 0) viewModelTime.range = smallXY/60f
        val range = viewModelTime.range


        chart.apply {
            data = pieData
            isRotationEnabled = false                               //드래그로 회전 x
            isDrawHoleEnabled = false                               //중간 홀 그리기 x
            setExtraOffsets(range,range,range,range)    //크기 조절
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
                        bundle.putString("today",today)
                        bundle.putSerializable("pieChartData", pieChartDataArray[label.toInt()])
                        bundle.putSerializable("pieChartDataArray", pieChartDataArray)
                        Navigation.findNavController(requireView()).navigate(R.id.action_homeTimetableFragment_to_timeAddFragment,bundle)
                    } else {
                        pieDataSet.selectionShift = 1f //하이라이트 크기
                    }
                }
            }
            override fun onNothingSelected() {
                // 아무 것도 선택되지 않았을 때의 동작을 구현합니다.
            }
        })
    }

}

