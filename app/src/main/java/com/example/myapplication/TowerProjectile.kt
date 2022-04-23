package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF

class Projectile (var view: DrawingView, var step :Float, val x :Int, val y :Int) {
    var canonball = PointF()
    var canonballVitesse = 0f
    var canonballVitesseX = 0f
    var canonballVitesseY = 0f
    var canonballOnScreen = true
    var canonballRadius = 0f

    fun launch(angle: Double) {
        canonball.x = canonballRadius
        //canonball.y = view.screenHeight / 2f
        canonballVitesseX=(canonballVitesse*Math.sin(angle)).toFloat()
        canonballVitesseY=(-canonballVitesse*Math.cos(angle)).toFloat()
        canonballOnScreen = true
    }

    fun draw(canvas: Canvas) {
        val red = Paint()
        red.color = Color.argb(255, 255,0, 0)
        canvas.drawCircle((x+0.5f)*step,(y+0.5f)*step,15f,red)
    }

}