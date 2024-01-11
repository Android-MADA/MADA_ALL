package com.mada.myapplication.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.mada.myapplication.R
import com.mada.myapplication.TimeFunction.TimeViewModel
import com.mada.myapplication.TimeFunction.calendar.TimeBottomSheetDialog
import com.mada.myapplication.TimeFunction.util.CustomCircleBarView
import com.mada.myapplication.TimeFunction.util.YourMarkerView
import com.mada.myapplication.databinding.FragTimeBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class FragTime : Fragment() {

    lateinit var binding : FragTimeBinding
    private val viewModelTime: TimeViewModel by activityViewModels()
    var lastSelectedEntry = -1
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
        val currentDate: LocalDate = LocalDate.now()
        var formattedDate: String = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        if(viewModelTime.todayString!="")  formattedDate = viewModelTime.todayString


        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko","KR"))
        val outputDateFormat = SimpleDateFormat("M월 d일 E요일", Locale("ko", "KR"))


        customCircleBarView = binding.progressbar
        // 원형 프로그레스 바 진행 상태 변경 (0부터 100까지)
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val progressPercentage = ((hour * 60 + minute).toFloat() / (24 * 60) * 100).toInt()
        customCircleBarView.setProgress(progressPercentage)

        //파이차트
        binding.fragtimeCalendarBtn.setOnClickListener {
            val bottomSheet = TimeBottomSheetDialog(viewModelTime)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }
        viewModelTime.updateData(formattedDate)
        viewModelTime.myLiveToday.observe(viewLifecycleOwner, { newValue ->
            binding.textHomeTimeName.text = outputDateFormat.format(inputDateFormat.parse(newValue))
            today = newValue
            viewModelTime.getScheduleDatas(newValue) { result ->
                when (result) {
                    1 -> {
                        val tmpList = viewModelTime.getTimeDatas(newValue)
                        pirChartOn(tmpList)
                    }
                    2 -> {
                        Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        binding.timeEditBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("today",today)
            bundle.putSerializable("pieChartData", pieChartDataArray[lastSelectedEntry])
            bundle.putSerializable("pieChartDataArray", pieChartDataArray)
            bundle.putInt("frag",R.id.action_fragTimeAdd_to_fragTime)
            Navigation.findNavController(requireView()).navigate(R.id.action_fragTime_to_fragTimeAdd,bundle)
        }
        binding.timeRemoveBtn.setOnClickListener {
            val id = pieChartDataArray[lastSelectedEntry].id
            viewModelTime.delTimeDatas(id) { result ->
                when (result) {
                    1 -> {
                        val tmpList = viewModelTime.hashMapArraySchedule.get(today)!!
                        for(data in tmpList) {
                            if(data.id==id) {
                                tmpList.remove(data)
                                break
                            }
                        }
                        Navigation.findNavController(requireView()).navigate(R.id.action_fragTime_to_fragTime)
                    }
                    2 -> {
                        Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.textTimeTodayHanmadi.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // 포커스를 잃었을 때 (입력이 완료되었을 때) 수행할 작업을 여기에 추가합니다.
                val enteredText = binding.textTimeTodayHanmadi.text.toString()
                Log.d("test",enteredText)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.timeChangeBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragTime_to_fragTimeTable)
        }

        binding.fabHomeTime.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("today",today)
            bundle.putSerializable("pieChartDataArray", pieChartDataArray)
            bundle.putInt("frag",R.id.action_fragTimeAdd_to_fragTime)
            Navigation.findNavController(view).navigate(R.id.action_fragTime_to_fragTimeAdd,bundle)
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
        val marker_ = YourMarkerView(requireContext(), R.layout.home_time_custom_label,pieChartDataArray)

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
            marker = marker_
        }

        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e is PieEntry) {
                    val pieEntry = e as PieEntry
                    val label = pieEntry.label
                    Log.d("select",label)
                    if (label == "999") {
                        binding.timeInfoFl.visibility = View.INVISIBLE
                        lastSelectedEntry = label.toInt()
                        pieDataSet.selectionShift = 0f //하이라이트 크기
                    } else {
                        binding.timeInfoFl.visibility = View.VISIBLE
                        lastSelectedEntry = label.toInt()
                        pieDataSet.selectionShift = smallXY/27f// 다른 라벨의 경우 선택 시 하이라이트 크기 설정
                        binding.timeInfo1Iv.setColorFilter(Color.parseColor(pieChartDataArray[label.toInt()].colorCode))
                        binding.timeInfo2Iv.setColorFilter(Color.parseColor(pieChartDataArray[label.toInt()].colorCode))
                        binding.timeTitleTv.text = pieChartDataArray[label.toInt()].title
                        binding.timeMemoTv.text =  pieChartDataArray[label.toInt()].memo
                        val startTime = timeChangeReverse("${String.format("%02d", pieChartDataArray[label.toInt()].startHour)}:${String.format("%02d", pieChartDataArray[label.toInt()].startMin)}:00")
                        val endTime = timeChangeReverse("${String.format("%02d", pieChartDataArray[label.toInt()].endHour)}:${String.format("%02d", pieChartDataArray[label.toInt()].endMin)}:00")
                        binding.timeTimeTv.text = "$startTime ~ $endTime"
                    }
                }
            }
            override fun onNothingSelected() {
                lastSelectedEntry =-1
                binding.timeInfoFl.visibility = View.INVISIBLE
            }
        })
    }
    fun timeChangeReverse(time: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale("en", "US"))
        val outputFormat = SimpleDateFormat(" a h:mm ", Locale("en", "US"))
        val calendar = Calendar.getInstance()

        calendar.time = inputFormat.parse(time)

        return outputFormat.format(calendar.time)//.replace("AM","오전").replace("PM","오후")
    }

}

