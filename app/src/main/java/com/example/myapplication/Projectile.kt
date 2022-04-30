package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.SystemClock
import kotlin.concurrent.thread

class Projectile (var view: DrawingView,val start_Position: List<Int>,val cible:Monster,var last_time:Long,val damage:Int) {
    val Step = view.Step
    var x = (start_Position[0]+0.5)*Step
    var y = (start_Position[1]+0.5)*Step
    var angle = 0.0
    val v = 2f
    var vx = 0.0
    var vy = 0.0
    var Colision = false
    val tf = 100
    /*
    fun launch(angle: Double) {
        canonball.x = canonballRadius
        //canonball.y = view.screenHeight / 2f
        canonballVitesseX=(canonballVitesse*Math.sin(angle)).toFloat()
        canonballVitesseY=(-canonballVitesse*Math.cos(angle)).toFloat()
        canonballOnScreen = true
    }
     */
    init {this.calculate_speed()}

    fun calculate_speed(){
/*
        angle =Math.atan( Math.abs((y-cible.y)/(x-cible.x)))
        if (x >= cible.x && y <= cible.y){angle+=Math.PI/2}
        else if (x <= cible.x && y < cible.y){angle+=Math.PI}
        else if (x < cible.x && y >= cible.y){angle+=3*Math.PI/2}
        vx = v*Math.sin(angle+Math.PI)
        vy = v*Math.sin(angle+Math.PI)
        */

        vx = (cible.x - x)/tf
        vy = (cible.y - y)/tf
    }

    fun move(){
        //x += vx.toFloat() * (SystemClock.elapsedRealtime() - last_time).toFloat()
        //y += vy.toFloat() * (SystemClock.elapsedRealtime() - last_time).toFloat()
        x += vx*(SystemClock.elapsedRealtime()-last_time)
        y += vy*(SystemClock.elapsedRealtime()-last_time)
        last_time = SystemClock.elapsedRealtime()
        if (Math.abs(x - cible.x) < 30 && Math.abs(y - cible.y) < 30){
            Colision = true
            cible.attacked(damage)
        }
    }

    fun draw(canvas: Canvas) {
        if(!Colision) {
            val green = Paint()
            green.color = Color.argb(255, 0, 255, 0)
            canvas.drawCircle(x.toFloat(), y.toFloat(), 15f, green)
        }
    }

}