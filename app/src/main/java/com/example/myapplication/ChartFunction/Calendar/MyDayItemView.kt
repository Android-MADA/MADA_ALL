package com.example.myapplication.ChartFunction.Calendar

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
import androidx.fragment.app.Fragment
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.isSameDay
import com.example.myapplication.R
import org.joda.time.DateTime

class MyDayItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes private val defStyleAttr: Int = R.attr.itemSmallViewStyle,
    @StyleRes private val defStyleRes: Int = R.style.CalendarSmall_ItemSmallViewStyle,
    private val date: DateTime = DateTime(),
    private val fm: Fragment
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {
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

            }
        }
//        setOnClickListener {
//            Log.d("click","${date}")
//            val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
//            (fm as? MyRecordDayFragment)?.dayChange(formatter.print(date))
//        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        val dateString = date.dayOfMonth.toString()
        paint.getTextBounds(dateString, 0, dateString.length, bounds)
        Log.d("date",date.toString())
        //날짜 그리기
        if(isSameDay(date)) {
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
}