package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Cell (coordX : Int, coordY : Int, val step: Float, var type:Int) {
    private val X = coordX*step
    private val Y = coordY*step
    private val R = RectF(coordX*step,coordY*step,(coordX+1)*step,(coordY+1)*step)
    private val paint = Paint()
    private val green = Color.argb(255, 64,160, 22)
    private val brown = Color.argb(255, 155,103, 60)
    fun draw(canvas: Canvas){
        when(type){
            // change la couleur par rapport à la type de case
            0->paint.color = green
            1->paint.color = brown
            2->paint.color = Color.BLACK
            3->paint.color = Color.GRAY
            4->paint.color = Color.BLUE
            5->paint.color = Color.RED
        }
        canvas.drawRect(R,paint)
        paint.color = Color.BLACK
        canvas.drawLine(X,Y,X+step,Y,paint)  // dessine un contour noir autour de la case
        canvas.drawLine(X,Y,X,Y+step,paint)
        canvas.drawLine(X+step,Y,X+step,Y+step,paint)
        canvas.drawLine(X,Y+step,X+step,Y+step,paint)
    }
}