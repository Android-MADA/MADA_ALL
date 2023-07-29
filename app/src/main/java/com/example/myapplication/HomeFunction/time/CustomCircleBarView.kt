package com.example.myapplication

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
        val centerX = width / 2.toFloat()
        val centerY = height / 2.toFloat()

        val radius = 500f // 반지름 크기 (원의 크기 조절)
        val rectLeft = centerX - radius
        val rectTop = centerY - radius
        val rectRight = centerX + radius
        val rectBottom = centerY + radius

        // 1. 회색 원(배경) 그리기
        paint.color = Color.parseColor("#F0F0F0") // grey4
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 60f         //두께 조절
        paint.isAntiAlias = true        //테투디를 부드럽게 처리
        // 0도에서 시작하는 360도의 호를 그린다.
        canvas?.drawArc(rectLeft, rectTop, rectRight, rectBottom, 0f, 360f, false, paint)

        // 2. 파란 원(프로그레스) 그리기
        paint.color = Color.parseColor("#F0768C") // sub3
        paint.strokeCap = Paint.Cap.ROUND
        // -90도에서 시작하는 진행 상태에 해당하는 호를 그린다.
        val sweepAngle = (progress.toFloat() / 100) * 360
        canvas?.drawArc(rectLeft, rectTop, rectRight, rectBottom, -90f, sweepAngle, false, paint)
    }

    // 진행 상태 설정
    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate() // View를 다시 그리도록 호출
    }
}
