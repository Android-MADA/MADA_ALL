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

                if (!isSameMonth(date, firstDayOfMonth)) {
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

        val x = width - bounds.width()
        val y = bounds.height() + 40

        //날짜 그리기
        if(date.toString("yyyy-MM-dd")==LocalDate.now().toString("yyyy-MM-dd")) {
            val paint2 = Paint()
            paint2.isAntiAlias = true
            paint2.color = Color.parseColor("#486DA3")

            // Draw rounded rectangle
            val roundedRect = RectF(0f+width*2.5f/10+3f,   ((bounds.height() + height/8.87)/5f).toFloat(), width*7.5f/10+3f,
                width*5f/10+((bounds.height() + height/8.87)/5f).toFloat()
            )
            val cornerRadius = width/5f // 반지름 값 설정
            canvas.drawRoundRect(roundedRect, cornerRadius, cornerRadius, paint2)
            paint.color =Color.parseColor("#FFFFFF")
        }


        canvas.drawText(
            dateString,
            (width / 2 - bounds.width() / 2).toFloat(),
            (bounds.height() + height/8.87).toFloat(),
            paint
        )
    }
}