package com.example.myapplication.TimeFunction


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.TimeFunction.adapter.TimeTableAdpater
import com.example.myapplication.TimeFunction.util.CustomCircleBarView
import com.example.myapplication.databinding.TimeTableFragmentBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class TimeTableFragment : Fragment() {

    lateinit var binding : TimeTableFragmentBinding
    private val viewModel : HomeViewModel by activityViewModels()
    private val viewModelTime: TimeViewModel by activityViewModels()

    lateinit var today : String
    var pieChartDataArray : ArrayList<TimeViewModel.PieChartData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TimeTableFragmentBinding.inflate(layoutInflater)
        today = viewModel.homeDate.value.toString()
        // 원형 프로그레스 바 진행 상태 변경 (0부터 100까지)

        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val progressPercentage = (hour * 60 + minute)
        val layoutParams: ViewGroup.LayoutParams = binding.timetableTimelineBlank.getLayoutParams()

        layoutParams.height = progressPercentage

        binding.timetableTimelineBlank.setLayoutParams(layoutParams)
        binding.timetableTimelineTv.text = "${hour}:${minute}"

        //파이차트
        viewModelTime.getScheduleDatas(today) { result ->
            when (result) {
                1 -> {
                    val tmpList = viewModelTime.getTimeDatas(today)
                    timeTableOn(tmpList)
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

        binding.timeChangeBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragTimeTable_to_fragTime)
        }

        binding.fabHomeTime.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("today",today)
            bundle.putSerializable("pieChartDataArray", pieChartDataArray)
            Navigation.findNavController(view).navigate(R.id.action_fragTimeTable_to_fragTimeAdd,bundle)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun timeTableOn(arrays : ArrayList<TimeViewModel.PieChartData>) {
        if(arrays.size==0) {     //그날 정보가 없다
            pieChartDataArray.add(TimeViewModel.PieChartData("","",0,0,0,0,"#FFFFFF",0,0))
        } else {
            var tmp = 0     //시작 시간

            for(data in arrays) {
                val start = data.startHour.toString().toInt() * 60 + data.startMin.toString().toInt()
                val end = data.endHour.toString().toInt() * 60 + data.endMin.toString().toInt()
                if(tmp==start) {      //이전 일정과 사이에 빈틈이 없을때
                    data.startHour = start
                    data.endHour = end
                    pieChartDataArray.add(data)
                    tmp = end
                } else {            //이전 일정 사이에 빈틈이 있을 때
                    pieChartDataArray.add(TimeViewModel.PieChartData("","",tmp,0,start,0,"#FFFFFF",0,0))
                    data.startHour = start
                    data.endHour = end
                    pieChartDataArray.add(data)
                    tmp = end
                }
            }
        }

        val recyclerView = binding.timetableRv

// Create and set the layout manager
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

// Create and set the adapter
        val adapter = TimeTableAdpater(pieChartDataArray)
        recyclerView.adapter = adapter
    }

}

