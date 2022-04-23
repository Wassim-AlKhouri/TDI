package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Monster(val Birth_time:Double, var view: DrawingView) {
    val R =RectF(30f,30f,30f,30f)
    var road = view.map.road
    val Step = view.Step
    var x = road[0][0]*Step
    var y = 0f
    var speed = 0.0007f
    var pos = 0
    var end = false
    var health = 100
    var dead = false

    fun draw(canvas: Canvas){
        if (!dead) {
                val red = Paint()
                red.color = Color.argb(255, 255, 0, 0)
                canvas.drawCircle(x, y, 30f, red)
            }
    }

    fun move(){
         val totaltime = view.totaltime
        pos = ((totaltime-Birth_time)*speed/Step).toInt()
        //val time_in_Cell = (totaltime-time_birth) - (pos*Step)/speed
        val d =( (totaltime-Birth_time)*speed - pos*Step ).toFloat()
        if (pos < (road.size - 1)) {
            x = ((road[pos][0] + 0.5)*Step).toFloat()
            y = ((road[pos][1] + 0.5)*Step).toFloat()
            if (road[pos][0] - road[pos + 1][0] > 0) {
                x -= d
            } else if (road[pos][0] - road[pos + 1][0] < 0) {
                x += d
            } else if (road[pos][1] - road[pos + 1][1] < 0) {
                y += d
            }
        }
        /*
        if (pos < (road.size - 1)){
            if ( ( this.x-road[pos+1][0] ) < 0) {this.x+=1}
            else if ( ( this.x-road[pos+1][0] ) > 0) {this.x-=1}
            else if ( (this.y-road[pos+1][1]) < 0) {this.y+=1}
            pos+=1
        }
        else if (pos == road.size){end = true}
         */
    }

    fun attacked(damage:Int){
        health -= damage
        if (health <= 0){
            dead = true
        }
    }

}


