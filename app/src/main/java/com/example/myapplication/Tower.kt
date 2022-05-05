package com.example.myapplication

import android.graphics.Canvas
import android.os.SystemClock
import android.util.Log
import kotlin.math.pow

abstract class Tower (open val Position: List<Int>, open val view: DrawingView) {
    abstract val damage:Int
    abstract val price:Int
    abstract val Step:Float
    abstract val type:Int

    abstract fun attack()
    /*
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

     */

}

class Money_Tower(override val Position: List<Int>, override val view: DrawingView, var last_time:Long):Tower(Position,view){
    override val Step = view.Step
    override var damage: Int = 5
    override val price: Int = 250
    private val attack_interval: Long = 2000
    override val type: Int = 3
    init {
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }

    override fun attack(){
        if ((SystemClock.elapsedRealtime()-last_time) >= attack_interval){
            last_time = SystemClock.elapsedRealtime()
            view.money+=damage
        }
    }

}

 abstract class Attack_Tower(override val Position: List<Int>, override val view: DrawingView):Tower(Position, view){
    abstract val projectile_type:Int
     var distanceattack :Float =250f
     var cible : Monster? = null
     var projectile:Projectile? = null
     var attacking = false

     override fun attack() {
         if (projectile == null && cible != null){
             projectile = Projectile(view,Position, cible!!, SystemClock.elapsedRealtime(), damage, projectile_type)
         }
         if (cible !=null){
             val distance = Math.sqrt(((Math.abs(((Position[0]+0.5)*Step)- cible!!.x)).pow(2)+(Math.abs(((Position[1]+0.5)*Step)-cible!!.y)).pow(2)))
             if (distance > distanceattack){
                 cible = null
             }
         }
     }

     fun detect_monster(monster: Monster){
         if(cible == null){attacking = false}
         if (!attacking) {
             val xm = monster.x
             val ym = monster.y
             val distance = Math.sqrt(((Math.abs(((Position[0]+0.5)*Step)-xm)).pow(2)+(Math.abs(((Position[1]+0.5)*Step)-ym)).pow(2)))
             if (distance <= distanceattack) {
                 attacking = true
                 cible = monster
             }
         }
     }

     fun move_projectile(){
         if(cible!=null){ if(cible!!.dead){cible = null;attacking=false} }
         if(projectile?.Colision == true){projectile = null}
         else{projectile?.move()}
     }

     fun draw(canvas: Canvas) {projectile?.draw(canvas)}
}

class Ice_Tower(override val Position: List<Int>,override val view: DrawingView):Attack_Tower(Position, view){
    override val Step = view.Step
    override val damage: Int = 30
    override val price: Int = 300
    override val projectile_type: Int = 1
    override val type: Int = 4
    init {
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }
    /*
    override fun attack() {
        if (projectile == null && cible != null){
            projectile = Projectile(view,Position, cible!!, SystemClock.elapsedRealtime(), damage, 1)
        }
    }

     */
}

class Normal_Attack_Tower(override val Position: List<Int>,override val view: DrawingView):Attack_Tower(Position, view){
    override val Step = view.Step
    override val damage: Int = 50
    override val price: Int = 50
    override val projectile_type: Int = 0
    override val type: Int = 2
    init {
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }
    /*
    override fun attack() {
        if (projectile == null && cible != null){
            projectile = Projectile(view,Position, cible!!, SystemClock.elapsedRealtime(), damage, 0)
        }
    }

     */
}
