package com.mada.reapp.TimeFunction


import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mada.reapp.HomeFunction.Model.CommentAdd
import com.mada.reapp.HomeFunction.viewModel.HomeViewModel
import com.mada.reapp.R
import com.mada.reapp.TimeFunction.adapter.TimeTableAdpater
import com.mada.reapp.TimeFunction.calendar.TimeBottomSheetDialog
import com.mada.reapp.databinding.TimeTableFragmentBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class TimeTableFragment : Fragment() {

    lateinit var binding : TimeTableFragmentBinding
    private val viewModel : HomeViewModel by activityViewModels()
    private val viewModelTime: TimeViewModel by activityViewModels()
    var loadWeek = false
    var commentIs = false

    lateinit var today : String
    var errHanmadi = false
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
        val newHeightInPixel = (progressPercentage * scale).toInt()
        val layoutParams: ViewGroup.LayoutParams = binding.timetableTimelineBlank.layoutParams

        layoutParams.height = newHeightInPixel

        binding.timetableTimelineBlank.layoutParams = layoutParams
        binding.timetableTimelineTv.text = "${hour}:${minute}"
        scrollToPosition(newHeightInPixel)

        binding.fragtimeCalendarBtn.setOnClickListener {
            val bottomSheet = TimeBottomSheetDialog(viewModelTime)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        //시간표
        viewModelTime.updateData(formattedDate)
        viewModelTime.myLiveToday.observe(viewLifecycleOwner, { newValue ->
            Log.d("comment !!!!",newValue)
            binding.textHomeTimeName.text = outputDateFormat.format(inputDateFormat.parse(newValue))
            today = newValue
            viewModelTime.getScheduleDatas(newValue) { result ->
                when (result) {
                    1 -> {
                        val tmpList = viewModelTime.getTimeDatas(newValue)
                        if(tmpList.size==0) {
                            loadWeek= true
                            binding.fabHomeTime.setImageResource(R.drawable.time_clock_img)
                        } else {
                            loadWeek= false
                            binding.fabHomeTime.setImageResource(R.drawable.calendar_plus_btn)
                        }
                        timeTableOn(tmpList,today)
                    }
                    2 -> {
                        Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            viewModelTime.getComment(newValue) { result, content ->
                Log.d("comment !", content)
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
                        errHanmadi = true
                        binding.textTimeTodayHanmadi.setHint(content)
                        Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        binding.textTimeTodayHanmadi.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(!errHanmadi) {
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

        }
        return binding.root
    }
    private fun scrollToPosition( y: Int) {
        binding.timeTimetableSv.post(Runnable {
            binding.timeTimetableSv.scrollTo(0, y)
        })
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
                            Navigation.findNavController(view).navigate(R.id.action_fragTimeTable_to_fragTimeTableWeek)
                        }
                        2 -> {
                            Navigation.findNavController(view).navigate(R.id.action_fragTimeTable_to_fragTimeTableWeek)
                            Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                viewModelTime.addComment(CommentAdd(today,enteredText)) { result ->
                    when (result) {
                        1 -> {
                            Navigation.findNavController(view).navigate(R.id.action_fragTimeTable_to_fragTimeTableWeek)
                        }
                        2 -> {
                            Navigation.findNavController(view).navigate(R.id.action_fragTimeTable_to_fragTimeTableWeek)
                            Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.fabHomeTime.setOnClickListener {
            if(loadWeek) {
                viewModelTime.loadWeek(today) { result ->
                    when (result) {
                        1 -> {
                            val tmpList = viewModelTime.getTimeDatas(today)
                            binding.fabHomeTime.setImageResource(R.drawable.calendar_plus_btn)
                            loadWeek = false
                            timeTableOn(tmpList,today)
                        }
                        2 -> {
                            Toast.makeText(context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                val bundle = Bundle()
                bundle.putString("today",today)
                bundle.putSerializable("pieChartDataArray", pieChartDataArray)
                bundle.putInt("frag",R.id.action_fragTimeAdd_to_fragTimeTable)
                Navigation.findNavController(view).navigate(R.id.action_fragTimeTable_to_fragTimeAdd,bundle)
            }
        }
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                Navigation.findNavController(view).navigate(R.id.action_fragTimeTable_to_fragHome)
                return@OnKeyListener true
            }
            false
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun timeTableOn(arrays : ArrayList<TimeViewModel.PieChartData>, today : String) {
        pieChartDataArray.clear()
        if(arrays.size==0) {     //그날 정보가 없다
            pieChartDataArray.add(TimeViewModel.PieChartData("","",0,0,0,0,"#FFFFFF",-1,0))
        } else {
            var tmp = 0     //시작 시간
            var tmp2 = 0
            for(data in arrays) {
                val start = data.startHour * 60 + data.startMin
                val end = data.endHour * 60 + data.endMin
                Log.d("timehelp","$start $end $tmp $tmp2")
                if((tmp*60 + tmp2)==start) {      //이전 일정과 사이에 빈틈이 없을때
                    pieChartDataArray.add(data)
                    tmp = data.endHour
                    tmp2 = data.endMin
                } else {            //이전 일정 사이에 빈틈이 있을 때
                    pieChartDataArray.add(TimeViewModel.PieChartData("","",tmp,tmp2,data.startHour,data.startMin,"#FFFFFF",-1,0))
                    pieChartDataArray.add(data)
                    tmp = data.endHour
                    tmp2 = data.endMin
                }
            }
        }
        Log.d("daydata", pieChartDataArray.toString())
        val recyclerView = binding.timetableRv

// Create and set the layout manager
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

// Create and set the adapter
        val adapter = TimeTableAdpater(pieChartDataArray, today,viewModelTime)
        recyclerView.adapter = adapter
    }


}

