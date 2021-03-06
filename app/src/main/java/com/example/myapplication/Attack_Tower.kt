package com.example.myapplication

import android.graphics.Canvas
import android.os.SystemClock
import kotlin.math.pow

abstract class Attack_Tower(override val Position: List<Int>, override val view: DrawingView):Tower(Position, view){
    // Les tours attaquantes
    protected abstract val projectile_type:Int
    protected abstract val damage:Int
    private var distanceattack :Float =250f
    private var cible : Monster? = null
    private var attacking = false // si la tour est en train d'attaquer
    private var lastattack :Long = 0 // le temps correspondant à la dernière attaque
    private var attack_interval = 500 // le temps entre chaque attaque
    var projectile:Projectile? = null

    override fun attack() {
        if (projectile == null && cible != null && SystemClock.elapsedRealtime() - lastattack > attack_interval){
            // Teste si un projectile est déjà lancé, si une cible a déjà été choisie et si l'intervalle pour lancer un nouveau projectile est bon
            projectile = Projectile(view,Position, cible!!, SystemClock.elapsedRealtime(), damage, projectile_type) // créer un nouveau projectile
            lastattack = SystemClock.elapsedRealtime()
        }
        if (cible !=null){
            val distance = Math.sqrt(((Math.abs(((Position[0]+0.5)*Step)- cible!!.x)).pow(2)+(Math.abs(((Position[1]+0.5)*Step)-cible!!.y)).pow(2)))
            if (distance > distanceattack){
                // Teste si la cible est trop éloignée. Si oui, le monstre n'est plus ciblé
                cible = null
            }
        }
    }

    override fun explode() {
        // efface la tour et réinitialise la case qu'elle occupait
        view.Towers.remove(this)
        view.map.Cells[(view.Col * Position[1]) + Position[0]].type = 0
    }

    fun detect_monster(monster: Monster){
        // teste si le monstre est assez proche pour être attaqué par la tour
        if(cible == null){attacking = false}
        if (!attacking) {
            // la tour ne prend pas le monstre comme cible si elle attaque dêjà un autre
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
        // la tour dit à son projectile de se déplacer
        if(cible!=null){ if(cible!!.dead){cible = null;attacking=false} }
        if(projectile?.Colision == true){projectile = null}
        else{projectile?.move()}
    }

    override fun upgrade() {
        if(attack_interval>100) {
            // si l'intervalle d'attaque est plus grande que 0.1 seconde alors la tour la diminue
            attack_interval -= 25
            level+=1
            upgrade_price = (upgrade_price*1.4).toInt()
        }
    }

    fun draw_projectile(canvas: Canvas) {projectile?.draw(canvas)} // dessine le projectile
}

class Ice_Tower(override val Position: List<Int>,override val view: DrawingView):Attack_Tower(Position, view){
    override val Step = view.Step
    override val damage: Int = 30
    override val projectile_type: Int = 1
    override val type: Int = 4
    override val name = "ICE"
    override var upgrade_price = get_price(type)/2 + level*30
    init {
        // à la création de la tour, elle change le type de la case sur laquelle elle va se placer
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }
}

class Normal_Attack_Tower(override val Position: List<Int>,override val view: DrawingView):Attack_Tower(Position, view){
    override val Step = view.Step
    override val damage: Int = 50
    override val projectile_type: Int = 0
    override val type: Int = 2
    override val name = "ATT"
    override var upgrade_price = get_price(type)/2 + level*15
    init {
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }

}
