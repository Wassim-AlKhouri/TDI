package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import java.util.ArrayList

class Monster(val step:Float, val road: ArrayList<Array<Int>>) {
    val R =RectF(30f,30f,30f,30f)
    var x = road[0][0]
    var y = 0
    var pos = 0
    var end = false
    var health = 100
    fun draw(canvas: Canvas){
        val red = Paint()
        red.color = Color.argb(255, 255,0, 0)
        canvas.drawCircle((x+0.5f)*step,(y+0.5f)*step,30f,red)
    }
    fun move(){
        if (pos < (road.size - 1)){
            if ( ( this.x-road[pos+1][0] ) < 0) {this.x+=1}
            else if ( ( this.x-road[pos+1][0] ) > 0) {this.x-=1}
            else if ( (this.y-road[pos+1][1]) < 0) {this.y+=1}
            pos+=1
        }
        else if (pos == road.size){end = true}
    }
    fun detect_choc(damage:Int){
        health -= damage
    }
}


