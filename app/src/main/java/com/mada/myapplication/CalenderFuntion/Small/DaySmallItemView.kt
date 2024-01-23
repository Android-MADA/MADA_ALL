package com.mada.myapplication.CalenderFuntion.Small

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.lifecycle.ViewModel
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.isSameDay
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.isSameMonth
import com.mada.myapplication.R
import org.joda.time.DateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DaySmallItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes private val defStyleAttr: Int = R.attr.itemSmallViewStyle,
    @StyleRes private val defStyleRes: Int = R.style.CalendarSmall_ItemSmallViewStyle,
    private val date: DateTime = DateTime(),
    private val firstDayOfMonth: DateTime = DateTime(),
    private val Scheldule: TextView,
    private val ScheduleNum: TextView,
    private val cal: LinearLayout,
    private val textDday : TextView,
    private var repeatFlag: Boolean = false
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {
    private val weekdays = arrayOf("월","화", "수", "목", "금", "토","일")
    private val bounds = Rect()

    private var paint: Paint = Paint()
    init {
        /* Attributes */
        context.withStyledAttributes(attrs, R.styleable.CalendarSmallView, defStyleAttr, defStyleRes) {
            val dayTextSize = getDimensionPixelSize(R.styleable.CalendarSmallView_dayTextSmallSize, 0).toFloat()

            /* 흰색 배경에 유색 글씨 */
            paint = TextPaint().apply {
                isAntiAlias = true
                textSize = dayTextSize

                if (!isSameMonth(date, firstDayOfMonth)) {
                    alpha = 50
                }
            }
        }
        setOnClickListener {
            cal.visibility = GONE
            ScheduleNum.text = date.toString("yyyy-MM-dd")
            if(repeatFlag){
                Scheldule.text = " ${date.year}년 ${date.monthOfYear}월 ${date.dayOfMonth}일"

            }
            else{
                Scheldule.text = "  ${date.monthOfYear}월 ${date.dayOfMonth}일 (${weekdays[date.dayOfWeek().get()-1]})  "
            }
            var remainDday = daysRemainingToDate(ScheduleNum.text.toString()).toString()
            if(remainDday.toInt() <=0) remainDday = "DAY"
            textDday.text = "D - ${remainDday}"
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        val dateString = date.dayOfMonth.toString()
        paint.getTextBounds(dateString, 0, dateString.length, bounds)

        //날짜 그리기
        if(ScheduleNum.text.toString()=="${date.year}-${String.format("%02d", date.monthOfYear)}-${String.format("%02d", date.dayOfMonth)}") {
            val paint2 = Paint()
            paint2.isAntiAlias = true
            paint2.color = Color.parseColor("#486DA3")
            // Draw rounded rectangle
            val roundedRect = RectF(0f+width*2f/10+3f,   ((bounds.height() + height/3.5)/7f).toFloat(), width*8f/10+3f,
                width*6f/10+ ((bounds.height() + height/3.5)/7f).toFloat())
            val cornerRadius = width/4.37f // 반지름 값 설정
            canvas.drawRoundRect(roundedRect, cornerRadius, cornerRadius, paint2)
            paint.color =Color.parseColor("#FFFFFF")
            canvas.drawText(
                dateString,
                (width / 2 - bounds.width() / 2).toFloat(),
                (bounds.height() + height/3.5f).toFloat(),
                paint

            )
        }else {
            canvas.drawText(
                dateString,
                (width / 2 - bounds.width() / 2).toFloat(),
                (bounds.height() + height/3.5f).toFloat(),
                paint
            )
        }

    }
    fun daysRemainingToDate(targetDate: String): Int {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()
        val target = LocalDate.parse(targetDate, dateFormatter)
        val daysRemaining = target.toEpochDay() - today.toEpochDay()
        return daysRemaining.toInt()
    }
}