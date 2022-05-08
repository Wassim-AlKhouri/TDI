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
    abstract val name:String // Nom de la tour (visible en jeu)
    abstract var upgrade_price : Int
    var level = 1
    init {
        whitepaint.color = Color.WHITE
        whitepaint.textSize = 50f
    }
    abstract fun attack()//Détermine comment la tour va attacker
    abstract fun upgrade()//Détermine comment la tour va s'améliorer
    abstract fun explode()//Détermine l'explosion de la tour
    fun ask_for_upgrade(){
        //Test si le joueur a assez d'argent pour améliorer sa tour et si oui l'améliore
        if (view.player.afford(upgrade_price)){
            view.player.money -= upgrade_price
            this.upgrade()
        }
    }

    fun draw(canvas: Canvas) {
        //Dessine la tour
        canvas.drawText(level.toString(),(Position[0]).toFloat()*Step,(Position[1]+0.3).toFloat()*Step,whitepaint)
        if(view.upgrade){
            canvas.drawText(upgrade_price.toString(),(Position[0]+0.3).toFloat()*Step,(Position[1]+0.6).toFloat()*Step,whitepaint)
        }
        else{
            canvas.drawText(name,(Position[0]+0.3).toFloat()*Step,(Position[1]+0.6).toFloat()*Step,whitepaint)
        }
    }

}


