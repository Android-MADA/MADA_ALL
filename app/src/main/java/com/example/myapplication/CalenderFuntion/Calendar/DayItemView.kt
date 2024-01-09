package com.example.myapplication.CalenderFuntion.Calendar

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
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.getDateColor
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.isSameDay
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.isSameMonth
import com.example.myapplication.R
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DayItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes private val defStyleAttr: Int = R.attr.itemViewStyle,
    @StyleRes private val defStyleRes: Int = R.style.Calendar_ItemViewStyle,
    private val date: DateTime = DateTime(),
    private val firstDayOfMonth: DateTime = DateTime()
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    private val bounds = Rect()

    private var paint: Paint = Paint()

    init {
        /* Attributes */
        context.withStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes) {
            val dayTextSize = getDimensionPixelSize(R.styleable.CalendarView_dayTextSize, 0).toFloat()

            /* 흰색 배경에 유색 글씨 */
            paint = TextPaint().apply {
                isAntiAlias = true
                textSize = dayTextSize
                if (getMonth()!="${date.monthOfYear}") {
                    alpha = 50
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        val dateString = date.dayOfMonth.toString()
        paint.getTextBounds(dateString, 0, dateString.length, bounds)

        val textWidth = paint.measureText(dateString)
        val textHeight = bounds.height().toFloat()

        val x =(width - textWidth)/2.toFloat()
        val y = 72f             //0+textHeight*1.7f = 71.4 ~ 69
        //날짜 그리기
        if(getToday()=="${date.year}-${date.monthOfYear}-${date.dayOfMonth}") {
            val paint2 = Paint()
            paint2.isAntiAlias = true
            paint2.color = Color.parseColor("#486DA3")
            // Draw rounded rectangle
            val roundedRect = RectF(width/2-textHeight*1.2f,   y-textHeight/2 +textHeight*1.2f, width/2+textHeight*1.2f, y-textHeight/2 -textHeight*1.2f)
            val cornerRadius = textHeight // 반지름 값 설정
            canvas.drawRoundRect(roundedRect, cornerRadius, cornerRadius, paint2)
            paint.color =Color.parseColor("#FFFFFF")
            canvas.drawText(dateString,x,y,paint)
        }else {
            canvas.drawText(dateString,x,y,paint)
        }
    }

    fun getToday():String {
        try {
            // 한국 시간대를 지정
            val koreaZoneId = ZoneId.of("Asia/Seoul")

            // 현재 날짜를 한국 시간대로 가져오기
            val currentDate = java.time.LocalDate.now(koreaZoneId)

            // 날짜를 원하는 형식으로 포맷팅
            val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
            return currentDate.format(formatter)

        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
    fun getMonth():String {
        try {
            // 한국 시간대를 지정
            val koreaZoneId = ZoneId.of("Asia/Seoul")

            // 현재 날짜를 한국 시간대로 가져오기
            val currentDate = java.time.LocalDate.now(koreaZoneId)

            // 날짜를 원하는 형식으로 포맷팅
            val formatter = DateTimeFormatter.ofPattern("M")
            return currentDate.format(formatter)

        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}