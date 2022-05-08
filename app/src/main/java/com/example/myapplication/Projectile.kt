package com.example.myapplication

import android.graphics.*
import android.os.SystemClock
import kotlin.concurrent.thread

class Projectile (val view: DrawingView,val start_Position: List<Int>,val cible:Monster,var last_time:Long,val damage:Int, val type:Int) {
    private val Step = view.Step
    private var x = (start_Position[0]+0.5)*Step
    private var y = (start_Position[1]+0.5)*Step
    private var r = RectF(0f,0f,0f,0f)
    private val radius = 10f
    private var vx = 0.0 // vitesse en x
    private var vy = 0.0 // vitesse en y
    var Colision = false // si oui on non la balle a touché le monstre
    private val tf = 100 // le temps que prendra la projectile pour atteindre sa cible
    private val birth_time = last_time // le temps quand la projectile a été créé
    private val cible_pos = cible.get_pos(tf) // position de la cible

    init {this.calculate_speed()}

    private fun calculate_speed(){
        // calcule vx et vy par rapport à la position du monstre et à "tf"
        vx = (cible_pos[0]  - x)/tf
        vy = (cible_pos[1]+8  - y)/tf
    }

    fun move(){
        // met à jour la position du projectile
        x += vx*(SystemClock.elapsedRealtime()-last_time)
        y += vy*(SystemClock.elapsedRealtime()-last_time)
        last_time = SystemClock.elapsedRealtime()
        this.r = RectF((x-radius).toFloat(),(y-radius).toFloat(),(x+radius).toFloat(),(y+radius).toFloat())
        if(r.intersect(cible.r)){
            // teste si le projectile touche le monstre
            Colision = true
            cible.attacked(damage,ice= type==1)
        }
        else if(SystemClock.elapsedRealtime() - birth_time > (tf+80)){
            /*
            si le projectile existe depuis un temps plus grand que "tf" alors il a raté sa cible.
            On considère que Colision = true pour que le projectile soit effacé par la tour mais il ne fait aucun dégât au monstre
            */
            Colision = true
        }
    }

    fun draw(canvas: Canvas) {
        // dessine le projectile
        if(!Colision) {
            val paint = Paint()
            if (type==1){
                // dessine un carrée bleu si type = 1(projectile de glace)
                paint.color = Color.BLUE
                canvas.drawRect((x-15f).toFloat(), (y-15f).toFloat(),(x+15f).toFloat(),(y+15f).toFloat(),paint)
            }
            else{
                // dessine un cercle (projectile normal)
                paint.color = Color.LTGRAY
                canvas.drawCircle(x.toFloat(), y.toFloat(), 15f, paint)
            }
        }
    }

}