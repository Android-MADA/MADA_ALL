package com.example.myapplication.HomeFunction.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.CustomCircleBarView
import com.example.myapplication.HomeFunction.Model.ScheduleList
import com.example.myapplication.HomeFunction.Model.ScheduleListData
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.time.HomeTimeAdapter
import com.example.myapplication.HomeFunction.time.SampleTimeData
import com.example.myapplication.HomeFunction.time.TimeViewModel
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.YourMarkerView
import com.example.myapplication.databinding.HomeFragmentTimetableBinding
import com.example.myapplication.databinding.HomeFragmentViewpagerTimetableBinding
import com.example.myapplication.hideBottomNavigation
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar

class HomeViewpagerTimetableFragment : Fragment() {

    private lateinit var customCircleBarView: CustomCircleBarView       //프로그래스바
    lateinit var binding : HomeFragmentViewpagerTimetableBinding
    private val viewModelHome: HomeViewModel by activityViewModels()
    private val viewModelTime: TimeViewModel by activityViewModels()

    var today = "2023-06-01"
    override fun onCreate(savedInstanceState: Bundle?) {
        today = viewModelHome.homeDate.value.toString()
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentViewpagerTimetableBinding.inflate((layoutInflater))
        customCircleBarView = binding.progressbar
        // 원형 프로그레스 바 진행 상태 변경 (0부터 100까지)
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val progressPercentage = ((hour * 60 + minute).toFloat() / (24 * 60) * 100).toInt()

        customCircleBarView.setProgress(progressPercentage)

        viewModelHome.homeDate.observe(viewLifecycleOwner, Observer {
            today = viewModelHome.homeDate.value.toString()
            //Log.d("today",today)
            viewModelTime.getScheduleDatas(today) { result ->
                when (result) {
                    1 -> {
                        pirChartOn(viewModelTime.getTimeDatas(today))
                    }
                    2 -> {
                        Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 뷰의 크기가 확정되면 호출됩니다.
                viewModelTime.range = view.width/80f
                viewModelTime.getScheduleDatas(today) { result ->
                    when (result) {
                        1 -> {
                            val layoutParams = binding.timetablelayout.layoutParams as ConstraintLayout.LayoutParams
                            layoutParams.height = view.width
                            binding.timetablelayout.layoutParams = layoutParams

                            pirChartOn(viewModelTime.getTimeDatas(today))

                        }
                        2 -> {
                            Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                // 필요한 작업 수행 후 리스너 제거
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

    }
    companion object {
        @JvmStatic
        fun newInstance() =
            HomeViewpagerTimetableFragment()
    }
    fun pirChartOn(arrays : ArrayList<TimeViewModel.PieChartData>) {
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

        val range = viewModelTime.range
        if(pieChartDataArray.size==0) {     //그날 정보가 없다면
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
                        val bundle = Bundle()
                        bundle.putString("today",today)
                        findNavController().navigate(R.id.action_fragHome_to_timeAddFragment,bundle)
                    }
                }
                override fun onNothingSelected() {
                }
            })
            binding.none.visibility = View.VISIBLE
        } else {
            binding.none.visibility = View.GONE
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

            var lastSelectedEntry = -1
            chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    if (e is PieEntry) {
                        val pieEntry = e as PieEntry
                        val label = pieEntry.label
                        if (label == "999") {
                            pieDataSet.selectionShift = 0f //하이라이트 크기
                            lastSelectedEntry = label.toInt()
                        }else {
                            pieDataSet.selectionShift = 30f// 다른 라벨의 경우 선택 시 하이라이트 크기 설정
                            lastSelectedEntry = label.toInt()
                        }
                    }
                }
                override fun onNothingSelected() {
                    if(lastSelectedEntry>=0&&lastSelectedEntry<900) {
                        val bundle = Bundle()
                        bundle.putString("today",today)
                        bundle.putSerializable("pieChartData", pieChartDataArray[lastSelectedEntry])
                        bundle.putSerializable("pieChartDataArray", pieChartDataArray)
                        findNavController().navigate(R.id.action_fragHome_to_timeAddFragment,bundle)
                    }
                    lastSelectedEntry =-1
                }
            })
        }



    }
}