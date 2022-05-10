package com.example.myapplication

import android.os.SystemClock

abstract class Utility_Tower(override val Position: List<Int>, override val view: DrawingView):Tower(Position, view){
    protected abstract val specialattack_interval: Long //Intervalle entre l'utilisation de l'abilité spéciale
    var last_time = SystemClock.elapsedRealtime()
    override fun attack(){
        if ((SystemClock.elapsedRealtime()-last_time) >= specialattack_interval){
            last_time = SystemClock.elapsedRealtime()
            special_ability()
        }
    }
    abstract fun special_ability()
}

class Money_Tower(override val Position: List<Int>, override val view: DrawingView):Utility_Tower(Position,view){
    override val Step = view.Step
    private var money_gen: Int = 5 //Argent généré par la tour
    override val specialattack_interval: Long = 2000
    override val type: Int = 3 //Type de la tour
    override val name ="$$"
    override var upgrade_price = get_price(type)/2 + level*30 //prix tour
    init {
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }

    override fun special_ability() {
        //La tour génère de l'argent
        view.player.money+=money_gen
    }

    override fun upgrade() {
        money_gen+=5
        level+=1
        upgrade_price = (upgrade_price*2).toInt()
    }

    override fun explode() {
        view.Towers.remove(this)
        view.map.Cells[(view.Col * Position[1]) + Position[0]].type = 0
    }

}

class Sacrifice_Tower(override val Position: List<Int>, override val view: DrawingView):Utility_Tower(Position,view){
    override val Step = view.Step
    override val specialattack_interval: Long = 50000
    override val type: Int = 5
    override val name ="ST"
    override var upgrade_price = get_price(type)/2 //Prix de la tour
    private var lives = 1  // vie de la tour
    init {
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }

    override fun special_ability() {
        //La tour permet d'améliorer les autres tours et est choisi en premier par les montres explosifs
        (view.Towers.plus(view.Sacrifice_Towers)).random().upgrade()
    }

    override fun upgrade() {
        lives+=1
        level+=1
        upgrade_price = (upgrade_price*2.5).toInt()
    }

    override fun explode(){
        lives-=1
        if(lives==0){
            view.Sacrifice_Towers.remove(this)
            view.map.Cells[(view.Col * Position[1]) + Position[0]].type = 0
        }
    }
}