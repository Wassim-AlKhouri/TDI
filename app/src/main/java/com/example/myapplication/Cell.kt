package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Cell (coordX : Int, coordY : Int, val step: Float, val type:Int) {
    val X = coordX*step
    val Y = coordY*step
    val R = RectF(coordX*step,coordY*step,(coordX+1)*step,(coordY+1)*step)
    val paint = Paint()
    val green = Color.argb(255, 0,255, 0)
    val red = Color.argb(255, 255,0, 0)
    val black = Color.argb(255, 0,0, 0)
    fun draw(canvas: Canvas){
        if (type == 0) paint.color = green
        else if (type == 1) paint.color = red
        canvas.drawRect(R,paint)
        paint.color = black
        canvas.drawLine(X,Y,X+step,Y,paint)
        canvas.drawLine(X,Y,X,Y+step,paint)
        canvas.drawLine(X+step,Y,X+step,Y+step,paint)
        canvas.drawLine(X,Y+step,X+step,Y+step,paint)
    }
}