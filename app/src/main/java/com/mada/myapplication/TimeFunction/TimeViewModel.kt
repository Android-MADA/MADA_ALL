package com.mada.myapplication.TimeFunction

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mada.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.mada.myapplication.HomeFunction.Model.CommentAdd
import com.mada.myapplication.HomeFunction.Model.CommentData
import com.mada.myapplication.HomeFunction.Model.Schedule
import com.mada.myapplication.HomeFunction.Model.ScheduleAdd
import com.mada.myapplication.HomeFunction.Model.ScheduleListData
import com.mada.myapplication.HomeFunction.Model.ScheduleResponse
import com.mada.myapplication.HomeFunction.Model.ScheduleTodoCalList
import com.mada.myapplication.HomeFunction.Model.ScheduleWeekListData
import com.mada.myapplication.HomeFunction.Model.ScheduleWeekResponse
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TimeViewModel : ViewModel() {
    val retrofit = Retrofit.Builder().baseUrl("http://www.madaumc.store/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = RetrofitInstance.getInstance().create(HomeApi::class.java)
    var token = Splash2Activity.prefs.getString("token","")

    //날짜가 변할 때 알아올 LiveData
    private val _myLiveToday = MutableLiveData<String>()
    val myLiveToday: LiveData<String>
        get() = _myLiveToday

    var todayString : String =""
    var hanmadi = ""
    val hanmadiMap: HashMap<String, String> = HashMap()


    //2023-08-01 형식
    val hashMapArraySchedule = HashMap<String, ArrayList<Schedule>>()

    val pieChartDataArrayList = listOf(ArrayList<Schedule>(),ArrayList<Schedule>(),ArrayList<Schedule>(),ArrayList<Schedule>()
        ,ArrayList<Schedule>(),ArrayList<Schedule>(),ArrayList<Schedule>())
    val pieChartWeekDatas = listOf(ArrayList<PieChartData>(),ArrayList<PieChartData>(),ArrayList<PieChartData>(),ArrayList<PieChartData>()
        ,ArrayList<PieChartData>(),ArrayList<PieChartData>(),ArrayList<PieChartData>())
    var range : Float = 0.0f

    var addId = 0

    data class PieChartData(
        val title: String,
        val memo: String="",
        var startHour: Int,
        val startMin: Int,
        var endHour: Int,
        val endMin: Int,
        var colorCode: String,
        var divisionNumber: Int,
        val id : Int
    ) : Serializable

    fun updateData(newValue: String) {
        todayString = newValue
        _myLiveToday.value = newValue
    }

    fun extractTime(timeString: String,hourOrMin : Boolean): Int {
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
    fun setPopupBuffering(theContext: Context) : AlertDialog {
        val mDialogView = LayoutInflater.from(theContext).inflate(R.layout.calendar_add_popup_buffering, null)
        val mBuilder = AlertDialog.Builder(theContext)
            .setView(mDialogView)
            .create()

        mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        mBuilder.show()
        mBuilder.setCancelable(false)
        mBuilder.setCanceledOnTouchOutside(false)

        return mBuilder
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
        calendar2.add(Calendar.MINUTE,-29)
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
    fun setPopupOne(theContext: Context, title : String, theView : View) {
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
        mDialogView.findViewById<TextView>(R.id.popone_desc).visibility = View.GONE

        //팝업 타이틀 설정, 버튼 작용 시스템
        mDialogView.findViewById<TextView>(R.id.textTitle).text = title
        mDialogView.findViewById<TextView>(R.id.yesBtnText).setOnClickListener( {
            mBuilder.dismiss()
        })
    }

    fun getScheduleDatas(date : String, callback: (Int) -> Unit) {
        if(hashMapArraySchedule.get(date)==null) {
            val call = service.getTimetable(token,date)
            var arrays = ArrayList<Schedule>()
            call.enqueue(object : Callback<ScheduleListData> {
                override fun onResponse(call2: Call<ScheduleListData>, response: Response<ScheduleListData>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            val datas = apiResponse.datas2.datas
                            for (data in datas) {
                                if(!data.isDeleted)
                                    arrays.add(data)
                            }

                            hashMapArraySchedule.put(date,arrays)
                            callback(1)
                        } else callback(2)
                    } else callback(2)
                }
                override fun onFailure(call: Call<ScheduleListData>, t: Throwable) {
                    callback(2)
                }
            })
        } else callback(1)
    }
    fun getScheduleWeeks(callback: (Int) -> Unit) {
        val hasValues = pieChartDataArrayList.any { it.isNotEmpty() }
        if(!hasValues) {
            val call = service.getWeekTimetable(token)
            call.enqueue(object : Callback<ScheduleWeekListData> {
                override fun onResponse(call2: Call<ScheduleWeekListData>, response: Response<ScheduleWeekListData>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        Log.d("week",apiResponse.toString())
                        if (apiResponse != null) {
                            val datas = apiResponse.datas2.datas
                            for (data in datas) {
                                if(!data.isDeleted) {
                                    if (data.dayOfWeek == "MONDAY") pieChartDataArrayList[0].add(data)
                                    else if (data.dayOfWeek == "TUESDAY") pieChartDataArrayList[1].add(data)
                                    else if (data.dayOfWeek == "WEDNESDAY") pieChartDataArrayList[2].add(data)
                                    else if (data.dayOfWeek == "THURSDAY") pieChartDataArrayList[3].add(data)
                                    else if (data.dayOfWeek == "FRIDAY") pieChartDataArrayList[4].add(data)
                                    else if (data.dayOfWeek == "SATURDAY") pieChartDataArrayList[5].add(data)
                                    else if (data.dayOfWeek == "SUNDAY") pieChartDataArrayList[6].add(data)
                                }
                            }
                            callback(1)
                        } else callback(2)
                    } else callback(2)
                }
                override fun onFailure(call: Call<ScheduleWeekListData>, t: Throwable) {
                    callback(2)
                }
            })
        } else callback(1)
    }
    fun loadWeek(date : String, callback: (Int) -> Unit) {
        val call = service.loadWeekly(token,date)
        var arrays = ArrayList<Schedule>()
        call.enqueue(object : Callback<ScheduleListData> {
            override fun onResponse(call2: Call<ScheduleListData>, response: Response<ScheduleListData>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas2.datas
                        Log.d("hey",apiResponse.toString())
                        for (data in datas) {
                            if(!data.isDeleted)
                                arrays.add(data)
                        }
                        hashMapArraySchedule.put(date,arrays)
                        callback(1)
                    } else callback(2)
                } else callback(2)
            }
            override fun onFailure(call: Call<ScheduleListData>, t: Throwable) {
                callback(2)
            }
        })
    }
    fun getTimeDatas(date : String) :  ArrayList<PieChartData>{
        var arrays = ArrayList<PieChartData>()
        if(hashMapArraySchedule.get(date) != null) {
            val arrays2 = hashMapArraySchedule.get(date)!!.sortedWith(compareBy(
                { it.startTime }
            ))
            var i = 0
            for (data in arrays2) {
                var end00 = 0
                if(data.endTime=="00:00:00")
                    end00 = 24
                val tmp = PieChartData(data.scheduleName,data.memo,extractTime(data.startTime,true),extractTime(data.startTime,false),
                    extractTime(data.endTime,true)+end00,extractTime(data.endTime,false),data.color,i++,data.id)
                arrays.add(tmp)
            }

        }

        return  arrays
    }
    fun getTimeWeekDatas(week : Int) :  List<PieChartData>{
        var arrays = ArrayList<PieChartData>()
        arrays.clear()
        if(pieChartDataArrayList[week] != null) {
            var i = 0
            for (data in pieChartDataArrayList[week]) {
                var end00 = 0
                if(data.endTime=="00:00:00")
                    end00 = 24
                if(data.memo==null) data.memo=""
                val tmp = PieChartData(data.scheduleName,data.memo,extractTime(data.startTime,true),extractTime(data.startTime,false),
                    extractTime(data.endTime,true)+end00,extractTime(data.endTime,false),data.color,i++,data.id)
                arrays.add(tmp)
            }

        }
        val arrays2 = arrays.sortedWith(compareBy(
            { it.startHour },
            { it.startMin }
        ))
        Log.d("week arranged",arrays2.toString())
        return arrays2
    }
    fun addTimeDatas(data : ScheduleAdd, callback: (Int) -> Unit) {
        service.addTime(token,data).enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call2: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        Log.d("data",apiResponse.toString())
                        val datas = apiResponse
                        addId = apiResponse.data.DailyTimetable.id
                        callback(1)
                    } else {
                        Log.d("error!!!!","dsadsa")
                        callback(2)
                    }
                } else {
                    Log.d(response.errorBody().toString(),response.code().toString())
                    callback(2)
                }
            }
            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                Log.d("token",token)
                callback(2)
            }
        })
    }
    fun addTimeWeekDatas(data : ScheduleAdd, callback: (Int) -> Unit) {
        service.addWeekTime(token,data).enqueue(object : Callback<ScheduleWeekResponse> {
            override fun onResponse(call2: Call<ScheduleWeekResponse>, response: Response<ScheduleWeekResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse
                        addId = apiResponse.data.DailyTimetable.id
                        callback(1)
                    } else {
                        callback(2)
                    }
                } else {
                    callback(2)
                }
            }
            override fun onFailure(call: Call<ScheduleWeekResponse>, t: Throwable) {
                Log.d("token",token)
                callback(2)
            }
        })
    }
    fun delTimeDatas(id : Int, callback: (Int) -> Unit) {
        service.deleteTime(token, id).enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call2: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse
                        callback(1)
                    } else {
                        callback(2)
                    }

                } else {
                    callback(2)
                }
            }
            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                callback(2)
            }
        })
    }
    fun delTimeWeekDatas(id : Int, callback: (Int) -> Unit) {
        service.deleteWeekTime(token, id).enqueue(object : Callback<ScheduleWeekResponse> {
            override fun onResponse(call2: Call<ScheduleWeekResponse>, response: Response<ScheduleWeekResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse
                        callback(1)
                    } else {
                        callback(2)
                    }

                } else {
                    callback(2)
                }
            }
            override fun onFailure(call: Call<ScheduleWeekResponse>, t: Throwable) {
                callback(2)
            }
        })
    }
    fun editTimeData(id : Int, data : ScheduleAdd, callback: (Int) -> Unit) {
        service.editTime(token, id,data).enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call2: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        callback(1)
                    } else {
                        callback(2)
                    }
                    /*
                    val bundle = Bundle()
                    bundle.putString("today",today)
                    if(viewpager) {
                        findNavController().navigate(R.id.action_timeAddFragment_to_fragHome)
                    }
                    else
                        findNavController().navigate(R.id.action_timeAddFragment_to_homeTimetableFragment,bundle)*/
                } else {
                    callback(2)
                }
            }
            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                callback(2)
            }
        })
    }
    fun editTimeWeekData(id : Int, data : ScheduleAdd, callback: (Int) -> Unit) {
        service.editWeekTime(token, id,data).enqueue(object : Callback<ScheduleWeekResponse> {
            override fun onResponse(call2: Call<ScheduleWeekResponse>, response: Response<ScheduleWeekResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        callback(1)
                    } else {
                        callback(2)
                    }
                } else {
                    callback(2)
                }
            }
            override fun onFailure(call: Call<ScheduleWeekResponse>, t: Throwable) {
                callback(2)
            }
        })
    }
    fun getTodoCalDatas(date : String,arrays: ArrayList<AndroidCalendarData>, callback: (Int) -> Unit) {
        service.getCalendarTodo(token,date).enqueue(object : Callback<ScheduleTodoCalList> {
            override fun onResponse(call2: Call<ScheduleTodoCalList>, response: Response<ScheduleTodoCalList>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.datas
                        if(datas != null) {
                            for(data in datas.todoList) {
                                arrays.add(AndroidCalendarData("","","","","",
                                    "","","",data.todoName,data.iconId,false,"","TODO",7,0))
                            }
                            for(data in datas.calendarList) {
                                Log.d("cal","${data.CalendarName} ${data.startTime} ${data.endTime} ${data.color} ${data.dday}" )
                                if(data.dday=="N")
                                    arrays.add(AndroidCalendarData("","","",data.startTime,data.endTime,
                                        data.color,"","N",data.CalendarName,1,false,"","CAL",7,0))
                                else
                                    arrays.add(AndroidCalendarData("","","",data.startTime,data.endTime,
                                        data.color,"","Y",data.CalendarName,1,true,"","CAL",7,0))
                            }
                            callback(1)
                        } else callback(2)
                    } else callback(2)
                } else callback(2)
            }
            override fun onFailure(call: Call<ScheduleTodoCalList>, t: Throwable) {
                callback(2)
            }
        })
    }
    fun getComment(date : String, callback: (Int, String) -> Unit) {
        service.getComment(token, date).enqueue(object : Callback<CommentData> {
            override fun onResponse(call2: Call<CommentData>, response: Response<CommentData>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("comment",apiResponse.toString())
                    if (apiResponse != null) {
                        if (apiResponse.data != null) {
                            callback(1,apiResponse.data.comment.content)
                        } else {
                            callback(2,"오류 발생")
                        }
                    } else {
                        callback(2,"오류 발생")
                    }
                } else {
                    Log.d("comment error",response.toString())
                    callback(3,"오류 발생")
                }
            }
            override fun onFailure(call: Call<CommentData>, t: Throwable) {
                Log.d("comment error",t.toString())
                callback(3,"오류 발생")
            }
        })
    }
    fun addComment(data : CommentAdd, callback: (Int) -> Unit) {
        service.addComment(token, data).enqueue(object : Callback<CommentData> {
            override fun onResponse(call2: Call<CommentData>, response: Response<CommentData>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("comment",apiResponse.toString())
                    if (apiResponse != null) {
                        callback(1)
                    } else {
                        callback(2)
                    }
                } else {
                    callback(2)
                }
            }
            override fun onFailure(call: Call<CommentData>, t: Throwable) {
                callback(2)
            }
        })
    }
    fun editComment(date : String, data : CommentAdd, callback: (Int) -> Unit) {
        service.editComment(token, date, data).enqueue(object : Callback<CommentData> {
            override fun onResponse(call2: Call<CommentData>, response: Response<CommentData>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("comment",apiResponse.toString())
                    if (apiResponse != null) {
                        callback(1)
                    } else {
                        callback(2)
                    }
                } else {
                    callback(2)
                }
            }
            override fun onFailure(call: Call<CommentData>, t: Throwable) {
                callback(2)
            }
        })
    }
}