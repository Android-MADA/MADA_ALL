package com.example.myapplication.HomeFunction.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.CalendarDATA
import com.example.myapplication.HomeFunction.HomeBackCustomDialog
import com.example.myapplication.HomeFunction.HomeCustomDialogListener
import com.example.myapplication.HomeFunction.HomeDeleteCustomDialog
import com.example.myapplication.HomeFunction.Model.ScheduleAdd
import com.example.myapplication.HomeFunction.Model.ScheduleResponse
import com.example.myapplication.HomeFunction.Model.ScheduleTodoCalList
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.time.HomeScheduleAndTodoAdapter
import com.example.myapplication.HomeFunction.time.HomeTimeColorAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentTimeAddBinding
import com.example.myapplication.hideBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class TimeAddFragment : Fragment(), HomeCustomDialogListener {

    private lateinit var binding : HomeFragmentTimeAddBinding
    private var bottomFlag = true
    var timeColorArray = ArrayList<Int>()
    var colorAdapter = HomeTimeColorAdapter(timeColorArray)
    private val viewModel : HomeViewModel by activityViewModels()

    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(HomeApi::class.java)
    lateinit var today: String

    var curColor = "#89A9D9"
    lateinit var token : String
    var viewpager = false


    private lateinit var backDialog: HomeBackCustomDialog
    //private lateinit var deleteDialog: HomeDeleteCustomDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback : OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //할일 작성
                customBackDialog()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initColorArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_time_add, container, false)
        hideBottomNavigation(bottomFlag,  requireActivity())

        today = viewModel.homeDate.value.toString()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivColor = binding.ivHomeTimeColor
        val colorSelector = binding.rvHomeTimeColor

        val times = arrayOf<TextView>(
            binding.tvHomeTimeStart,
            binding.tvHomeTimeEnd
        )
        val timepicker = binding.timePicker
        val ticker1 = binding.numberPicker1
        val ticker2 = binding.numberPicker2
        val ticker3 = binding.numberPicker3

        val textTitle = binding.edtHomeCategoryName
        val btnSubmit = binding.btnHomeTimeAddSubmit
        val btnDelete = binding.btnHomeTimeEditDelete
        val textMemo = binding.edtHomeScheduleMemo

        val btnBack = binding.ivHomeAddTimeBack
        // 데이터 받기
        val receivedData = arguments?.getSerializable("pieChartDataArray") as?  ArrayList<HomeViewpagerTimetableFragment.PieChartData>?: null
        Log.d("reciedvd",receivedData.toString())
        val recievedPieData =  arguments?.getSerializable("pieChartData") as  HomeViewpagerTimetableFragment.PieChartData?
        viewpager = arguments?.getBoolean("viewpager")?: false
        today = arguments?.getString("today")?: "2023-06-01"
        token= arguments?.getString("Token")?: ""
        var id = 0

        if(recievedPieData != null) {
            btnSubmit.text = "수정"
            btnDelete.isVisible = true

            textTitle.setText(recievedPieData.title)
            ivColor.imageTintList =  ColorStateList.valueOf(Color.parseColor(recievedPieData.colorCode))
            //ivColor.setColorFilter(Color.parseColor(recievedPieData.colorCode), PorterDuff.Mode.SRC_IN)
            colorSelector.isGone = true

            times[0].text = timeChangeReverse(String.format(Locale("en", "US"), "%02d:%02d:00", recievedPieData.startHour, recievedPieData.startMin))
            times[1].text = timeChangeReverse(String.format(Locale("en", "US"), "%02d:%02d:00", recievedPieData.endHour, recievedPieData.endMin))
            id = recievedPieData.id
            textMemo.setText(recievedPieData.memo)
        }

        //파라미터가 전달된다면(생성이 아니라 수정이라면)
//        if(){
//            //3. 받아온 파라미터들을 알맞은 장소에 넣기
//            //4. 이전 시간표 데이터는 삭제하기
//        }

        //색상 선택창
        val colorListManager = GridLayoutManager(this.activity, 6)
        colorAdapter.setItemClickListener(object : HomeTimeColorAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                binding.ivHomeTimeColor.imageTintList =
                    ColorStateList.valueOf(timeColorArray[position])
                binding.rvHomeTimeColor.isGone = true
                curColor = String.format("#%06X", 0xFFFFFF and timeColorArray[position])
            }
        })
        var colorRecyclerList = binding.rvHomeTimeColor.apply {
            setHasFixedSize(true)
            layoutManager = colorListManager
            adapter = colorAdapter
        }

        ivColor.setOnClickListener {
            if (colorSelector.isVisible) {
                colorSelector.isGone = true
            } else {
                colorSelector.isVisible = true
            }
        }

        // 데이터 클래스 작성해서 옮기기


        var scheduleSelect = 0
        val data1 = arrayOf("오전", "오후")
        val data2 = arrayOf("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55")
        val regex = """\s*(오전|오후)\s+(\d{1,2}):(\d{2})\s*""".toRegex()

        times[0].setOnClickListener {
            times[0].setBackgroundResource(R.drawable.calendar_prebackground)
            times[1].setBackgroundColor(Color.TRANSPARENT)
            timepicker.isVisible = true
            scheduleSelect = 0
            val matchResult = regex.find(times[0].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                if (ampm == "오전") {
                    ticker1.value = 0
                } else {
                    ticker1.value = 1
                }

                ticker2.value = hour.toInt()
                ticker3.value = minute.toInt() / 5
            } else {
                ticker1.value = 0
                ticker2.value = 10
                ticker3.value = 0
            }
        }

        times[1].setOnClickListener {
            times[1].setBackgroundResource(R.drawable.calendar_prebackground)
            times[0].setBackgroundColor(Color.TRANSPARENT)
            timepicker.isVisible = true
            scheduleSelect = 1
            val matchResult = regex.find(times[1].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                if (ampm == "오전") {
                    ticker1.value = 0
                }
                else {
                    ticker1.value = 1
                }

                ticker2.value = hour.toInt()
                ticker3.value = minute.toInt() / 5
            } else {
                ticker1.value = 0
                ticker2.value = 11
                ticker3.value = 0
            }
        }


        ticker1.minValue = 0
        ticker1.maxValue = 1
        ticker1.displayedValues = data1
        ticker3.minValue = 0
        ticker3.maxValue = 11
        ticker3.displayedValues = data2

        ticker1.setOnValueChangedListener { picker, oldVal, newVal ->
            if (newVal == 0) {
                times[scheduleSelect].text =
                    times[scheduleSelect].text.toString().replace("오후", "오전")
            } else {
                times[scheduleSelect].text =
                    times[scheduleSelect].text.toString().replace("오전", "오후")
            }
        }
        ticker2.setOnValueChangedListener { picker, oldVal, newVal ->
            val matchResult = regex.find(times[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                times[scheduleSelect].text = "  " + ampm + " " + newVal + ":" + minute + "  "
            } else {

            }
        }
        ticker3.setOnValueChangedListener { picker, oldVal, newVal ->
            val matchResult = regex.find(times[scheduleSelect].text.toString())
            if (matchResult != null) {
                val (ampm, hour, minute) = matchResult.destructured
                times[scheduleSelect].text =
                    "  " + ampm + " " + hour + ":" + data2[newVal] + "  "
            } else {
            }
        }

        //등록 btn
        btnSubmit.setOnClickListener {
            var start = timePlusMinutes(binding.tvHomeTimeStart.text.toString())
            var end = timePlusMinutes(binding.tvHomeTimeEnd.text.toString())
            if(end == 0)
                end =24*60
            var check = true
            if (receivedData != null) {
                for (data in receivedData) {
                    var tmpStart = data.startHour * 60 + data.startMin
                    var tmpEnd = data.endHour * 60 + data.endMin
                    if(tmpEnd==0)
                        tmpEnd = 24*60
                    if ((start < tmpEnd && start >= tmpStart) || (end > tmpStart && end <= tmpEnd)) {
                        if (data.divisionNumber != recievedPieData?.divisionNumber)
                            check = false
                    }
                }
            } else {

            }
            if(binding.edtHomeCategoryName.text.toString()=="") {
                val mDialogView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.home_fragment_time_add_warningsign, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                val displayMetrics = DisplayMetrics()
                val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val size = Point()
                val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                display.getSize(size)
                val screenWidth = size.x
                val popupWidth = (screenWidth * 0.8).toInt()
                mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)

                mDialogView.findViewById<TextView>(R.id.nickname).setText("스케줄명을 입력하시오")
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener({
                    mBuilder.dismiss()
                })
            } else if(compareTimes(binding.tvHomeTimeStart.text.toString(),binding.tvHomeTimeEnd.text.toString())) {
                val mDialogView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.home_fragment_time_add_warningsign, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                val displayMetrics = DisplayMetrics()
                val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val size = Point()
                val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                display.getSize(size)
                val screenWidth = size.x
                val popupWidth = (screenWidth * 0.8).toInt()
                mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)

                mDialogView.findViewById<TextView>(R.id.nickname).setText("올바른 시간을 입력해 주십시오")
                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener({
                    mBuilder.dismiss()
                })
            } else if(!check){            //이미 해당 시간에 일정이 있을 때
                val mDialogView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.home_fragment_time_add_warningsign, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                val displayMetrics = DisplayMetrics()
                val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val size = Point()
                val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                display.getSize(size)
                val screenWidth = size.x
                val popupWidth = (screenWidth * 0.8).toInt()
                mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)

                mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener({
                    mBuilder.dismiss()
                })
            } else if (check&&!compareTimes(binding.tvHomeTimeStart.text.toString(),binding.tvHomeTimeEnd.text.toString())) {
            //Log.d("time","${timeChange(binding.tvHomeTimeStart.text.toString())} ${timeChange(binding.tvHomeTimeEnd.text.toString())}")
            val tmp = ScheduleAdd(today,binding.edtHomeCategoryName.text.toString(),curColor,timeChange(binding.tvHomeTimeStart.text.toString()),
                timeChange(binding.tvHomeTimeEnd.text.toString()),binding.edtHomeScheduleMemo.text.toString())

            if(binding.btnHomeTimeAddSubmit.text.toString()=="등록") {
                addTimeDatas(tmp)
            } else {
                editTimeData(id,tmp)
            }

            }

        // list 만들고 그 안에 파라미터를 받고(데이터를 저장해서) db에 넘기기
            //2. 시간표 화면으로 이동

        }
        binding.ivHomeAddTimeBack.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("today",today)
            if(viewpager) {
                bottomFlag = false
                findNavController().navigate(R.id.action_timeAddFragment_to_fragHome)
            }
            else
                findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment,bundle)
        }
        btnDelete.setOnClickListener {
            delTimeDatas(id)
        }
        binding.homeFragmentTimeAddLayout.setFocusableInTouchMode(true);
        binding.homeFragmentTimeAddLayout.setOnClickListener {
            binding.homeFragmentTimeAddLayout.requestFocus()
        }

        val datas = ArrayList<CalendarDATA>()
        getTodoCalDatas(today,datas)






        binding.edtHomeCategoryName.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.homeTimeTodoListView.visibility = View.VISIBLE
            } else
                binding.homeTimeTodoListView.visibility = View.GONE
        }
    }

    private fun initColorArray(){
        with(timeColorArray){
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub5))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.main))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub4))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub6))
            timeColorArray.add(android.graphics.Color.parseColor("#FDA4B4"))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub3))
            timeColorArray.add(android.graphics.Color.parseColor("#D4ECF1"))
            timeColorArray.add(android.graphics.Color.parseColor("#7FC7D4"))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.point_main))
            timeColorArray.add(android.graphics.Color.parseColor("#FDF3CF"))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub1))
            timeColorArray.add(resources.getColor(com.example.myapplication.R.color.sub2))


        }
    }
    private fun addTimeDatas(data : ScheduleAdd) {
        val call = service.addTime(token,data)
        Log.d("data",data.toJson())
        call.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call2: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse
                        Log.d("success","${datas.status} ${datas.message} ${datas.succcess} ${datas.data.date} ${datas.data.scheduleName} ${datas.data.color}")
                    } else {
                        Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                    Log.d("333213","itemType: ${response.code()}")
                }
                val bundle = Bundle()
                bundle.putString("today",today)
                if(viewpager) {
                    bottomFlag = false
                    findNavController().navigate(R.id.action_timeAddFragment_to_fragHome)
                }
                else
                    findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment,bundle)
            }
            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }
    private fun delTimeDatas(id : Int) {
        val call = service.deleteTime(token, id)

        call.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call2: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse
                        Log.d("success","${datas.status} ${datas.message} ${datas.succcess} ")
                    } else {
                        Log.d("222","Request was not successful. Message: hi")
                    }
                    val bundle = Bundle()
                    bundle.putString("today",today)
                    if(viewpager) {
                        findNavController().navigate(R.id.action_timeAddFragment_to_fragHome)
                    }
                    else
                        findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment,bundle)
                } else {
                    Log.d("333213","itemType: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }

    private fun editTimeData(id : Int,data : ScheduleAdd) {
        val call = service.editTime(token, id,data)
        call.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call2: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse
                        Log.d("success","${datas.status} ${datas.message} ${datas.succcess} ")
                    } else {
                        Log.d("222","Request was not successful. Message: hi")
                    }
                    val bundle = Bundle()
                    bundle.putString("today",today)
                    if(viewpager) {
                        findNavController().navigate(R.id.action_timeAddFragment_to_fragHome)
                    }
                    else
                        findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment,bundle)
                } else {
                    Log.d("333213","itemType: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })

    }
    // CalendarDATA(
    //                "2023-7-2", "2023-7-2", "2023-7-6", "00:00", "24:00",
    //                "#2AA1B7", "반복 안함", "N", "데이터분석기초 기말고사", -1, true, "","CAL",7
    //            ),
    private fun getTodoCalDatas(date : String,arrays: ArrayList<CalendarDATA>) {
        val call = service.getCalendarTodo(token,date)
        call.enqueue(object : Callback<ScheduleTodoCalList> {
            override fun onResponse(call2: Call<ScheduleTodoCalList>, response: Response<ScheduleTodoCalList>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas
                        if(datas != null) {
                            for(data in datas.todoList) {
                                arrays.add(CalendarDATA("","","","","",
                                    "","","",data.todoName,1,false,"","TODO",7))
                            }

                            for(data in datas.calendarList) {
                                Log.d("cal","${data.CalendarName} ${data.startTime} ${data.endTime} ${data.color} ${data.dday}" )
                                if(data.dday=="N")
                                    arrays.add(CalendarDATA("","","",data.startTime,data.endTime,
                                        data.color,"","N",data.CalendarName,1,false,"","CAL",7))
                                else
                                    arrays.add(CalendarDATA("","","",data.startTime,data.endTime,
                                        data.color,"","Y",data.CalendarName,1,true,"","CAL",7))
                            }

                            val adapter = HomeScheduleAndTodoAdapter(arrays,LocalDate.parse(today).dayOfMonth,binding.edtHomeCategoryName,binding.tvHomeTimeStart,binding.tvHomeTimeEnd, binding.homeTimeTodoListView)
                            var manager: RecyclerView.LayoutManager = GridLayoutManager(context,1)
                            binding.homeTimeTodoList.layoutManager = manager
                            binding.homeTimeTodoList.adapter = adapter
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ScheduleTodoCalList>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }
    fun timeChange(time: String): String {
        val inputFormat = SimpleDateFormat("ah:mm", Locale("en","US"))
        val calendar = Calendar.getInstance()

        val timeModified = time.replace("오전", "AM").replace("오후", "PM").replace(" ","")

        calendar.time = inputFormat.parse(timeModified)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return String.format("%02d:%02d:00", hour, minute)
    }
    fun compareTimes(time1: String, time2: String): Boolean {
        val inputFormat = SimpleDateFormat("ah:mm", Locale("en","US")) // 수정된 형식
        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()

        val time1Modified = time1.replace("오전", "AM").replace("오후", "PM").replace(" ","")
        val time2Modified = time2.replace("오전", "AM").replace("오후", "PM").replace(" ","")

        calendar1.time = inputFormat.parse(time1Modified)
        calendar2.time = inputFormat.parse(time2Modified)

        val hour1 = calendar1.get(Calendar.HOUR_OF_DAY)
        val minute1 = calendar1.get(Calendar.MINUTE)

        val hour2 = calendar2.get(Calendar.HOUR_OF_DAY)
        val minute2 = calendar2.get(Calendar.MINUTE)

        if (hour1 > hour2 || (hour1 == hour2 && minute1 >= minute2)) {
            return true
        } else {
            return false
        }
    }
    fun timeChangeReverse(time: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale("en", "US"))
        val outputFormat = SimpleDateFormat(" a h:mm ", Locale("en", "US"))
        val calendar = Calendar.getInstance()

        calendar.time = inputFormat.parse(time)

        return outputFormat.format(calendar.time).replace("AM","오전").replace("PM","오후")
    }

    fun timePlusMinutes(time: String): Int {
        val inputFormat = SimpleDateFormat("ah:mm", Locale("en","US"))
        val calendar = Calendar.getInstance()

        val timeModified = time.replace("오전", "AM").replace("오후", "PM").replace(" ","")

        calendar.time = inputFormat.parse(timeModified)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val totalMinutes = hour * 60 + minute


        return totalMinutes
    }

    private fun customBackDialog() {
        backDialog = HomeBackCustomDialog(requireActivity(), this)
        backDialog.show()
    }



    // 커스텀 다이얼로그에서 버튼 클릭 시
    override fun onYesButtonClicked(dialog: Dialog, flag: String) {
            Navigation.findNavController(requireView()).navigate(R.id.action_timeAddFragment_to_homeTimetableFragment)
        dialog.dismiss()
    }

    override fun onNoButtonClicked(dialog: Dialog) {
        dialog.dismiss()
    }

}