package com.mada.myapplication.CalenderFuntion.Model

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
import com.mada.myapplication.BuildConfig
import com.mada.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.mada.myapplication.R
import com.mada.myapplication.StartFuction.Splash2Activity
import org.joda.time.DateTime
import org.joda.time.Days
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


class CalendarViewModel : ViewModel(){
    //서버 통신
    val retrofit = Retrofit.Builder().baseUrl(BuildConfig.MADA_BASE)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(RetrofitServiceCalendar::class.java)
    var token = Splash2Activity.prefs.getString("token","")?: "123"


    //포멧
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")


    val hashMapArrayCal = HashMap<String, ArrayList<AndroidCalendarData>>()
    val ddayArrayList = ArrayList<AndroidCalendarData>()
    //val repeatArrayList = ArrayList<AndroidCalendarData>()

    var addId = 0

    fun getToday():String {
        try {
            // 한국 시간대를 지정
            val koreaZoneId = ZoneId.of("Asia/Seoul")

            // 현재 날짜를 한국 시간대로 가져오기
            val currentDate = LocalDate.now(koreaZoneId)

            // 날짜를 원하는 형식으로 포맷팅
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return currentDate.format(formatter)

        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
    fun calculateDaysPassedFromStartOfYear(inputDate: String): Int {
        try {
            // 현재 연도 가져오기
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            // 입력된 날짜 문자열을 Date 객체로 변환
            val dateFormat = SimpleDateFormat("M-d")

            // 현재 연도의 1월 1일로 설정
            val startOfYear = Calendar.getInstance()
            startOfYear.set(Calendar.YEAR, currentYear)
            startOfYear.set(Calendar.MONTH, Calendar.JANUARY)
            startOfYear.set(Calendar.DAY_OF_MONTH, 1)

            // 입력된 날짜를 설정
            val targetDate = Calendar.getInstance()
            targetDate.time = dateFormat.parse(inputDate) ?: return -1
            targetDate.set(Calendar.YEAR, currentYear)

            // 두 날짜 사이의 차이를 계산하여 일 수로 반환
            val diffInMillis: Long = targetDate.timeInMillis - startOfYear.timeInMillis
            return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }


    //dday 몇일 남은지 함수
    fun daysRemainingToDate(targetDate: String): Int {
        val today = LocalDate.now(ZoneId.of("Asia/Seoul"))
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
        val currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
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
            if(moveFragment!=0) {
                Navigation.findNavController(theView).navigate(moveFragment)
                mBuilder.dismiss()
            } else {
                System.exit(0)
            }

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
    fun convertToDateKoreanFormatDday(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy년 M월 d일 (E)", Locale("ko", "KR"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
    fun convertToDateKoreanFormat(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en","US"))
        val outputFormat = SimpleDateFormat("M월 d일 (E)", Locale("ko", "KR"))

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

        return outputFormat.format(calendar.time).replace("AM","오전").replace("PM","오후")
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
        Log.d("id",id.toString())
        service.deleteCal(token,id).enqueue(object : Callback<AddCalendarData1> {
            override fun onResponse(call: Call<AddCalendarData1>, response: Response<AddCalendarData1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody!=null) {
                        if (responseBody.data != null) {
                            callback(1)
                        } else callback(2)
                    } else callback(2)
                } else callback(2)
            }
            override fun onFailure(call: Call<AddCalendarData1>, t: Throwable) {
                callback(2)
            }
        })
    }
    //달력 불러오고 hashMap에 담기
    fun getMonthDataArray(month : Int,year : Int, callback: (Int) -> Unit) {
        //임시 데이터, 수정 날짜 순서대로 정렬해야하며 점 일정은 나중으로 넣어야함
        if(hashMapArrayCal.get("${year}-${month}")==null) {
            val monthArray = ArrayList<AndroidCalendarData>()
            val monthArray2 = ArrayList<AndroidCalendarData>()
            val repeatArray = HashMap<Int,CalendarDataId>()
            service.monthCalRequest(token,year,month).enqueue(object : Callback<CalendarDatasData> {
                override fun onResponse(call2: Call<CalendarDatasData>, response: Response<CalendarDatasData>) {
                    Log.d("respone",response.toString())
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        //Log.d("datas",apiResponse.toString())
                        if (apiResponse != null) {
                            val datas = apiResponse.data.datas
                            //startMon = apiResponse.data.startMon
                            if(datas != null) {
                                for (data in datas) {
                                    Log.d("data",data.toString())
                                    val dura : Boolean
                                    if(data.start_date==data.end_date) dura = false
                                    else dura = true
                                    if(data.repeat=="N"||data.repeat==null) {
                                        if(data.d_day=="N") {
                                            val tmp = AndroidCalendarData("${(data.start_date)}","${(data.start_date)}","${(data.end_date)}",
                                                "${data.start_time}","${data.end_time}","${data.color}","${data.repeat}","${data.d_day}","${data.name}",
                                                -1,dura,"${data.memo}","CAL",data.id,data.repeatInfo)
                                            if(dura) {
                                                monthArray.add(0,tmp)
                                            } else {
                                                monthArray2.add(tmp)
                                            }
                                        } else if(daysRemainingToDate(data.end_date)<0){
                                            //deleteCalendar(data.id) {}
                                        } else {
                                            val tmp = AndroidCalendarData("${(data.end_date)}","${(data.end_date)}","${(data.end_date)}",
                                                "${data.start_time}","${data.end_time}","${data.color}","${data.repeat}","${data.d_day}","${data.name}",
                                                -1,false,"${data.memo}","CAL",data.id,data.repeatInfo)
                                            monthArray2.add(tmp)
                                        }
                                    } else {
                                        repeatArray.put(data.id,data)
                                    }
                                }
                            }
                            val repeats = apiResponse.data.repeats
                            if(repeats != null) {
                                for (repeat in repeats) {
                                    Log.d("repeat",repeat.toString())
                                    val data = repeatArray.get(repeat.id)
                                    if(data !=null) {
                                        val tmp = AndroidCalendarData("${(repeat.date)}","${(repeat.date)}","${(repeat.date)}",
                                            "${data.start_time}","${data.end_time}","${data.color}","${data.repeat}","${data.d_day}","${data.name}",
                                            -1,false,"${data.memo}","CAL",data.id,data.repeatInfo)
                                        monthArray2.add(tmp)
                                    }
                                }
                            }
                            monthArray.addAll(monthArray2)
                        }

                        hashMapArrayCal.put("${year}-${month}",monthArray)
                        callback(1)
                    } else callback(2)
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
        /*
        for(data in repeatArrayList) {

            if(data.repeat=="Day") {
                val calendar = Calendar.getInstance()
                calendar.clear()
                calendar.set(Calendar.YEAR, Year.toInt())
                calendar.set(Calendar.MONTH, Month.toInt() - 1) // 월은 0부터 시작합니다 (1월 = 0)
                calendar.set(Calendar.DAY_OF_MONTH, 1)

                val dates = mutableListOf<Date>()

                // 주어진 월에 해당하는 요일이 있는 경우에만 날짜를 추가합니다.
                while (calendar.get(Calendar.MONTH) == Month.toInt() - 1) {
                    val clone = data.copy()
                    val todayTmp =dateFormat.format(calendar.time)
                    clone.startDate = todayTmp
                    clone.startDate2 = todayTmp
                    clone.endDate = todayTmp
                    hashMapArrayCalTmp.add(clone)
                    calendar.add(Calendar.DAY_OF_MONTH, 1) // 7일씩 증가시켜 다음 주로 이동합니다.
                }
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
                if(data.repeatDate=="32") {
                    val calendar = Calendar.getInstance()
                    calendar.clear()
                    calendar.set(Calendar.YEAR, Year.toInt())
                    calendar.set(Calendar.MONTH, Month.toInt() - 1) // 월은 0부터 시작합니다 (1월 = 0)
                    calendar.set(Calendar.DAY_OF_MONTH, 1) // 해당 월의 첫 번째 날로 설정합니다

                    calendar.add(Calendar.MONTH, 1) // 다음 달로 이동합니다
                    calendar.add(Calendar.DAY_OF_MONTH, -1)

                    todayTmp =dateFormat.format(calendar.time)
                } else {
                    todayTmp = String.format("%d-%02d-%02d", Year.toInt(), Month.toInt(),data.repeatDate.toInt())
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
        }*/
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

        return hashMapDataMonth
    }

    fun addCalendar(data : CalendarData,callback: (Int) -> Unit){
        service.addCal(token,data).enqueue(object : Callback<AddCalendarData1> {
            override fun onResponse(call: Call<AddCalendarData1>, response: Response<AddCalendarData1>) {
                Log.d("ddddddddddddddddd","${response.body()} ${response}")
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("viewmodel",responseBody.toString())
                    if(responseBody!=null) {
                        addId = responseBody.data.calendars.id    //responseBody.data.id
                        callback(1)
                    }else
                        callback(2)
                    //Log.d("777","${response.code()}")

                } else {
                    callback(2)
                }
            }
            override fun onFailure(call: Call<AddCalendarData1>, t: Throwable) {
                callback(2)
            }

        })

    }

    fun editCalendar(data : CalendarDataEdit,id : Int, callback: (Int) -> Unit){
        service.editCal(token,id,data).enqueue(object : Callback<CalendarData> {
            override fun onResponse(call: Call<CalendarData>, response: Response<CalendarData>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("editcal",responseBody.toString())
                    if(responseBody!=null) callback(1)
                    else callback(2)
                } else callback(2)
            }
            override fun onFailure(call: Call<CalendarData>, t: Throwable) {
                callback(2)
            }
        })
    }

    fun delRepeat(id : Int, option : Int, date : String, callback: (Int) -> Unit){
        service.delRepeatCal(token,id,option,date).enqueue(object : Callback<CalendarData> {
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
            service.getAllDday(token).enqueue(object : Callback<CalendarDatasData> {
                override fun onResponse(call2: Call<CalendarDatasData>, response: Response<CalendarDatasData>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            val datas = apiResponse.data.datas
                            Log.d("data!!!",datas.toString())
                            if(datas != null) {
                                for (data in datas) {
                                    val tmp = AndroidCalendarData(data.start_date,data.start_date,data.end_date, data.start_time,data.end_time,
                                        data.color,data.repeat,data.d_day,data.name, -1,false,data.memo,"CAL",data.id,data.repeatInfo)
                                    ddayArrayList.add(tmp)
                                }
                            }
                            ddayArrayList.sortBy { daysRemainingToDate(it.endDate) }
                            //ddayArrayList.add(AndroidCalendarData("","","4000-01-01", "","",
                            //    "","","","", -1,false,"","BLANK",-1,""))
                            callback(1)
                        } else callback(2)
                    } else callback(2)
                }
                override fun onFailure(call: Call<CalendarDatasData>, t: Throwable) {
                    callback(2)
                }
            })
        } else callback(1)

    }
}