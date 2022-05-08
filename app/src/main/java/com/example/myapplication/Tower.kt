package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock
import android.util.Log
import kotlin.math.pow

abstract class Tower (open val Position: List<Int>, open val view: DrawingView):Price {
    abstract val Step:Float
    abstract val type:Int
    val whitepaint = Paint()
    abstract val name:String
    abstract var upgrade_price : Int
    var level = 1
    init {
        whitepaint.color = Color.WHITE
        whitepaint.textSize = 50f
    }
    abstract fun attack()
    abstract fun upgrade()
    abstract fun explode()
    fun ask_for_upgrade(){
        if (view.player.afford(upgrade_price)){
            view.player.money -= upgrade_price
            this.upgrade()
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawText(level.toString(),(Position[0]).toFloat()*Step,(Position[1]+0.3).toFloat()*Step,whitepaint)
        if(view.upgrade){
            canvas.drawText(upgrade_price.toString(),(Position[0]+0.3).toFloat()*Step,(Position[1]+0.6).toFloat()*Step,whitepaint)
        }
        else{
            canvas.drawText(name,(Position[0]+0.3).toFloat()*Step,(Position[1]+0.6).toFloat()*Step,whitepaint)
        }
    }

}


