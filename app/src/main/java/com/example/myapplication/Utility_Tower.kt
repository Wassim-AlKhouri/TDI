package com.example.myapplication

import android.os.SystemClock

abstract class Utility_Tower(override val Position: List<Int>, override val view: DrawingView):Tower(Position, view){
    // tours utilitaires qui n'attaque pas les monstres
    abstract val specialattack_interval: Long // l'intervalle de temps entre chaque "attaque" spéciale
    var last_time = SystemClock.elapsedRealtime()

    override fun attack(){
        if ((SystemClock.elapsedRealtime()-last_time) >= specialattack_interval){
            last_time = SystemClock.elapsedRealtime()
            special_ability()
        }
    }
    abstract fun special_ability() // ce qui rendra chaque type de tour spéciale
}

class Money_Tower(override val Position: List<Int>, override val view: DrawingView):Utility_Tower(Position,view){
    // donne de l'argent au joueur chaque 2 secondes
    override val Step = view.Step
    var money_gen: Int = 5
    override val specialattack_interval: Long = 2000
    override val type: Int = 3
    override val name ="$$"
    override var upgrade_price = get_price(type)/2 + level*30
    init {
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }

    override fun special_ability() {
        view.player.money+=money_gen
    }

    override fun upgrade() {
        // la tour donne plus d'argent
        money_gen+=5
        level+=1
        upgrade_price+=25
    }

    override fun explode() {
        // efface la tour et réinitialise la case qu'elle occupait
        view.Towers.remove(this)
        view.map.Cells[(view.Col * Position[1]) + Position[0]].type = 0
    }

}

class Sacrifice_Tower(override val Position: List<Int>, override val view: DrawingView):Utility_Tower(Position,view){
    //tours de sacrifice sont les cibles prioritaires des monstres explosives et améliorent une tour au hasard chaque 50 secondes
    override val Step = view.Step
    override val specialattack_interval: Long = 50000
    override val type: Int = 5
    override val name ="ST"
    override var upgrade_price = get_price(type)/2
    var lives = 1
    init {
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }

    override fun special_ability() {
        (view.Towers.plus(view.Sacrifice_Towers)).random().upgrade()
    }

    override fun upgrade() {
        // la tour a une vie en plus
        lives+=1
        level+=1
        upgrade_price+=10
    }

    override fun explode(){
        // efface la tour et réinitialise la case qu'elle occupait si la tour n'a plus de vies
        lives-=1
        if(lives==0){
            view.Sacrifice_Towers.remove(this)
            view.map.Cells[(view.Col * Position[1]) + Position[0]].type = 0
        }
    }
}