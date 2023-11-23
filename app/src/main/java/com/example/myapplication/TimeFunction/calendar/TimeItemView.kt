package com.example.myapplication.TimeFunction.calendar

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
import androidx.lifecycle.ViewModel
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.isSameDay
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.TimeFunction.TimeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.joda.time.DateTime

class TimeItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes private val defStyleAttr: Int = R.attr.itemSmallViewStyle,
    @StyleRes private val defStyleRes: Int = R.style.CalendarSmall_ItemSmallViewStyle,
    private val date: DateTime = DateTime(),
    private val viewModel : ViewModel,
    private val dialog: BottomSheetDialogFragment,
    private val firstDayOfMonth: DateTime = DateTime()
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
                if (!CalendarUtils.isSameMonth(date, firstDayOfMonth)) {
                    alpha = 50
                }
            }
        }
        setOnClickListener {
            //Log.d("click",date.toString("yyyy-MM-dd"))
            val homeViewModel: HomeViewModel? = if (viewModel is HomeViewModel) {
                viewModel as HomeViewModel
            } else {
                null
            }

            val timeViewModel: TimeViewModel? = if (viewModel is TimeViewModel) {
                viewModel as TimeViewModel

            } else {
                null
            }
            if (timeViewModel != null) {
                timeViewModel.updateData(date.toString("yyyy-MM-dd"))
            }
            if (homeViewModel != null) {
                homeViewModel.updateData(date.toString("yyyy-MM-dd"))
            }
            dialog.dismiss()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        val dateString = date.dayOfMonth.toString()
        paint.getTextBounds(dateString, 0, dateString.length, bounds)

        val textWidth = paint.measureText(dateString)
        val textHeight = paint.fontMetrics.bottom - paint.fontMetrics.top

        val x =(width - textWidth)/2.toFloat()
        val y =( height-textHeight)/2.toFloat()- paint.fontMetrics.top
        //날짜 그리기
        if(isSameDay(date)) {
            val paint2 = Paint()
            paint2.isAntiAlias = true
            paint2.color = Color.parseColor("#486DA3")
            // Draw rounded rectangle
            val roundedRect = RectF(width/2-textWidth,   height/2+textWidth, width/2+textWidth, height/2-textWidth)
            val cornerRadius = textWidth*2/3 // 반지름 값 설정
            canvas.drawRoundRect(roundedRect, cornerRadius, cornerRadius, paint2)
            paint.color =Color.parseColor("#FFFFFF")
            canvas.drawText(dateString,x,y,paint)
        }else {
            canvas.drawText(dateString,x,y,paint)
        }

    }
}