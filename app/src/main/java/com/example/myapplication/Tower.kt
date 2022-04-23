package com.example.myapplication

import android.graphics.Canvas
import kotlin.math.pow

class Tower (val Position: List<Int>, var view: DrawingView) {
    var attacking = false
    var projectile:Projectile? = null
    var distanceattack = 500f
    var damage :Int = 30
    var cible : Monster? = null
    val Step = view.Step

    fun attack(){
        if (projectile == null && cible != null){
            projectile = Projectile(view,Position, cible!!, view.totaltime, damage)
        }
    }

    fun detect_monster(monster: Monster){
        if (!attacking) {
            val xm = monster.x
            val ym = monster.y
            val distance = Math.sqrt(
                ((Math.abs(Position[0] * Step - xm)).pow(2) + (Math.abs(Position[1] * Step - ym)).pow(2)).toDouble())
            if (distance <= distanceattack) {
                attacking = true
                cible = monster
            }
        }
    }
    fun move_projectile(){
        projectile?.move()
    }
    fun draw(canvas: Canvas) {projectile?.draw(canvas)}
}

