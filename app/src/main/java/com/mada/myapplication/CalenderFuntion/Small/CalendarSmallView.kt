package com.mada.myapplication.CalenderFuntion.Small
import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import androidx.lifecycle.ViewModel
import com.mada.myapplication.CalenderFuntion.Calendar.CalendarUtils.Companion.WEEKS_PER_MONTH
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.R
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants.DAYS_PER_WEEK
import kotlin.math.max

class CalendarSmallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.calendarSmallViewStyle,
    @StyleRes defStyleRes: Int = R.style.CalendarSmall_CalendarSmallViewStyle
) : ViewGroup(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    private var _height: Float = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.CalendarSmallView, defStyleAttr, defStyleRes) {
            _height = getDimension(R.styleable.CalendarSmallView_daySmallHeight, 40f)
        }
    }

    /**
     * Measure
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = paddingTop + paddingBottom + max(suggestedMinimumHeight, (_height * WEEKS_PER_MONTH).toInt())
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), h)
    }

    /**
     * Layout
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
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
    fun initCalendar(
        firstDayOfMonth: DateTime,
        list: List<DateTime>,
        Scheldule: TextView,
        ScheduleNum: TextView,
        cal: LinearLayout,
        textDday : TextView,
        repeatFlag: String? = null,
        viewModel : HomeViewModel?
    ) {
        list.forEach {
            addView(DaySmallItemView(
                context = context,
                date = it,
                firstDayOfMonth = firstDayOfMonth,
                Scheldule= Scheldule,
                ScheduleNum = ScheduleNum,
                cal = cal,
                textDday =  textDday,
                repeatFlag = repeatFlag,
                viewModel = viewModel
            ))
        }

    }
}