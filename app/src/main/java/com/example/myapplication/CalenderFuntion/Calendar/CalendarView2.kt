package com.example.myapplication.CalenderFuntion.Calendar
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.allViews
import androidx.core.view.children
import com.example.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.WEEKS_PER_MONTH
import com.example.myapplication.CalenderFuntion.Model.AndroidCalendarData
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.R
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants.DAYS_PER_WEEK
import kotlin.math.max

class CalendarView2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.calendarViewStyle,
    @StyleRes defStyleRes: Int = R.style.Calendar_CalendarViewStyle
) : ViewGroup(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    private lateinit var list: List<DateTime>
    private lateinit var CalendarViewModel: CalendarViewModel
    private var year : String = ""
    private var month : String =""
    private var maxFloor = 100
    /**
     * Measure
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), getDefaultSize(suggestedMinimumHeight, heightMeasureSpec))
    }

    /**
     * Layout
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d("dsadasd","${height}")
        val iWidth = (width / DAYS_PER_WEEK).toFloat()
        val iHeight = (height / WEEKS_PER_MONTH).toFloat()
        var index = 0
        children.forEach { view ->
            val left = (index % DAYS_PER_WEEK) * iWidth
            val top = (index / DAYS_PER_WEEK) * iHeight

            view.layout(left.toInt(), top.toInt(), (left + iWidth).toInt(), (top + iHeight).toInt())

            index++
        }
    }

    /**
     * 달력 그리기 시작한다.
     * @param firstDayOfMonth   한 달의 시작 요일
     * @param list              달력이 가지고 있는 요일과 이벤트 목록 (총 42개)
     */
    fun getCalendarData(list: List<DateTime>,year :  String ,month : String,CalendarViewModel : CalendarViewModel) {
        Log.d("dsadasd2","${height}")
        list.forEach {
            this.list = list
            this.CalendarViewModel = CalendarViewModel
            this.year = year
            this.month = month
            addView(DayDataItemView(
                context = context,
                date  = it,
                year = year,
                month = month,
                //hashMapDataCal = CalendarViewModel.setMonthData(year,month,false,maxFloor),
                CalendarViewModel = CalendarViewModel
            ))
        }
    }

    fun updateCalendarData() {
        removeAllViews() // Remove all existing child views
        // Add new child views based on the new calendar data
        list.forEach { date ->
            addView(DayDataItemView(
                context = context,
                date = date,
                year = year,
                month = month,
                CalendarViewModel = CalendarViewModel
            ))
        }
    }
}