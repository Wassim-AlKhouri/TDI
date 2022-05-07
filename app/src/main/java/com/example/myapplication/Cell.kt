package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Cell (coordX : Int, coordY : Int, val step: Float, var type:Int) {
    val X = coordX*step
    val Y = coordY*step
    val R = RectF(coordX*step,coordY*step,(coordX+1)*step,(coordY+1)*step)
    val paint = Paint()
    val green = Color.argb(255, 64,160, 22)
    val brown = Color.argb(255, 155,103, 60)
    fun draw(canvas: Canvas){
        when(type){
            0->paint.color = green
            1->paint.color = brown
            2->paint.color = Color.BLACK
            3->paint.color = Color.GRAY
            4->paint.color = Color.BLUE
            5->paint.color = Color.RED
        }
        canvas.drawRect(R,paint)
        paint.color = Color.BLACK
        canvas.drawLine(X,Y,X+step,Y,paint)
        canvas.drawLine(X,Y,X,Y+step,paint)
        canvas.drawLine(X+step,Y,X+step,Y+step,paint)
        canvas.drawLine(X,Y+step,X+step,Y+step,paint)
    }
}