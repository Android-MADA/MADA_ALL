package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.HomeFunction.time.TimeViewModel
import com.example.myapplication.HomeFunction.view.HomeViewpagerTimetableFragment
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class YourMarkerView(context: Context, layoutResource: Int, private val pieChartDataArray: ArrayList<TimeViewModel.PieChartData>) : MarkerView(context, layoutResource) {

    private val title: TextView = findViewById(R.id.textLabel1)
    private val memo: TextView = findViewById(R.id.textLabel2)
    private val time: TextView = findViewById(R.id.textLabel3)
    private val color: ImageView = findViewById(R.id.background1)
    private val back: ImageView = findViewById(R.id.background2)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val pieEntry = e as PieEntry
            val label = pieEntry.label
            if (label.toString() != "999" && label.toString() != "998") {
                title.text = pieChartDataArray[label.toInt()].title
                memo.text = pieChartDataArray[label.toInt()].memo
                val startH = pieChartDataArray[label.toInt()].startHour
                val endH = pieChartDataArray[label.toInt()].endHour
                val startM = pieChartDataArray[label.toInt()].startMin
                val endM = pieChartDataArray[label.toInt()].endMin
                val startTime = timeChangeReverse("${String.format("%02d", startH)}:${String.format("%02d", startM)}:00")
                val endTime = timeChangeReverse("${String.format("%02d", endH)}:${String.format("%02d", endM)}:00")
                time.text = "$startTime ~ $endTime"
                color.setColorFilter(Color.parseColor(pieChartDataArray[label.toInt()].colorCode), PorterDuff.Mode.SRC_IN)
                visibility = VISIBLE // 마커를 보이도록 설정
            } else {
                visibility = INVISIBLE // 마커를 숨기도록 설정
            }
        }
        super.refreshContent(e, highlight)
    }

    fun timeChangeReverse(time: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale("en", "US"))
        val outputFormat = SimpleDateFormat(" a h:mm ", Locale("en", "US"))
        val calendar = Calendar.getInstance()

        calendar.time = inputFormat.parse(time)

        return outputFormat.format(calendar.time).replace("AM","오전").replace("PM","오후")
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
