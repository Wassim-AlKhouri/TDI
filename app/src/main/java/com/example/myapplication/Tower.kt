package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock
import android.util.Log
import kotlin.math.pow

abstract class Tower (open val Position: List<Int>, open val view: DrawingView) {
    abstract val Step:Float
    abstract val type:Int
    val whitepaint = Paint()
    abstract val name:String
    init {
        whitepaint.color = Color.WHITE
        whitepaint.textSize = 50f
    }
    abstract fun attack()
    abstract fun upgrade()
    abstract fun explode()

    fun draw(canvas: Canvas) {
        canvas.drawText(name,(Position[0]+0.3).toFloat()*Step,(Position[1]+0.6).toFloat()*Step,whitepaint)
    }

}


