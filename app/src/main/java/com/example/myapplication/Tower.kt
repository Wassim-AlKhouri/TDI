package com.example.myapplication

import android.graphics.Canvas
import android.os.SystemClock
import android.util.Log
import kotlin.math.pow

abstract class Tower (open val Position: List<Int>, open val view: DrawingView) {
    var attacking = false
    var projectile:Projectile? = null
    var distanceattack :Float =500f
    abstract val damage :Int
    abstract val price:Int
    var cible : Monster? = null
    abstract val Step :Float

    abstract fun attack()

    fun detect_monster(monster: Monster){
        if(cible == null){attacking = false}
        if (!attacking) {
            Log.d("detect","oui")
            val xm = monster.x
            val ym = monster.y
            val distance = Math.sqrt( ((Math.abs( ((Position[0]+0.5)*Step) - xm) ).pow(2) + (Math.abs( ((Position[1]+0.5)*Step) - ym) ).pow(2)).toDouble() )
            if (distance <= distanceattack) {
                attacking = true
                cible = monster
            }
        }
    }

    fun move_projectile(){
        if(cible!=null){ if((cible!!.health <=0)){cible = null;attacking=false} }
        if(projectile?.Colision == true){projectile = null}
        else{projectile?.move()}
    }

    fun draw(canvas: Canvas) {projectile?.draw(canvas)}

}

class Money_Tower(override val Position: List<Int>, override val view: DrawingView, var last_time:Long):Tower(Position,view){
    override val Step = view.Step
    override var damage: Int = 10
    override val price: Int = 100
    val attack_interval: Long = 2000

    override fun attack(){
        if ((SystemClock.elapsedRealtime()-last_time) >= attack_interval){
            last_time = SystemClock.elapsedRealtime()
            view.money+=damage
        }
    }

}

class Ice_Tower(override val Position: List<Int>,override val view: DrawingView):Tower(Position, view){
    override val Step = view.Step
    override val damage: Int = 10
    override val price: Int = 300
    override fun attack() {
        if (projectile == null && cible != null){
            projectile = Projectile(view,Position, cible!!, SystemClock.elapsedRealtime(), damage, 1)
        }
    }
}

class Attack_Tower(override val Position: List<Int>,override val view: DrawingView):Tower(Position, view){
    override val Step = view.Step
    override val damage: Int = 40
    override val price: Int = 50
    override fun attack() {
        if (projectile == null && cible != null){
            projectile = Projectile(view,Position, cible!!, SystemClock.elapsedRealtime(), damage, 0)
        }
    }
}
