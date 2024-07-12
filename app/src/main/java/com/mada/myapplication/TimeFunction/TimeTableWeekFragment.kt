package com.mada.myapplication.TimeFunction


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mada.myapplication.HomeFunction.Model.CommentAdd
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.R
import com.mada.myapplication.TimeFunction.adapter.TimeTableWeekAdpater
import com.mada.myapplication.TimeFunction.calendar.TimeBottomSheetDialog
import com.mada.myapplication.databinding.TimeTableWeekFragmentBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class TimeTableWeekFragment : Fragment() {

    lateinit var binding : TimeTableWeekFragmentBinding
    private val viewModel : HomeViewModel by activityViewModels()
    private val viewModelTime: TimeViewModel by activityViewModels()

    lateinit var today : String
    var recyclerViewList = ArrayList<RecyclerView>()
    var commentIs = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TimeTableWeekFragmentBinding.inflate(layoutInflater)
        recyclerViewList.add(binding.monRv)
        recyclerViewList.add(binding.tusRv)
        recyclerViewList.add(binding.wedRv)
        recyclerViewList.add(binding.thuRv)
        recyclerViewList.add(binding.friRv)
        recyclerViewList.add(binding.satRv)
        recyclerViewList.add(binding.sunRv)
        today = viewModel.homeDate.value.toString()
        val currentDate: LocalDate = LocalDate.now()
        var formattedDate: String = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        //Log.e("todayString",viewModelTime.todayString)
        if(viewModelTime.todayString!="")  formattedDate = viewModelTime.todayString

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko","KR"))
        val outputDateFormat = SimpleDateFormat("M월 d일 E요일", Locale("ko", "KR"))

        // 원형 프로그레스 바 진행 상태 변경 (0부터 100까지)
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val progressPercentage = (hour * 60 + minute)
        val scale = resources.displayMetrics.density
        val newWidthInPixel = (progressPercentage * scale).toInt()
        val layoutParams: ViewGroup.LayoutParams = binding.timetableTimelineBlank.layoutParams

        layoutParams.width = newWidthInPixel

        binding.timetableTimelineBlank.layoutParams = layoutParams
        binding.timetableTimelineTv.text = "${hour}:${minute}"
        binding.timeTimetableSv.setHorizontalScrollBarEnabled(false);

        scrollToPosition(newWidthInPixel)

        binding.fragtimeCalendarBtn.setOnClickListener {
            val bottomSheet = TimeBottomSheetDialog(viewModelTime)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        //파이차트
        viewModelTime.getScheduleWeeks() { result ->
            when (result) {
                1 -> {
                    for (i in 0..6) {
                        val tmpList = viewModelTime.getTimeWeekDatas(i)
                        timeTableOn(tmpList, i)
                    }

                }
                2 -> {
                    Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModelTime.myLiveToday.observe(viewLifecycleOwner, { newValue ->
            viewModelTime.getComment(newValue) { result, content ->
                when (result) {
                    1 -> {
                        binding.textTimeTodayHanmadi.setText(content)
                        commentIs = true
                    }
                    2 -> {
                        binding.textTimeTodayHanmadi.setText("")
                        binding.textTimeTodayHanmadi.setHint("오늘의 한마디!")
                        commentIs = false
                    }
                    3-> {
                        Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        viewModelTime.updateData(formattedDate)
        viewModelTime.myLiveToday.observe(viewLifecycleOwner, { newValue ->
            Log.d("observe","dsadas")
            binding.textHomeTimeName.text = outputDateFormat.format(inputDateFormat.parse(newValue))
            today = newValue
        })
        binding.textTimeTodayHanmadi.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // 포커스를 잃었을 때 (입력이 완료되었을 때) 수행할 작업을 여기에 추가합니다.
                val enteredText = binding.textTimeTodayHanmadi.text.toString()
                Log.d("test",enteredText)
                if(commentIs) {
                    viewModelTime.editComment(today, CommentAdd(today,enteredText)) { result ->
                        when (result) {
                            1 -> {
                            }
                            2 -> {
                                Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    viewModelTime.addComment(CommentAdd(today,enteredText)) { result ->
                        when (result) {
                            1 -> {
                            }
                            2 -> {
                                Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timeChangeBtn.setOnClickListener {
            val enteredText = binding.textTimeTodayHanmadi.text.toString()
            Log.d("test",enteredText)
            if(commentIs) {
                viewModelTime.editComment(today, CommentAdd(today,enteredText)) { result ->
                    when (result) {
                        1 -> {
                            Navigation.findNavController(view).navigate(R.id.action_fragTimeTableWeek_to_fragTime)
                        }
                        2 -> {
                            Navigation.findNavController(view).navigate(R.id.action_fragTimeTableWeek_to_fragTime)
                            Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                viewModelTime.addComment(CommentAdd(today,enteredText)) { result ->
                    when (result) {
                        1 -> {
                            Navigation.findNavController(view).navigate(R.id.action_fragTimeTableWeek_to_fragTime)
                        }
                        2 -> {
                            Navigation.findNavController(view).navigate(R.id.action_fragTimeTableWeek_to_fragTime)
                            Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.fabHomeTime.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragTimeTableWeek_to_fragTimeWeekAdd)
        }


    }

    private fun scrollToPosition( x: Int) {
        binding.timeTimetableSv.post(Runnable {
            // scrollTo()를 사용하여 스크롤뷰를 이동
            binding.timeTimetableSv.scrollTo(x, 0)

            // 또는 smoothScrollTo()를 사용하여 부드럽게 스크롤
            // scrollView.smoothScrollTo(x, y);
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun timeTableOn(arrays : List<TimeViewModel.PieChartData>, i : Int) {
        viewModelTime.pieChartWeekDatas[i].clear()
        if(arrays.size==0) {     //그날 정보가 없다
            viewModelTime.pieChartWeekDatas[i].add(TimeViewModel.PieChartData("","",0,0,0,0,"#FFFFFF",-1,0))
        } else {
            var tmp = 0     //시작 시간
            var tmp2 = 0
            for(data in arrays) {
                val start = data.startHour * 60 + data.startMin
                val end = data.endHour * 60 + data.endMin
                if((tmp*60 + tmp2)==start) {      //이전 일정과 사이에 빈틈이 없을때
                    viewModelTime.pieChartWeekDatas[i].add(data)
                    tmp = data.endHour
                    tmp2 = data.endMin
                } else {            //이전 일정 사이에 빈틈이 있을 때
                    viewModelTime.pieChartWeekDatas[i].add(TimeViewModel.PieChartData("","",tmp,tmp2,data.startHour,data.startMin,"#FFFFFF",-1,0))
                    viewModelTime.pieChartWeekDatas[i].add(data)
                    tmp = data.endHour
                    tmp2 = data.endMin
                }
            }
        }
        Log.d("weekdata", viewModelTime.pieChartWeekDatas[i].toString())
        val recyclerView = recyclerViewList.get(i)

// Create and set the layout manager for horizontal orientation
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

// Create and set the adapter
        val adapter = TimeTableWeekAdpater(viewModelTime.pieChartWeekDatas[i], i, viewModelTime)
        recyclerView.adapter = adapter

    }

}

