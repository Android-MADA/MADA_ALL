package com.example.myapplication.TimeFunction.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomCircleBarView : View {
    // 생성자
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    // 진행 상태를 나타내는 변수
    private var progress = 0

    // 원형 프로그레스바 그리기
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Paint 객체 생성
        val paint = Paint()

        // View의 크기 가져오기
        val smallXY = if(width > height) height else width

        val centerX = width / 2.toFloat()
        val centerY = height / 2.toFloat()
        val radius = smallXY/2 - smallXY / 30.toFloat()// 반지름 크기 (원의 크기 조절)
        val rectLeft = centerX - radius
        val rectTop = centerY - radius
        val rectRight = centerX + radius
        val rectBottom = centerY + radius

        // 1. 회색 원(배경) 그리기
        paint.color = Color.parseColor("#F0F0F0") // grey4
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = smallXY / 15.toFloat()         //두께 조절
        paint.isAntiAlias = true        //테투디를 부드럽게 처리
        // 0도에서 시작하는 360도의 호를 그린다.
        canvas?.drawArc(rectLeft, rectTop, rectRight, rectBottom, 0f, 360f, false, paint)

        // 2. 파란 원(프로그레스) 그리기
        paint.color = Color.parseColor("#F0768C") // sub3
        paint.strokeCap = Paint.Cap.ROUND
        // -90도에서 시작하는 진행 상태에 해당하는 호를 그린다.
        val sweepAngle = (progress.toFloat() / 100) * 360
        canvas?.drawArc(rectLeft, rectTop, rectRight, rectBottom, -90f, sweepAngle, false, paint)

        // 3. 각도에 따라 텍스트 그리기
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = smallXY / 20.toFloat()
        textPaint.color = Color.BLACK

        val textX = centerX - textPaint.measureText("0") / 2
        val textY = centerY + radius * Math.sin(Math.toRadians(-90.0)).toFloat() + textPaint.textSize / 2.5f
        canvas?.drawText("0", textX, textY, textPaint)

        val textAngle90 = -90
        val textX90 = centerX + radius * Math.cos(Math.toRadians(0.0)).toFloat() - textPaint.measureText("6") / 2
        val textY90 = centerY + textPaint.textSize / 2
        canvas?.drawText("6", textX90, textY90, textPaint)

        val textAngle180 = 0
        val textX180 = centerX - textPaint.measureText("12") / 2
        val textY180 = centerY + radius * Math.sin(Math.toRadians(90.0)).toFloat() + textPaint.textSize / 2.5f
        canvas?.drawText("12", textX180, textY180, textPaint)

        val textAngle270 = 90
        val textX270 = centerX + radius * Math.cos(Math.toRadians(180.0)).toFloat() - textPaint.measureText("18") / 2
        val textY270 = centerY  + textPaint.textSize / 2
        canvas?.drawText("18", textX270, textY270, textPaint)


    }

    // 진행 상태 설정
    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate() // View를 다시 그리도록 호출
    }
}
