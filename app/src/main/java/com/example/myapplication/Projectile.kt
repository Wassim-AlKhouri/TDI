package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF

class Projectile (var view: DrawingView,val start_Position: List<Int>,val cible:Monster,var last_time:Double,val damage:Int) {
    var canonball = PointF()
    var canonballVitesse = 0f
    var canonballVitesseX = 0f
    var canonballVitesseY = 0f
    var canonballOnScreen = true
    var canonballRadius = 0f
    val Step = view.Step
    var x = start_Position[0]*Step
    var y = start_Position[1]*Step
    var angle = 0.0
    val v = 0.01f
    var vx = 0.0
    var vy = 0.0
    var Colision = false
    /*
    fun launch(angle: Double) {
        canonball.x = canonballRadius
        //canonball.y = view.screenHeight / 2f
        canonballVitesseX=(canonballVitesse*Math.sin(angle)).toFloat()
        canonballVitesseY=(-canonballVitesse*Math.cos(angle)).toFloat()
        canonballOnScreen = true
    }
     */

    fun calculate_speed(){
        angle = Math.atan(( (y-cible.y) / (x-cible.x) ).toDouble())
        vx = v*Math.sin(angle)
        vy = v*Math.sin(angle)
    }

    fun move(){
        calculate_speed()
        x += vx.toFloat() * (view.totaltime - last_time).toFloat()
        y += vy.toFloat() * (view.totaltime - last_time).toFloat()
        last_time = view.totaltime
        if (Math.abs(x - cible.x) < 5 && Math.abs(y - cible.y) < 5){
            Colision = true
            cible.attacked(damage)
        }
    }

    fun draw(canvas: Canvas) {
        if(!Colision) {
            val green = Paint()
            green.color = Color.argb(255, 0, 255, 0)
            canvas.drawCircle(x, y, 15f, green)
        }
    }

}