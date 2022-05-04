package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.SystemClock
import java.util.ArrayList

abstract class Monster(open var LastMouvement:Long, open var view: DrawingView) {

    abstract val road : ArrayList<Array<Int>>
    abstract val Step :Float
    var paint = Paint()
    var x = 0f
    var y = 0f
    var r =RectF(0f,0f,0f,0f)
    val radius = 40f
    var speed = 0.1f
    var pos = 0
    var health = 1000
    var dead = false
    var iced = false
    var iced_time:Long = 0
    var ran = 0
    var d = 0f


    init {
        val r = ((Step/2)-radius).toInt()
        this.ran = (r..-r).random()
    }

    abstract fun special_move()
    abstract fun attacked(damage:Int,ice:Boolean)

    fun draw(canvas: Canvas){
        if (!dead) {
                canvas.drawRect(r,paint)
            }
    }

    fun move(){
        special_move()
        this.d += (SystemClock.elapsedRealtime() - this.LastMouvement)*speed
        this.LastMouvement = SystemClock.elapsedRealtime()
        if (d>=Step){
            d-=Step
            pos+=1
        }
        if (iced) { if ((SystemClock.elapsedRealtime() - iced_time) <= 2000) { this.speed/=0.09f } }
        /*
        pos = ((SystemClock.elapsedRealtime()-Birth_time)*speed/Step).toInt()
        val d =( (SystemClock.elapsedRealtime()-Birth_time)*speed - pos*Step )
         */
        if (pos < (road.size - 1)) {
            x = ((road[pos][0] + 0.5)*Step).toFloat()
            y = ((road[pos][1] + 0.5)*Step).toFloat()
            if (road[pos][0] - road[pos+1][0] > 0) {
                x -= d
            } else if (road[pos][0] - road[pos+1][0] < 0) {
                x += d
            } else if (road[pos][1] - road[pos+1][1] < 0) {
                y += d
            }
            this.r = RectF(x-radius/2+ran,y-radius/2,x+radius/2+ran,y+radius/2)
        }
    }

}

class Normal_Monster(override var LastMouvement:Long, override var view: DrawingView):Monster(LastMouvement, view){
    override val road = view.map.road
    override val Step = view.Step
    init {
        x=( (road[0][0]+0.5)*Step ).toFloat()
    }

    override fun special_move() {paint.color = Color.BLACK}

    override fun attacked(damage: Int, ice: Boolean) {
        health -= damage
        if (health <= 0){ dead = true }
        if (ice){iced=true;iced_time=SystemClock.elapsedRealtime()}
    }

}

class Immune_Monster(override var LastMouvement:Long, override var view: DrawingView):Monster(LastMouvement, view){
    override val road = view.map.road
    override val Step = view.Step
    var immune:Boolean = false

    override fun special_move() {
        paint.color = Color.GRAY
        if((SystemClock.elapsedRealtime()-LastMouvement).toInt().mod(2) == 0){immune = true}
    }

    override fun attacked(damage: Int, ice: Boolean) {
        if(!immune){
            health -= damage
            if (health <= 0){ dead = true }
        }
    }

}

class Explosif_Monster(override var LastMouvement:Long, override var view: DrawingView):Monster(LastMouvement, view){
    override val road = view.map.road
    override val Step = view.Step

    override fun attacked(damage: Int, ice: Boolean) {
        health -= damage
        if (health <= 0){ dead = true }
        if (ice){iced=true;iced_time=SystemClock.elapsedRealtime()}
    }

    override fun special_move() {
        paint.color = Color.RED
        if((SystemClock.elapsedRealtime()-LastMouvement) >= 5000){
            if(view.Towers.size !=0) {
                val ran = (0 until view.Towers.size).random()
                view.map.Cells[(view.Col * view.Towers[ran].Position[1]) + view.Towers[ran].Position[0]].type = 0
                view.Towers.remove(view.Towers[ran])
                this.dead = true
            }
        }
    }

}

