package com.mada.myapplication.TimeFunction.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.lifecycle.ViewModel
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarUtils
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.R
import com.mada.myapplication.TimeFunction.TimeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.joda.time.DateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

    private var selectDay =""
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
            selectDay = timeViewModel.myLiveToday.value.toString()
        } else if (homeViewModel != null) {
            selectDay = homeViewModel.myLiveToday.value.toString()
        }
        setOnClickListener {
            //Log.d("click",date.toString("yyyy-MM-dd"))

            if (timeViewModel != null) {
                timeViewModel.updateData(date.toString("yyyy-MM-dd"))
                dialog.dismiss()
            }
            if (homeViewModel != null) {
                if(homeViewModel.isTodoMenu){
                    homeViewModel.selectedChangedDate = date.toString("yyyy-MM-dd")
                }
                else{
                    homeViewModel.updateData(date.toString("yyyy-MM-dd"))
                    dialog.dismiss()
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
        val y =( height-textHeight)/2.toFloat()- paint.fontMetrics.top
        //날짜 그리기
        if(selectDay=="${date.year}-${String.format("%02d", date.monthOfYear)}-${String.format("%02d", date.dayOfMonth)}") {
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