package com.example.myapplication

import android.graphics.*
import android.os.SystemClock
import kotlin.concurrent.thread

class Projectile (val view: DrawingView,val start_Position: List<Int>,val cible:Monster,var last_time:Long,val damage:Int, val type:Int) {
    private val Step = view.Step //Le pas de chaque case
    private var x = (start_Position[0]+0.5)*Step //Position initiale en x du projectile
    private var y = (start_Position[1]+0.5)*Step //Position initiale en y du projectile
    private var r = RectF(0f,0f,0f,0f) //Forme des projectile de glace
    private val radius = 10f // rayon des projectiles normaux
    private var vx = 0.0 //vitesse en x du projectile
    private var vy = 0.0 //vitesse en y du projectile
    var Colision = false
    private val tf = 100 //Temps max pour atteindre projectile
    private val birth_time = last_time //Temps auquel le projectile a été crée
    private val cible_pos = cible.get_pos(tf)//Position du monstre ciblé par le projectile


    init {this.calculate_speed()
    }

    private fun calculate_speed(){
        //Calcul vitesse projectile
        vx = (cible_pos[0]  - x)/tf
        vy = (cible_pos[1]+8  - y)/tf
    }

    fun move(){
        //Calcul le déplacement du projectile
        x += vx*(SystemClock.elapsedRealtime()-last_time)
        y += vy*(SystemClock.elapsedRealtime()-last_time)
        last_time = SystemClock.elapsedRealtime()
        this.r = RectF(x.toFloat(),y.toFloat(),(x+radius).toFloat(),(y+radius).toFloat())
        if(r.intersect(cible.r)){
            Colision = true
            cible.attacked(damage,ice= type==1)

        }
        else if(SystemClock.elapsedRealtime() - birth_time > (tf+80)){
            Colision = true
        }
    }

    fun draw(canvas: Canvas) {
        //Dessine le projectile jusqu'à ce qu'il y'ait colision
        if(!Colision) {
            val paint = Paint()
            if (type==1){
                paint.color = Color.BLUE
                canvas.drawRect((x-15f).toFloat(), (y-15f).toFloat(),(x+15f).toFloat(),(y+15f).toFloat(),paint)
            }
            else{
                paint.color = Color.LTGRAY
                canvas.drawCircle(x.toFloat(), y.toFloat(), 15f, paint)
            }
        }
    }

}