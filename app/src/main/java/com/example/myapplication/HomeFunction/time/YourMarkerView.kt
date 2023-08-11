package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myapplication.HomeFunction.view.HomeTimetableFragment
import com.example.myapplication.HomeFunction.view.viewpager2.HomeViewpagerTimetableFragment
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class YourMarkerView(context: Context, layoutResource: Int, private val pieChartDataArray: Array<HomeViewpagerTimetableFragment.PieChartData>) : MarkerView(context, layoutResource) {

    private val title: TextView = findViewById(R.id.textLabel1)
    private val memo: TextView = findViewById(R.id.textLabel2)
    private val time: TextView = findViewById(R.id.textLabel3)
    private val color: ImageView = findViewById(R.id.background1)
    private val back: ImageView = findViewById(R.id.background2)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val pieEntry = e as PieEntry
            val label = pieEntry.label
            if (label.toString() != "999") {

                title.text = pieChartDataArray[label.toInt()].title
                memo.text = pieChartDataArray[label.toInt()].memo
                val startH = pieChartDataArray[label.toInt()].startHour
                val endH = pieChartDataArray[label.toInt()].endHour
                val startM = pieChartDataArray[label.toInt()].startMin
                val endM = pieChartDataArray[label.toInt()].endMin
                val startTime = formatTime(startH, startM)
                val endTime = formatTime(endH, endM)
                time.text = "$startTime ~ $endTime"
                color.setColorFilter(Color.parseColor(pieChartDataArray[label.toInt()].colorCode), PorterDuff.Mode.SRC_IN)
                visibility = VISIBLE // 마커를 보이도록 설정
            } else {
                visibility = INVISIBLE // 마커를 숨기도록 설정
            }
        }
        super.refreshContent(e, highlight)
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val amPm = if (hour < 12) "AM" else "PM"
        val formattedHour = if (hour > 12) hour - 12 else hour
        return "$amPm${String.format("%02d", formattedHour)}:${String.format("%02d", minute)}"
    }
    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        if (visibility == VISIBLE) {
            // 가시성이 VISIBLE인 경우에만 마커를 그립니다.

            super.draw(canvas, posX, posY)
        }
    }

    private val mOffset: MPPointF = MPPointF(-(width / 2f), -height.toFloat() / 2)

    override fun getOffset(): MPPointF {
        return mOffset
    }
}
