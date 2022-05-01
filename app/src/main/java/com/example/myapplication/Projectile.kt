package com.example.myapplication

import android.graphics.*
import android.os.SystemClock
import kotlin.concurrent.thread

class Projectile (var view: DrawingView,val start_Position: List<Int>,val cible:Monster,var last_time:Long,val damage:Int) {
    val Step = view.Step
    var x = (start_Position[0]+0.5)*Step
    var y = (start_Position[1]+0.5)*Step
    var r = RectF(0f,0f,0f,0f)
    val radius = 10f
    var vx = 0.0
    var vy = 0.0
    var Colision = false
    val tf = 100

    init {this.calculate_speed()}

    fun calculate_speed(){
        vx = (cible.x - x)/tf
        vy = (cible.y - y)/tf
    }

    fun move(){
        x += vx*(SystemClock.elapsedRealtime()-last_time)
        y += vy*(SystemClock.elapsedRealtime()-last_time)
        last_time = SystemClock.elapsedRealtime()
        this.r = RectF(x.toFloat(),y.toFloat(),(x+radius).toFloat(),(y+radius).toFloat())
        //if (Math.abs(x - cible.x) < 30 && Math.abs(y - cible.y) < 30){
        if(r.intersect(cible.r)){
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