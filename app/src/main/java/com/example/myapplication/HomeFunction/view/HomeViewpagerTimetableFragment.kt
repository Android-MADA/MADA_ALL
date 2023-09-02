package com.example.myapplication.HomeFunction.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myapplication.CustomCircleBarView
import com.example.myapplication.HomeFunction.Model.ScheduleListData
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.time.SampleTimeData
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.YourMarkerView
import com.example.myapplication.databinding.HomeFragmentViewpagerTimetableBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.Calendar

class HomeViewpagerTimetableFragment : Fragment() {

    private lateinit var customCircleBarView: CustomCircleBarView       //프로그래스바
    lateinit var binding : HomeFragmentViewpagerTimetableBinding
    private val viewModel: HomeViewModel by activityViewModels()

    data class PieChartData(
        val title: String,
        val memo: String,
        val startHour: Int,
        val startMin: Int,
        val endHour: Int,
        val endMin: Int,
        val colorCode: String,
        val divisionNumber: Int,
        val id : Int
    ) : Serializable

//    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
//        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = RetrofitInstance.getInstance().create(HomeApi::class.java)
    var token = ""

    var today = "2023-06-01"
    override fun onCreate(savedInstanceState: Bundle?) {
        today = viewModel.homeDate.value.toString()
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.homeDate.observe(viewLifecycleOwner, Observer {
            today = viewModel.homeDate.value.toString()
            getTimeDatas(today)

        })

        binding = HomeFragmentViewpagerTimetableBinding.inflate((layoutInflater))
        customCircleBarView = binding.progressbar
        token = Splash2Activity.prefs.getString("token","")
        // 원형 프로그레스 바 진행 상태 변경 (0부터 100까지)
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val progressPercentage = ((hour * 60 + minute).toFloat() / (24 * 60) * 100).toInt()

        customCircleBarView.setProgress(progressPercentage.toInt())
        //파이차트
        getTimeDatas(today)

        viewModel.homeDate.observe(viewLifecycleOwner, Observer {
            Log.d("timetable date 확인", viewModel.homeDate.value.toString())
        })

        binding.chart.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("viewpager",true)
            bundle.putString("Token",token)
            bundle.putString("today",today)
            Navigation.findNavController(requireView()).navigate(R.id.action_fragHome_to_timeAddFragment,bundle)
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeViewpagerTimetableFragment()
    }
    private fun extractTime(timeString: String,hourOrMin : Boolean): Int {
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
    private fun getTimeDatas(date : String) {
        val call = service.getTimetable(token,date)
        val arrays = ArrayList<HomeViewpagerTimetableFragment.PieChartData>()
        val sampleTimeArray = ArrayList<SampleTimeData>()
        call.enqueue(object : Callback<ScheduleListData> {
            override fun onResponse(call2: Call<ScheduleListData>, response: Response<ScheduleListData>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas2.datas
                        if(datas != null) {
                            var i = 0
                            for (data in datas) {
                                var end00 = 0
                                if(data.endTime=="00:00:00")
                                    end00 = 24
                                val tmp = HomeViewpagerTimetableFragment.PieChartData(data.scheduleName,data.memo,extractTime(data.startTime,true),extractTime(data.startTime,false),
                                    extractTime(data.endTime,true)+end00,extractTime(data.endTime,false),data.color,i++,data.id)
                                arrays.add(tmp)
                                sampleTimeArray.add(SampleTimeData(data.scheduleName,data.color))
                                Log.d("time","${data.scheduleName} ${data.startTime} ${data.endTime} ${data.id}")
                            }
                        }
                        pirChartOn(arrays)

                    }
                }
            }
            override fun onFailure(call: Call<ScheduleListData>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }
    private fun pirChartOn(arrays : ArrayList<PieChartData>) {
        val tmp2 = arrays.sortedWith(compareBy(
            { it.startHour },
            { it.startMin }
        ))


        var tmp = 0     //시작 시간

        var dataArray = tmp2.toMutableList() as ArrayList<PieChartData>
        val pieChartDataArray = dataArray
        //Pi Chart
        var chart = binding.chart


        val marker_ = YourMarkerView(requireContext(), R.layout.home_time_custom_label,pieChartDataArray)
        val entries = ArrayList<PieEntry>()
        val colorsItems = ArrayList<Int>()

        if(pieChartDataArray.size==0) {     //그날 정보가 없다면
            binding.none.visibility = View.VISIBLE
            entries.add(PieEntry(10f, "998"))
            colorsItems.add(Color.parseColor("#F0F0F0"))
        } else {
            binding.none.visibility = View.GONE
        }
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
        if(pieChartDataArray.size>0&&pieChartDataArray[pieChartDataArray.size-1].endHour!=24) {
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
        Log.d("chartWidth","${chart.width} ${chart.height}")
        val smallXY = if(chart.width > chart.height) chart.height else chart.width
        val range = smallXY/60f

        chart.apply {
            data = pieData
            isRotationEnabled = false                               //드래그로 회전 x
            isDrawHoleEnabled = false                               //중간 홀 그리기 x
            setExtraOffsets(range,range,range,range)    //크기 조절
            setUsePercentValues(false)
            setEntryLabelColor(Color.BLACK)
            marker = marker_
            setDrawEntryLabels(false) //라벨 끄기
            //rotationAngle = 30f // 회전 각도, 굳이 필요 없을듯
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
                    } else if(label == "998") {
                        pieDataSet.selectionShift = 0f //하이라이트 크기
                        lastSelectedEntry = 998
                    } else {
                        pieDataSet.selectionShift = 30f// 다른 라벨의 경우 선택 시 하이라이트 크기 설정
                        lastSelectedEntry = label.toInt()
                    }

                }
            }
            override fun onNothingSelected() {
                if(lastSelectedEntry==998) {
                    val bundle = Bundle()
                    bundle.putBoolean("viewpager",true)
                    bundle.putString("Token",token)
                    bundle.putString("today",today)
                    findNavController().navigate(R.id.action_fragHome_to_timeAddFragment,bundle)
                } else if(lastSelectedEntry>=0&&lastSelectedEntry<900) {
                    val bundle = Bundle()
                    bundle.putSerializable("pieChartData", pieChartDataArray[lastSelectedEntry])
                    bundle.putSerializable("pieChartDataArray", pieChartDataArray)

                    bundle.putBoolean("viewpager",true)
                    bundle.putString("Token",token)
                    findNavController().navigate(R.id.action_fragHome_to_timeAddFragment,bundle)
                }
                lastSelectedEntry =-1
            }
        })

    }
}