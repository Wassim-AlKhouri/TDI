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
        // méthode appelé pour améliorer les tours
        if (view.player.afford(upgrade_price)){
            // teste si le joueur a assez d'argent pour que la tour s'améliore
            view.player.money -= upgrade_price
            this.upgrade()
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawText(level.toString(),(Position[0]).toFloat()*Step,(Position[1]+0.3).toFloat()*Step,whitepaint) // dessine le niveau de la tour en haut à gauche de la case
        if(view.upgrade){
            // teste su upgrade est activié, si oui on dessine le prix de l'amélioration de la tour au milieu de la case
            canvas.drawText(upgrade_price.toString(),(Position[0]+0.3).toFloat()*Step,(Position[1]+0.6).toFloat()*Step,whitepaint)
        }
        else{
            // dessine le nom de la tour au milieu de la tour
            canvas.drawText(name,(Position[0]+0.3).toFloat()*Step,(Position[1]+0.6).toFloat()*Step,whitepaint)
        }
    }

}


