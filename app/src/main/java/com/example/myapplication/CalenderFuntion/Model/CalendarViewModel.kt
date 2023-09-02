package com.example.myapplication.CalenderFuntion.Model

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import org.joda.time.DateTime
import org.joda.time.Days
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale


class CalendarViewModel : ViewModel(){
    //서버 통신
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCalendar::class.java)
    var token = Splash2Activity.prefs.getString("token","")?: "123"


    //포멧
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")


    val hashMapArrayCal = HashMap<String, ArrayList<AndroidCalendarData>>()
    val ddayArrayList = ArrayList<AndroidCalendarData>()
    val repeatArrayList = ArrayList<AndroidCalendarData>()



    //dday 몇일 남은지 함수
    fun daysRemainingToDate(targetDate: String): Int {

        val today = LocalDate.now()
        val target = LocalDate.parse(targetDate, dateFormatter)
        val daysRemaining = target.toEpochDay() - today.toEpochDay()
        return daysRemaining.toInt()
    }
    fun RemainingTwoDates(date1: String, date2: String): Int {
        val date1 = LocalDate.parse(date1, dateFormatter)
        val date2 = LocalDate.parse(date2, dateFormatter)
        val daysRemaining = date2.toEpochDay() - date1.toEpochDay()
        return daysRemaining.toInt()
    }
    fun todayDate() :String {
        val currentDate = LocalDate.now()
        val formattedDate = currentDate.format(dateFormatter)
        return formattedDate
    }
    fun setPopupTwo(theContext: Context,title : String, theView : View, moveFragment : Int) {
        val mDialogView = LayoutInflater.from(theContext).inflate(R.layout.calendar_add_popup, null)
        val mBuilder = AlertDialog.Builder(theContext)
            .setView(mDialogView)
            .create()

        mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        mBuilder.show()

        //팝업 사이즈 조절
        DisplayMetrics()
        theContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        val display = (theContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getSize(size)
        val screenWidth = size.x
        val popupWidth = (screenWidth * 0.8).toInt()
        mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)

        //팝업 타이틀 설정, 버튼 작용 시스템
        mDialogView.findViewById<TextView>(R.id.textTitle).text = title
        mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener( {
            mBuilder.dismiss()
        })
        mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
            Navigation.findNavController(theView).navigate(moveFragment)
            mBuilder.dismiss()
        })
    }
    fun setPopupOne(theContext: Context,title : String, theView : View) {
        val mDialogView = LayoutInflater.from(theContext).inflate(R.layout.calendar_add_popup_one, null)
        val mBuilder = AlertDialog.Builder(theContext)
            .setView(mDialogView)
            .create()

        mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        mBuilder.show()

        //팝업 사이즈 조절
        DisplayMetrics()
        theContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        val display = (theContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getSize(size)
        val screenWidth = size.x
        val popupWidth = (screenWidth * 0.8).toInt()
        mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)

        //팝업 타이틀 설정, 버튼 작용 시스템
        mDialogView.findViewById<TextView>(R.id.textTitle).text = title
        mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener( {
            mBuilder.dismiss()
        })
    }
    //달력 관련 함수들
    fun convertToDateKoreanFormat123(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val outputFormat = SimpleDateFormat("M월 d일", Locale("ko", "KR"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    fun convertToDateKoreanFormat(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en","US"))
        val outputFormat = SimpleDateFormat("  M월 d일 (E)  ", Locale("ko", "KR"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    fun convertToDate2(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    fun TimeChangeKor(time: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale("en","US"))
        val outputFormat = SimpleDateFormat("a hh:mm", Locale("en","US"))
        val calendar = Calendar.getInstance()

        calendar.time = inputFormat.parse(time)

        return "  "+outputFormat.format(calendar.time).replace("AM","오전").replace("PM","오후")+"  "
    }
    fun timeChangeNum(time: String): String {
        val inputFormat = SimpleDateFormat("ah:mm", Locale("en","US"))
        val calendar = Calendar.getInstance()

        val timeModified = time.replace("오전", "AM").replace("오후", "PM").replace(" ","")

        calendar.time = inputFormat.parse(timeModified)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return String.format("%02d:%02d:00", hour, minute)
    }
    fun compareDates(date1: String, date2: String): Boolean {
        //date1 <= date2
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate1 = LocalDate.parse(date1, formatter)
        val localDate2 = LocalDate.parse(date2, formatter)
        return localDate1.isBefore(localDate2) || localDate1.isEqual(localDate2)
    }
    fun compareTimes(time1: String, time2: String): Boolean {
        val inputFormat = SimpleDateFormat("ah:mm", Locale("en","US"))
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

        return (hour1 < hour2 || (hour1 == hour2 && minute1 < minute2))
    }

    //달력 delete 함수

    fun deleteHashmapCal(yearMonth : String,id : Int) {
        val tmpArrayList = hashMapArrayCal.get(yearMonth)
        if (tmpArrayList != null) {
            for(data in tmpArrayList) {
                if(data.id==id) {
                    tmpArrayList.remove(data)
                    return
                }
            }
        }
    }
    fun deleteHashmapDday(id : Int) {
        for(data in ddayArrayList) {
            if(data.id==id) {
                ddayArrayList.remove(data)
                return
            }
        }
    }
    fun deleteCalendar(id : Int , callback: (Int) -> Unit) {
        service.deleteCal(token,id).enqueue(object : Callback<AddCalendarData> {
            override fun onResponse(call: Call<AddCalendarData>, response: Response<AddCalendarData>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) callback(1)
                    else callback(2)
                } else callback(2)
            }
            override fun onFailure(call: Call<AddCalendarData>, t: Throwable) {
                callback(2)
            }
        })
    }
    //달력 불러오고 hashMap에 담기
    fun getMonthDataArray(month : String,year : String, callback: (Int) -> Unit) {
        //임시 데이터, 수정 날짜 순서대로 정렬해야하며 점 일정은 나중으로 넣어야함
        if(hashMapArrayCal.get("${year}-${month}")==null) {
            val monthArray = ArrayList<AndroidCalendarData>()
            val monthArray2 = ArrayList<AndroidCalendarData>()
            service.monthCalRequest(token,year,month).enqueue(object : Callback<CalendarDatasData> {
                override fun onResponse(call2: Call<CalendarDatasData>, response: Response<CalendarDatasData>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            val datas = apiResponse.data.datas
                            //startMon = apiResponse.data.startMon
                            if(datas != null) {
                                for (data in datas) {
                                    val dura : Boolean
                                    if(data.start_date==data.end_date) dura = false
                                    else dura = true
                                    if(data.repeat=="No") {
                                        if(data.d_day=="N") {
                                            val tmp = AndroidCalendarData("${(data.start_date)}","${(data.start_date)}","${(data.end_date)}",
                                                "${data.start_time}","${data.end_time}","${data.color}","${data.repeat}","${data.d_day}","${data.name}",
                                                -1,dura,"${data.memo}","CAL",data.id,data.repeatDate)
                                            if(dura) {
                                                monthArray.add(0,tmp)
                                            } else {
                                                monthArray2.add(tmp)
                                            }
                                        } else if(daysRemainingToDate(data.end_date)<0){
                                            deleteCalendar(data.id) {}
                                        } else {
                                            val tmp = AndroidCalendarData("${(data.end_date)}","${(data.end_date)}","${(data.end_date)}",
                                                "${data.start_time}","${data.end_time}","${data.color}","${data.repeat}","${data.d_day}","${data.name}",
                                                -1,false,"${data.memo}","CAL",data.id,data.repeatDate)
                                            monthArray2.add(tmp)
                                        }
                                    }
                                    //Log.d("data","${data.name} ${data.start_time} ${data.end_time}")
                                }
                            }

                        }
                        monthArray.addAll(monthArray2)
                        hashMapArrayCal.put("${year}-${month}",monthArray)
                        callback(1)
                    }
                }
                override fun onFailure(call: Call<CalendarDatasData>, t: Throwable) {
                    callback(2)
                }
            })
        } else callback(1)

    }
    fun setMonthData(Year : String,Month : String, startMon : Boolean,maxFloor : Int) : HashMap<DateTime,ArrayList<AndroidCalendarData>>{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        val startMon : Boolean
        if(hashMapArrayCal.get("${Year}-${Month}")==null){
            //startMon = getMonthDataArray(Month,Year)
            hashMapArrayCal.put("${Year}-${Month}",ArrayList<AndroidCalendarData>())
        }

        val hashMapDataMonth = HashMap<DateTime, ArrayList<AndroidCalendarData>>()

        val hashMapArrayCalTmp = hashMapArrayCal.get("${Year}-${Month}")!!.clone() as ArrayList<AndroidCalendarData>

        for(data in repeatArrayList) {

            if(data.repeat=="Day") {

            } else if(data.repeat=="Week") {
                val calendar = Calendar.getInstance()
                calendar.clear()
                calendar.set(Calendar.YEAR, Year.toInt())
                calendar.set(Calendar.MONTH, Month.toInt() - 1) // 월은 0부터 시작합니다 (1월 = 0)
                calendar.set(Calendar.DAY_OF_WEEK, data.repeatDate.toInt()+1)

                val dates = mutableListOf<Date>()

                // 주어진 월에 해당하는 요일이 있는 경우에만 날짜를 추가합니다.
                while (calendar.get(Calendar.MONTH) == Month.toInt() - 1) {
                    val clone = data.copy()
                    val todayTmp =dateFormat.format(calendar.time)
                    clone.startDate = todayTmp
                    clone.startDate2 = todayTmp
                    clone.endDate = todayTmp
                    hashMapArrayCalTmp.add(clone)
                    calendar.add(Calendar.DAY_OF_MONTH, 7) // 7일씩 증가시켜 다음 주로 이동합니다.
                }
            } else if(data.repeat=="Month") {
                val clone = data.copy()
                var todayTmp =""
                if(data.repeatDate=="마지막 날") {
                    val calendar = Calendar.getInstance()
                    calendar.clear()
                    calendar.set(Calendar.YEAR, Year.toInt())
                    calendar.set(Calendar.MONTH, Month.toInt() - 1) // 월은 0부터 시작합니다 (1월 = 0)
                    calendar.set(Calendar.DAY_OF_MONTH, 1) // 해당 월의 첫 번째 날로 설정합니다

                    calendar.add(Calendar.MONTH, 1) // 다음 달로 이동합니다
                    calendar.add(Calendar.DAY_OF_MONTH, -1)

                    todayTmp =dateFormat.format(calendar.time)
                } else {
                    todayTmp = String.format("%d-%02d-%02d", Year, Month,data.repeatDate.toInt())
                }
                clone.startDate = todayTmp
                clone.startDate = todayTmp
                clone.startDate2 = todayTmp
                clone.endDate = todayTmp
                hashMapArrayCalTmp.add(clone)
            } else if(data.repeat=="Year"){
                val todayTmp = "${Year}-${data.repeatDate}"
                val clone = data.copy()
                clone.startDate = todayTmp
                clone.startDate = todayTmp
                clone.startDate2 = todayTmp
                clone.endDate = todayTmp
                hashMapArrayCalTmp.add(clone)
            }
        }
        hashMapArrayCalTmp.sortWith(compareBy<AndroidCalendarData> { it.startDate }.thenByDescending { it.endDate })
        for(data in hashMapArrayCalTmp) {
            if(data!=null) {
                val clone = data.copy()
                val startDate = DateTime.parse(clone.startDate)
                val endDate = DateTime.parse(clone.endDate)
                //Log.d("start end", "${startDate} ${endDate}")
                //그리는 층수 정하기
                if(hashMapDataMonth.get(startDate)==null) {
                    hashMapDataMonth.put(startDate,ArrayList<AndroidCalendarData>())
                    clone.floor = 0
                } else if(hashMapDataMonth.get(startDate)!!.size<100) {
                    hashMapDataMonth.get(startDate)!!.sortBy { it.floor }
                    var tmpFloor = 0
                    for (data in hashMapDataMonth.get(startDate)!!) {
                        if(data.floor!=tmpFloor) {
                            clone.floor = tmpFloor
                            break
                        } else tmpFloor++;
                    }
                    if(clone.floor==-1) clone.floor = hashMapDataMonth.get(startDate)!!.size

                }
                hashMapDataMonth.get(startDate)?.add(clone)

                val daysBetween = Days.daysBetween(startDate, endDate).days
                val clone2 = clone.copy()
                for(day in 1..daysBetween) {
                    val currentDate = startDate.plusDays(day)
                    if(hashMapDataMonth.get(currentDate)==null) {
                        hashMapDataMonth.put(currentDate,ArrayList<AndroidCalendarData>())
                    } else {
                        if(clone2.floor>=maxFloor&&hashMapDataMonth.get(currentDate)!!.size<maxFloor) {
                            var tmpFloor = 0
                            for (data in hashMapDataMonth.get(currentDate)!!) {
                                if(data.floor!=tmpFloor) {
                                    clone2.startDate2 = currentDate.toString("yyyy-MM-dd")
                                    clone2.floor = tmpFloor
                                    break
                                } else tmpFloor++;
                            }
                            if(tmpFloor<maxFloor)  {
                                clone2.floor = tmpFloor
                                clone2.startDate2 = currentDate.toString("yyyy-MM-dd")
                            }
                            //Log.d("${currentDate} ${clone2.title}","${clone2.floor} ${hashMapDataMonth.get(currentDate)!!.size}")
                        }
                    }
                    hashMapDataMonth.get(currentDate)?.add(clone2)
                }
            }
        }
        /*
        for ((key, value) in hashMapDataMonth) {
            Log.d("HashMap", "Key: $key, Value: $value")
        }*/

        return hashMapDataMonth
    }
    fun addCalendar(data : CalendarData,callback: (Int) -> Unit){
        service.addCal(token,data).enqueue(object : Callback<CalendarDataId> {
            override fun onResponse(call: Call<CalendarDataId>, response: Response<CalendarDataId>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) {
                        Log.d("response","${responseBody.id} ${responseBody.name}")
                        callback(1)
                    }else
                        callback(2)
                    //Log.d("777","${response.code()}")

                } else {
                    callback(2)
                }
            }
            override fun onFailure(call: Call<CalendarDataId>, t: Throwable) {
                callback(2)
            }

        })

    }
    fun editCalendar(data : CalendarData,id : Int, callback: (Int) -> Unit){
        service.editCal(token,id,data).enqueue(object : Callback<CalendarData> {
            override fun onResponse(call: Call<CalendarData>, response: Response<CalendarData>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) callback(1)
                    else callback(2)
                } else callback(2)
            }
            override fun onFailure(call: Call<CalendarData>, t: Throwable) {
                callback(2)
            }
        })
    }
    fun getDdayDataArray(callback: (Int) -> Unit) {
        if(ddayArrayList.size == 0) {
            service.getAllDday(token).enqueue(object : Callback<CalendarDataDday> {
                override fun onResponse(call2: Call<CalendarDataDday>, response: Response<CalendarDataDday>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            val datas = apiResponse.datas.datas
                            if(datas != null) {
                                for (data in datas) {
                                    val tmp = AndroidCalendarData(data.start_date,data.start_date,data.end_date, data.start_time,data.end_time,
                                        data.color,data.repeat,data.d_day,data.name, -1,false,data.memo,"CAL",data.id,data.repeatDate)
                                    ddayArrayList.add(tmp)
                                }
                            }
                            ddayArrayList.sortBy { daysRemainingToDate(it.endDate) }
                            ddayArrayList.add(AndroidCalendarData("","","3000-01-01", "","",
                                "","","","", -1,false,"","BLANK",-1,""))
                            callback(1)
                        } else callback(2)
                    } else callback(2)
                }
                override fun onFailure(call: Call<CalendarDataDday>, t: Throwable) {
                    callback(2)
                }
            })
        }

    }
    /*
    fun getDdayDataArray() {
        val ddayDatas = ArrayList<AndroidCalendarData>()
        val call2 = service.getAllDday(token)
        call2.enqueue(object : Callback<CalendarDataDday> {
            override fun onResponse(call2: Call<CalendarDataDday>, response: Response<CalendarDataDday>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas.datas
                        if(datas != null) {
                            for (data in datas) {
                                val tmp = AndroidCalendarData("${CalendarViewModel.convertToDate2(data.start_date)}","${CalendarViewModel.convertToDate2(data.start_date)}","${CalendarViewModel.convertToDate2(data.end_date)}",
                                    "${data.start_time}","${data.end_time}","${data.color}","${data.repeat}","${data.d_day}","${data.name}",
                                    -1,true,"${data.memo}","CAL",data.id)
                                ddayDatas.add(tmp)
                                Log.d("111","datas: ${data.name} ${data.color} ${data.d_day}")
                                // ...
                            }
                        }
                        ddayDatas.sortBy { CalendarViewModel.daysRemainingToDate(it.endDate) }

                        for (i in 0 until min(ddayDatas.size, 3)) {
                            val color = ddayDatas[i].color
                            //Log.d("color",color)
                            val imageResource = when (color) {
                                "#89A9D9" -> R.drawable.calendar_ddayblue_smallbackground
                                "#F0768C" -> R.drawable.calendar_ddaypink_smallbackground
                                "#F8D141" -> R.drawable.calendar_ddayyellow_smallbackground
                                else -> R.drawable.calendar_dday_plus
                            }
                            when (i) {
                                0 -> {
                                    binding.dday1.setImageResource(imageResource)
                                    binding.dday1Text.text = "D-${CalendarViewModel.daysRemainingToDate(ddayDatas[i].endDate)}"
                                    binding.dday1TextInfo.text = ddayDatas[i].title
                                }
                                1 -> {
                                    binding.dday2.setImageResource(imageResource)
                                    binding.dday2Text.text = "D-${CalendarViewModel.daysRemainingToDate(ddayDatas[i].endDate)}"
                                    binding.dday2TextInfo.text = ddayDatas[i].title
                                }
                                2 -> {
                                    binding.dday3.setImageResource(imageResource)
                                    binding.dday3Text.text = "D-${CalendarViewModel.daysRemainingToDate(ddayDatas[i].endDate)}"
                                    binding.dday3TextInfo.text = ddayDatas[i].title
                                }
                            }
                        }
                        binding.dday1.setOnClickListener {
                            if(ddayDatas.size>=1) {
                                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                                if(ddayDatas[0].color == "#89A9D9") {
                                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_blue_popup)

                                } else if(ddayDatas[0].color == "#F0768C") {
                                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_pink_popup)
                                } else if(ddayDatas[0].color == "#F8D141"){
                                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_yellow_popup)
                                }
                                mDialogView.findViewById<TextView>(R.id.textTitle).text = ddayDatas[0].title
                                mDialogView.findViewById<TextView>(R.id.textDay).text =CalendarViewModel.convertToDateKoreanFormat123(ddayDatas[0].endDate)
                                mDialogView.findViewById<TextView>(R.id.textDday).text =binding.dday1Text.text.toString()
                                val mBuilder = AlertDialog.Builder(requireContext())
                                    .setView(mDialogView)
                                    .create()
                                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                                mBuilder.show()

                                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                                    val bundle = Bundle()
                                    bundle.putString("title",ddayDatas[0].title)
                                    bundle.putString("startDate",ddayDatas[0].startDate)
                                    bundle.putString("endDate",ddayDatas[0].endDate)
                                    bundle.putString("memo",ddayDatas[0].memo)
                                    bundle.putString("color",ddayDatas[0].color)
                                    bundle.putInt("dday",CalendarViewModel.daysRemainingToDate(ddayDatas[0].endDate))
                                    bundle.putString("Token",token)
                                    bundle.putInt("id",ddayDatas[0].id)
                                    bundle.putBoolean("edit",true)
                                    Navigation.findNavController(requireView()).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                                    mBuilder.dismiss()
                                }
                                mDialogView.findViewById<ImageButton>(R.id.delbutton).setOnClickListener {
                                    //deleteCalendar(ddayDatas[0].id)
                                    Navigation.findNavController(requireView()).navigate(R.id.action_fragCalendar_to_fragCalendar)
                                    mBuilder.dismiss()
                                }
                                //mDialogView.findViewById<ImageButton>(R.id.plus).setImageResource(R.drawable.)

                            } else {
                                val bundle = Bundle()
                                bundle.putString("Token",token)
                                Navigation.findNavController(requireView()).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)

                            }
                        }
                        binding.dday2.setOnClickListener {
                            if(ddayDatas.size>=2) {
                                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                                if(ddayDatas[1].color == "#89A9D9") {
                                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_blue_popup)
                                } else if(ddayDatas[1].color == "#F0768C") {
                                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_pink_popup)
                                } else if(ddayDatas[1].color == "#F8D141"){
                                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_yellow_popup)
                                }
                                mDialogView.findViewById<TextView>(R.id.textTitle).text = ddayDatas[1].title
                                mDialogView.findViewById<TextView>(R.id.textDay).text =CalendarViewModel.convertToDateKoreanFormat123(ddayDatas[1].endDate)
                                mDialogView.findViewById<TextView>(R.id.textDday).text =binding.dday2Text.text.toString()
                                val mBuilder = AlertDialog.Builder(requireContext())
                                    .setView(mDialogView)
                                    .create()
                                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                                mBuilder.show()
                                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                                    val bundle = Bundle()
                                    bundle.putString("Token",token)
                                    bundle.putBoolean("edit",true)
                                    bundle.putString("title",ddayDatas[1].title)
                                    bundle.putString("startDate",ddayDatas[1].startDate)
                                    bundle.putString("endDate",ddayDatas[1].endDate)
                                    bundle.putString("memo",ddayDatas[1].memo)
                                    bundle.putString("color",ddayDatas[1].color)
                                    bundle.putInt("dday",CalendarViewModel.daysRemainingToDate(ddayDatas[1].endDate))
                                    bundle.putInt("id",ddayDatas[1].id)
                                    Navigation.findNavController(requireView()).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                                    mBuilder.dismiss()
                                }
                                mDialogView.findViewById<ImageButton>(R.id.delbutton).setOnClickListener {
                                    //deleteCalendar(ddayDatas[1].id)
                                    Navigation.findNavController(requireView()).navigate(R.id.action_fragCalendar_to_fragCalendar)
                                    mBuilder.dismiss()
                                }
                            } else {
                                val bundle = Bundle()
                                bundle.putString("Token",token)
                                Navigation.findNavController(requireView()).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                            }

                        }
                        binding.dday3.setOnClickListener {
                            if(ddayDatas.size==3) {
                                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.calendar_dday_popup_plus, null)
                                if(ddayDatas[2].color == "#89A9D9") {
                                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_blue_popup)
                                } else if(ddayDatas[2].color == "#F0768C") {
                                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_pink_popup)
                                } else if(ddayDatas[2].color == "#F8D141"){
                                    mDialogView.findViewById<AppCompatImageButton>(R.id.plus).setImageResource(R.drawable.calendar_dday_yellow_popup)
                                }
                                mDialogView.findViewById<TextView>(R.id.textTitle).text = ddayDatas[2].title
                                mDialogView.findViewById<TextView>(R.id.textDay).text =CalendarViewModel.convertToDateKoreanFormat123(ddayDatas[2].endDate)
                                mDialogView.findViewById<TextView>(R.id.textDday).text =binding.dday3Text.text.toString()
                                val mBuilder = AlertDialog.Builder(requireContext())
                                    .setView(mDialogView)
                                    .create()
                                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                                mBuilder.show()
                                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                                    val bundle = Bundle()
                                    bundle.putString("Token",token)
                                    bundle.putBoolean("edit",true)
                                    bundle.putString("title",ddayDatas[2].title)
                                    bundle.putString("startDate",ddayDatas[2].startDate)
                                    bundle.putString("endDate",ddayDatas[2].endDate)
                                    bundle.putString("memo",ddayDatas[2].memo)
                                    bundle.putString("color",ddayDatas[2].color)
                                    bundle.putInt("dday",CalendarViewModel.daysRemainingToDate(ddayDatas[2].endDate))
                                    bundle.putInt("id",ddayDatas[2].id)
                                    Navigation.findNavController(requireView()).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                                    mBuilder.dismiss()
                                }
                                mDialogView.findViewById<ImageButton>(R.id.delbutton).setOnClickListener {
                                    //deleteCalendar(ddayDatas[2].id)
                                    Navigation.findNavController(requireView()).navigate(R.id.action_fragCalendar_to_fragCalendar)
                                    mBuilder.dismiss()
                                }
                            } else {
                                val bundle = Bundle()
                                bundle.putString("Token",token)
                                Navigation.findNavController(requireView()).navigate(R.id.action_fragCalendar_to_calendarAddDday,bundle)
                            }

                        }
                    }
                }
            }
            override fun onFailure(call: Call<CalendarDataDday>, t: Throwable) {
                Log.d("444","itemType: ${t.message}")
            }
        })
    }

     */
}