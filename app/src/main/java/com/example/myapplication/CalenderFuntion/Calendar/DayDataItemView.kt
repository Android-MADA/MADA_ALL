package com.example.myapplication.CalenderFuntion.Calendar

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.R
import org.joda.time.DateTime

class DayDataItemView @JvmOverloads constructor(

    context: Context,
    attrs: AttributeSet? = null,
    private val date: DateTime = DateTime(),
    private val year: String,
    private val month: String,
    //private val hashMapDataCal: HashMap<DateTime, ArrayList<AndroidCalendarData>>,
    private val CalendarViewModel: CalendarViewModel

) : View(context, attrs) {
    private lateinit var hashMapDataCal : HashMap<DateTime, ArrayList<AndroidCalendarData>>
    private val weekdays = arrayOf("월","화", "수", "목", "금", "토","일")
    private var maxItemNum = 0
    init {
        setOnClickListener {
            // 클릭 이벤트 발생 시 실행할 코드 작성
            var mDialogView = LayoutInflater.from(context).inflate(R.layout.calendar_popup,null)
            var mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .create()

            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)

            mBuilder.show()
            DisplayMetrics()
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val size = Point()
            val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            display.getSize(size)

            val screenWidth = size.x
            val popupWidth = (screenWidth * 0.8).toInt()

            mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)


            mBuilder?.window?.setGravity(Gravity.CENTER)

            mDialogView.findViewById<TextView>(R.id.textDay2).text = "${date.dayOfMonth} 일"
            mDialogView.findViewById<TextView>(R.id.textPosition).text = weekdays[date.dayOfWeek().get()-1] + "요일"
            mDialogView.findViewById<AppCompatImageButton>(R.id.addBtn).setOnClickListener( {
                val today = date.toString("yyyy-MM-dd")

                if(mDialogView.findViewById<EditText>(R.id.textTitle222).text.toString()=="") {
                    val bundle = Bundle()
                    bundle.putSerializable("calData",AndroidCalendarData(today,today,today,
                        "10:00:00","11:00:00","#2AA1B7","No","N","",
                        -1,false,"","CAL",-1,""))
                    bundle.putString("yearMonth", date.toString("yyyy-M"))
                    Navigation.findNavController(this).navigate(R.id.action_fragCalendar_to_calendarAdd,bundle)
                }
                else {
                    val title = mDialogView.findViewById<EditText>(R.id.textTitle222).text.toString()
                    val tmpData = CalendarData(title,today,date.toString("yyyy-MM-dd")
                        ,"#89A9D9","No","N" , "","10:00:00","11:00:00","")

                    CalendarViewModel.addCalendar(tmpData) { result ->
                        when (result) {
                            1 -> {
                                //통신 성공
                                val tmpId = CalendarViewModel.addId        //서버로 부터 얻은 아이디
                                Toast.makeText(context, "추가 성공", Toast.LENGTH_SHORT).show()

                                val inputYear = date.toString("yyyy").toInt() // 연도 정보
                                val inputMonth = date.toString("M").toInt() // 월 정보 (예: 12는 12월을 나타냄)

                                // 다음 월 계산
                                val nextMonth: String

                                if (inputMonth < 12) {
                                    nextMonth = "${inputYear}-${inputMonth + 1}"
                                } else {
                                    nextMonth = "${inputYear + 1}-1"
                                }
                                val preMonth : String
                                if (inputMonth >1) {
                                    preMonth = "${inputYear}-${inputMonth - 1}"
                                } else {
                                    preMonth = "${inputYear - 1}-12"
                                }
                                CalendarViewModel.hashMapArrayCal.get(nextMonth)?.add(AndroidCalendarData(today,today,today,
                                    "10:00:00","11:00:00","#89A9D9","No","N",title,
                                    -1,false,"","CAL",tmpId,""))
                                CalendarViewModel.hashMapArrayCal.get(preMonth)?.add(AndroidCalendarData(today,today,today,
                                    "10:00:00","11:00:00","#89A9D9","No","N",title,
                                    -1,false,"","CAL",tmpId,""))
                                CalendarViewModel.hashMapArrayCal.get(date.toString("yyyy-M"))?.add(AndroidCalendarData(today,today,today,
                                    "10:00:00","11:00:00","#89A9D9","No","N",title,
                                    -1,false,"","CAL",tmpId,""))

                                //그 달 달력 재생성
                                val parentCalendarView: CalendarView2? = getParent() as? CalendarView2
                                parentCalendarView?.let {
                                    it.updateCalendarData()
                                }
                            }
                            2 -> {
                                Toast.makeText(context, "추가 실패", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                }


                mBuilder.dismiss()
            })

            if(hashMapDataCal.get(date)!=null) {
                val adapter = CalendarScheduleAdapter(hashMapDataCal.get(date)!!,date.dayOfMonth,this,mBuilder)
                var manager: RecyclerView.LayoutManager = GridLayoutManager(context,1)
                mDialogView.findViewById<RecyclerView>(R.id.scheduleList).layoutManager = manager
                mDialogView.findViewById<RecyclerView>(R.id.scheduleList).adapter = adapter
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {

        hashMapDataCal = CalendarViewModel.setMonthData(year,month,false,(height - 130)/55)
        maxItemNum = ((height*0.63f)/55).toInt()
        super.onDraw(canvas)
        if (canvas == null) return
        // date에 있는 데이터 들이
        if(hashMapDataCal.get(date)!=null) {
            Log.d("height",height.toString())
            val size = hashMapDataCal.get(date)!!.size
            var floor = 0
            for(data in hashMapDataCal.get(date)!!) {
                if(data.floor +1 == maxItemNum&&size > maxItemNum) {
                    val paint3 = Paint()
                    paint3.isAntiAlias = true
                    paint3.color = Color.BLACK
                    paint3.textSize = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, getResources().getDisplayMetrics()))
                    canvas.drawText(
                        "+${size-maxItemNum+1}",
                        10f,
                        height/2.2f+45f*data.floor,
                        paint3
                    )
                }
                else if(data.floor < maxItemNum ) {
                    val sizePre : Int
                    var leftRound = false
                    if(hashMapDataCal.get(date.plusDays(-1)) !=null) {
                        sizePre = hashMapDataCal.get(date.plusDays(-1))!!.size
                        if(data.floor +1 == maxItemNum&&sizePre > maxItemNum) {
                            leftRound = true
                        }

                    }
                    val sizeNext :Int
                    var rightRound = false
                    if(hashMapDataCal.get(date.plusDays(1)) !=null) {
                        sizeNext = hashMapDataCal.get(date.plusDays(1))!!.size
                        if(data.floor +1 == maxItemNum&&sizeNext > maxItemNum) {
                            rightRound = true
                        }

                    }

                    val paint2 = Paint()
                    paint2.isAntiAlias = true
                    paint2.color = Color.parseColor(data.color)
                    val roundedRect = RectF(0f, height/2.7f+45f*data.floor, width.toFloat(), height/2.7f+40f+45f*data.floor)
                    val roundedRectCenter = RectF(-50f, height/2.7f+45f*data.floor, width.toFloat()+45f, height/2.7f+40f+45f*data.floor)
                    val roundedRectRight = RectF(-50f, height/2.7f+45f*data.floor, width.toFloat(), height/2.7f+40f+45f*data.floor)
                    val roundedRectLeft = RectF(0f, height/2.7f+45f*data.floor, width.toFloat()+45f, height/2.7f+40f+45f*data.floor)
                    val roundedRectNoDuration = RectF(0f, height/2.7f+45f*data.floor, 13f, height/2.7f+40f+45f*data.floor)
                    val paint3 = Paint()
                    paint3.isAntiAlias = true
                    if(data.duration) {
                        //Log.d("dsadsa",date.dayOfWeek().get().toString())
                        val tmpToday= date.toString("yyyy-MM-dd")
                        if((data.startDate==tmpToday&& date.dayOfWeek().get()==6) ||(data.endDate==tmpToday&& date.dayOfWeek().get()==7)
                            || (leftRound&& date.dayOfWeek().get()==6)||(rightRound&& date.dayOfWeek().get()==7) || (data.startDate==tmpToday&&rightRound)
                            || (data.startDate2==tmpToday&& date.dayOfWeek().get()==6) ||(data.startDate2==tmpToday&&rightRound)) {
                            canvas.drawRoundRect(roundedRect, 15f, 15f, paint2)
                        } else if(data.startDate==tmpToday || leftRound ||date.dayOfWeek().get()==7 || data.startDate2==tmpToday) {
                            canvas.drawRoundRect(roundedRectLeft, 15f, 15f, paint2)
                        } else if(data.endDate==tmpToday || date.dayOfWeek().get()==6 || rightRound) {
                            canvas.drawRoundRect(roundedRectRight, 15f, 15f, paint2)
                        } else {
                            canvas.drawRoundRect(roundedRectCenter, 15f, 15f, paint2)
                        }
                        paint3.color = Color.WHITE
                        paint3.textSize = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11f, getResources().getDisplayMetrics()))
                        canvas.drawText(
                            data.title,
                            20f-width*CalendarViewModel.RemainingTwoDates(data.startDate,date.toString("yyyy-MM-dd")),
                            167f+55f*data.floor,
                            paint3
                        )
                    } else {
                        canvas.drawRoundRect(roundedRectNoDuration, 10f, 10f, paint2)
                        val maxTextWidth = width - 20f // 20f로부터 시작하므로
                        val ellipsis = "..."
                        val textPaint = Paint()
                        textPaint.isAntiAlias = true
                        textPaint.color = Color.BLACK
                        textPaint.textSize = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,
                            11f,
                            resources.displayMetrics
                        )

                        val originalText = data.title
                        val textWidth = textPaint.measureText(originalText)

                        if (textWidth > maxTextWidth) {
                            var newText = originalText
                            var endIndex = newText.length
                            while (textPaint.measureText(newText + ellipsis) > maxTextWidth - 10f && endIndex >= 0) {
                                endIndex--
                                newText = newText.substring(0, endIndex)
                            }
                            newText += ellipsis

                            canvas.drawText(
                                newText,
                                30f,
                                height/2.2f + 45f * data.floor,
                                textPaint
                            )
                        } else {
                            canvas.drawText(
                                originalText,
                                30f,
                                height/2.2f + 45f * data.floor,
                                textPaint
                            )
                        }
                    }



                }
            }
        }
    }
}