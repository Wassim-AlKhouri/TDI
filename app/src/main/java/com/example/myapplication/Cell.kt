package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Cell (coordX : Int, coordY : Int, step: Float) {
    val R = RectF(coordX*step,coordY*step,(coordX+1)*step,(coordY+1)*step)
    val paint = Paint()
    val green = Color.argb(255, 0,255, 0)
    fun draw(canvas: Canvas){
        paint.color =green
        canvas.drawRect(R,paint)
    }
}