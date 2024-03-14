package com.mada.myapplication.CalenderFuntion.Calendar

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextPaint
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
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.withStyledAttributes
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mada.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.mada.myapplication.CalenderFuntion.Model.CalendarData
import com.mada.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.mada.myapplication.R
import org.joda.time.DateTime

class DayDataItemView @JvmOverloads constructor(

    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes private val defStyleAttr: Int = R.attr.itemViewStyle,
    @StyleRes private val defStyleRes: Int = R.style.Calendar_ItemViewStyle,
    private val date: DateTime = DateTime(),
    private val year: String,
    private val month: String,
    //private val hashMapDataCal: HashMap<DateTime, ArrayList<AndroidCalendarData>>,
    private val CalendarViewModel: CalendarViewModel

) : View(context, attrs) {
    private lateinit var hashMapDataCal : HashMap<DateTime, ArrayList<AndroidCalendarData>>
    private val weekdays = arrayOf("월","화", "수", "목", "금", "토","일")
    private var maxItemNum = 0
    private var paint: Paint = Paint()
    private val bounds = Rect()
    init {
        context.withStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes) {
            val dayTextSize = getDimensionPixelSize(R.styleable.CalendarView_dayTextSize, 0).toFloat()
            /* 흰색 배경에 유색 글씨 */
            paint = TextPaint().apply {
                textSize = dayTextSize
            }
        }
        setOnClickListener {
            Log.d("click","dsadsa")
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
            val popupWidth = (screenWidth * 0.9).toInt()

            mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)


            mBuilder?.window?.setGravity(Gravity.CENTER)

            mDialogView.findViewById<TextView>(R.id.textDay2).text = "${date.dayOfMonth} 일"
            mDialogView.findViewById<TextView>(R.id.textPosition).text = weekdays[date.dayOfWeek().get()-1] + "요일"
            mDialogView.findViewById<AppCompatImageButton>(R.id.addBtn).setOnClickListener( {
                val today = date.toString("yyyy-MM-dd")

                if(mDialogView.findViewById<EditText>(R.id.textTitle222).text.toString()=="") {
                    val bundle = Bundle()
                    bundle.putSerializable("calData",AndroidCalendarData(today,today,today,
                        "10:00:00","11:00:00","#2AA1B7","N","N","",
                        -1,false,"","CAL",-1,0))
                    Navigation.findNavController(this).navigate(R.id.action_fragCalendar_to_calendarAdd,bundle)
                }
                else {
                    val title = mDialogView.findViewById<EditText>(R.id.textTitle222).text.toString()
                    val tmpData = CalendarData(title,today,date.toString("yyyy-MM-dd")
                        ,"#89A9D9","N",0 , "10:00:00","11:00:00","N","")

                    CalendarViewModel.addCalendar(tmpData) { result ->
                        when (result) {
                            1 -> {
                                //통신 성공
                                val tmpId = CalendarViewModel.addId        //서버로 부터 얻은 아이디
                                Toast.makeText(context, "추가 성공", Toast.LENGTH_SHORT).show()

                                val inputYear = date.toString("yyyy").toInt() // 연도 정보
                                val inputMonth = date.toString("M").toInt() // 월 정보 (예: 12는 12월을 나타냄)

                                CalendarViewModel.hashMapArrayCal.get(date.toString("yyyy-M"))?.add(AndroidCalendarData(today,today,today,
                                    "10:00:00","11:00:00","#89A9D9","N","N",title,
                                    -1,false,"","CAL",tmpId,0))

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
        val dateString = date.dayOfMonth.toString()
        paint.getTextBounds(dateString, 0, dateString.length, bounds)
        val textHeight = bounds.height().toFloat()
        val y =110

        hashMapDataCal = CalendarViewModel.setMonthData(year,month,false,(height - 130)/55)
        maxItemNum = ((height-y)/50).toInt()                 //(height)/55).toInt()
        super.onDraw(canvas)
        if (canvas == null) return
        if(hashMapDataCal.get(date)!=null) {
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
                        11f,
                        y+32f+50f*data.floor,       //y는 초기 위치, 32는 글자 크기, 45*data.floor는 층
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
                    val roundedRect = RectF(0f, y+43f+50f*data.floor, width.toFloat(), y+50f*data.floor)
                    val roundedRectCenter = RectF(-50f, y+43f+50f*data.floor, width.toFloat()+45f, y+50f*data.floor)
                    val roundedRectRight = RectF(-50f, y+43f+50f*data.floor, width.toFloat(), y+50f*data.floor)
                    val roundedRectLeft = RectF(0f, y+43f+50f*data.floor, width.toFloat()+45f, y+50f*data.floor)
                    val roundedRectNoDuration = RectF(0f, y+43f+50f*data.floor, 13f, y+50f*data.floor)
                    val paint3 = Paint()
                    paint3.isAntiAlias = true
                    if(data.duration) {
                        //Log.d("dsadsa",date.dayOfWeek().get().toString())
                        val tmpToday= date.toString("yyyy-MM-dd")
                        paint3.color = Color.WHITE
                        paint3.textSize = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11f, getResources().getDisplayMetrics()))
                        if((data.startDate==tmpToday&& date.dayOfWeek().get()==6) ||(data.endDate==tmpToday&& date.dayOfWeek().get()==7)
                            || (leftRound&& date.dayOfWeek().get()==6)||(rightRound&& date.dayOfWeek().get()==7) || (data.startDate==tmpToday&&rightRound)
                            || (data.startDate2==tmpToday&& date.dayOfWeek().get()==6) ||(data.startDate2==tmpToday&&rightRound)) {
                            canvas.drawRoundRect(roundedRect, 15f, 15f, paint2)
                            canvas.drawText(
                                data.title,
                                30f,
                                y+32f + 50f*data.floor,
                                paint3
                            )
                        } else if(data.startDate==tmpToday || leftRound ||date.dayOfWeek().get()==7 || data.startDate2==tmpToday) {
                            canvas.drawRoundRect(roundedRectLeft, 15f, 15f, paint2)
                            canvas.drawText(
                                data.title,
                                30f,
                                y+32f + 50f*data.floor,
                                paint3
                            )
                        } else if(data.endDate==tmpToday || date.dayOfWeek().get()==6 || rightRound) {
                            canvas.drawRoundRect(roundedRectRight, 15f, 15f, paint2)
                        } else {
                            canvas.drawRoundRect(roundedRectCenter, 15f, 15f, paint2)
                        }


                    } else {
                        canvas.drawRoundRect(roundedRectNoDuration, 10f, 10f, paint2)
                        val maxTextWidth = width - 30f // 20f로부터 시작하므로
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
                                y+32f+ 50f * data.floor,
                                textPaint
                            )
                        } else {
                            canvas.drawText(
                                originalText,
                                30f,
                                y+32f + 50f * data.floor,
                                textPaint
                            )
                        }
                    }



                }
            }
        }
    }
}